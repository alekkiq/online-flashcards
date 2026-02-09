import { useEffect, useState } from "react";
import { NavLink } from "react-router";
import LoginForm from "/src/components/auth/LoginForm";
import RegisterForm from "/src/components/auth/RegisterForm";
import { useSearchParams } from "react-router";

export default function Login() {
  const [searchParams] = useSearchParams();
  const [activeTab, setActiveTab] = useState("login");

  useEffect(() => {
    setActiveTab(searchParams.get("signup") ? "register" : "login");
  }, [searchParams]);

  return (
    <div className="min-h-[calc(100vh-56px)] py-12">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-16 items-start pt-6">
        <div className="md:flex flex-col hidden">
          <h1 className="font-serif text-5xl md:text-6xl lg:text-7xl text-main font-bold leading-tight">
            Login to start
            <br />
            <span className="text-gray-500 font-bold">learning</span>
            <br />
            <span className="text-primary/50 font-bold">teaching</span>
          </h1>

          <p className="mt-6 text-sm text-gray-500 leading-relaxed">
            In vulputate cursus sem ac consectetur. Nam nec ex scelerisque, blandit neque sit amet, sollicitudin eros. Maecenas risus eros, sodales quis diam sed, cursus cursus magna. Nunc venenatis
          </p>
        </div>

        {/* FORM CARD */}
        <div className="w-full max-w-lg bg-white rounded-xl border border-gray-200 px-8 py-8 shadow-sm justify-self-center">
          {/* HERO – mobile */}
          <div className="md:hidden text-center mb-6">
            <h1 className="text-2xl font-semibold text-main">
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
              className={`pb-2 text-sm font-medium ${
                activeTab === "login"
                  ? "text-[var(--color-primary)] border-b-2 border-[var(--color-primary)]"
                  : "text-gray-300 hover:text-[var(--color-main)]"
              }`}
            >
              Log In
            </NavLink>

            <NavLink
              to="/login?signup=true"
              className={`pb-2 text-sm font-medium ${
                activeTab === "register"
                  ? "text-[var(--color-primary)] border-b-2 border-[var(--color-primary)]"
                  : "text-gray-300 hover:text-[var(--color-main)]"
              }`}
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
