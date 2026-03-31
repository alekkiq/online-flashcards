import { useEffect, useState } from "react";
import { NavLink, useNavigate } from "react-router";
import LoginForm from "/src/components/auth/LoginForm";
import RegisterForm from "/src/components/auth/RegisterForm";
import { useSearchParams } from "react-router";
import { useAuth } from "../hooks/useAuth";
import { useTranslation } from "react-i18next";

export default function Login() {
  const [searchParams] = useSearchParams();
  const [activeTab, setActiveTab] = useState("login");
  const navigate = useNavigate();
  const { user, isLoading } = useAuth();
  const { t } = useTranslation();

  useEffect(() => {
    setActiveTab(searchParams.get("signup") ? "register" : "login");
  }, [searchParams]);

  useEffect(() => {
    if (user && !isLoading) {
      const redirectPath = searchParams.get("redirect") || "/";
      navigate(redirectPath, { replace: true });
    }
  }, [user, isLoading, searchParams, navigate]);

  if (isLoading) {
    return null;
  }

  return (
    <div className="min-h-screen grid md:grid-cols-2">
      <div className="hidden md:flex flex-col min-h-screen px-12 lg:px-20 pt-[20vh] bg-grid">
        <h1 className="relative z-10 font-serif text-start font-bold text-5xl md:text-6xl lg:text-7xl text-main">
          {t("login.loginToStart")}
          <br />
          <span className="text-secondary">{t("login.teaching")}</span>
          <br />
          <span className="text-gray-400">{t("login.learning")}</span>
        </h1>

        <div className="z-10 flex flex-col font-serif items-start font-bold text-start text-base md:text-lg mt-6">
          <p className="text-secondary">{t("home.heroDescription1")}</p>
          <p className="text-gray-500">{t("home.heroDescription2")}</p>
        </div>
      </div>

      <div className="md:min-h-screen bg-white flex flex-col items-center px-8 md:px-12 lg:px-20 md:mt-0 mt-10 md:pt-[20vh] pt-10 rounded-3xl md:rounded-none">
        <div className="md:hidden text-center mb-8">
          <h1 className="font-serif text-3xl font-bold text-main">
            {t("login.loginToStartLearning")}
          </h1>
          <p className="mt-2 text-gray-500">{t("login.learnFaster")}</p>
        </div>

        <div className="flex mb-8 w-full max-w-md">
          <NavLink
            to="/login"
            end
            className={`flex-1 pb-3 text-center text-sm font-medium border-b-2 ${
              activeTab === "login"
                ? "text-primary border-primary"
                : "text-gray-300 border-gray-200 hover:text-primary"
            }`}
          >
            {t("login.logIn")}
          </NavLink>

          <NavLink
            to="/login?signup=true"
            className={`flex-1 pb-3 text-center text-sm font-medium border-b-2 ${
              activeTab === "register"
                ? "text-primary border-primary"
                : "text-gray-300 border-gray-200 hover:text-primary"
            }`}
          >
            {t("login.signUp")}
          </NavLink>
        </div>

        <div className="w-full max-w-md">
          {activeTab === "login" ? <LoginForm /> : <RegisterForm />}
        </div>
      </div>
    </div>
  );
}
