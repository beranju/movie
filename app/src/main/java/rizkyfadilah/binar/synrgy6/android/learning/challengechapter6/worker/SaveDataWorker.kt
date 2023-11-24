package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.beran.common.Constants.KEY_IMAGE_URI
import com.beran.common.Resource
import com.beran.domain.model.UserData
import com.beran.domain.usecase.auth.SaveDataProfileUseCase
import com.beran.domain.usecase.auth.UpdateUserImageUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

private const val TAG = "SaveDataWorker"

@HiltWorker
class SaveDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val updateUserImageUseCase: UpdateUserImageUseCase
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val imgUrl = inputData.getString(KEY_IMAGE_URI)
        var loading: Boolean = false
        var error: String? = null

        try {
            updateUserImageUseCase.invoke(imgUrl.orEmpty()).collect { result ->
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
                    }
                }

            }
            if (error != null){
                Result.failure(workDataOf("ERROR" to error))
            }
            if (loading) {
                (0..100 step 10).forEach {
                    setProgressAsync(workDataOf("PROGRESS" to it))
                    sleep()
                }
            }
            Result.success()
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error saving photo")
            throwable.printStackTrace()
            Result.failure()
        }
    }
}