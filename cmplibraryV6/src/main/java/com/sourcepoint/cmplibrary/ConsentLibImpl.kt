package com.sourcepoint.cmplibrary

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.sourcepoint.cmplibrary.core.web.ConsentWebView
import com.sourcepoint.cmplibrary.core.web.JSReceiver
import com.sourcepoint.cmplibrary.data.Service
import com.sourcepoint.cmplibrary.data.network.converter.JsonConverter
import com.sourcepoint.cmplibrary.data.network.model.ConsentAction
import com.sourcepoint.cmplibrary.data.network.model.PmUrlConfig
import com.sourcepoint.cmplibrary.data.network.util.HttpUrlManager
import com.sourcepoint.cmplibrary.data.network.util.HttpUrlManagerSingleton
import com.sourcepoint.cmplibrary.model.Campaign
import com.sourcepoint.cmplibrary.model.toMessageReq
import com.sourcepoint.cmplibrary.util.* // ktlint-disable
import com.sourcepoint.cmplibrary.util.ExecutorManager
import com.sourcepoint.cmplibrary.util.ViewsManager
import com.sourcepoint.gdpr_cmplibrary.* // ktlint-disable
import com.sourcepoint.gdpr_cmplibrary.exception.* // ktlint-disable
import org.json.JSONObject

