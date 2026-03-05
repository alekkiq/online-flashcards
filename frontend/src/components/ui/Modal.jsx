import * as React from "react";
import { X } from "lucide-react";
import { cn } from "/src/lib/utils";

/**
 * Reusable modal/dialog component.
 * @param {boolean} open - whether the modal is visible
 * @param {function} onClose - callback to close the modal
 * @param {string} [title] - optional title displayed at the top
 * @param {string} [className] - extra classes for the content panel
 * @param {React.ReactNode} children - modal body content
 */
export function Modal({ open, onClose, title, className, children }) {
  React.useEffect(() => {
    if (!open) return;
    const handleEsc = (e) => {
      if (e.key === "Escape") onClose();
    };
    document.addEventListener("keydown", handleEsc);
    return () => document.removeEventListener("keydown", handleEsc);
  }, [open, onClose]);

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="fixed inset-0 bg-black/40 transition-opacity" onClick={onClose} />
      <div
        className={cn(
          "relative z-50 w-full max-w-md rounded-2xl bg-white p-6 shadow-xl",
          className
        )}
      >
        {(title || onClose) && (
          <div className="flex items-center justify-between mb-4">
            {title && (
              <h2 className="font-serif text-xl md:text-2xl font-bold text-main">{title}</h2>
            )}
            <button
              onClick={onClose}
              className="ml-auto p-1 rounded-lg text-secondary hover:bg-secondary/10 hover:text-main transition-colors"
              aria-label="Close"
            >
              <X size={20} />
            </button>
          </div>
        )}
        {children}
      </div>
    </div>
  );
}
