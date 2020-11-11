package com.sourcepoint.gdpr_cmplibrary;

public enum MessageLanguage {

    BULGARIAN("BG"),
    CATALAN("CA"),
    CHINESE("ZH"),
    CROATIAN("HR"),
    CZECH("CS"),
    DANISH("DA"),
    DUTCH("NL"),
    ENGLISH("EN"),
    ESTONIAN("ET"),
    FINNISH("FI"),
    FRENCH("FR"),
    GAELIC("GD"),
    GERMAN("DE"),
    GREEK("EL"),
    HUNGARIAN("HU"),
    ICELANDIC("IS"),
    ITALIAN("IT"),
    JAPANESE("JA"),
    LATVIAN("LV"),
    LITHUANIAN("LT"),
    NORWEGIAN("NO"),
    POLISH("PL"),
    PORTUGEESE("PT"),
    ROMANIAN("RO"),
    RUSSIAN("RU"),
    SERBIAN_CYRILLIC("SR-CYRL"),
    SERBIAN_LATIN("SR-LATN"),
    SLOVAKIAN("SK"),
    SLOVENIAN("SL"),
    SPANISH("ES"),
    SWEDISH("SV"),
    TURKISH("TR");

    final String language;

    MessageLanguage(String language) {
        this.language = language;
    }
}
