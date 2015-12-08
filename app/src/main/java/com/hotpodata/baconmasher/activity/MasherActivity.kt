package com.hotpodata.baconmasher.activity;

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.analytics.HitBuilders
import com.hotpodata.baconmasher.AnalyticsMaster
import com.hotpodata.baconmasher.BuildConfig
import com.hotpodata.baconmasher.MashMaster
import com.hotpodata.baconmasher.R
import com.hotpodata.baconmasher.adapter.MasherSettingsAdapter
import com.hotpodata.baconmasher.adapter.SideBarAdapter
import com.hotpodata.baconmasher.data.ExceptionMissingSettings
import com.hotpodata.baconmasher.data.GravityStringer
import com.hotpodata.baconmasher.data.MashData
import com.hotpodata.baconmasher.data.TypefaceCache
import com.hotpodata.redchain.utils.IntentUtils
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
import java.security.MessageDigest
import java.util.*

public class MasherActivity : AppCompatActivity() {

    val STATE_LAST_MASH = "STATE_LAST_MASH"
    val STATE_LAST_MASH_IMAGE_LOADED = "STATE_LAST_MASH_IMAGE_LOADED"
    val STATE_MASH_COUNT = "STATE_MASH_COUNT"
    val STATE_SELECTED_TEXT_COLOR_INDEX = "STATE_SELECTED_TEXT_COLOR_INDEX"

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
    var loadingGoProContainer: View? = null
    var loadingGoProBtn: View? = null

    var fab: FloatingActionButton? = null
    var toolbar: Toolbar? = null

    var sideBarAdapter: SideBarAdapter? = null
    var settingsAdapter: MasherSettingsAdapter? = null

    var animLoading: ValueAnimator? = null
    var animHideMash: ValueAnimator? = null
    var animShowMash: ValueAnimator? = null
    var animTextColor: Animator? = null

    var subMash: Subscription? = null
    var subShare: Subscription? = null

    var immersiveMode = false
    var resumed = false

    var lastMash: MashData? = null
    var lastMashImageLoaded = false
    var shareProgDialog: ProgressDialog? = null
    var errorDialog: AlertDialog? = null

    //Text colors
    var textColors: List<Int>? = null
    var selectedColorIndex: Int = 0


