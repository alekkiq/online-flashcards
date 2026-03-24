import { LANGUAGES } from "../../config";
import { useTranslation } from "react-i18next";
import { DropdownMenu } from "./DropdownMenu.jsx";
import { Globe } from "lucide-react";

export function LanguageSwitcher() {
  const { i18n } = useTranslation();
  const currentLang = i18n.language?.split("-")[0] ?? "en";

  const items = Object.values(LANGUAGES).map((lang) => ({
    label: lang.label,
    active: currentLang === lang.lng,
    onClick: () => i18n.changeLanguage(lang.lng),
  }));

  const trigger = (
    <button
      className="flex items-center gap-1.5 rounded-md px-2 py-1.5 text-sm text-secondary hover:bg-secondary/10 hover:text-main transition-colors cursor-pointer"
      aria-label="Switch language"
    >
      <Globe size={16} />
      <span className="hidden sm:inline uppercase">
        {LANGUAGES[currentLang]?.lng ?? currentLang}
      </span>
    </button>
  );

  return <DropdownMenu items={items} trigger={trigger} triggerLabel="Switch language" />;
}
