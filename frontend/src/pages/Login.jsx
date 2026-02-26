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
    <div className="min-h-screen grid md:grid-cols-2">
      <div className="hidden md:flex flex-col items-center min-h-screen px-12 lg:px-20 pt-[20vh] bg-grid">
        <h1 className="relative z-10 font-serif text-left font-bold text-5xl md:text-6xl lg:text-7xl text-main">
          Login to start
          <br />
          <span className="text-secondary">teaching</span>
          <br />
          <span className="text-gray-400">learning</span>
        </h1>

        <div className="relative z-10 flex flex-col font-serif items-start font-bold text-left max-w-md text-base md:text-lg mt-6">
          <p className="text-secondary">
            In vulputate cursus sem ac consectetur. Nam nec ex scelerisque, blandit neque sit amet.
          </p>
          <p className="text-gray-500">
            Morbi efficitur augue in odio posuere, vel lacinia purus auctor. Donec finibus non odio sed pellentesque.
          </p>
        </div>
      </div>

      <div className="md:min-h-screen bg-white flex flex-col items-center px-8 md:px-12 lg:px-20 md:mt-0 mt-10 md:pt-[20vh] pt-10 rounded-3xl md:rounded-none">
        <div className="md:hidden text-center mb-8">
          <h1 className="font-serif text-3xl font-bold text-main">
            Login to start learning
          </h1>
          <p className="mt-2 text-gray-500">
            Learn faster and smarter with online flashcards
          </p>
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
            Log In
          </NavLink>

          <NavLink
            to="/login?signup=true"
            className={`flex-1 pb-3 text-center text-sm font-medium border-b-2 ${
              activeTab === "register"
                ? "text-primary border-primary"
                : "text-gray-300 border-gray-200 hover:text-primary"
            }`}
          >
            Sign Up
          </NavLink>
        </div>

        <div className="w-full max-w-md">
          {activeTab === "login" ? <LoginForm /> : <RegisterForm />}
        </div>
      </div>
    </div>
  );
}
