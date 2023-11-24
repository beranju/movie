package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.beran.common.Constants.OUTPUT_PATH
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R
import java.io.File

@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters
) :
    Worker(ctx, params) {

    override fun doWork(): Result {
        val context = applicationContext

        makeStatusNotification(context.getString(R.string.clean_worker_notify_message), applicationContext)
        sleep()

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            entry.delete()
                        }
                    }
                }
            }
            Result.success()
        } catch (exception: Exception) {
            Log.e("CleanUpWorker", exception.localizedMessage.orEmpty())
            Result.failure()
        }
    }
}