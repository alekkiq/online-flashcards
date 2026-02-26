import * as React from "react";
import { cn } from "/src/lib/utils";

/**
 * Input component for forms
 * @param {string} type - the type of the input (text, email, password, etc.)
 * @param {React.ReactNode} startIcon - icon displayed on the left side of the input
 * @param {React.ReactNode} endIcon - icon displayed on the right side of the input
 * @param {boolean} hasError - when true makes the input borders red
 * @param {string} className change classnames if needed like paddings etc...
 */
export const Input = React.forwardRef(
  ({ className, type, startIcon, endIcon, hasError, ...props }, ref) => {
    return (
      <div className="relative flex w-full items-center">
        {startIcon && (
          <div className="absolute left-3 flex items-center justify-center text-secondary pointer-events-none">
            {startIcon}
          </div>
        )}
        <input
          type={type}
          className={cn(
            "flex h-10 w-full rounded-xl border border-secondary/30 bg-white py-1 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50",
            hasError && "border-destructive focus-visible:ring-destructive",
            startIcon ? "pl-10 pr-3" : "px-3",
            endIcon ? "pr-10" : "",
            className
          )}
          ref={ref}
          {...props}
        />
        {endIcon && (
          <div className="absolute right-3 flex items-center justify-center text-secondary pointer-events-none">
            {endIcon}
          </div>
        )}
      </div>
    );
  }
);
Input.displayName = "Input";
