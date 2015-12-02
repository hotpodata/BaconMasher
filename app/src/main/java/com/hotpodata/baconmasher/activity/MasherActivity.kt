package com.hotpodata.baconmasher.activity;

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.FileProvider
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.analytics.HitBuilders
import com.hotpodata.baconmasher.AnalyticsMaster
import com.hotpodata.baconmasher.MashMaster
import com.hotpodata.baconmasher.R
import com.hotpodata.baconmasher.adapter.MasherSettingsAdapter
import com.hotpodata.baconmasher.adapter.SideBarAdapter
import com.hotpodata.baconmasher.data.ExceptionMissingSettings
import com.hotpodata.baconmasher.data.GravityStringer
import com.hotpodata.baconmasher.data.MashData
import com.hotpodata.baconmasher.data.TypefaceCache
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.json.JSONObject
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

public class MasherActivity : AppCompatActivity() {

    val STATE_LAST_MASH = "STATE_LAST_MASH"

    var mashContentContainer: FrameLayout? = null
    var mashTextView: TextView? = null
    var mashImageView: ImageView? = null

    var drawerLayout: DrawerLayout? = null
    var leftDrawer: RecyclerView? = null
    var rightDrawer: RecyclerView? = null
    var drawerToggle: ActionBarDrawerToggle? = null

    var mashLoadingContainer: FrameLayout? = null
    var loadingBaconMasher: View? = null
    var loadingBacon: View? = null

    var fab: FloatingActionButton? = null
    var toolbar: Toolbar? = null

    var sideBarAdapter: SideBarAdapter? = null
    var settingsAdapter: MasherSettingsAdapter? = null

    var animLoading: ValueAnimator? = null
    var animHideMash: ValueAnimator? = null
    var animShowMash: ValueAnimator? = null

    var subMash: Subscription? = null
    var subShare: Subscription? = null

    var immersiveMode = false
    var resumed = false

