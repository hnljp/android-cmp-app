package com.sourcepoint.cmplibrary

import android.view.View
import com.sourcepoint.gdpr_cmplibrary.NativeMessage

interface ConsentLib {
    fun loadMessage()
    fun loadMessage(authId: String)
    fun loadMessage(nativeMessage: NativeMessage)
    fun loadMessage(authId: String, nativeMessage: NativeMessage)
    fun loadPrivacyManager()
    fun loadPrivacyManager(authId: String)

    fun showView(view: View)
    fun removeView(view: View)
}
