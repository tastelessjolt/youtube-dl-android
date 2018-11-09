package me.harshithgoka.youtubedl.YoutubeDL

import java.io.IOException

import okhttp3.Request
import okhttp3.Response
import okhttp3.OkHttpClient

class HttpGetter {
    var client: OkHttpClient = OkHttpClient()

    @Throws(IOException::class)
    fun run(url: String): String {
        val request = Request.Builder()
                .url(url)
                .build()

        val response = client.newCall(request).execute()
        return response.body()!!.string()
    }
}
