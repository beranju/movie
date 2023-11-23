package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

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
        setPositiveButton("OK") { dialog, _ ->
            positiveCallback()
            dialog.cancel()
        }
        setNegativeButton("Batal") { dialog, _ ->
            dialog.cancel()
        }
    }

    val alertDialog = builder.create()
    alertDialog.setCanceledOnTouchOutside(true)
    alertDialog.show()
}