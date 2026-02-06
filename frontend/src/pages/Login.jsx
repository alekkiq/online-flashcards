import { useEffect, useState } from "react";
import { NavLink, useLocation } from "react-router";
import { FormField } from "@/components/ui/FormField";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";

/* login form */
function LoginForm() {
  return (
    <form className="space-y-4">
      <FormField label="Email address *">
        <Input type="email" placeholder="Email address" autoComplete="email" />
      </FormField>

      <FormField label="Password *">
        <Input
          type="password"
          placeholder="Password"
          autoComplete="current-password"
        />
      </FormField>

      <Button type="button" className="w-full h-11">
        Log In
      </Button>
    </form>
  );
}

/* register form */
function RegisterForm() {
  const [isTeacher, setIsTeacher] = useState(false);

  return (
    <form className="space-y-4">
      <div className="grid grid-cols-2 gap-3">
        <FormField label="First name *">
          <Input type="text" placeholder="First name" />
        </FormField>

        <FormField label="Last name *">
          <Input type="text" placeholder="Last name" />
        </FormField>
      </div>

      <FormField label="Email address *">
        <Input type="email" placeholder="Email address" />
      </FormField>

      <FormField label="Password *">
        <Input type="password" placeholder="Password" />
      </FormField>

      <FormField label="Repeat password *">
        <Input type="password" placeholder="Repeat password" />
      </FormField>

      <label className="flex items-center gap-2 text-xs text-[var(--color-secondary)]">
        <input
          type="checkbox"
          checked={isTeacher}
          onChange={(e) => setIsTeacher(e.target.checked)}
        />
        Are you a teacher?
      </label>

      {isTeacher && (
        <div className="space-y-2">
          <FormField label="Organization name *">
            <Input type="text" placeholder="Organization name" />
          </FormField>

          <p className="text-[11px] text-gray-500">
            We review teacher accounts before first sign in.
          </p>
        </div>
      )}

      <Button type="button" className="w-full h-11">
        Sign Up
      </Button>
    </form>
  );
}

export default function Login() {
  const location = useLocation();
  const [activeTab, setActiveTab] = useState("login");

  // switch tab by route
  useEffect(() => {
    setActiveTab(location.pathname === "/signup" ? "register" : "login");
  }, [location.pathname]);

  return (
    <div className="min-h-[calc(100vh-56px)] py-16">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-16 items-start pt-6">

        {/* left hero (desktop) */}
        <div className="hidden md:block max-w-md">
          <h1 className="font-serif text-5xl leading-tight text-[var(--color-main)] font-medium">
            Login to start
            <br />
            <span className="text-gray-500 font-normal">teaching</span>
            <br />
            <span className="text-gray-300 font-normal">learning</span>
          </h1>

          <p className="mt-6 text-sm text-gray-500 leading-relaxed">
            Create, study, and share flashcards to learn more effectively.
            Online Flashcards helps you organize knowledge and track progress.
          </p>
        </div>

        {/* form card */}
        <div className="w-full max-w-lg bg-white rounded-xl border border-gray-200 px-10 py-9 shadow-sm justify-self-center">

          {/* mobile hero */}
          <div className="md:hidden text-center mb-6">
            <h1 className="text-2xl font-semibold text-[var(--color-main)]">
              Login
            </h1>
            <p className="mt-2 text-sm text-gray-500">
              Learn faster with flashcards
            </p>
          </div>

          {/* tabs */}
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