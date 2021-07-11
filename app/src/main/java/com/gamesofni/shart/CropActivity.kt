package com.gamesofni.shart

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.takusemba.cropme.CropLayout
import com.takusemba.cropme.OnCropListener

class CropActivity : AppCompatActivity() {

  private val cropButton by lazy { findViewById<ImageView>(R.id.crop) }
  private val parent by lazy { findViewById<ConstraintLayout>(R.id.container) }
  private val cropLayout by lazy { findViewById<CropLayout>(R.id.crop_view) }
  private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress) }


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
        MaterialAlertDialogBuilder(this@CropActivity)
            .setTitle(R.string.dialog_title_result)
            .setView(view)
            .setPositiveButton(R.string.dialog_button_close) { dialog, _ -> dialog.dismiss() }
            .show()
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
  }

}
