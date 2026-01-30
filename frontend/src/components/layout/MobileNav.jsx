import { useState } from "react";
import { Link } from "react-router";
import { X, Menu } from "lucide-react";
import { navLinks } from "/src/config";
import { NavButton } from "/src/components/ui/NavButton";

export default function MobileNav() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <button
        onClick={() => setIsOpen(true)}
        aria-label="Open mobile menu"
        className="md:hidden p-2 text-secondary"
      >
        <Menu size={28} />
      </button>
      <div
        className={`fixed inset-0 z-[100] bg-black/30 transition-opacity duration-300 ${
          isOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"
        }`}
        onClick={() => setIsOpen(false)}
      />
      <div
        className={`fixed top-0 left-0 h-full w-full max-w-xs bg-white shadow-lg z-[101] p-6 transition-transform duration-300 ${
          isOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <button
          onClick={() => setIsOpen(false)}
          aria-label="Close mobile drawer"
          className="absolute top-5 right-5 p-2 text-secondary"
        >
          <X size={28} />
        </button>
        <div className="mb-8">
          <Link to="/" onClick={() => setIsOpen(false)}>
            <p className="text-2xl font-bold">Online Flashcards</p>
          </Link>
        </div>
        <ul className="flex flex-col gap-4">
          {navLinks.map((item) => (
            <li
              key={item.to}
              onClick={() => setIsOpen(false)}
              className="[&>a]:w-full [&>a]:block [&>a]:text-center"
            >
              <NavButton to={item.to}>{item.label}</NavButton>
            </li>
          ))}
        </ul>
      </div>
    </>
  );
}
