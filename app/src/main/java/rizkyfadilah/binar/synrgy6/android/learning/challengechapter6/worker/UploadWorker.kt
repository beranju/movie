package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.worker

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.failure
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.beran.common.Constants
import com.beran.common.Constants.KEY_IMAGE_URI
import com.beran.common.Constants.PROGRESS
import com.beran.common.Resource
import com.beran.domain.usecase.auth.UploadPhotoUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.uriToTempFile

private const val TAG = "UploadWorker"

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val uploadPhotoUseCase: UploadPhotoUseCase
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val context = applicationContext
        val imageUri = inputData.getString(Constants.KEY_IMAGE_URI)
        var loading: Boolean = false
        var error: String? = null
        var imgUrl: String = ""
        makeStatusNotification(context.getString(R.string.upload_worker_notify_message), applicationContext)
        sleep()
        try {
            if (TextUtils.isEmpty(imageUri.toString())) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            val file = context.uriToTempFile(Uri.parse(imageUri.toString()))
            if (file != null) {
                uploadPhotoUseCase.invoke(file).collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            loading = true
                            error = null
                        }

                        is Resource.Error -> {
                            loading = false
                            error = result.error
                        }

                        is Resource.Success -> {
                            loading = false
                            error = null
                            imgUrl = result.data
                        }
                    }
                }
            }
            if (loading) {
                (0..100 step 10).forEach {
                    setProgressAsync(workDataOf(PROGRESS to it))
                    sleep()
                }
            }
            if (error != null) {
                failure()
            } else {
                Result.success(workDataOf(KEY_IMAGE_URI to imgUrl))
            }
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error uploading photo")
            throwable.printStackTrace()
            Result.failure()
        }
    }
}