    //Ads...
    var interstitialAd: InterstitialAd? = null
    var mashcount = 0
    var adRandom = Random()


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masher)
        toolbar = findViewById(R.id.toolbar) as Toolbar?
        fab = findViewById(R.id.fab) as FloatingActionButton?
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout?
        leftDrawer = findViewById(R.id.left_drawer) as RecyclerView?
        rightDrawer = findViewById(R.id.right_drawer) as RecyclerView?
        mashTextView = findViewById(R.id.text) as TextView?
        mashImageView = findViewById(R.id.image) as ImageView?
        loadingBaconMasher = findViewById(R.id.bacon_masher)
        loadingBacon = findViewById(R.id.bacon)
        mashContentContainer = findViewById(R.id.mash_content_container) as FrameLayout?
        mashLoadingContainer = findViewById(R.id.mash_loading_container) as FrameLayout?
        loadingGoProContainer = findViewById(R.id.loading_go_pro_container)
        loadingGoProBtn = findViewById(R.id.loading_go_pro_btn)

        textColors = resources.getIntArray(R.array.text_colors).toList()

        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_LAST_MASH)) {
                try {
                    lastMash = MashData.Serializer.fromJSON(JSONObject(savedInstanceState.getString(STATE_LAST_MASH)))
                } catch(ex: Exception) {
                    Timber.e(ex, "Failure to parse out mash data from savedInstanceState")
                }
            }
            mashcount = savedInstanceState.getInt(STATE_MASH_COUNT, 0)
            selectedColorIndex = savedInstanceState.getInt(STATE_SELECTED_TEXT_COLOR_INDEX, 0)
            lastMashImageLoaded = savedInstanceState.getBoolean(STATE_LAST_MASH_IMAGE_LOADED, false)
        }


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
            if (subMash?.isUnsubscribed() ?: true) {
                actionMashBacon()
            }
        };

        mashLoadingContainer?.setOnClickListener() {
            if (subMash?.isUnsubscribed() ?: true) {
                actionMashBacon()
            }
        }

        mashImageView?.setOnClickListener() {
            if (immersiveMode) {
                showSystemUI()
            } else {
                hideSystemUI()
            }

        }

        loadingGoProBtn?.setOnClickListener() {
            startActivity(IntentUtils.goPro())
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setShowHideAnimationEnabled(true)

        setUpLeftDrawer()
        setUpRightDrawer()



        //Set up ads..
        if (!BuildConfig.IS_PRO) {
            var ad = InterstitialAd(this);
            ad.setAdUnitId(getString(R.string.interstitial_add_unit_id))
            ad.adListener = object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                    requestNewInterstitial()
                    loadingGoProContainer?.visibility = View.VISIBLE
                    actionMashBacon()
                }
            }
            interstitialAd = ad
            requestNewInterstitial()
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
        if (lastMash != null) {
            bindMash(lastMash!!)
        } else {
            fab?.hide()
            setMashHidden()
            var mashSub = MashMaster.activeDataSubject
            if (mashSub != null) {
                subscribeToMash(mashSub.asObservable())
            }
        }
        resumed = true
        Timber.i("Setting screen name:" + AnalyticsMaster.SCREEN_MASHER);
        AnalyticsMaster.getTracker(this).setScreenName(AnalyticsMaster.SCREEN_MASHER);
        AnalyticsMaster.getTracker(this).send(HitBuilders.ScreenViewBuilder().build());
    }

    public override fun onPause() {
        super.onPause()
        resumed = false
        subShare?.unsubscribe()

        animTextColor?.cancel()
        animShowMash?.cancel()
        animHideMash?.cancel()
        animLoading?.cancel()
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
        var colorItem = menu?.findItem(R.id.action_color)
        if (colorItem != null) {
            var icon = colorItem.icon.mutate()
            icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            colorItem.setIcon(icon)
        }
        return true;
    }

    public override fun onBackPressed() {
        if (drawerLayout?.isDrawerOpen(leftDrawer) ?: false || drawerLayout?.isDrawerOpen(rightDrawer) ?: false) {
            drawerLayout?.closeDrawers()
        } else if (immersiveMode) {
            showSystemUI()
        } else if (shareProgDialog?.isShowing ?: false) {
            shareProgDialog?.cancel()
        } else if (errorDialog?.isShowing ?: false) {
            errorDialog?.cancel()
        } else {
            super.onBackPressed()
        }

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
            R.id.action_color -> {
                actionShiftTextColor()
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        var shareItem = menu?.findItem(R.id.action_share)
        shareItem?.setVisible(lastMash != null)
        shareItem?.setEnabled(lastMash != null)
        var colorItem = menu?.findItem(R.id.action_color)
        colorItem?.setVisible(lastMash != null)
        colorItem?.setEnabled(lastMash != null)


        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSaveInstanceState(out: Bundle?) {
        super.onSaveInstanceState(out)
        var mash = lastMash
        if (mash != null) {
            out?.putString(STATE_LAST_MASH, MashData.Serializer.toJSON(mash).toString())
            out?.putBoolean(STATE_LAST_MASH_IMAGE_LOADED, lastMashImageLoaded)
        }
        out?.putInt(STATE_MASH_COUNT, mashcount)
        out?.putInt(STATE_SELECTED_TEXT_COLOR_INDEX, selectedColorIndex)
    }


    fun actionShiftTextColor() {
        var currentColor: Int = textColors?.get(selectedColorIndex) ?: Color.WHITE
        selectedColorIndex = ((selectedColorIndex + 1) % (textColors?.size as Int))
        var newColor: Int = textColors?.get(selectedColorIndex) ?: Color.WHITE

        if (animTextColor?.isRunning ?: false) {
            animTextColor?.cancel()
        }

        animTextColor = with(ValueAnimator.ofObject(ArgbEvaluator(), currentColor, newColor)) {
            addUpdateListener {
                if (resumed) {
                    mashTextView?.setTextColor(it.animatedValue as Int)
                }
            }
            setDuration(150)
        }
        animTextColor?.start()
    }

    fun actionMashBacon() {
        if (!BuildConfig.IS_PRO && mashcount > 0 && mashcount % 5 == 0 && adRandom.nextBoolean()) {
            if (interstitialAd?.isLoaded ?: false) {
                interstitialAd?.show()
                return
            }
        }

        try {
            AnalyticsMaster.getTracker(this).send(HitBuilders.EventBuilder()
                    .setCategory(AnalyticsMaster.CATEGORY_ACTION)
                    .setAction(AnalyticsMaster.ACTION_MASH)
                    .setLabel(AnalyticsMaster.LABEL_MASH_COUNT)
                    .setValue(mashcount.toLong())
                    .build());
        } catch(ex: Exception) {
            Timber.e(ex, "Analytics Exception");
        }

        subscribeToMash(MashMaster.doMash(this))
        startHideMashAnim()
    }

    private fun subscribeToMash(observable: Observable<MashData>) {
        subMash?.unsubscribe()
        subMash = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    lastMash = null
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
                            resetLoadingAnimation()
                            setMashHidden()
                            if (it is ExceptionMissingSettings) {
                                drawerLayout?.openDrawer(rightDrawer)
                            }
                            showErrorDialog(it.message ?: getString(R.string.unknown_error))
                        })
    }


    fun bindMash(data: MashData) {
        var wasResumed = resumed
        var wasLoaded = (lastMash == data && lastMashImageLoaded)
        lastMashImageLoaded = wasLoaded
        with(Picasso.with(this).load(data.imageUrl)) {
            if (wasResumed && !wasLoaded) {
                noFade()
            }
            noPlaceholder()
            fit()
            centerCrop()
            into(mashImageView, object : Callback {
                override fun onSuccess() {
                    if (wasResumed || !wasLoaded) {
                        startShowMashAnim()
                    } else {
                        setMashShowing()
                    }
                    stopLoadingAnimation()
                    lastMashImageLoaded = true
                    if (!wasLoaded) {
                        //We only update the mashcount after successful displays
                        //This way if the thing is throwing errors, we don't penalize
                        //the user
                        mashcount++
                    }
                }

                override fun onError() {
                    stopLoadingAnimation()
                    resetLoadingAnimation()
                    showErrorDialog(getString(R.string.error_couldnt_load_image))
                    lastMash = null
                    lastMashImageLoaded = false
                }
            })
        }

        mashTextView?.gravity = GravityStringer.strToGrav[data.textGravity]
        mashTextView?.text = data.comment
        mashTextView?.typeface = TypefaceCache.getTypeFace(this, data.font);
        mashTextView?.setTextColor(textColors?.get(selectedColorIndex) ?: 0)
        lastMash = data
    }

    fun setMashShowing() {
        mashTextView?.translationX = 0f
        mashImageView?.translationY = 0f
        mashImageView?.alpha = 1f
        mashContentContainer?.visibility = View.VISIBLE
        loadingGoProContainer?.visibility = View.GONE
        fab?.show()
        //We reset the loading stuff here because it is hidden at this point
        resetLoadingAnimation()
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
            var anim = ValueAnimator.ofFloat(0f, 1f, 0f);
            with(anim!!) {
                addUpdateListener {
                    masherView.translationY = (it.animatedValue as Float * (masherView.height / 4f))
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

    fun resetLoadingAnimation() {
        loadingBacon?.rotation = 0f
        loadingBaconMasher?.translationY = 0f
    }


    private fun hideSystemUI() {
        supportActionBar.hide()
        fab?.hide()
        immersiveMode = true
    }

    private fun showSystemUI() {
        supportActionBar.show()
        fab?.show()
        immersiveMode = false
    }

    public fun setUpLeftDrawer() {
        if (sideBarAdapter == null) {
            sideBarAdapter = with(SideBarAdapter(this)) {
                setAccentColor(ContextCompat.getColor(this@MasherActivity, R.color.colorPrimary))
                this
            }
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
            errorDialog = with(AlertDialog.Builder(this)) {
                setTitle(R.string.error)
                setMessage(msg)
                setNegativeButton(getString(R.string.ok)) { dialogInterface: DialogInterface?, i: Int ->
                    dialogInterface?.dismiss()
                }
                setCancelable(true)
                create()
            }
            errorDialog?.show()

            try {
                AnalyticsMaster.getTracker(this).send(HitBuilders.EventBuilder()
                        .setCategory(AnalyticsMaster.CATEGORY_ACTION)
                        .setAction(AnalyticsMaster.ACTION_ERROR_SHOWN)
                        .setLabel(msg)
                        .build());
            } catch(ex: Exception) {
                Timber.e(ex, "Analytics Exception");
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
        try {
            AnalyticsMaster.getTracker(this).send(HitBuilders.EventBuilder()
                    .setCategory(AnalyticsMaster.CATEGORY_ACTION)
                    .setAction(AnalyticsMaster.ACTION_SHARE)
                    .setLabel(AnalyticsMaster.LABEL_MASH_COUNT)
                    .setValue(mashcount.toLong())
                    .build());
        } catch(ex: Exception) {
            Timber.e(ex, "Analytics Exception");
        }
    }

    fun loadBitmapFromView(v: View): Bitmap {
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
            var contentUri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authorities), sharedImageFile)
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

    /*
    ADD STUFF
     */

    private fun requestNewInterstitial() {
        if (!BuildConfig.IS_PRO) {
            var adRequest = with(AdRequest.Builder()) {
                if (BuildConfig.IS_DEBUG_BUILD) {
                    addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    var andId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                    var hash = md5(andId).toUpperCase()
                    Timber.d("Adding test device. hash:" + hash)
                    addTestDevice(hash)
                }
                build()
            }
            interstitialAd?.loadAd(adRequest);
        }
    }

    private fun md5(s: String): String {
        try {
            var digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            var messageDigest = digest.digest()

            var hexString = StringBuffer()
            for (i in messageDigest.indices) {
                var h = Integer.toHexString(0xFF and messageDigest[i].toInt())
                while (h.length < 2)
                    h = "0" + h
                hexString.append(h)
            }
            return hexString.toString()
        } catch(ex: Exception) {
            Timber.e(ex, "Fail in md5");
        }
        return ""
    }


}
