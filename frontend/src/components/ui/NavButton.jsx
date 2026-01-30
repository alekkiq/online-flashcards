import { NavLink } from "react-router";
import { cn } from "/src/lib/utils";

export function NavButton({ to, children, className, ...props }) {
  return (
    <NavLink
      to={to}
      className={({ isActive }) =>
        cn(
          "px-4 py-2 text-lg font-bold transition-colors",
          isActive
            ? "text-primary hover:bg-secondary/5"
            : "text-main hover:bg-secondary/5",
          className
        )
      }
      {...props}
    >
      {children}
    </NavLink>
  );
}
