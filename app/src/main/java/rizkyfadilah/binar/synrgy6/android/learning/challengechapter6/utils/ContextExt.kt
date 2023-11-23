package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.beran.common.Constants.FILE_NAME_FORMAT
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.BuildConfig
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

/**
 * extension function ini digunakan untuk menampilkan alert dialog
 */
fun Context.showAlert(title: String, message: String, positiveCallback: () -> Unit) {
    val builder = AlertDialog.Builder(this)
    with(builder) {
        setTitle(title)
        setMessage(message)
        setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            positiveCallback()
            dialog.cancel()
        }
        setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }
    }

    val alertDialog = builder.create()
    alertDialog.setCanceledOnTouchOutside(true)
    alertDialog.show()
}

fun Context.showCameraOptions(
    title: String,
    message: String,
    galleryCallback: () -> Unit,
    cameraCallback: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
    with(builder) {
        setTitle(title)
        setMessage(message)
        setPositiveButton(getString(R.string.gallery)) { dialog, _ ->
            galleryCallback()
            dialog.cancel()
        }
        setNegativeButton(getString(R.string.camera)) { dialog, _ ->
            cameraCallback()
            dialog.cancel()
        }
        setOnDismissListener {
            it.dismiss()
            showToast(getString(R.string.select_image_source))
        }
    }

    val alertDialog = builder.create()
    alertDialog.setCanceledOnTouchOutside(true)
    alertDialog.show()
}

private val timeStamp: String = SimpleDateFormat(FILE_NAME_FORMAT, Locale.US).format(Date())

fun Context.getImgUri(): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MovieKu/")
        }
        uri = this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: this.getImageUriForPreQ()
}

fun Context.getImageUriForPreQ(): Uri {
    val filesDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MovieKu/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        this,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}