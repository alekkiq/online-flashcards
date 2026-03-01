import { useNavigate } from "react-router";
import { ArrowLeft } from "lucide-react";
import { cn } from "/src/lib/utils";

/**
 * A simple "← Back" link that navigates to the previous page in browser history.
 * @param {string} [label="Back"] - text shown next to the arrow
 * @param {string} [className] - extra classes
 */
export function BackLink({ label = "Back", className }) {
  const navigate = useNavigate();

  return (
    <button
      onClick={() => navigate(-1)}
      className={cn(
        "inline-flex items-center w-fit gap-1.5 py-2 mb-4 text-md text-secondary hover:text-main transition-colors cursor-pointer",
        className
      )}
    >
      <ArrowLeft size={20} />
      {label}
    </button>
  );
}

