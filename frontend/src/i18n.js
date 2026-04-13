import { LANGUAGES } from "./config";

import i18n from "i18next";
import { initReactI18next } from "react-i18next";

import Backend from "i18next-http-backend";

const LOCALE_STORAGE_KEY = "locale";

const supportedLanguages = Object.keys(LANGUAGES);
const browserLanguage = (navigator.language || "en").split("-")[0];

const savedLanguage = localStorage.getItem(LOCALE_STORAGE_KEY);
const defaultLanguage =
  (supportedLanguages.includes(savedLanguage) && savedLanguage) ||
  (supportedLanguages.includes(browserLanguage) && browserLanguage) ||
  "en";

i18n
  .use(Backend)
  .use(initReactI18next)
  .init({
    fallbackLng: "en",
    lng: defaultLanguage,
    debug: true,
    interpolation: {
      escapeValue: false,
    },
  });

i18n.on("languageChanged", (lng) => {
  localStorage.setItem(LOCALE_STORAGE_KEY, lng);
});

export default i18n;
