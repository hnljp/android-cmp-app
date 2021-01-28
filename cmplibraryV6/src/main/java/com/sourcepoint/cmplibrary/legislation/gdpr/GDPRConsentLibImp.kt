package com.sourcepoint.cmplibrary.legislation.gdpr

import android.content.Context
import com.sourcepoint.cmplibrary.Account
import com.sourcepoint.cmplibrary.core.web.ConsentWebView
import com.sourcepoint.cmplibrary.core.web.JSReceiver
import com.sourcepoint.cmplibrary.data.network.converter.JsonConverter
import com.sourcepoint.cmplibrary.util.ConnectionManager
import com.sourcepoint.cmplibrary.util.map
import com.sourcepoint.gdpr_cmplibrary.ConsentLibException
import com.sourcepoint.gdpr_cmplibrary.NativeMessage
import com.sourcepoint.gdpr_cmplibrary.PrivacyManagerTab
import com.sourcepoint.gdpr_cmplibrary.exception.Logger
import com.sourcepoint.gdpr_cmplibrary.exception.RenderingAppException

internal class GDPRConsentLibImpl(
    private val account: Account,
    private val pPrivacyManagerTab: PrivacyManagerTab,
    private val context: Context,
    private val pLogger: Logger,
    private val pJsonConverter: JsonConverter,
    private val pConnectionManager: ConnectionManager
) : GDPRConsentLibClient, JSReceiver {

    override var clientInteraction: GDPRClientInteraction? = null

    /** Start Client's methods */
    override fun loadMessage(authId: String?) {}
    override fun loadMessage() {}
    override fun loadMessage(nativeMessage: NativeMessage) {}
    override fun loadMessage(authId: String, nativeMessage: NativeMessage) {}
    override fun loadPrivacyManager() {}
    override fun loadPrivacyManager(authId: String) {}

    /** end Client's methods */

    private fun createWebView(): ConsentWebView {
        return object : ConsentWebView(context) {
            override val jsReceiver: JSReceiver = this@GDPRConsentLibImpl
            override val logger: Logger = pLogger
            override val connectionManager: ConnectionManager = pConnectionManager
            override val jsonConverter: JsonConverter = pJsonConverter
            override val onNoIntentActivitiesFoundFor: (url: String) -> Unit = { url -> }
            override val onError: (error: ConsentLibException) -> Unit = { error -> }
        }
    }

    /** Start Receiver methods */
    override fun log(tag: String?, msg: String?) {}
    override fun log(msg: String?) {}
    override fun onConsentUIReady(isFromPM: Boolean) {}
    override fun onAction(actionData: String) {
        pJsonConverter
            .toConsentAction(actionData)
            .map { /** ConsentAction  */ }
    }

    override fun onError(errorMessage: String) {
        clientInteraction?.onErrorCallback(ConsentLibException(errorMessage))
        pLogger.error(RenderingAppException(description = errorMessage, pCode = errorMessage))
    }
    /** End Receiver methods */
}