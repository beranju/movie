package com.beran.common

object Constants {

    // API SERVICE CONSTANTS
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMG_BASE_URL = "https://image.tmdb.org/t/p/original/"
    const val IMG_BB_BASE_URL = "https://api.imgbb.com/1/"

    // RV CONSTANTS
    const val NOW_MOVIE_TYPE = 0
    const val POP_MOVIE_TYPE = 1

    // PREFS CONSTANTS
    const val PREF_NAME = "movie_user"
    const val LOGIN_KEY = "is_login"
    const val USERNAME_KEY = "username"
    const val NAME_KEY = "name"
    const val BIRTHDAY_KEY = "birthday"
    const val ADDRESS_KEY = "address"
    const val IMAGE_KEY = "image"

    // IMAGE CONSTANTS
    const val FILE_NAME_FORMAT = "yyyyMMdd_HHmmss"

    // WORKER CONSTANTS
    @JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
        "Verbose WorkManager Notifications"
    const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
        "Shows notifications whenever work starts"
    @JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
    const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
    const val NOTIFICATION_ID = 1

    const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

    const val OUTPUT_PATH = "blur_filter_outputs"
    const val KEY_IMAGE_URI = "image_uri"
    const val TAG_OUTPUT = "OUTPUT"
    const val TAG_PROGRESS = "TAG_PROGRESS"
    const val PROGRESS = "PROGRESS"
    const val DELAY_TIME_MILLIS: Long = 1000

}