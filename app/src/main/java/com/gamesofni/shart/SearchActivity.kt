package com.gamesofni.shart

import androidx.appcompat.app.AppCompatActivity
import android.opengl.GLSurfaceView
import com.bumptech.glide.RequestManager
import com.gamesofni.shart.common.helpers.SnackbarHelper
import com.gamesofni.shart.common.helpers.DisplayRotationHelper
import com.gamesofni.shart.common.helpers.TrackingStateHelper
import com.gamesofni.shart.common.rendering.BackgroundRenderer
import com.gamesofni.shart.rendering.AugmentedImageRenderer
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.ar.core.ArCoreApk.InstallStatus
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.google.ar.core.exceptions.UnavailableApkTooOldException
import com.google.ar.core.exceptions.UnavailableSdkTooOldException
import com.google.ar.core.exceptions.CameraNotAvailableException
import android.widget.Toast
import com.gamesofni.shart.common.helpers.FullScreenHelper
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import com.gamesofni.shart.common.helpers.CameraPermissionHelper
import com.gamesofni.shart.data.Model3d
import com.gamesofni.shart.data.ShartObject
import com.gamesofni.shart.data.ShartViewModel
import com.gamesofni.shart.data.ShartViewModelFactory
import com.google.ar.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception
import java.util.HashMap
import javax.microedition.khronos.egl.EGLConfig

class SearchActivity() : AppCompatActivity(), GLSurfaceView.Renderer {
    // Rendering. The Renderers are created here, and initialized when the GL surface is created.
    private lateinit var surfaceView: GLSurfaceView
    private lateinit var fitToScanView: ImageView
    private var glideRequestManager: RequestManager? = null
    private var installRequested = false
    private var session: Session? = null
    private val messageSnackbarHelper = SnackbarHelper()
    private var displayRotationHelper: DisplayRotationHelper? = null
    private val trackingStateHelper = TrackingStateHelper(this)
    private val backgroundRenderer = BackgroundRenderer()

    private lateinit var models : List<Model3d>
    private lateinit var sharts : List<ShartObject>

    private var shouldConfigureSession = false

    // Augmented image configuration and rendering.
    // Load a single image (true) or a pre-generated image database (false).
    private val useSingleImage = false

    // Augmented image and its associated center pose anchor, keyed by index of the augmented image in
    // the
    // database.
    private val augmentedImageMap: MutableMap<Int, Pair<AugmentedImage, Anchor>> = HashMap()

    private val viewModel: ShartViewModel by viewModels<ShartViewModel>() {
        ShartViewModelFactory(
            (application as ShartApplication).database.model3dDao(),
            (application as ShartApplication).database.shartObjectDao()
        )
    }
    private lateinit var  augmentedImageRenderer : AugmentedImageRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationScope = CoroutineScope(SupervisorJob())
        applicationScope.launch {
            models = viewModel.all3dModels
            sharts = viewModel.allShartObjects
//            Log.e(TAG, "models::::" + models)
            augmentedImageRenderer = AugmentedImageRenderer(models)
        }

        setContentView(R.layout.activity_search)
        surfaceView = findViewById(R.id.surfaceview)
        displayRotationHelper = DisplayRotationHelper( /*context=*/this)

