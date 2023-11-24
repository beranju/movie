package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.beran.common.Constants.KEY_IMAGE_URI
import com.beran.common.Constants.PROGRESS
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R

private const val TAG = "BlurWorker"

@HiltWorker
class BlurWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) :
    Worker(context, params) {
    override fun doWork(): Result {
        val context = applicationContext
        val imageUri = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification(context.getString(R.string.blur_worker_notify_blurring_image), context)
        (0..100 step 10).forEach {
            setProgressAsync(workDataOf(PROGRESS to it))
            sleep()
        }

        return try {
            if (TextUtils.isEmpty(imageUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            val resolver = context.contentResolver
            val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(imageUri)))
            val output = blurBitmap(picture, context)
            val outputUri = writeBitmapToFile(context, output)
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            return Result.success(outputData)
        }catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            throwable.printStackTrace()
            Result.failure()
        }
    }
}