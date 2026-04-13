package com.example.flashcards.config;

import java.util.Locale;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@EnableConfigurationProperties(I18nProperties.class)
public class LocaleConfig {
    @Bean
    public LocaleResolver localeResolver(I18nProperties props) {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag(props.getDefaultLanguage()));
        resolver.setSupportedLocales(
            props.getSupportedLanguages().stream()
                .map(l -> Locale.forLanguageTag(l.getTag()))
                .toList()
        );
        return resolver;
    }
}
