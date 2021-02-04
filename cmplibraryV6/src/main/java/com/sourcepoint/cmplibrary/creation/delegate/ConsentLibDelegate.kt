package com.sourcepoint.cmplibrary.creation.delegate

import android.app.Activity
import com.sourcepoint.cmplibrary.ConsentLib
import com.sourcepoint.cmplibrary.creation.makeConsentLib
import com.sourcepoint.cmplibrary.model.Campaign
import com.sourcepoint.gdpr_cmplibrary.PrivacyManagerTab
import kotlin.reflect.KProperty

class ConsentLibDelegate(
    private val privacyManagerTab: PrivacyManagerTab = PrivacyManagerTab.PURPOSES,
    private val campaign: Campaign
) {

    private lateinit var lib: ConsentLib

    operator fun getValue(thisRef: Activity, property: KProperty<*>): ConsentLib {
        if (!this::lib.isInitialized) {
            lib = makeConsentLib(
                propertyName = campaign.propertyName,
                context = thisRef,
                pmId = campaign.pmId,
                accountId = campaign.accountId,
                propertyId = campaign.propertyId,
                privacyManagerTab = privacyManagerTab
            )
        }
        return lib
    }
}
