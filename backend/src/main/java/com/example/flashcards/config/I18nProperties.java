package com.example.flashcards.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.i18n")
public class I18nProperties {
    private String defaultLanguage;
    private List<LanguageEntry> supportedLanguages;

    public String getDefaultLanguage() {
        return this.defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public List<LanguageEntry> getSupportedLanguages() {
        return this.supportedLanguages;
    }

    public void setSupportedLanguages(List<LanguageEntry> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public static class LanguageEntry {
        private String code;
        private String tag;
        private String label;

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTag() {
            return this.tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}