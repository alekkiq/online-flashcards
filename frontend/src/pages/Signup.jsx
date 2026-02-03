import { useState } from "react";
import { NavLink } from "react-router";
import AuthLayout from "/src/components/auth/AuthLayout";

export default function Signup() {
  const [isTeacher, setIsTeacher] = useState(false);

  const tabBase = "pb-2 text-sm font-medium";
  const tabActive =
    "text-[var(--color-primary)] border-b-2 border-[var(--color-primary)]";
  const tabInactive =
    "text-gray-300 border-b-2 border-transparent hover:text-[var(--color-main)]";

  const inputClass =
    "w-full rounded border border-gray-300 px-3 py-2 text-sm outline-none focus:border-[var(--color-primary)]";

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
            <div className="space-y-1">
              <label className="text-xs text-gray-500">First name *</label>
              <input type="text" className={inputClass} />
            </div>
            <div className="space-y-1">
              <label className="text-xs text-gray-500">Last name *</label>
              <input type="text" className={inputClass} />
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-xs text-gray-500">Email address *</label>
            <input type="email" className={inputClass} />
          </div>

          <div className="space-y-1">
            <label className="text-xs text-gray-500">Password *</label>
            <input type="password" className={inputClass} />
          </div>

          <div className="space-y-1">
            <label className="text-xs text-gray-500">Repeat password *</label>
            <input type="password" className={inputClass} />
          </div>

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
              <div className="space-y-1">
                <label className="text-xs text-gray-500">
                  Organization name *
                </label>
                <input type="text" className={inputClass} />
              </div>

              <p className="text-[11px] leading-relaxed text-gray-500">
                We will review your request for the teacher role before you can
                sign in for the first time, make sure you have entered correct
                details and your organization email.
              </p>
            </div>
          )}

          <button
            type="button"
            className="w-full bg-[var(--color-primary)] text-white py-2 rounded hover:opacity-90 text-sm"
          >
            Sign Up
          </button>
        </form>
      </div>
    </AuthLayout>
  );
}
