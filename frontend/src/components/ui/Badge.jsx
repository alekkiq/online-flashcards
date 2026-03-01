import { cn } from "/src/lib/utils";

const sizes = {
  sm: "px-2 py-0.5 text-xs",
  md: "px-3 py-1 text-sm",
  lg: "px-4 py-2 text-base",
  xl: "px-5 py-3 text-lg",
};

/**
 * badge component the small blue blob
 * @param {string} textColor text color class (e.g., 'text-primary', 'text-white')
 * @param {string} bgColor background color class (e.g., 'bg-primary/10', 'bg-primary')
 * @param {string} className additional custom classes
 */
export function Badge({
  children,
  textColor = "text-primary",
  bgColor = "bg-primary/10",
  size = "sm",
  className,
}) {
  return (
    <span
      className={cn(
        "inline-flex w-fit items-center rounded-full font-medium",
        sizes[size] || sizes["sm"],
        textColor,
        bgColor,
        className
      )}
    >
      {children}
    </span>
  );
}
