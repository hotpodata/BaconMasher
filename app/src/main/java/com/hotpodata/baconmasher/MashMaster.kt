package com.hotpodata.baconmasher

import android.content.Context
import android.graphics.Point
import android.text.Html
import android.util.Patterns
import android.view.WindowManager
import com.hotpodata.baconforkotlin.RedditSessionService
import com.hotpodata.baconforkotlin.network.model.ImageInfo
import com.hotpodata.baconforkotlin.network.model.Listing
import com.hotpodata.baconforkotlin.network.model.t1
import com.hotpodata.baconforkotlin.network.model.t3
import com.hotpodata.baconmasher.data.*
import com.hotpodata.baconmasher.utils.UserAgentUtils
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import timber.log.Timber
import java.util.*

/**
 * Created by jdrotos on 11/29/15.
 */
object MashMaster {
    val COMMENT_MAX_LENGTH = 100

    val PREF_KEY_IMAGE_REDDITS = "IMAGE_REDDITS"
    val PREF_KEY_COMMENT_REDDITS = "COMMENT_REDDITS"
    val PREF_KEY_TYPEFACES = "TYPEFACES"
    val PREF_KEY_TEXTGRAVITY = "TEXTGRAVITY"

    val PREF_KEY_APPINFO = "APPINFO"
    val PREF_UNIQUE_ID = "UNIQUE_ID"

    //Regex
    val COM_RGX_DELETED = "^(\\[deleted\\]|\\[DELETED\\]|\\[removed\\]|\\[REMOVED\\])$".toRegex()
    val COM_RGX_LINK = Patterns.WEB_URL.toRegex()
    val COM_RGX_EMBEDDEDLINK = "\\[(.*?)\\]\\((.*?)\\)".toRegex()
    val COM_RGX_SPACE = "\\s+".toRegex()

    var activeData: MashData? = null
    var activeDataSubject: BehaviorSubject<MashData>? = null
    var activeDataSubscription: Subscription? = null

    var imageReddits: ActiveStringManager? = null
    var commentReddits: ActiveStringManager? = null
    var typefaces: ActiveStringManager? = null
    var textgravity: ActiveStringManager? = null

    val random = Random()
    var context: Context? = null

    private var _service: RedditSessionService? = null
    var service: RedditSessionService
        get() {
            var ctx = context
            if (_service == null && ctx != null) {
                _service = RedditSessionService(UserAgentUtils.genUserAgentStr(ctx, ctx.getString(R.string.reddit_user_name)), getUniqueId(), ctx.getString(R.string.reddit_app_id) ?: "")
            }
            return _service!!
        }
        set(value) {
            _service = value
        }

    public fun initMashMaster(ctx: Context) {
        context = ctx;

        imageReddits = ActiveStringManager(ctx.applicationContext, PREF_KEY_IMAGE_REDDITS, object : ActiveStringManager.DefaultGenerator() {
            override fun getActiveDefault(): List<String> {
                return ArrayList<String>(ctx.resources.getStringArray(R.array.imagereddits).asList())
            }

            override fun getAllDefault(): List<String> {
                return ArrayList<String>(ctx.resources.getStringArray(R.array.imagereddits).asList())
            }
        })

        commentReddits = ActiveStringManager(ctx.applicationContext, PREF_KEY_COMMENT_REDDITS, object : ActiveStringManager.DefaultGenerator() {
            override fun getActiveDefault(): List<String> {
                return ArrayList<String>(ctx.resources.getStringArray(R.array.commentreddits).asList())
            }

            override fun getAllDefault(): List<String> {
                return ArrayList<String>(ctx.resources.getStringArray(R.array.commentreddits).asList())
            }
        })

        typefaces = ActiveStringManager(ctx.applicationContext, PREF_KEY_TYPEFACES, object : ActiveStringManager.DefaultGenerator() {
            override fun getActiveDefault(): List<String> {
                return ArrayList<String>(ctx.resources.getStringArray(R.array.fonts).asList())
            }

            override fun getAllDefault(): List<String> {
                return ArrayList<String>(ctx.resources.getStringArray(R.array.fonts).asList())
            }
        })

        textgravity = ActiveStringManager(ctx.applicationContext, PREF_KEY_TEXTGRAVITY, object : ActiveStringManager.DefaultGenerator() {
            override fun getActiveDefault(): List<String> {
                return listOf(GravityStringer.BL, GravityStringer.TL)
            }

            override fun getAllDefault(): List<String> {
                return ArrayList<String>(GravityStringer.strToGrav.keys)
            }
        })


    }

