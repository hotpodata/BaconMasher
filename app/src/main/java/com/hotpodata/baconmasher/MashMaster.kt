package com.hotpodata.baconmasher

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import com.hotpodata.baconforkotlin.Bacon
import com.hotpodata.baconforkotlin.network.model.Listing
import com.hotpodata.baconforkotlin.network.model.t1
import com.hotpodata.baconforkotlin.network.model.t3
import com.hotpodata.baconmasher.data.ActiveStringManager
import com.hotpodata.baconmasher.data.ExceptionMissingSettings
import com.hotpodata.baconmasher.data.GravityStringer
import com.hotpodata.baconmasher.data.MashData
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
    val IMAGE_POOL_SIZE = 100
    val COMMENT_POOL_SIZE = 100

    val PREF_KEY_IMAGE_REDDITS = "IMAGE_REDDITS"
    val PREF_KEY_COMMENT_REDDITS = "COMMENT_REDDITS"
    val PREF_KEY_TYPEFACES = "TYPEFACES"
    val PREF_KEY_TEXTGRAVITY = "TEXTGRAVITY"

    //Regex
    val IMG_RGX_RAW = "(?:([^:/?#]+):)?(?://([^/?#]*))?([^?#]*\\.(?:jpg|gif|png))(?:\\?([^#]*))?(?:#(.*))?".toRegex()
    val COM_RGX_DELETED = "^(\\[deleted\\]|\\[DELETED\\]|\\[removed\\]|\\[REMOVED\\])$".toRegex()
    val COM_RGX_LINK = Patterns.WEB_URL.toRegex()
    val COM_RGX_EMBEDDEDLINK = "\\[.*\\]\\(.*\\)"

    //var activeConfig: MashConfig? = null
    var activeData: MashData? = null
    var activeDataSubject: BehaviorSubject<MashData>? = null
    var activeDataSubscription: Subscription? = null

    var imageReddits: ActiveStringManager? = null
    var commentReddits: ActiveStringManager? = null
    var typefaces: ActiveStringManager? = null
    var textgravity: ActiveStringManager? = null

    val random = Random()

    public fun initMashMaster(ctx: Context) {
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
                    var imageObservable = getImageUrlFromSub(imageReddits!!.getRandomActive(), IMAGE_POOL_SIZE)
                    //var imageObservable = Observable.just("http://i.imgur.com/692QYLe.jpg")
                    var commentObservable = getCommentFromSub(commentReddits!!.getRandomActive(), COMMENT_POOL_SIZE)
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

    fun getImageUrlSampleFromSub(subreddit: String, next: String?): Observable<String> {
        Timber.d("getImageUrlSampleFromSub subreddit:" + subreddit + " next:" + next)
        return Bacon.service.getSubReddit(subreddit, next)
                .filter { it != null && it.data != null && it.data is Listing }
                .map { it.data }
                .cast(Listing::class.java)
                .flatMap {
                    (if (it.after == null) Observable.empty() else getImageUrlSampleFromSub(subreddit, it.after)).startWith(
                            Observable.from(it.children)
                                    .filter() { it != null && it.data != null && it.data is t3 }
                                    .map { it.data }
                                    .cast(t3::class.java)
                                    .map { getImageUrlFromT3(it) }
                                    .filterNotNull())

                }
    }

    fun getImageUrlFromSub(subreddit: String, sampleSize: Int): Observable<String> {
        Timber.d("getImageUrlFromSub subreddit:" + subreddit + " sampleSize:" + sampleSize)
        return getImageUrlSampleFromSub(subreddit, null)
                .distinct()
                .take(sampleSize)
                .toList()
                .map {
                    Timber.d("getImageUrlFromSub samples got:" + it.size)
                    if (it.size > 0) {
                        it[random.nextInt(it.size)]
                    } else {
                        ""
                    }
                }
    }

    fun getCommentFromSub(subreddit: String, sampleSize: Int): Observable<String> {
        Timber.d("getCommentFromSub subreddit:" + subreddit + " sampleSize:" + sampleSize)
        return getCommentSampleFromSub(subreddit, null)
                .distinct()
                .filter {
                    //Filter out comments that are plain links and deleted/removed
                    !COM_RGX_DELETED.matches(it) && !COM_RGX_LINK.matches(it)
                }
                .map {
                    //IF the comment contains embedded links, just replace with the text portion
                    it.replace("\\[(.*?)\\]\\((.*?)\\)".toRegex(), "$1")
                }
                .take(sampleSize)
                .toList()
                .map {
                    Timber.d("getCommentFromSub samples got:" + it.size)
                    if (it.size > 0) {
                        it[random.nextInt(it.size)]
                    } else {
                        ""
                    }
                }
    }

    fun getCommentSampleFromSub(subreddit: String, next: String?): Observable<String> {
        Timber.d("getCommentSampleFromSub subreddit:" + subreddit + " next:" + next)
        return Bacon.service.getSubReddit(subreddit, next)
                .filter { it.data is Listing }
                .map { it.data as Listing }
                .flatMap {
                    Timber.d("getCommentSampleFromSub subreddit:" + subreddit + " articles:" + it.children?.size)
                    //List is a list of articles from the supplied subreddit
                    var scrambledChildren = if (it.children == null) ArrayList() else ArrayList(it.children)
                    Collections.shuffle(scrambledChildren)
                    //We return that list but in scrambled order for better randomness
                    Observable.from(scrambledChildren)
                            .filter { it.data is t3 }
                            .map { it.data as t3 }
                            .filter { it.num_comments > 0 }
                            .map { it.id }
                            .flatMap {
                                Timber.d("getCommentSampleFromSub subreddit:" + subreddit + " articlesid:" + it)
                                if (it != null) {
                                    getCommentsFromSubArticle(subreddit, it)
                                } else {
                                    Observable.empty()
                                }
                            }

                }
    }

    fun getCommentsFromSubArticle(subreddit: String, articleId: String): Observable<String> {
        Timber.d("getCommentsFromSubArticle subreddit:" + subreddit + " articleId:" + articleId)
        return Bacon.service
                .getArticleComments(subreddit, articleId, 3, 25)//TODO: THINK ABOUT THIS
                .flatMap {
                    Timber.d("getCommentsFromSubArticle - got array of results:" + it.size)
                    Observable.from(it)
                            .map { it.data }
                            .flatMap {
                                if (it is t1) {
                                    Timber.d("getCommentsFromSubArticle - child is t1")
                                    Observable.just(it)
                                } else if (it is Listing) {
                                    Timber.d("getCommentsFromSubArticle - child is Listing")
                                    getT1sFromListing(it)
                                } else {
                                    Timber.d("getCommentsFromSubArticle - child is neither")
                                    Observable.empty()
                                }
                            }
                            .flatMap {
                                Timber.d("getCommentsFromSubArticle - t1 got- calling getcommentsFromT1")
                                getCommentsFromT1(it)
                            }
                }
    }

    fun getT1sFromListing(listing: Listing): Observable<t1> {
        Timber.d("getT1sFromListing listing:" + listing.modhash)
        if (listing.children != null) {
            return Observable.from(listing.children)
                    .map { it.data }
                    .flatMap {
                        if (it is t1) {
                            Timber.d("getT1sFromListing child is t1 listing:" + listing.modhash)
                            Observable.just(it)
                        } else if (it is Listing) {
                            Timber.d("getT1sFromListing child is Listing, calling getT1sFromListing - listing:" + listing.modhash)
                            getT1sFromListing(it)
                        } else {
                            Timber.d("getT1sFromListing child is neither- listing:" + listing.modhash)
                            Observable.empty()
                        }
                    }
        }
        return Observable.empty()
    }

    fun getCommentsFromT1(data: t1): Observable<String> {
        Timber.d("getCommentsFromT1 t1:" + data.body)
        if (data.body != null) {
            return Observable
                    .just(data.replies)
                    .map { it?.data }
                    .flatMap {
                        if (it is Listing) {
                            Timber.d("getCommentsFromT1 - calling getT1sFromListing modhash:" + it.modhash)
                            getT1sFromListing(it)
                        } else if (it is t1) {
                            Timber.d("getCommentsFromT1 - it is t1 returning body:" + it.body)
                            Observable.just(it)
                        } else {
                            Timber.d("getCommentsFromT1 - return empty")
                            Observable.empty<t1>()
                        }
                    }
                    .flatMap {
                        Timber.d("getCommentsFromT1 - got t1:" + it.body)
                        getCommentsFromT1(it)
                    }
                    .startWith(data.body)
        } else {
            return Observable.empty()
        }
    }

}