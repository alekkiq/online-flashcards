import { useEffect, useState } from "react";
import { NavLink, useLocation } from "react-router";
import LoginForm from "@/components/auth/LoginForm";
import RegisterForm from "@/components/auth/RegisterForm";

export default function Login() {
  const location = useLocation();
  const [activeTab, setActiveTab] = useState("login");

  useEffect(() => {
    setActiveTab(location.pathname === "/signup" ? "register" : "login");
  }, [location.pathname]);

  return (
    <div className="min-h-[calc(100vh-56px)] py-12">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-16 items-start pt-6">
        {/* LEFT HERO – desktop */}
        <div className="hidden md:block max-w-md">
          <h1 className="font-serif text-5xl leading-tight text-[var(--color-main)] font-medium">
            Learn by
            <br />
            <span className="text-gray-500 font-normal">creating</span>
            <br />
            <span className="text-gray-300 font-normal">flashcards</span>
          </h1>

          <p className="mt-6 text-sm text-gray-500 leading-relaxed">
            Create, study, and share flashcards to organize knowledge
            and follow your learning progress in one place.
          </p>
        </div>

        {/* FORM CARD */}
        <div className="w-full max-w-lg bg-white rounded-xl border border-gray-200 px-8 py-8 shadow-sm justify-self-center">
          {/* HERO – mobile */}
          <div className="md:hidden text-center mb-6">
            <h1 className="text-2xl font-semibold text-[var(--color-main)]">
              Online Flashcards
            </h1>
            <p className="mt-2 text-sm text-gray-500">
              Learn faster with smart flashcards
            </p>
          </div>

          {/* TABS */}
          <div className="flex justify-center gap-12 mb-8 border-b border-gray-200">
            <NavLink
              to="/login"
              end
              className={({ isActive }) =>
                `pb-2 text-sm font-medium ${
                  isActive
                    ? "text-[var(--color-primary)] border-b-2 border-[var(--color-primary)]"
                    : "text-gray-300 hover:text-[var(--color-main)]"
                }`
              }
            >
              Log In
            </NavLink>

            <NavLink
              to="/signup"
              className={({ isActive }) =>
                `pb-2 text-sm font-medium ${
                  isActive
                    ? "text-[var(--color-primary)] border-b-2 border-[var(--color-primary)]"
                    : "text-gray-300 hover:text-[var(--color-main)]"
                }`
              }
            >
              Register
            </NavLink>
          </div>

          {activeTab === "login" ? <LoginForm /> : <RegisterForm />}
        </div>
      </div>
    </div>
  );
}
