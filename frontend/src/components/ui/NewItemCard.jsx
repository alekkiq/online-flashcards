import { Plus } from "lucide-react";
import { cn } from "/src/lib/utils";

/**
 * Generalized "create new" card.
 * @param {function} onClick - click handler
 * @param {string} [title="New Item"] - heading text
 * @param {string} [subtitle="Create a new item"] - description text
 * @param {React.ReactNode} [icon] - custom icon (defaults to Plus)
 * @param {string} [className] - extra classes
 */
export function NewItemCard({
  onClick,
  title = "New Item",
  subtitle = "Create a new item",
  icon,
  className,
}) {
  return (
    <div
      onClick={onClick}
      className={cn(
        "flex flex-col items-center justify-center bg-gray-100/50 rounded-xl border-2 border-dashed border-gray-300 overflow-hidden cursor-pointer transition-all duration-200 hover:bg-gray-100 hover:border-gray-400 min-h-[174px] md:min-h-[250px] min-w-[240px] gap-4 group",
        className
      )}
    >
      {icon || (
        <Plus className="w-12 h-12 text-gray-400 group-hover:text-gray-600 transition-colors" />
      )}
      <div className="text-center">
        <h3 className="font-inter font-bold text-lg text-main">{title}</h3>
        <p className="text-sm text-secondary">{subtitle}</p>
      </div>
    </div>
  );
}
