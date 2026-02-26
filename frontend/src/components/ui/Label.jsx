import * as React from "react";
import { cn } from "/src/lib/utils";
/**
 * this is a label component that can be used to create forms
 * @param {string} className change classnames if needed like paddings etc...
 */
export const Label = React.forwardRef(({ className, ...props }, ref) => {
  return (
    <label
      ref={ref}
      className={cn(
        "text-sm font-medium text-secondary leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70",
        className
      )}
      {...props}
    />
  );
});
Label.displayName = "Label";