internal class ConsentLibImpl(
    private val urlManager: HttpUrlManager = HttpUrlManagerSingleton,
    internal val campaign: Campaign,
    internal val pPrivacyManagerTab: PrivacyManagerTab,
    internal val context: Context,
    internal val pLogger: Logger,
    internal val pJsonConverter: JsonConverter,
    internal val pConnectionManager: ConnectionManager,
    internal val service: Service,
    private val viewManager: ViewsManager,
    private val executor: ExecutorManager
) : ConsentLib {

    override var spClient: SpClient? = null
    private val nativeMsgClient by lazy { NativeMsgDelegate() }

    /** Start Client's methods */
    override fun loadMessage(authId: String) {
        checkMainThread("loadMessage")
        throwsExceptionIfClientNoSet()
        service.getMessage(
            messageReq = campaign.toMessageReq(),
            pSuccess = { messageResp ->
                println()
            },
            pError = { throwable ->
                println()
            }
        )
    }

    override fun loadMessage() {
        checkMainThread("loadMessage")
        throwsExceptionIfClientNoSet()


        service.getMessage(
            messageReq = campaign.toMessageReq(),
            pSuccess = { messageResp ->
                /**
                 * Test with webview
                 */
                executor.executeOnMain {
//                    val webView = viewManager.createWebView(this)
                    val receiver = JSReceiverDelegate()
                    val webView = createWebView(receiver)
                    receiver.wv = webView
                    webView.run {
                        onError = { consentLibException ->
                            println()
                        }
                        onNoIntentActivitiesFoundFor = { url ->
                            println()
                        }
                    }

                    (webView as? ConsentWebView)?.let {
                        it.settings
                        it.loadConsentUIFromUrl(urlManager.urlLocalTest())
                        Handler(Looper.getMainLooper()).postDelayed( {
                            println("msg[${messageResp.gdpr!!.message}]")
                            val obj = messageResp.gdpr.message.put("name","sp.loadMessage")
                            it.loadUrl("""
                                javascript: window.postMessage($obj);
                            """.trimIndent())
                        },1000)

                    } ?: throw RuntimeException("webView is not a ConsentWebView")
                    /**
                     * Test with webview
                     */
//                spClient?.onConsentUIReady(View(context))
//                    spClient?.onAction(ActionTypes.REJECT_ALL)
                }
            },
            pError = { throwable -> spClient?.onError(throwable.toConsentLibException()) }
        )
    }

    override fun loadMessage(nativeMessage: NativeMessage) {
        checkMainThread("loadMessage")
        throwsExceptionIfClientNoSet()

        service.getNativeMessage(
            campaign.toMessageReq(),
            { messageResp ->
                val jsonResult = messageResp.msgJSON
                executor.executeOnMain {
                    /** configuring onClickListener and set the parameters */
                    nativeMessage.setAttributes(NativeMessageAttrs(jsonResult, pLogger))
                    /** set the action callback */
                    nativeMessage.setActionClient(nativeMsgClient)
                    /** calling the client */
                    spClient?.onConsentUIReady(nativeMessage)
                }
            },
            { throwable -> pLogger.error(throwable.toConsentLibException()) }
        )
    }

    override fun loadMessage(authId: String, nativeMessage: NativeMessage) {
        checkMainThread("loadMessage")
        throwsExceptionIfClientNoSet()
    }

    override fun loadPrivacyManager() {
        checkMainThread("loadPrivacyManager")
        throwsExceptionIfClientNoSet()
        val webView = viewManager.createWebView(this)
        webView?.run {
            onError = { consentLibException -> }
            onNoIntentActivitiesFoundFor = { url -> }
        }
        val pmConfig = PmUrlConfig(
            consentUUID = "89b2d14b-70ee-4344-8cc2-1b7b281d0f2d",
            siteId = "7639",
            messageId = campaign.pmId
        )
        webView?.loadConsentUIFromUrl(urlManager.urlPm(pmConfig))
    }

    override fun loadPrivacyManager(authId: String) {
        checkMainThread("loadPrivacyManager")
        throwsExceptionIfClientNoSet()
    }

    override fun showView(view: View) {
        checkMainThread("showView")
        viewManager.showView(view)
    }

    override fun removeView(view: View?) {
        checkMainThread("removeView")
        viewManager.removeView(view)
    }

    /** end Client's methods */

    private fun createWebView(pjsReceiver : JSReceiver): com.sourcepoint.cmplibrary.core.web.ConsentWebView {
        return ConsentWebView(
            context = context,
            connectionManager = pConnectionManager,
            jsReceiver = pjsReceiver,
            logger = pLogger
        )
    }

//    /** Start Receiver methods */
    inner class JSReceiverDelegate() : JSReceiver {
//
        var wv: ConsentWebView? = null

        @JavascriptInterface
        override fun log(tag: String?, msg: String?) {
            println("===================tag log [$tag]: $msg=============================")
        }

        @JavascriptInterface
        override fun log(msg: String?) {
            println("===================== msg log [$msg] ===========================")
        }

        @JavascriptInterface
        override fun onConsentUIReady(isFromPM: Boolean) {
            println("===================== msg [onConsentUIReady]  ===========================")
            wv?.let { viewManager.showView(it) } ?: throw GenericSDKException(description = "WebView is null")
        }

        @JavascriptInterface
        override fun onAction(actionData: String) {
            println("===================== msg actionData [$actionData]  ===========================")
            pJsonConverter
                .toConsentAction(actionData)
                .map { onActionFromWebViewClient(it) }
        }

        @JavascriptInterface
        override fun onError(errorMessage: String) {
            println("===================== msg errorMessage [$errorMessage]  ===========================")
            spClient?.onError(GenericSDKException(description = errorMessage))
            pLogger.error(RenderingAppException(description = errorMessage, pCode = errorMessage))
        }
    }

    /** End Receiver methods */

    private fun throwsExceptionIfClientNoSet() {
        spClient ?: throw MissingClientException(description = "spClient instance is missing")
    }

    /**
     * Receive the action performed by the user from the WebView
     */
    internal fun onActionFromWebViewClient(action: ConsentAction) {
        executor.executeOnMain { spClient?.onAction(action.actionType) }
        when (action.actionType) {
            ActionTypes.ACCEPT_ALL -> {
            }
            ActionTypes.MSG_CANCEL -> {
            }
            ActionTypes.SAVE_AND_EXIT -> {
            }
            ActionTypes.SHOW_OPTIONS -> {
            }
            ActionTypes.REJECT_ALL -> {
            }
            ActionTypes.PM_DISMISS -> {
            }
        }
    }

    /**
     * Delegate used by the [NativeMessage] to catch events performed by the user
     */
    inner class NativeMsgDelegate : NativeMessageClient {
        /**
         * onclick listener connected to the acceptAll button in the NativeMessage View
         */
        override fun onClickAcceptAll(ca: com.sourcepoint.gdpr_cmplibrary.ConsentAction) {
            spClient?.onAction(ActionTypes.ACCEPT_ALL)
        }

        /**
         * onclick listener connected to the RejectAll button in the NativeMessage View
         */
        override fun onClickRejectAll(ca: com.sourcepoint.gdpr_cmplibrary.ConsentAction) {
            spClient?.onAction(ActionTypes.REJECT_ALL)
        }

        override fun onPmDismiss(ca: com.sourcepoint.gdpr_cmplibrary.ConsentAction) {}

        /**
         * onclick listener connected to the ShowOptions button in the NativeMessage View
         */
        override fun onClickShowOptions(ca: com.sourcepoint.gdpr_cmplibrary.ConsentAction) {
            spClient?.onAction(ActionTypes.SHOW_OPTIONS)
        }

        /**
         * onclick listener connected to the Cancel button in the NativeMessage View
         */
        override fun onClickCancel(ca: com.sourcepoint.gdpr_cmplibrary.ConsentAction) {
            spClient?.onAction(ActionTypes.MSG_CANCEL)
        }

        override fun onDefaultAction(ca: com.sourcepoint.gdpr_cmplibrary.ConsentAction) {
        }
    }

}
