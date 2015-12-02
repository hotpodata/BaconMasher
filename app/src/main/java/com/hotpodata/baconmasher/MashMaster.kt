package com.hotpodata.baconmasher

import android.content.Context
import android.text.TextUtils
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
    val IMAGE_POOL_SIZE = 50
    val COMMENT_POOL_SIZE = 100

    val PREF_KEY_IMAGE_REDDITS = "IMAGE_REDDITS"
    val PREF_KEY_COMMENT_REDDITS = "COMMENT_REDDITS"
    val PREF_KEY_TYPEFACES = "TYPEFACES"
    val PREF_KEY_TEXTGRAVITY = "TEXTGRAVITY"

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
                Timber.d("IMGURTEST isImgurUrl:" + url)
                if (ImgurUtils.isImgurDirectLinkUrl(url)) {
                    Timber.d("IMGURTEST isImgurDirectLinkUrl")
                    return url
                } else if (ImgurUtils.isImgurHashLinkUrl(url)) {
                    Timber.d("IMGURTEST isImgurHashLinkUrl")
                    return url + ".png"
                }
                //TODO: Support imgur albums and galleries
            } else if (url.matches("(?:([^:/?#]+):)?(?://([^/?#]*))?([^?#]*\\.(?:jpg|gif|png))(?:\\?([^#]*))?(?:#(.*))?".toRegex())) {
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
                .filter { it != null && it.data != null && (it.data as? Listing != null) }
                .map { it.data }
                .cast(Listing::class.java)
                .flatMap {
                    //List is a list of articles from the supplied subreddit
                    var scrambledChildren = if (it.children == null) ArrayList() else ArrayList(it.children)
                    Collections.shuffle(scrambledChildren)
                    //We return that list but in scrambled order for better randomness
                    Observable.from(scrambledChildren)
                            .filterNotNull()
                            .map { it.data }
                            .filterNotNull()
                            .filter { it is t3 }
                            .cast(t3::class.java)
                            .filter { it.num_comments > 0 }
                            .map { it.id }
                            .filterNotNull()
                            .flatMap {
                                getCommentsFromSubArticle(subreddit, it)
                            }

                }
    }

    fun getCommentsFromSubArticle(subreddit: String, articleId: String): Observable<String> {
        Timber.d("getCommentsFromSubArticle subreddit:" + subreddit + " articleId:" + articleId)
        return Bacon.service
                .getArticleComments(subreddit, articleId)
                .flatMap {
                    Observable.from(it)
                            .filterNotNull()
                            .map { it.data }
                            .filterNotNull()
                            .flatMap {
                                if (it is t1) {
                                    Observable.just(it)
                                } else if (it is Listing) {
                                    getT1sFromListing(it)
                                } else {
                                    Observable.empty()
                                }
                            }
                            .flatMap {
                                getCommentsFromT1(it)
                            }
                            .filterNotNull()
                }
    }

    fun getT1sFromListing(listing: Listing): Observable<t1> {
        Timber.d("getT1sFromListing listing")
        if (listing.children != null) {
            return Observable.from(listing.children)
                    .filterNotNull()
                    .map { it.data }
                    .filterNotNull()
                    .filter { it is t1 || it is Listing }
                    .flatMap {
                        if (it is t1) {
                            Observable.just(it)
                        } else if (it is Listing) {
                            getT1sFromListing(it)
                        } else {
                            Observable.empty()
                        }
                    }
        }
        return Observable.empty()
    }

    fun getCommentsFromT1(data: t1): Observable<String> {
        Timber.d("getCommentsFromT1 t1:" + data.subreddit)
        if (data.body != null) {
            return Observable
                    .just(data.replies)
                    .filterNotNull()
                    .map { it.data }
                    .filterNotNull()
                    .flatMap {
                        if (it is Listing) {
                            getT1sFromListing(it)
                        } else if (it is t1) {
                            Observable.just(it)
                        } else {
                            Observable.empty<t1>()
                        }
                    }
                    .flatMap {
                        getCommentsFromT1(it)
                    }
                    .startWith(data.body)
        } else {
            return Observable.empty()
        }
    }

}