@file:JvmName("LoggerFactory")

package com.sourcepoint.gdpr_cmplibrary.exception

import com.example.gdpr_cmplibrary.BuildConfig
import com.sourcepoint.gdpr_cmplibrary.enqueue
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/**
 * Factory method used to create an instance of the [Logger] interface
 * @param networkClient network client
 * @param errorMessageManager entity used to build the network request body
 * @param url server url
 */
internal fun createLogger(
    networkClient: OkHttpClient,
    errorMessageManager: ErrorMessageManager,
    url: String,
    debuggable : Boolean
): Logger = LoggerImpl(networkClient, errorMessageManager, url, debuggable)

/**
 * Implementation of [Logger]
 */
private class LoggerImpl(
    val networkClient: OkHttpClient,
    val errorMessageManager: ErrorMessageManager,
    val url: String,
    val debuggable : Boolean
) : Logger {
    override fun error(e: ConsentLibExceptionK) {
        if(debuggable){
            val mediaType = MediaType.parse("application/json")
            val body: RequestBody = RequestBody.create(mediaType, errorMessageManager.build(e))
            val request: Request = Request.Builder().url(url).post(body)
                .header("Accept", mediaType?.type() ?: "")
                .header("Content-Type", mediaType?.type() ?: "")
                .build()

            networkClient.newCall(request).enqueue { }
        }
    }
}