        // Set up renderer.
        surfaceView.setPreserveEGLContextOnPause(true)
        surfaceView.setEGLContextClientVersion(2)
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0) // Alpha used for plane blending.
        surfaceView.setRenderer(this)
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
        surfaceView.setWillNotDraw(false)
        fitToScanView = findViewById(R.id.image_view_fit_to_scan)
        glideRequestManager = Glide.with(this)
        glideRequestManager!!
            .load(Uri.parse("file:///android_asset/fit_to_scan.png"))
            .into(fitToScanView)
        installRequested = false
    }

    override fun onDestroy() {
        if (session != null) {
            // Explicitly close ARCore Session to release native resources.
            // Review the API reference for important considerations before calling close() in apps with
            // more complicated lifecycle requirements:
            // https://developers.google.com/ar/reference/java/arcore/reference/com/google/ar/core/Session#close()
            session!!.close()
            session = null
        }
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        if (session == null) {
            var exception: Exception? = null
            var message: String? = null
            try {
                when (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
                    InstallStatus.INSTALL_REQUESTED -> {
                        installRequested = true
                        return
                    }
                    InstallStatus.INSTALLED -> {
                    }
                }

                // ARCore requires camera permissions to operate. If we did not yet obtain runtime
                // permission on Android M and above, now is a good time to ask the user for it.
                if (!CameraPermissionHelper.hasCameraPermission(this)) {
                    CameraPermissionHelper.requestCameraPermission(this)
                    return
                }
                session = Session( /* context = */this)
            } catch (e: UnavailableArcoreNotInstalledException) {
                message = "Please install ARCore"
                exception = e
            } catch (e: UnavailableUserDeclinedInstallationException) {
                message = "Please install ARCore"
                exception = e
            } catch (e: UnavailableApkTooOldException) {
                message = "Please update ARCore"
                exception = e
            } catch (e: UnavailableSdkTooOldException) {
                message = "Please update this app"
                exception = e
            } catch (e: Exception) {
                message = "This device does not support AR"
                exception = e
            }
            if (message != null) {
                messageSnackbarHelper.showError(this, message)
                Log.e(TAG, "Exception creating session", exception)
                return
            }
            shouldConfigureSession = true
        }

        // TODO: check whether need to configure each time
        if (shouldConfigureSession) {
            configureSession()
            shouldConfigureSession = false
        }

        // Note that order matters - see the note in onPause(), the reverse applies here.
        try {
            session!!.resume()
        } catch (e: CameraNotAvailableException) {
            messageSnackbarHelper.showError(this, "Camera not available. Try restarting the app.")
            session = null
            return
        }
        surfaceView!!.onResume()
        displayRotationHelper!!.onResume()
        fitToScanView!!.visibility = View.VISIBLE
    }

    public override fun onPause() {
        super.onPause()
        if (session != null) {
            // Note that the order matters - GLSurfaceView is paused first so that it does not try
            // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
            // still call session.update() and get a SessionPausedException.
            displayRotationHelper!!.onPause()
            surfaceView!!.onPause()
            session!!.pause()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(
                this, "Camera permissions are needed to run this application", Toast.LENGTH_LONG
            )
                .show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this)
            }
            finish()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)

        // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
        try {
            // Create the texture and pass it to ARCore session to be filled during update().
            backgroundRenderer.createOnGlThread( /*context=*/this)
            augmentedImageRenderer.createOnGlThread( /*context=*/this)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to read an asset file", e)
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        displayRotationHelper!!.onSurfaceChanged(width, height)
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        // Clear screen to notify driver it should not load any pixels from previous frame.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        if (session == null) {
            return
        }
        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        displayRotationHelper!!.updateSessionIfNeeded(session)
        try {
            session!!.setCameraTextureName(backgroundRenderer.textureId)

            // Obtain the current frame from ARSession. When the configuration is set to
            // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
            // camera framerate.
            session!!.resume()
            val frame = session!!.update()
            val camera = frame.camera

            // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
            trackingStateHelper.updateKeepScreenOnFlag(camera.trackingState)

            // If frame is ready, render camera preview image to the GL surface.
            backgroundRenderer.draw(frame)

            // Get projection matrix.
            val projmtx = FloatArray(16)
            camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f)

            // Get camera matrix and draw.
            val viewmtx = FloatArray(16)
            camera.getViewMatrix(viewmtx, 0)

            // Compute lighting from average intensity of the image.
            val colorCorrectionRgba = FloatArray(4)
            frame.lightEstimate.getColorCorrection(colorCorrectionRgba, 0)

            // Visualize augmented images.
            val applicationScope = CoroutineScope(SupervisorJob())

            applicationScope.launch {
                drawAugmentedImages(frame, projmtx, viewmtx, colorCorrectionRgba)
            }
        } catch (t: Throwable) {
            // Avoid crashing the application due to unhandled exceptions.
            Log.e(TAG, "Exception on the OpenGL thread", t)
        }
    }

    private fun configureSession() {
        val config = Config(session)
        config.focusMode = Config.FocusMode.AUTO
        if (!setupAugmentedImageDatabase(config)) {
            messageSnackbarHelper.showError(this, "Could not setup augmented image database")
        }
        session!!.configure(config)
        session!!.resume()
    }

    suspend private fun drawAugmentedImages(
        frame: Frame, projmtx: FloatArray, viewmtx: FloatArray, colorCorrectionRgba: FloatArray
    ) {
        val updatedAugmentedImages = frame.getUpdatedTrackables(
            AugmentedImage::class.java
        )

        // Iterate to update augmentedImageMap, remove elements we cannot draw.
        for (augmentedImage: AugmentedImage in updatedAugmentedImages) {
            when (augmentedImage.trackingState) {
                TrackingState.PAUSED -> {
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    val text = String.format(
                        ("There's something here, keep searching" +  //                    );
                                "\nMagic number: " +
                                " %d"),
                        augmentedImage.index
                    )
                    messageSnackbarHelper.showMessage(this, text)
                }
                TrackingState.TRACKING -> {
                    // Have to switch to UI Thread to update View.
                    runOnUiThread(
                        Runnable { fitToScanView!!.visibility = View.GONE })

                    // Create a new anchor for newly found images.
                    if (!augmentedImageMap.containsKey(augmentedImage.index)) {
                        val centerPoseAnchor =
                            augmentedImage.createAnchor(augmentedImage.centerPose)
                        augmentedImageMap[augmentedImage.index] =
                            Pair.create(augmentedImage, centerPoseAnchor)
                    }
                }
                TrackingState.STOPPED -> augmentedImageMap.remove(augmentedImage.index)
                else -> {
                }
            }
        }

        // Draw all images in augmentedImageMap
        for (pair: Pair<AugmentedImage, Anchor> in augmentedImageMap.values) {
            val augmentedImage = pair.first
            val centerAnchor = augmentedImageMap[augmentedImage.index]!!.second
            when (augmentedImage.trackingState) {

                TrackingState.TRACKING -> {
//                    Log.e(TAG, "augmentedImage.index:::" + augmentedImage.index)
                    val model = getModelByAugImgId(augmentedImage.index)
                    if (model != null) {
                        augmentedImageRenderer.draw(
                            viewmtx, projmtx, augmentedImage, model,  centerAnchor,
                            colorCorrectionRgba
                        )
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun getModelByAugImgId(index: Int): Model3d? {
        val shart = sharts.filter { s -> s.indexAugmentedImg == index }.firstOrNull()
        if (shart == null) {
            return null
        }
        val model = models.filter { m -> m.id == shart.modelId  }.firstOrNull()
        return model
    }

    private fun setupAugmentedImageDatabase(config: Config): Boolean {
        var augmentedImageDatabase: AugmentedImageDatabase

        // There are two ways to configure an AugmentedImageDatabase:
        // 1. Add Bitmap to DB directly
        // 2. Load a pre-built AugmentedImageDatabase
        // Option 2) has
        // * shorter setup time
        // * doesn't require images to be packaged in apk.
        if (useSingleImage) {
            val augmentedImageBitmap = loadAugmentedImageBitmap() ?: return false
            augmentedImageDatabase = AugmentedImageDatabase(session)
            augmentedImageDatabase.addImage("image_name", augmentedImageBitmap)
            // If the physical size of the image is known, you can instead use:
            //     augmentedImageDatabase.addImage("image_name", augmentedImageBitmap, widthInMeters);
            // This will improve the initial detection speed. ARCore will still actively estimate the
            // physical size of the image as it is viewed from multiple viewpoints.
        } else {
            // This is an alternative way to initialize an AugmentedImageDatabase instance,
            // load a pre-existing augmented image database.
            val file = File(applicationContext.filesDir, "sample_database.imgdb")
            // open file from saved db file
            if (file.exists()) {
                Log.e(TAG, "used db from files")
                try {
                    FileInputStream(file).use { `is` ->
                        augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, `is`)
                        val numImages: Int = augmentedImageDatabase.getNumImages()
                        Log.e(TAG, "Num Images: " + numImages)
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "IO exception loading augmented image database.", e)
                    return false
                }
            } else {
                Log.e(TAG, "loaded db from assets")

                try {
                    assets.open("sample_database.imgdb").use { `is` ->
                        augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, `is`)
                        val numImages: Int = augmentedImageDatabase.getNumImages()
                        Log.e(TAG, "Num Images: " + numImages)
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "IO exception loading augmented image database.", e)
                    return false
                }
            }
        }
        config.augmentedImageDatabase = augmentedImageDatabase
        return true
    }

    private fun loadAugmentedImageBitmap(): Bitmap? {
        try {
            assets.open("default.jpg").use { `is` -> return BitmapFactory.decodeStream(`is`) }
        } catch (e: IOException) {
            Log.e(TAG, "IO exception loading augmented image bitmap.", e)
        }
        return null
    }

    companion object {
        private val TAG = AugmentedImageActivity::class.java.simpleName
    }
}