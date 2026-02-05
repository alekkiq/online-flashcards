import * as React from "react";
import { Label } from "./Label";
import { cn } from "/src/lib/utils";

/**
 * this is a form field component that can be used to create forms
 * @param {string} label the label of the form field
 * @param {string} error the error message of the form field
 * @param {string} className change classnames if needed like paddings etc...
 */
export const FormField = React.forwardRef(
  ({ label, error, className, children, ...props }, ref) => {
    const id = React.useId();

    const childrenWithId = React.isValidElement(children)
      ? React.cloneElement(children, { id })
      : children;

    return (
      <div ref={ref} className={cn("space-y-2", className)} {...props}>
        {label && <Label htmlFor={id}>{label}</Label>}
        {childrenWithId}
        {error && <p className="text-sm font-medium text-destructive">{error}</p>}
      </div>
    );
  }
);
FormField.displayName = "FormField";
