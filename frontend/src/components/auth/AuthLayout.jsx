import { NavLink } from "react-router";

export default function AuthLayout({ children }) {
  const linkClass =
    "text-sm text-[var(--color-main)] hover:opacity-80";

  const loginBtnClass =
    "text-sm bg-[var(--color-primary)] text-white px-3 py-1 rounded hover:opacity-90";

  return (
    <div className="min-h-screen bg-white">
      {/* Top bar */}
      <header className="h-14 border-b border-gray-200 flex items-center px-6">
        <div className="font-semibold tracking-wide">LOGO</div>

        <div className="ml-auto flex items-center gap-4">
          {/* Sign Up is just a link (text) */}
          <NavLink to="/signup" className={linkClass}>
            Sign Up
          </NavLink>

          {/* Login button is ALWAYS purple (like in Figma) */}
          <NavLink to="/login" className={loginBtnClass}>
            Login In
          </NavLink>
        </div>
      </header>

      {/* 2-column layout */}
      <div className="min-h-[calc(100vh-56px)] grid grid-cols-1 md:grid-cols-2">
        {/* Left hero */}
        <section className="bg-[var(--color-background)] flex items-center px-10 py-12">
          <div className="max-w-md">
            <h1 className="font-serif text-5xl leading-tight text-[var(--color-main)]">
              Login to start
              <br />
              <span className="text-gray-500">teaching</span>
              <br />
              <span className="text-gray-300">learning</span>
            </h1>

            <p className="mt-6 text-sm text-gray-500 leading-relaxed">
              In vulputate cursus sem ac consectetur. Nam nec ex scelerisque,
              blandit neque sit amet, sollicitudin eros. Maecenas risus eros,
              sodales quis diam sed, cursus cursus magna. Nunc venenatis.
            </p>
          </div>
        </section>

        {/* Right form area */}
        <section className="flex items-center justify-center px-6 py-12">
          <div className="w-full max-w-md">{children}</div>
        </section>
      </div>
    </div>
  );
}
