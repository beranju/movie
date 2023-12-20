package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.beran.common.Constants.FILE_NAME_FORMAT
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.BuildConfig
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

/**
 * extension function ini digunakan untuk menampilkan alert dialog
 */
fun Context.showAlert(title: String, message: String, positiveCallback: (DialogInterface) -> Unit) {
    val builder = AlertDialog.Builder(this)
    with(builder) {
        setTitle(title)
        setMessage(message)
        setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            positiveCallback(dialog)
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

fun Context.uriToTempFile(uri: Uri): File? {
    try {
        val myFile = File.createTempFile(timeStamp, ".jpg", this.externalCacheDir)
        val inputStream = contentResolver.openInputStream(uri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(
            buffer,
            0,
            length
        )
        outputStream.close()
        inputStream.close()
        return myFile
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 * This Extension function used to show custom dialog to update a field
 *
 * @param title => Digunakan untuk membuat title dari dialog
 * @param label => Digunakan untuk memberikan label pada edit text
 * @param initialValue => Merupakan value default
 * @param onSaveClick => Merupakan lambda atau anonymous fun to handle update action
 */
fun Context.customAlertDialog(
    title: String,
    label: String,
    initialValue: String,
    onSaveClick: (String) -> Unit
) {
    val dialogBuilder =
        AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_form, null)
    val btnUpdate = dialogView.findViewById<MaterialButton>(R.id.btn_update)
    val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btn_cancel)
    val tvTitle = dialogView.findViewById<TextView>(R.id.tv_dialog_title)
    val etValue = dialogView.findViewById<EditText>(R.id.tie_value)
    val tilValue = dialogView.findViewById<TextInputLayout>(R.id.til_value)
    dialogBuilder.setView(dialogView)
    etValue.setText(initialValue)
    tilValue.hint = label
    tvTitle.text = title
    btnUpdate.setOnClickListener {
        val newValue = etValue.text.toString()
        onSaveClick(newValue)
        dialogBuilder.dismiss()
    }
    btnCancel.setOnClickListener { dialogBuilder.dismiss() }
    dialogBuilder.setCanceledOnTouchOutside(true)
    dialogBuilder.show()
}
