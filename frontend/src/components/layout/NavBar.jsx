import { Link } from "react-router";
import { navLinks } from "/src/config";
import { NavButton } from "/src/components/ui/NavButton";
import MobileNav from "./MobileNav";
import AuthLinks from "/src/components/auth/AuthLinks";
import { useAuth } from "/src/hooks/useAuth";
import { Spade, BookCopy } from "lucide-react";

export default function NavBar() {
  const { user } = useAuth();
  const links = navLinks(user);

  return (
    <nav className="relative flex items-center justify-between p-4 w-full border-b border-secondary/30 bg-white py-5">
      <div>
        <Link to="/">
          <div className="flex items-center gap-1">
              {/* <Spade className="text-primary"> */}
            <BookCopy className="text-primary" strokeWidth={2.25} />
            <p className="font-serif text-xl md:text-2xl font-black">OnlyCards</p>
          </div>
        </Link>
      </div>

      <div className="hidden md:flex absolute left-1/2 -translate-x-1/2 flex-row gap-2">
        {links.map((item) => (
          <NavButton key={item.to} to={item.to}>
            {item.label}
          </NavButton>
        ))}
      </div>

      <div className="flex items-center gap-3">
        <div className="hidden md:flex">
          <AuthLinks />
        </div>
        <MobileNav />
      </div>
    </nav>
  );
}
