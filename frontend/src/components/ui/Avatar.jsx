import { cn } from "/src/lib/utils";

/**
 * avatar component shows user initials
 * @param {string} name user name
 * @param {string} size size (e.g., 'w-6 h-6', 'w-8 h-8')
 * @param {string} bgColor background color
 * @param {string} textColor text color
 * @param {string} className additional custom classes
 */
export function Avatar({
  name,
  size = "w-6 h-6",
  bgColor = "bg-gray-300",
  textColor = "text-gray-500",
  textSize = "text-xs",
  className,
}) {
  const initial = name?.charAt(0)?.toUpperCase() || "?";

  return (
    <div
      className={cn(
        "rounded-full flex items-center justify-center",
        size,
        bgColor,
        textColor,
        textSize,
        className
      )}
    >
      {initial}
    </div>
  );
}
