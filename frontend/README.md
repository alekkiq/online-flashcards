# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Babel](https://babeljs.io/) (or [oxc](https://oxc.rs) when used in [rolldown-vite](https://vite.dev/guide/rolldown)) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Localization (i18n)

The app uses [i18next](https://www.i18next.com/) with `react-i18next` for internationalization.

### Supported languages

| Code | Language | RTL |
|------|----------|-----|
| `en` | English  | No  |
| `fi` | Suomi    | No  |
| `fa` | فارسی    | Yes |
| `zh` | 普通话   | No  |

### Translation files

Translations are stored as JSON files under `public/locales/<lang>/translation.json`. The app loads them at runtime via `i18next-http-backend`.

```
public/
  locales/
    en/translation.json
    fi/translation.json
    fa/translation.json
    zh/translation.json
```

### Adding a new language

1. Add an entry to `src/config/languages.js`:
   ```js
   sv: {
     locale: 'sv_SE',
     lng: 'sv',
     label: 'Svenska',
     isRtl: false,
   }
   ```
2. Create `public/locales/sv/translation.json` with the translated strings (use `en/translation.json` as a reference).

### Using translations in components

```jsx
import { useTranslation } from 'react-i18next';

function MyComponent() {
  const { t } = useTranslation();
  return <p>{t('common.loading')}</p>;
}
```

The `LanguageSwitcher` component (`src/components/ui/LanguageSwitcher.jsx`) renders a dropdown in the navbar that lets users switch languages at runtime. The selected language is detected from the browser on first load and falls back to English if unsupported.

### RTL support

Languages marked `isRtl: true` in `src/config/languages.js` (currently Farsi) require RTL layout handling in CSS. This flag is available app-wide via the `LANGUAGES` config so components can conditionally apply RTL styles.

## React Compiler

The React Compiler is not enabled on this template because of its impact on dev & build performances. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation).

## Expanding the ESLint configuration

If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.
