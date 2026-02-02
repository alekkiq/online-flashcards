import { cn } from "/src/lib/utils";

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
    className 
}) {
    return (
        <span 
            className={cn(
                "inline-flex w-fit items-center px-2 py-0.5 rounded-full text-xs font-medium",
                textColor,
                bgColor,
                className
            )}
        >
            {children}
        </span>
    );
}