    fun doMash(ctx: Context): Observable<MashData> {
        var wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var display = wm.getDefaultDisplay()
        var size = Point()
        display.getSize(size)
        var screenWidth = size.x
        var screenHeight = size.y

        if (!(activeDataSubscription?.isUnsubscribed ?: true)) {
            activeDataSubscription?.unsubscribe()
            activeDataSubscription = null
        }

        activeDataSubject?.onCompleted()
        activeDataSubject = null

        var observable: Observable<MashData> =
                if (imageReddits?.active?.size ?: 0 <= 0) {
                    Observable.error<MashData>(ExceptionMissingSettings(ctx.resources.getString(R.string.error_needs_image_subreddit)))
                } else if (commentReddits?.active?.size ?: 0 <= 0) {
                    Observable.error<MashData>(ExceptionMissingSettings(ctx.resources.getString(R.string.error_needs_comment_subreddit)))
                } else if (typefaces?.active?.size ?: 0 <= 0) {
                    Observable.error<MashData>(ExceptionMissingSettings(ctx.resources.getString(R.string.error_needs_font)))
                } else if (textgravity?.active?.size ?: 0 <= 0) {
                    Observable.error<MashData>(ExceptionMissingSettings(ctx.resources.getString(R.string.error_needs_gravity)))
                } else {
                    var imageObservable = getImageUrlFromSub(0, 20, screenWidth, screenHeight)
                    var commentObservable = getCommentFromSub(0, 20)
                    var fontObservable = Observable.just(typefaces!!.getRandomActive())
                    var textGrav = Observable.just(textgravity!!.getRandomActive())
                    Observable.zip(imageObservable, commentObservable, fontObservable, textGrav) { img, com, font, tg -> MashData(img, com, font, tg) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext() {
                                if (!(activeDataSubscription?.isUnsubscribed ?: true)) {
                                    activeData = it
                                }
                            }

                }

        activeDataSubject = BehaviorSubject.create()
        activeDataSubscription = observable.subscribe(activeDataSubject)
        return activeDataSubject!!.asObservable()
    }

    fun getImageUrlFromSub(attemptNum: Int, maxAttempts: Int, targetWidth: Int, targetHeight: Int): Observable<String> {
        var subreddit = imageReddits?.getRandomActive()
        Timber.d("getImageUrlFromSub:" + subreddit + " attemptNum:" + attemptNum + " maxAttempts:" + maxAttempts)
        if (subreddit == null) {
            return Observable.error(ExceptionMissingSettings("No active image subreddits"))
        } else {
            return service.getAuthenticatedService()
                    .flatMap {
                        it.getRandomSubredditPost(subreddit, UUID.randomUUID().toString())
                                .flatMap {
                                    Timber.d("inFlatMap")
                                    //Load up all the t3s
                                    var t3s = ArrayList<t3>()
                                    for (item in it) {
                                        var tmp = item.data
                                        if (tmp is t3) {
                                            t3s.add(tmp)
                                        } else if (tmp is Listing) {
                                            t3s.addAll(getT3sFromListing(tmp))
                                        }

                                        if (t3s.size > 50) {
                                            //This is a big enough sampling
                                            Timber.d("t3s.size reached limit. Breaking.")
                                            break;
                                        }
                                    }

                                    Timber.d("collected t3s:" + t3s.size)
                                    //Get a filtered list of comments from the t3s
                                    var urls = ArrayList<String>()
                                    for (t3 in t3s) {
                                        getImageUrlFromT3(t3, targetWidth, targetHeight)?.let {
                                            urls.add(it)
                                        }
                                    }
                                    Timber.d("collected urls:" + urls.size)

                                    if (urls.size > 0) {
                                        Observable.just(urls[random.nextInt(urls.size)])
                                    } else {
                                        Observable.empty()
                                    }
                                }
                    }
                    .switchIfEmpty(
                            Observable.just(attemptNum < maxAttempts).flatMap {
                                if (it) getImageUrlFromSub(attemptNum + 1, maxAttempts, targetWidth, targetHeight) else Observable.error<String>(ExceptionNoImageInPost("Fail"))
                            }
                    )
                    .doOnNext { Timber.d("getImageUrlFromSub attempt#" + attemptNum + " of max#" + maxAttempts + " url:" + it) }
        }
    }

    fun getCommentFromSub(attemptNum: Int, maxAttempts: Int): Observable<String> {
        var subreddit = commentReddits?.getRandomActive()
        Timber.d("getCommentFromSub:" + subreddit + " attemptNum:" + attemptNum + " maxAttempts:" + maxAttempts)
        if (subreddit == null) {
            return Observable.error(ExceptionMissingSettings("No active comment subreddits"))
        } else {
            return service.getAuthenticatedService()
                    .flatMap {
                        it.getRandomSubredditPost(subreddit, UUID.randomUUID().toString())
                                .flatMap {
                                    Timber.d("inFlatMap")
                                    //Load up all the t1s
                                    var t1s = ArrayList<t1>()
                                    for (item in it) {
                                        var tmp = item.data
                                        if (tmp is t1) {
                                            t1s.add(tmp)
                                        } else if (tmp is Listing) {
                                            t1s.addAll(getT1sFromListing(tmp))
                                        }

                                        if (t1s.size > 50) {
                                            //This is a big enough sampling
                                            Timber.d("t1s.size reached limit. Breaking.")
                                            break;
                                        }
                                    }

                                    Timber.d("collected t1s:" + t1s.size)

                                    //Get a filtered list of comments from the t1s
                                    var comments = ArrayList<String>()
                                    var maxCommentsSize = 100
                                    for (t1 in t1s) {
                                        var t1Comments = getCommentsFromT1(t1)
                                        for (t1Comment in t1Comments) {
                                            if (!COM_RGX_DELETED.matches(t1Comment) && !COM_RGX_LINK.matches(t1Comment)) {
                                                var comment = t1Comment.replace(COM_RGX_EMBEDDEDLINK, "$1").replace(COM_RGX_SPACE, " ")
                                                if (comment.length > 0 && comment.length < COMMENT_MAX_LENGTH) {
                                                    if (!comments.contains(comment)) {
                                                        comments.add(comment)
                                                    } else {
                                                        Timber.d("comment was dup:" + comment)
                                                    }
                                                }
                                            }
                                            //This is a big enough sampling..
                                            if (comments.size > maxCommentsSize) {
                                                Timber.d("comments.size reached limit. Breaking.")
                                                break;
                                            }
                                        }

                                        //This is a big enough sampling..
                                        if (comments.size > maxCommentsSize) {
                                            Timber.d("comments.size reached limit. Breaking.")
                                            break;
                                        }
                                    }
                                    Timber.d("collected comments:" + comments.size)

                                    if (comments.size > 0) {
                                        Observable.just(comments[random.nextInt(comments.size)])
                                    } else {
                                        Observable.empty()
                                    }
                                }
                    }
                    .switchIfEmpty(
                            Observable.just(attemptNum < maxAttempts).flatMap {
                                if (it) getCommentFromSub(attemptNum + 1, maxAttempts) else Observable.error<String>(ExceptionNoCommentsInPost("Fail"))
                            }
                    )
                    .doOnNext { Timber.d("getCommentFromSub attempt#" + attemptNum + " comment:" + it) }
        }
    }

    fun getImageUrlFromT3(data: t3, targetWidth: Int, targetHeight: Int): String? {
        Timber.d("getImageUrlFromT3 t3:" + data.title + " tw:" + targetWidth + " th:" + targetHeight)
        return data.preview?.images?.first()?.let {
            var images = ArrayList<ImageInfo>()
            it.source?.let {
                images.add(it)
            }

            it.resolutions?.let {
                for (image in it) {
                    images.add(image)
                }
            }
            var hDom = targetHeight > targetWidth
            var closest: ImageInfo? = null
            var closestPercDiff = 1000f
            for (image in images) {
                var percDiff = if (hDom) Math.abs(100 - ((image.height.toFloat() / targetHeight.toFloat()) * 100)) else Math.abs(100 - ((image.width.toFloat() / targetWidth.toFloat()) * 100))
                Timber.d("getImageUrlFromT3 seeking - hdom:" + hDom + " w:" + targetWidth + " h:" + targetHeight + " closest w:" + closest?.width + " h:" + closest?.height + " %:" + closestPercDiff + " current - w:" + image.width + " h:" + image.height + " %:" + percDiff)
                if (closest == null) {
                    closest = image
                    closestPercDiff = percDiff
                } else if (percDiff < closestPercDiff) {
                    closest = image
                    closestPercDiff = percDiff
                }
            }
            Timber.d("getImageUrlFromT3 seeking - w:" + targetWidth + " h:" + targetHeight + " found w:" + closest?.width + " h:" + closest?.height)
            return closest?.url?.let {
                Html.fromHtml(it).toString()
            }
        }
    }

    fun getT1sFromListing(listing: Listing): List<t1> {
        var list = ArrayList<t1>()
        var children = listing.children ?: ArrayList()
        for (child in children) {
            if (child.data is t1) {
                list.add(child.data as t1)
            } else if (child.data is Listing) {
                list.addAll(getT1sFromListing(child.data as Listing))
            }
        }
        return list
    }

    fun getT3sFromListing(listing: Listing): List<t3> {
        var list = ArrayList<t3>()
        var children = listing.children ?: ArrayList()
        for (child in children) {
            if (child.data is t3) {
                list.add(child.data as t3)
            } else if (child.data is Listing) {
                list.addAll(getT3sFromListing(child.data as Listing))
            }
        }
        return list
    }

    fun getCommentsFromT1(data: t1): List<String> {
        var list = ArrayList<String>()
        if (!data.body.isNullOrEmpty()) {
            list.add(data.body as String)
        }

        var dataReplies = data.replies
        if (dataReplies != null) {
            if (dataReplies.data is t1) {
                list.addAll(getCommentsFromT1(dataReplies.data as t1))
            } else if (dataReplies.data is Listing) {
                var t1sinlisting = getT1sFromListing(dataReplies.data as Listing)
                for (t1inlist in t1sinlisting) {
                    list.addAll(getCommentsFromT1(t1inlist))
                }
            }
        }
        return list
    }


    private fun getUniqueId(): String {
        context?.getSharedPreferences(PREF_KEY_APPINFO, Context.MODE_PRIVATE)?.let {
            var pref = it.getString(PREF_UNIQUE_ID, "")
            if (pref.isNullOrEmpty()) {
                pref = UUID.randomUUID().toString()
                with(it.edit()) {
                    putString(PREF_UNIQUE_ID, pref)
                    commit()
                }
            }
            return pref
        }
        return ""
    }

}