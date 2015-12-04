package com.hotpodata.baconmasher

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import com.hotpodata.baconforkotlin.Bacon
import com.hotpodata.baconforkotlin.network.model.Listing
import com.hotpodata.baconforkotlin.network.model.t1
import com.hotpodata.baconforkotlin.network.model.t3
import com.hotpodata.baconmasher.data.*
import com.hotpodata.baconmasher.utils.ImgurUtils
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.filterNotNull
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

    //Regex
    val IMG_RGX_RAW = "(?:([^:/?#]+):)?(?://([^/?#]*))?([^?#]*\\.(?:jpg|png))$".toRegex()
    val COM_RGX_DELETED = "^(\\[deleted\\]|\\[DELETED\\]|\\[removed\\]|\\[REMOVED\\])$".toRegex()
    val COM_RGX_LINK = Patterns.WEB_URL.toRegex()
    val COM_RGX_EMBEDDEDLINK = "\\[(.*?)\\]\\((.*?)\\)".toRegex()
    val COM_RGX_SPACE = "\\s+".toRegex()

    //var activeConfig: MashConfig? = null
    var activeData: MashData? = null
    var activeDataSubject: BehaviorSubject<MashData>? = null
    var activeDataSubscription: Subscription? = null

    var imageReddits: ActiveStringManager? = null
    var commentReddits: ActiveStringManager? = null
    var typefaces: ActiveStringManager? = null
    var textgravity: ActiveStringManager? = null

    val random = Random()
    var context: Context? = null

    private var _bacon: Bacon? = null
    var bacon: Bacon
        get() {
            if (_bacon == null) {
                _bacon = Bacon("https://www.reddit.com", "android:com.hotpodata.baconmasher.free:v1.0.0 (by /u/hotpodata)")
            }
            return _bacon!!
        }
        set(value) {
            _bacon = value
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

        if (!(activeDataSubscription?.isUnsubscribed ?: true)) {
            activeDataSubscription?.unsubscribe()
            activeDataSubscription = null
        }
        if (activeDataSubject != null) {
            activeDataSubject?.onCompleted()
            activeDataSubject = null
        }

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
                    var imageObservable = getImageUrlFromSub(0, 20)
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

    fun getImageUrlFromSub(attemptNum: Int, maxAttempts: Int): Observable<String> {
        var subreddit = imageReddits?.getRandomActive()
        Timber.d("getImageUrlFromSub:" + subreddit + " attemptNum:" + attemptNum + " maxAttempts:" + maxAttempts)
        if (subreddit == null) {
            return Observable.error(ExceptionMissingSettings("No active image subreddits"))
        } else {
            return bacon.service.getRandomSubredditPost(subreddit, UUID.randomUUID().toString())
                    .flatMap {
                        Observable.from(it)
                    }
                    .map {
                        it.data
                    }
                    .flatMap {
                        if (it is t3) {
                            Observable.just(it)
                        } else if (it is Listing) {
                            Observable.from(getT3sFromListing(it))
                        } else {
                            Observable.empty()
                        }
                    }
                    .map {
                        var url = getImageUrlFromT3(it as t3)
                        Timber.d("gotUrlFromT3:" + url)
                        url
                    }
                    .filterNotNull()
                    .switchIfEmpty(if (attemptNum < maxAttempts) getImageUrlFromSub(attemptNum + 1, maxAttempts) else Observable.error<String>(ExceptionNoImageInPost("Fail")))
                    .doOnNext { Timber.d("getImageUrlFromSub attempt#" + attemptNum + " url:" + it) }
        }
    }

    fun getCommentFromSub(attemptNum: Int, maxAttempts: Int): Observable<String> {
        var subreddit = imageReddits?.getRandomActive()
        Timber.d("getImageUrlFromSub:" + subreddit + " attemptNum:" + attemptNum + " maxAttempts:" + maxAttempts)
        if (subreddit == null) {
            return Observable.error(ExceptionMissingSettings("No active image subreddits"))
        } else {
            return bacon.service.getRandomSubredditPost(subreddit, UUID.randomUUID().toString())
                    .flatMap {
                        Observable.from(it)
                    }
                    .map {
                        it.data
                    }
                    .flatMap {
                        if (it is t1) {
                            Observable.just(it)
                        } else if (it is Listing) {
                            Observable.from(getT1sFromListing(it))
                        } else {
                            Observable.empty()
                        }
                    }
                    .flatMap {
                        Observable.from(getCommentsFromT1(it))
                    }
                    .filter {
                        //Filter out comments that are plain links and deleted/removed
                        !COM_RGX_DELETED.matches(it) && !COM_RGX_LINK.matches(it)
                    }
                    .map {
                        it.replace(COM_RGX_EMBEDDEDLINK, "$1").replace(COM_RGX_SPACE, " ")
                    }
                    .filter { it.length < COMMENT_MAX_LENGTH }
                    .toList()
                    .filter { it.size > 0 }
                    .map {
                        it[random.nextInt(it.size)]
                    }
                    .filter { it.length > 0 }
                    .switchIfEmpty(if (attemptNum < maxAttempts) getCommentFromSub(attemptNum + 1, maxAttempts) else Observable.error(ExceptionNoCommentsInPost("Fail")))
                    .doOnNext { Timber.d("getCommentFromSub attempt#" + attemptNum + " comment:" + it) }
        }
    }

    fun getImageUrlFromT3(data: t3): String? {
        Timber.d("getImageUrlFromT3 t3:" + data.title)
        var url = data.url
        if (url != null && !TextUtils.isEmpty(url)) {
            if (ImgurUtils.isImgurUrl(url)) {
                //IMGUR LINKS
                if (ImgurUtils.isImgurDirectLinkUrl(url)) {
                    return url
                } else if (ImgurUtils.isImgurHashLinkUrl(url)) {
                    return url + ".png"
                }
                //TODO: Support imgur albums and galleries
            } else if (url.matches(IMG_RGX_RAW)) {
                //OTHER DIRECT IMAGE LINKS
                return url
            }
            //TODO: Support more image hosts
        }
        return null
    }

    fun getT1sFromListing(listing: Listing): List<t1> {
        var list = ArrayList<t1>()
        var children = listing?.children ?: ArrayList()
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
        var children = listing?.children ?: ArrayList()
        for (child in children) {
            if (child.data is t3) {
                list.add(child.data as t3)
            } else if (child.data is Listing) {
                list.addAll(getT3sFromListing(child.data as Listing))
            }
        }
        return list
    }

    fun getCommentsFromT1(data: t1): List<String>{
        var list = ArrayList<String>()
        if(!data.body.isNullOrEmpty()){
            list.add(data.body as String)
        }

        var dataReplies = data.replies
        if(dataReplies != null) {
            if(dataReplies.data is t1){
                list.addAll(getCommentsFromT1(dataReplies.data as t1))
            }else if(dataReplies.data is Listing){
                var t1sinlisting = getT1sFromListing(dataReplies.data as Listing)
                for(t1inlist in t1sinlisting){
                    list.addAll(getCommentsFromT1(t1inlist))
                }
            }
        }
        return list
    }
}