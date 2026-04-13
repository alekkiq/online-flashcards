package com.example.flashcards.common.provider;

import com.example.flashcards.config.I18nProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CurrentLanguageProvider {
    private final I18nProperties i18nProperties;

    public CurrentLanguageProvider(I18nProperties i18nProperties) {
        this.i18nProperties = i18nProperties;
    }

    public String getCurrentLanguage() {
        String language = LocaleContextHolder.getLocale().getLanguage();

        if (language.isBlank()) {
            return i18nProperties.getDefaultLanguage();
        }

        return language.toLowerCase(Locale.ROOT);
    }
}
