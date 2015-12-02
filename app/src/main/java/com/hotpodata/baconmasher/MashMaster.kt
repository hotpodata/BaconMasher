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
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import timber.log.Timber
import java.util.*
import kotlin.text.Regex

/**
 * Created by jdrotos on 11/29/15.
 */
object MashMaster {
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

        var observable : Observable<MashData> =
                if (imageReddits?.active?.size ?: 0 <= 0) {
                    Observable.error<MashData>(ExceptionMissingSettings(ctx.resources.getString(R.string.error_needs_image_subreddit)))
                } else if (commentReddits?.active?.size ?: 0 <= 0) {
                    Observable.error<MashData>(ExceptionMissingSettings(ctx.resources.getString(R.string.error_needs_comment_subreddit)))
                } else if (typefaces?.active?.size ?: 0 <= 0) {
                    Observable.error<MashData>(ExceptionMissingSettings(ctx.resources.getString(R.string.error_needs_font)))
                } else if (textgravity?.active?.size ?: 0 <= 0) {
                    Observable.error<MashData>(ExceptionMissingSettings(ctx.resources.getString(R.string.error_needs_gravity)))
                } else {
                    var imageObservable = genImageUrlObservable(imageReddits!!.getRandomActive())
                    var commentObservable = genCommentObservable(commentReddits!!.getRandomActive())
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

    fun genImageUrlObservable(subreddit: String): Observable<String> {
        return Bacon.service.getSubReddit(subreddit).flatMap {
            var data = it.data
            var imageurls = ArrayList<String>()
            if (data is Listing && data.children != null) {
                Timber.d("genImageUrlObservable data is Listing")

                for (child in data.children!!) {
                    Timber.d("genImageUrlObservable child:" + child.kind + " data:" + child.data)
                    //val x: String? = y as? String
                    var childData: t3? = child.data as? t3
                    if (childData != null) {
                        Timber.d("genImageUrlObservable child is t3:" + childData.url)
                        var url = childData.url
                        if (!TextUtils.isEmpty(url) && url!!.matches(Regex("(?:([^:/?#]+):)?(?://([^/?#]*))?([^?#]*\\.(?:jpg|gif|png))(?:\\?([^#]*))?(?:#(.*))?"))) {
                            imageurls.add(url)
                        }
                    }
                }
                Timber.d("genImageUrlObservable imageurls.size:" + imageurls.size)
            }
            if (imageurls.size > 0) {
                Observable.just(imageurls.get(Random().nextInt(imageurls.size)))
            } else {
                Observable.error<String>(RuntimeException("Couldn't find images in subreddit"))
            }
        }
    }

    fun genCommentObservable(subreddit: String): Observable<String> {
        return Bacon.service.getSubReddit(subreddit).flatMap {
            var data = it.data
            var articleIds = ArrayList<String>()
            if (data is Listing && data.children != null) {

                for (child in data.children!!) {
                    var childData: t3? = child.data as? t3
                    if (childData != null) {
                        var id = childData.id
                        if (childData.num_comments > 0 && id != null) {
                            articleIds.add(id)
                        }
                    }
                }

            }
            if (articleIds.size > 0) {
                Observable.just(articleIds.get(Random().nextInt(articleIds.size)))
            } else {
                Observable.error<String>(RuntimeException("Couldn't find articles with comments in subreddit"))
            }
        }.flatMap {
            Bacon.service.getArticleComments(subreddit, it).flatMap {
                var comments = ArrayList<String>()
                if (it != null && it.size > 0) {
                    Timber.d("it != null && it.size > 0")
                    for (thing in it) {
                        Timber.d("data.kind:" + thing.kind)
                        var listing: Listing? = thing.data as? Listing
                        if (listing != null) {
                            Timber.d("data was Listing!")
                            if (listing.children != null) {
                                Timber.d("listing.children != null")
                                for (child in listing.children!!) {
                                    Timber.d("child in listing.children!!")
                                    var childData: t1? = child.data as? t1
                                    if (childData != null) {
                                        Timber.d("childData is t1")
                                        comments.addAll(getAllComments(childData))
                                    }
                                }
                            }
                        } else {
                            Timber.d("data was NOT Listing!")
                            var childData: t1? = thing.data as? t1
                            if (childData != null) {
                                Timber.d("childData is t1")
                                comments.addAll(getAllComments(childData))
                            }
                        }
                    }

                }
                if (comments.size > 0) {
                    //TODO: SMARTER COMMENT SELECTION?
                    Observable.just(comments.get(Random().nextInt(comments.size)))
                } else {
                    Observable.error<String>(RuntimeException("Couldn't find comments in article"))
                }
            }

        }
    }

    fun getAllComments(data: t1): ArrayList<String> {
        Timber.d("getAllComments:" + data.body)
        var comments = ArrayList<String>()
        if (!TextUtils.isEmpty(data.body)) {
            comments.add(data.body!!)
        }
        return comments
    }

}