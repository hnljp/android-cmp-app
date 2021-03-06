package com.sourcepoint.example_app

import android.webkit.CookieManager
import com.example.uitestutil.*
import com.sourcepoint.example_app.TestData.ACCEPT
import com.sourcepoint.example_app.TestData.ACCEPT_ALL
import com.sourcepoint.example_app.TestData.CONSENT_LIST
import com.sourcepoint.example_app.TestData.FEATURES
import com.sourcepoint.example_app.TestData.MESSAGE
import com.sourcepoint.example_app.TestData.OPTIONS
import com.sourcepoint.example_app.TestData.PARTIAL_CONSENT_LIST
import com.sourcepoint.example_app.TestData.PRIVACY_MANAGER
import com.sourcepoint.example_app.TestData.PURPOSES
import com.sourcepoint.example_app.TestData.REJECT
import com.sourcepoint.example_app.TestData.REJECT_ALL
import com.sourcepoint.example_app.TestData.SAVE_AND_EXIT

class TestUseCase {

    companion object {

        fun checkConsentIsNotSelected() {
            CONSENT_LIST.forEach { consent ->
                checkConsentState(consent, false)
            }
        }

        fun checkConsentIsSelected() {
            CONSENT_LIST.forEach { consent ->
                checkConsentState(consent, true)
            }
        }

        fun checkMainWebViewDisplayed() {
            isDisplayedAllOfByResId(resId = R.id.review_consents)
        }

        fun clickOnReviewConsent() {
            performClickByIdCompletelyDisplayed(resId = R.id.review_consents)
        }

        fun openAuthIdActivity() {
            performClickByIdCompletelyDisplayed(resId = R.id.auth_id_activity)
        }

        fun checkAuthIdIsDisplayed(autId : String) {
            checkElementWithText("authId", autId)
        }

        fun checkAuthIdIsNotDisplayed() {
            checkElementWithText("authId", "no_auth_id")
        }

        fun checkWebViewDisplayedForMessage() {
            checkWebViewHasText(MESSAGE)
        }

        fun clickPMTabSelectedPurposes() {
            performClickPMTabSelected(PURPOSES)
        }

        fun tapOptionWebView() {
            performClickOnWebViewByContent(OPTIONS)
        }

        fun tapAcceptAllOnWebView() {
            performClickOnWebViewByContent(ACCEPT_ALL)
        }

        fun tapRejectOnWebView() {
            performClickOnWebViewByContent(REJECT)
        }

        fun tapSaveAndExitWebView() {
            performClickOnWebViewByContent(SAVE_AND_EXIT)
        }

        fun tapRejectAllWebView() {
            performClickOnWebViewByContent(REJECT_ALL)
        }

        fun tapDismissWebView() {
            performClickOnWebViewByClass("message-stacksclose")
        }

        fun tapAcceptOnWebView() {
            performClickOnWebViewByContent(ACCEPT)
        }

        fun setFocusOnLayoutActivity() {
            performClickByIdCompletelyDisplayed(resId = R.id.main_view)
        }

        fun checkWebViewDisplayedForPrivacyManager() {
            checkWebViewHasText(PRIVACY_MANAGER)
        }

        fun checkPartialConsentIsSelected() {
            PARTIAL_CONSENT_LIST.forEach { consent ->
                checkConsentState(consent, true)
            }
        }

        fun checkPartialConsentIsNotSelected() {
            PARTIAL_CONSENT_LIST.forEach { consent ->
                checkConsentState(consent, false)
            }
        }

        fun checkConsentAsSelectedFromPartialConsentList() {
            PARTIAL_CONSENT_LIST.forEach { consent ->
                setCheckBoxTrue(consent)
            }
        }

        fun checkPMTabSelectedFeatures() {
            checkPMTabSelected(FEATURES)
        }

        fun checkPMTabSelectedPurposes() {
            checkPMTabSelected(PURPOSES)
        }

        fun selectPartialConsentList() {
            PARTIAL_CONSENT_LIST.forEach { consent ->
                checkConsentWebView(consent)
            }
        }

        fun checkCookieExist(url : String, value : String){
            CookieManager.getInstance()
                .getCookie(url)
                .contains(value)
                .assertTrue()
        }

        fun checkCookieNotExist(url : String){
            CookieManager.getInstance()
                .getCookie(url)
                .contains("authId=")
                .assertFalse()
        }
    }
}