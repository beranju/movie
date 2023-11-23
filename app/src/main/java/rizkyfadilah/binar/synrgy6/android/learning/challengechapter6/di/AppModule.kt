package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
    @Provides
    fun provideBlurWorker(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}