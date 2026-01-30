import { Link } from "react-router";
import { navLinks } from "/src/config";
import { NavButton } from "/src/components/ui/NavButton";
import MobileNav from "./MobileNav";

export default function NavBar() {
  return (
    <nav className="relative flex items-center justify-between p-4 w-full border-b border-secondary/30 bg-white py-5">
      {/* Logo */}
      <div>
        <Link to="/">
          <p className="text-xl md:text-2xl font-bold">Online Flashcards</p>
        </Link>
      </div>
      
      {/* Desktop Nav - hidden on mobile */}
      <div className="hidden md:flex absolute left-1/2 -translate-x-1/2 flex-row gap-2">
        {navLinks.map((item) => (
          <NavButton key={item.to} to={item.to}>
            {item.label}
          </NavButton>
        ))}
      </div>
      
      {/* Mobile Nav */}
      <MobileNav />
    </nav>
  );
}
