import { useState, useEffect, useRef, forwardRef } from "react";
import { cn } from "/src/lib/utils";
import { Button } from "./Button";
import { EllipsisVertical } from "lucide-react";

/**
 * A generic 3-dot dropdown menu.
 * @param {Array<{ label: string, onClick: () => void, icon?: ReactNode, variant?: "default" | "destructive" }>} items
 *   The list of actions to show in the dropdown.
 * @param {string} triggerLabel  Accessible aria-label for the trigger button. Defaults to "Options".
 * @param {string} className     Extra classes forwarded to the outer wrapper div.
 */
export const DropdownMenu = forwardRef(({ items = [], triggerLabel = "Options", trigger, className }, ref) => {
  const internalRef = useRef(null);
  const resolvedRef = ref || internalRef;

  const [open, setOpen] = useState(false);

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (resolvedRef.current && !resolvedRef.current.contains(e.target)) {
        setOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const itemVariants = {
    default: "text-main hover:bg-secondary/10",
    destructive: "text-destructive hover:bg-destructive/5",
  };

  const defaultTrigger = (
      <button
        className="flex h-8 w-8 items-center justify-center rounded-md text-secondary hover:bg-secondary/10 hover:text-main transition-colors cursor-pointer"
        aria-label={triggerLabel}
        aria-haspopup="true"
        aria-expanded={open}
      >
        <EllipsisVertical size={20} />
      </button>
  );

  return (
    <div className={cn("relative", className)} ref={resolvedRef}>
      <div onClick={() => setOpen((prev) => !prev)} className="h-10">
        {trigger ?? defaultTrigger}
      </div>
      {open && (
        <div className="absolute right-0 z-10 mt-1 min-w-40 rounded-lg border border-secondary/20 bg-white py-1 shadow-lg">
          {items.map((item, index) => (
            <button
              key={index}
              onClick={() => {
                item.onClick();
                setOpen(false);
              }}
              className={cn(
                "flex w-full items-center gap-2 px-3 py-2 text-sm transition-colors",
                itemVariants[item.variant ?? "default"]
              )}
            >
              {item.icon && <span className="shrink-0" aria-hidden="true">{item.icon}</span>}
              {item.label}
            </button>
          ))}
        </div>
      )}
    </div>
  );
});
DropdownMenu.displayName = "DropdownMenu";
