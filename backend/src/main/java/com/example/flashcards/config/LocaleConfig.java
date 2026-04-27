package com.example.flashcards.config;

import java.util.List;
import java.util.Locale;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableConfigurationProperties(I18nProperties.class)
public class LocaleConfig {
    @Bean
    public LocaleResolver localeResolver(I18nProperties props) {
        Locale defaultLocale = Locale.forLanguageTag(props.getDefaultLanguage());
        List<Locale> supportedLocales = props.getSupportedLanguages().stream()
            .map(l -> Locale.forLanguageTag(l.getTag()))
            .toList();

        return new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String headerLang = request.getHeader("Accept-Language");

                if (headerLang != null && !headerLang.isBlank()) {
                    try {
                        Locale requestedLocale = Locale.forLanguageTag(headerLang);
                        if (supportedLocales.contains(requestedLocale)) {
                            return requestedLocale;
                        }
                        String language = requestedLocale.getLanguage();
                        Locale languageOnlyLocale = supportedLocales.stream()
                            .filter(l -> l.getLanguage().equals(language))
                            .findFirst()
                            .orElse(null);
                        if (languageOnlyLocale != null) {
                            return languageOnlyLocale;
                        }
                    } catch (Exception e) {
                        // Fall through to default
                    }
                }

                return defaultLocale;
            }
        };
    }
}
