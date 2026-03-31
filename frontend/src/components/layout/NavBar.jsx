import { Link } from "react-router";
import { navLinks } from "/src/config";
import { NavButton } from "/src/components/ui/NavButton";
import { LanguageSwitcher } from "/src/components/ui/LanguageSwitcher";
import MobileNav from "./MobileNav";
import AuthLinks from "/src/components/auth/AuthLinks";
import { useAuth } from "/src/hooks/useAuth";
import { Spade, BookCopy } from "lucide-react";
import { Logo } from "/src/components/ui/Logo";
import { useTranslation } from "react-i18next";

export default function NavBar() {
  const { user } = useAuth();
  const { t } = useTranslation();
  const links = navLinks(user, t);

  return (
    <nav className="relative flex items-center justify-between p-4 w-full border-b border-secondary/30 bg-white py-5">
      <div>
        <Link to="/">
            <Logo />
        </Link>
      </div>

      <div className="hidden md:flex absolute left-1/2 -translate-x-1/2 flex-row gap-2">
        {links.map((item) => (
          <NavButton key={item.to} to={item.to}>
            {item.label}
          </NavButton>
        ))}
      </div>

      <div className="flex items-center gap-3">
        <LanguageSwitcher />
        <div className="hidden md:flex">
          <AuthLinks />
        </div>
        <MobileNav />
      </div>
    </nav>
  );
}
