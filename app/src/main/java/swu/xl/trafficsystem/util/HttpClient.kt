package swu.xl.trafficsystem.util

import okhttp3.MediaType
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpClient {
    val MEDIA_TYPE_JSON: MediaType = MediaType.parse("application/json; charset=utf-8")!!

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    fun getInstance() = httpClient
}