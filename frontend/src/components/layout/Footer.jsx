import { Link } from "react-router";
import { BookCopy } from "lucide-react";
import { navLinks } from "/src/config";
import { useAuth } from "/src/hooks/useAuth";
import { useTranslation } from "react-i18next";
import {Logo} from "../ui/Logo.jsx";

export default function Footer() {
  const { user, handleLogout } = useAuth();
  const { t } = useTranslation();
  const links = navLinks(user, t);

  return (
    <footer className="bg-main text-white/80 mt-10 ">
      <div className="max-w-6xl mx-auto px-6 pt-14 pb-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-12 md:gap-8">
          <div className="md:col-span-1">
            <Link to="/" className="inline-flex items-center gap-2 group">
              <Logo />
            </Link>
            <p className="mt-4 text-sm text-white/50 leading-relaxed max-w-xs">
              {t("footer.tagline")}
            </p>
          </div>
          <div>
            <h4 className="font-inter text-xs font-semibold uppercase tracking-widest text-white/30 mb-4">
              {t("footer.navigate")}
            </h4>
            <ul className="space-y-3">
              {links.map((link) => (
                <li key={link.to}>
                  <Link
                    to={link.to}
                    className="text-sm text-white/50 hover:text-primary transition-colors duration-200"
                  >
                    {link.label}
                  </Link>
                </li>
              ))}
            </ul>
          </div>
          <div>
            <h4 className="font-inter text-xs font-semibold uppercase tracking-widest text-white/30 mb-4">
              {t("footer.actions")}
            </h4>
            {!user ? (
                <ul className="space-y-3">
                <li>
                    <Link to="/login" className="text-sm text-white/50 hover:text-primary transition-colors duration-200">
                    {t("footer.login")}
                    </Link>
                </li>
                <li>
                    <Link to="login?signup=true" className="text-sm text-white/50 hover:text-primary transition-colors duration-200">
                    {t("footer.signUp")}
                    </Link>
                </li>
                </ul>
            ) : (
                <ul className="space-y-3">
                    <li>
                        <Link to="/profile" className="text-sm text-white/50 hover:text-primary transition-colors duration-200">
                            {t("footer.profile")}
                        </Link>
                    </li>
                    <li>
                        <Link to="/my-quizzes/create" className="text-sm text-white/50 hover:text-primary transition-colors duration-200">
                            {t("footer.createQuiz")}
                        </Link>
                    </li>
                    <li>
                        <button onClick={handleLogout} className="text-sm text-white/50 hover:text-primary transition-colors duration-200">
                            {t("footer.logout")}
                        </button>
                    </li>
                </ul>
            )}
          </div>
        </div>
        <div className="mt-12 pt-6 border-t border-white/10 flex flex-col sm:flex-row items-center justify-between gap-3">
          <p className="text-xs text-white/30">
            &copy; {new Date().getFullYear()} OnlyCards.
          </p>
          <p className="text-xs text-white/30">
            {t("footer.madeWith")}
          </p>
        </div>
      </div>
    </footer>
  );
}