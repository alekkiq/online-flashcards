import {useState} from "react";
import { LANGUAGES } from "../../config";
import { useTranslation } from "react-i18next";
import {DropdownMenu} from "./DropdownMenu.jsx";
import { Globe } from "lucide-react";

export function LanguageSwitcher() {
    const { i18n } = useTranslation();
    const [language, setLanguage] = useState("en");

    const handleLanguageChange = e => {
        const lang = e.target.value;
        setLanguage(lang);
        i18n.changeLanguage(lang);
    };

    return (
        <div>
            <DropdownMenu>

            </DropdownMenu>
        </div>
    );
}