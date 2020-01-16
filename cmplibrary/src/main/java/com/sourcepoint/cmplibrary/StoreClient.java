package com.sourcepoint.cmplibrary;

import android.content.SharedPreferences;

public class StoreClient {
    /**
     * If the user has consent data stored, reading for this key in the shared preferences will return true
     */
    private static final String IAB_CONSENT_CMP_PRESENT = "IABConsent_CMPPresent";

    /**
     * If the user is subject to GDPR, reading for this key in the shared preferences will return "1" otherwise "0"
     */
    private static final String IAB_CONSENT_SUBJECT_TO_GDPR = "IABConsent_SubjectToGDPR";

    /**
     * They key used to store the IAB Consent string for the user in the shared preferences
     */
    private static final String IAB_CONSENT_CONSENT_STRING = "IABConsent_ConsentString";

    /**
     * They key used to read and write the parsed IAB Purposes consented by the user in the shared preferences
     */
    private static final String IAB_CONSENT_PARSED_PURPOSE_CONSENTS = "IABConsent_ParsedPurposeConsents";

    /**
     * They key used to read and write the parsed IAB Vendor consented by the user in the shared preferences
     */
    private static final String IAB_CONSENT_PARSED_VENDOR_CONSENTS = "IABConsent_ParsedVendorConsents";

    private static final String CONSENT_UUID_KEY = "consentUUID";

    private static final String META_DATA_KEY = "metaData";

    private SharedPreferences.Editor editor;

    private SharedPreferences pref;

    StoreClient(SharedPreferences pref){
        this.editor = pref.edit();
        this.pref = pref;
    }

    public void setConsentSubjectToGDPr(Boolean consentSubjectToGDPR){
        editor.putString(IAB_CONSENT_SUBJECT_TO_GDPR, consentSubjectToGDPR != null ? (consentSubjectToGDPR ? "1" : "0") : null);
    }

    public void setIabConsentCmpPresent(Boolean iabConsentCmpPresent){
        editor.putBoolean(IAB_CONSENT_CMP_PRESENT, iabConsentCmpPresent);
    }

    public void setIabConsentConsentString(String consentConsentString){
        editor.putString(IAB_CONSENT_CONSENT_STRING, consentConsentString);
    }

    public void setIabConsentParsedPurposeConsents(String consentParsedPurposeConsents){
        editor.putString(IAB_CONSENT_PARSED_PURPOSE_CONSENTS, consentParsedPurposeConsents);
    }

    public void setIabConsentParsedVendorConsents(String consentParsedVendorConsents){
        editor.putString(IAB_CONSENT_PARSED_VENDOR_CONSENTS, consentParsedVendorConsents);
    }

    public void setConsentUuid(String consentUuid){
        editor.putString(CONSENT_UUID_KEY, consentUuid);
    }

    public void setMetaData(String  metaData){
        editor.putString(META_DATA_KEY, metaData);
    }

    public void commit(){
        editor.commit();
    }

    public void clear(){
        editor.clear();
    }

    public String getMetaData() {
        return pref.getString(META_DATA_KEY, "{}");
    }

    public String getConsentUUID() {
        return pref.getString(CONSENT_UUID_KEY, null);
    }

    public String getConsentString() {
        return pref.getString(IAB_CONSENT_CONSENT_STRING, null);
    }
}
