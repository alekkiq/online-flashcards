import { NavLink } from "react-router";
import AuthLayout from "/src/components/auth/AuthLayout";

export default function Login() {
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
          <div className="space-y-1">
            <label className="text-xs text-gray-500">Email address *</label>
            <input type="email" className={inputClass} />
          </div>

          <div className="space-y-1">
            <label className="text-xs text-gray-500">Password *</label>
            <input type="password" className={inputClass} />
          </div>

          <button
            type="button"
            className="w-full bg-[var(--color-primary)] text-white py-2 rounded hover:opacity-90 text-sm"
          >
            Log In
          </button>
        </form>
      </div>
    </AuthLayout>
  );
}
