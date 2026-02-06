import { NavLink } from "react-router";
import AuthLayout from "/src/components/auth/AuthLayout";

import { FormField } from "@/components/ui/FormField";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";

export default function Login() {
  const tabBase = "pb-2 text-sm font-medium";
  const tabActive =
    "text-[var(--color-primary)] border-b-2 border-[var(--color-primary)]";
  const tabInactive =
    "text-gray-300 border-b-2 border-transparent hover:text-[var(--color-main)]";

  return (
    <AuthLayout>
      <div className="bg-white rounded-lg border border-gray-200 p-7">
        {/* Tabs */}
        <div className="flex justify-center gap-10 mb-7">
          <NavLink
            to="/login"
            className={({ isActive }) =>
              `${tabBase} ${isActive ? tabActive : tabInactive}`
            }
          >
            Log In
          </NavLink>

          <NavLink
            to="/signup"
            className={({ isActive }) =>
              `${tabBase} ${isActive ? tabActive : tabInactive}`
            }
          >
            Register
          </NavLink>
        </div>

        <form className="space-y-4">
          <FormField label="Email address *">
            <Input
              type="email"
              placeholder="Email address"
              autoComplete="email"
            />
          </FormField>

          <FormField label="Password *">
            <Input
              type="password"
              placeholder="Password"
              autoComplete="current-password"
            />
          </FormField>

          <Button type="button" className="w-full h-10">
            Log In
          </Button>
        </form>
      </div>
    </AuthLayout>
  );
}