    var lastMash: MashData? = null
    var shareProgDialog: ProgressDialog? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masher);
        toolbar = findViewById(R.id.toolbar) as Toolbar;
        fab = findViewById(R.id.fab) as FloatingActionButton;
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        leftDrawer = findViewById(R.id.left_drawer) as RecyclerView
        rightDrawer = findViewById(R.id.right_drawer) as RecyclerView
        mashTextView = findViewById(R.id.text) as TextView?
        mashImageView = findViewById(R.id.image) as ImageView?
        loadingBaconMasher = findViewById(R.id.bacon_masher)
        loadingBacon = findViewById(R.id.bacon)
        mashContentContainer = findViewById(R.id.mash_content_container) as FrameLayout;
        mashLoadingContainer = findViewById(R.id.mash_loading_container) as FrameLayout;

        setSupportActionBar(toolbar);

        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_closed) {
            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
            }
        }
        drawerLayout?.setDrawerListener(drawerToggle)

        fab?.setOnClickListener() {
            if (subMash == null || subMash?.isUnsubscribed() ?: false) {
                actionMashBacon()
            }
        };

        mashLoadingContainer?.setOnClickListener() {
            if (subMash == null || subMash?.isUnsubscribed() ?: false) {
                actionMashBacon()
            }
        }

        mashImageView?.setOnClickListener() {
            if (immersiveMode) {
                showSystemUI()
            } else {
                hideSystemUI()
            }
            immersiveMode = !immersiveMode
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setShowHideAnimationEnabled(true)

        setUpLeftDrawer()
        setUpRightDrawer()

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_LAST_MASH)) {
            try {
                var mashData = MashData.Serializer.fromJSON(JSONObject(savedInstanceState.getString(STATE_LAST_MASH)))
                if (mashData != null) {
                    bindMash(mashData)
                }
            } catch(ex: Exception) {
                Timber.e(ex, "Failure to parse out mash data from savedInstanceState")
            }
        }

        if (lastMash == null) {
            fab?.hide()
            setMashHidden()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopHideMashAnim()
        stopLoadingAnimation()
        stopShowMashAnim()
        subMash?.unsubscribe()
    }

    public override fun onResume() {
        super.onResume()
        resumed = true



        Timber.i("Setting screen name:" + AnalyticsMaster.SCREEN_MASHER);
        AnalyticsMaster.getTracker(this).setScreenName(AnalyticsMaster.SCREEN_MASHER);
        AnalyticsMaster.getTracker(this).send(HitBuilders.ScreenViewBuilder().build());
    }

    public override fun onPause() {
        super.onPause()
        resumed = false
        subShare?.unsubscribe()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    public override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_masher, menu);
        return true;
    }


    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (drawerToggle?.onOptionsItemSelected(item) ?: false) {
            return true
        }

        when (item.itemId) {
            R.id.action_settings -> {
                drawerLayout?.openDrawer(rightDrawer)
                return true
            }
            R.id.action_share -> {
                actionShare()
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        var item = menu?.findItem(R.id.action_share)
        item?.setVisible(lastMash != null)
        item?.setEnabled(lastMash != null)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSaveInstanceState(out: Bundle?) {
        super.onSaveInstanceState(out)
        var mash = lastMash
        if (mash != null) {
            out?.putString(STATE_LAST_MASH, MashData.Serializer.toJSON(mash).toString())
        }
    }


    fun actionMashBacon() {
        subMash?.unsubscribe()
        subMash = MashMaster
                .doMash(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    lastMash = null
                    startHideMashAnim()
                    startLoadingAnimation()
                    supportInvalidateOptionsMenu()
                }
                .subscribe (
                        {
                            bindMash(it)
                        },
                        {
                            Timber.e(it, "fail")
                            stopHideMashAnim()
                            stopLoadingAnimation()
                            setMashHidden()
                            if (it is ExceptionMissingSettings) {
                                drawerLayout?.openDrawer(rightDrawer)
                            }
                            showErrorDialog(it.message ?: getString(R.string.unknown_error))
                        })
    }


    fun bindMash(data: MashData) {
        Picasso.with(this)
                .load(data.imageUrl)
                .noFade()
                .noPlaceholder()
                .fit()
                .centerCrop()
                .into(mashImageView, object : Callback {
                    override fun onSuccess() {
                        if (resumed) {
                            startShowMashAnim()
                            stopLoadingAnimation()
                        } else {
                            setMashShowing()
                        }
                    }

                    override fun onError() {
                        stopLoadingAnimation()
                        showErrorDialog(getString(R.string.error_couldnt_load_image))
                    }
                })

        mashTextView?.gravity = GravityStringer.strToGrav[data.textGravity]
        mashTextView?.text = data.comment
        mashTextView?.typeface = TypefaceCache.getTypeFace(this, data.font);
        lastMash = data
    }

    fun setMashShowing() {
        mashTextView?.translationX = 0f
        mashImageView?.translationY = 0f
        mashImageView?.alpha = 1f
        mashContentContainer?.visibility = View.VISIBLE
        fab?.show()
        supportInvalidateOptionsMenu()
    }

    fun setMashHidden() {
        mashContentContainer?.visibility = View.INVISIBLE
    }

    fun startShowMashAnim() {
        var anim = ValueAnimator.ofFloat(0f, 1f);
        val mashImage = mashImageView
        val mashText = mashTextView
        val mashContainer = mashContentContainer
        if (mashImage != null && mashText != null && mashContainer != null) {
            with(anim) {
                addUpdateListener {
                    mashText.translationX = (1f - it.animatedFraction) * -mashImage.width.toFloat()
                    mashImage.alpha = it.animatedFraction
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        mashText.translationX = -mashImage.width.toFloat()
                        mashImage.alpha = 0f
                        mashContainer.translationY = 0f
                        mashContainer.translationX = 0f
                        mashContainer.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        setMashShowing()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        super.onAnimationCancel(animation)
                        setMashShowing()
                    }
                })
                interpolator = DecelerateInterpolator()
                setDuration(300)
                start()
            }
            animHideMash = anim
        }
    }

    fun stopShowMashAnim() {
        animShowMash?.cancel()
        animShowMash = null
    }

    fun startHideMashAnim() {
        var anim = ValueAnimator.ofFloat(0f, 1f);
        val mashContainer = mashContentContainer
        if (mashContainer != null) {
            with(anim) {
                addUpdateListener {
                    mashContainer.translationY = it.animatedFraction * mashContainer.height
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        setMashHidden()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        super.onAnimationCancel(animation)
                        setMashHidden()
                    }
                })
                interpolator = AccelerateInterpolator()
                setDuration(300)
                start()
            }
            animHideMash = anim
        }
    }

    fun stopHideMashAnim() {
        animHideMash?.cancel()
        animHideMash = null
    }


    fun startLoadingAnimation() {
        var masherView = loadingBaconMasher
        var baconView = loadingBacon
        if (animLoading == null && masherView != null && baconView != null) {
            var anim = ValueAnimator.ofFloat(0f, masherView.height / 4f, 0f);
            with(anim!!) {
                addUpdateListener {
                    masherView.translationY = it.animatedValue as Float
                    if (it.animatedFraction > 0.5f && it.animatedFraction < 0.70f) {
                        baconView.rotation = 20f - (it.animatedFraction * 20f)
                    }
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        masherView.translationY = 0f
                        baconView.rotation = 0f
                        fab?.hide()
                    }
                })
                repeatMode = ValueAnimator.INFINITE
                repeatCount = ValueAnimator.INFINITE
                setDuration(650)
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
            animLoading = anim
        }
    }

    fun stopLoadingAnimation() {
        animLoading?.cancel()
        animLoading = null
    }


    private fun hideSystemUI() {
        supportActionBar.hide()
        fab?.hide()
    }

    private fun showSystemUI() {
        supportActionBar.show()
        fab?.show()
    }

    public fun setUpLeftDrawer() {
        if (sideBarAdapter == null) {
            sideBarAdapter = SideBarAdapter(this);
            sideBarAdapter?.setAccentColor(resources.getColor(R.color.colorPrimary))
            leftDrawer?.adapter = sideBarAdapter
            leftDrawer?.layoutManager = LinearLayoutManager(this)
        }
    }

    public fun setUpRightDrawer() {
        if (settingsAdapter == null) {
            settingsAdapter = MasherSettingsAdapter(this)
            rightDrawer?.adapter = settingsAdapter
            rightDrawer?.layoutManager = LinearLayoutManager(this)
        } else {
            settingsAdapter?.syncWithMashMaster()
        }
    }

    fun showErrorDialog(msg: String) {
        if (resumed) {
            with(AlertDialog.Builder(this)) {
                setTitle(R.string.error)
                setMessage(msg)
                setNegativeButton(getString(R.string.ok)) { dialogInterface: DialogInterface?, i: Int ->
                    dialogInterface?.dismiss()
                }
                setCancelable(true)
                create().show()
            }
        }
    }


    /**
     * SHARING CODE
     */

    public fun actionShare() {
        subMash?.unsubscribe()

        shareProgDialog = ProgressDialog.show(this, getString(R.string.share_loading_title), getString(R.string.share_loading_message), true, true, {
            subShare?.unsubscribe()
        })

        var bmap = loadBitmapFromView(mashContentContainer!!)
        var shareObservable: Observable<Uri> = Observable.just(bmap).map<Uri, Bitmap> {
            saveFileToSharePath(it)
        }
        subShare = shareObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            var intent = Intent()
                            intent.setAction(Intent.ACTION_SEND)
                            intent.putExtra(Intent.EXTRA_STREAM, it);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            intent.setType("image/*");
                            startActivity(Intent.createChooser(intent, resources.getText(R.string.share)));
                            subShare = null
                            shareProgDialog?.dismiss()
                        },
                        {

                            Timber.e(it, "Error trying to share!")
                            showErrorDialog(it.message ?: getString(R.string.unknown_error))
                            subShare = null
                            shareProgDialog?.dismiss()
                        })
    }

    fun loadBitmapFromView(v: View): Bitmap {
        var start = System.currentTimeMillis()
        var b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        var c = Canvas(b)
        v.draw(c)
        return b
    }

    fun saveFileToSharePath(bmap: Bitmap): Uri {
        var outputStream: FileOutputStream? = null
        try {
            //Note that this path is important as it is used by the fileprovider
            var shareableImageDir = File(cacheDir, "sharedmash")
            if (!shareableImageDir.exists()) {
                shareableImageDir.mkdir()
            }
            var sharedImageFile = File(shareableImageDir, "sharedmash.png")
            outputStream = FileOutputStream(sharedImageFile)
            bmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            var contentUri = FileProvider.getUriForFile(this, "com.hotpodata.baconmasher.fileprovider", sharedImageFile)
            return contentUri
        } catch(ex: Exception) {
            Timber.e(ex, "Fail in saveFileToSharePath")
            throw ex; //We just throw here to let rx catch the exception
        } finally {
            try {
                outputStream?.close()
            } catch(ex: Exception) {
                Timber.e(ex, "Fail in saveFileToSharePath cleanup")
            }
        }
    }
}
