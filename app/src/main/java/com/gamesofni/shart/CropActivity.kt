package com.gamesofni.shart

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.gamesofni.shart.data.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.ArCoreApk
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableException
import com.takusemba.cropme.CropLayout
import com.takusemba.cropme.OnCropListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class CropActivity : AppCompatActivity() {

  private lateinit var session: Session
  private val cropButton by lazy { findViewById<ImageView>(R.id.crop) }
  private val selectModelButton by lazy { findViewById<Button>(R.id.launch_pick_model) }
  private val parent by lazy { findViewById<ConstraintLayout>(R.id.container) }
  private val cropLayout by lazy { findViewById<CropLayout>(R.id.crop_view) }
  private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress) }
  private val SECOND_ACTIVITY_REQUEST_CODE = 0
  private var selectedModel : Model3d? = null

  fun createSession(): Session {
    // Create a new ARCore session.
    val session = Session(this)

    // Create a session config.
    val config = Config(session)

    // Do feature-specific operations here, such as enabling depth or turning on
    // support for Augmented Faces.

    // Configure the session.
    session.configure(config)
    return session
  }

  private val viewModel: ShartViewModel by viewModels<ShartViewModel>() {
    ShartViewModelFactory(
      (application as ShartApplication).database.model3dDao(),
      (application as ShartApplication).database.shartObjectDao()
    )
  }

  // Verify that ARCore is installed and using the current version.
  // TODO: move the checks and istalls to the main activity (also remove from search activity)
//  fun isARCoreSupportedAndUpToDate(): Boolean {
//    return when (ArCoreApk.getInstance().checkAvailability(this)) {
//      Availability.SUPPORTED_INSTALLED -> true
//      Availability.SUPPORTED_APK_TOO_OLD, Availability.SUPPORTED_NOT_INSTALLED -> {
//        try {
//          // Request ARCore installation or update if needed.
//          when (ArCoreApk.getInstance().requestInstall(this, true)) {
//            InstallStatus.INSTALL_REQUESTED -> {
//              Log.i(TAG, "ARCore installation requested.")
//              false
//            }
//            InstallStatus.INSTALLED -> true
//          }
//        } catch (e: UnavailableException) {
//          Log.e(TAG, "ARCore not installed", e)
//          false
//        }
//      }
//
//      Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE ->
//        // This device is not supported for AR.
//        false
//
//      Availability.UNKNOWN_CHECKING -> {
//        // ARCore is checking the availability with a remote query.
//        // This function should be called again after waiting 200 ms to determine the query result.
//      }
//      Availability.UNKNOWN_ERROR, Availability.UNKNOWN_TIMED_OUT -> {
//        // There was an error checking for AR availability. This may be due to the device being offline.
//        // Handle the error appropriately.
//      }
//    }
//  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val layoutId = R.layout.activity_crop_rectangle

    setContentView(layoutId)

    val bundle :Bundle ?=intent.extras
    val uri:Uri = Uri.parse(bundle!!.getString( "fileUri"));
    cropLayout.setUri(uri)

    cropLayout.addOnCropListener(object : OnCropListener {
      override fun onSuccess(bitmap: Bitmap) {
        progressBar.visibility = View.GONE

        val view = layoutInflater.inflate(R.layout.dialog_result, null)
        view.findViewById<ImageView>(R.id.image).setImageBitmap(bitmap)

        // create ARcore session only to add img to the db
        session = createSession()

        val fileDb = File(applicationContext.filesDir, "sample_database.imgdb")
        val augmentedImageDatabase :AugmentedImageDatabase

        if (fileDb.exists()) {
          try {
            FileInputStream(fileDb).use { `is` ->
              augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, `is`)
//              val numImages: Int = augmentedImageDatabase.getNumImages()
//              Log.e("", "Num Images: $numImages")
            }
          } catch (e: IOException) {
            Log.e("", "IO exception loading augmented image database.", e)
            return
          }
        } else {
          try {
            applicationContext.assets.open("sample_database.imgdb").use { `is` ->
              augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, `is`)
//              val numImages: Int = augmentedImageDatabase.getNumImages()
//              Log.e("", "Num Images: $numImages")
            }
          } catch (e: IOException) {
            Log.e("", "IO exception loading augmented image database.", e)
            return
          }
        }


        val toString = bitmap.hashCode().toString()
        val index = augmentedImageDatabase.addImage("user_added_" + toString, bitmap)

        Log.e(TAG, "selectedModel:::::::" + selectedModel?.toString())
        // save new shart to db
        if (selectedModel != null) {
          val shartNew = ShartObject(selectedModel!!.id, index, index, 0, false)
          val applicationScope = CoroutineScope(SupervisorJob())

          applicationScope.launch {
            viewModel.addShart(shartNew)
          }

        }


        val config = Config(session)
        config.augmentedImageDatabase = augmentedImageDatabase
        session.configure(config)
        val numImages = augmentedImageDatabase.numImages

        // save to files
        val path: File = applicationContext.getFilesDir()
        // TODO: check to not rewrite
        val file = File(path, "sample_database.imgdb")
        val stream = FileOutputStream(file)
        try {
          augmentedImageDatabase.serialize(stream)
        } finally {
          stream.close()
        }

        // TODO: del, debug
        view.findViewById<TextView>(R.id.message).setText("index: $index hash: $toString num " +
                "imgs: $numImages")

        MaterialAlertDialogBuilder(this@CropActivity)
            .setTitle(R.string.dialog_title_result)
            .setView(view)
            .setPositiveButton(R.string.dialog_button_close) { dialog, _ ->
              dialog.dismiss()
              val intent = Intent(this@CropActivity, MainActivity::class.java)
              startActivity(intent)
              finish()
            }
            .show()
        session.close()
      }

      override fun onFailure(e: Exception) {
        Snackbar.make(parent, R.string.error_failed_to_clip_image, Snackbar.LENGTH_LONG).show()
      }
    })

    cropButton.setOnClickListener(View.OnClickListener {
      if (cropLayout.isOffFrame()) {
        Snackbar.make(parent, R.string.error_image_is_off_frame, Snackbar.LENGTH_LONG).show()
        return@OnClickListener
      }
      progressBar.visibility = View.VISIBLE
      cropLayout.crop()
    })
    cropButton.isEnabled = selectedModel != null


    selectModelButton.setOnClickListener(View.OnClickListener {
      val intent = Intent(this, PickModelActivity::class.java)
      startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
    })

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // Check that it is the SecondActivity with an OK result
    if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
      if (resultCode == Activity.RESULT_OK) {

        // Get String data from Intent
        val modelName = data!!.getStringExtra("modelName")
        val model = Datasource().getByName(modelName!!)
        // Set text view with string
        val textView = findViewById<TextView>(R.id.model_preview_text)
        textView.text = modelName
        // se preview img
        val imgView = findViewById<ImageView>(R.id.model_preview_img)
        imgView.setImageDrawable(applicationContext.getDrawable(model.previewResourceId))

        selectedModel = model

        cropButton.isEnabled = true
      }
    }
  }
}
