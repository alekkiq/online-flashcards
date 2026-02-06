import { useState } from "react";
import { NavLink } from "react-router";
import AuthLayout from "/src/components/auth/AuthLayout";

import { FormField } from "@/components/ui/FormField";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";

export default function Signup() {
  const [isTeacher, setIsTeacher] = useState(false);

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
          <div className="grid grid-cols-2 gap-3">
            <FormField label="First name *">
              <Input
                type="text"
                placeholder="First name"
                autoComplete="given-name"
              />
            </FormField>

            <FormField label="Last name *">
              <Input
                type="text"
                placeholder="Last name"
                autoComplete="family-name"
              />
            </FormField>
          </div>

          <FormField label="Email address *">
            <Input type="email" placeholder="Email address" autoComplete="email" />
          </FormField>

          <FormField label="Password *">
            <Input
              type="password"
              placeholder="Password"
              autoComplete="new-password"
            />
          </FormField>

          <FormField label="Repeat password *">
            <Input
              type="password"
              placeholder="Repeat password"
              autoComplete="new-password"
            />
          </FormField>

          {/* Teacher checkbox */}
          <label className="flex items-center gap-2 text-xs text-[var(--color-secondary)]">
            <input
              type="checkbox"
              checked={isTeacher}
              onChange={(e) => setIsTeacher(e.target.checked)}
            />
            Are you a teacher?
          </label>

          {/* Organization + info */}
          {isTeacher && (
            <div className="space-y-2">
              <FormField label="Organization name *">
                <Input type="text" placeholder="Organization name" />
              </FormField>

              <p className="text-[11px] leading-relaxed text-gray-500">
                We will review your request for the teacher role before you can
                sign in for the first time, make sure you have entered correct
                details and your organization email.
              </p>
            </div>
          )}

          <Button type="button" className="w-full h-10">
            Sign Up
          </Button>
        </form>
      </div>
    </AuthLayout>
  );
}