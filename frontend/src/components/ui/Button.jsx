import * as React from "react";
import { cn } from "/src/lib/utils";
import { Spinner } from "./Spinner";

/**
 * this button covers the (Blue) default, outline (Gray), link (blue) and red (destructive) from figma.
 * @param {string} variant default is default "blue", other variants are "destructive", "outline", "secondary", "ghost", "link"
 * @param {string} size default is default "default", other sizes are "sm", "lg", "icon", "link", change directly from className if needed
 * @param {boolean} loading loading makes the button disabled and shows a spinner
 * @param {string} loadingText display this text when the button is loading
 * @param {string} className change classnames if needed like paddings etc...
 */

export const Button = React.forwardRef(
  (
    {
      className,
      variant = "default",
      size = "default",
      loading = false,
      loadingText,
      children,
      ...props
    },
    ref
  ) => {
    const baseStyles =
      "w-full sm:w-auto inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-lg text-sm font-semibold transition-colors focus-visible:outline-none focus-visible:ring-1 cursor-pointer focus-visible:ring-primary disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0";

    const variants = {
      default:
        "bg-primary text-white  hover:bg-primary/90 active:bg-primary/80 active:scale-[0.98]",
      destructive:
        "bg-destructive text-white hover:bg-destructive/90 active:bg-destructive/80 active:scale-[0.98]",
      outline:
        "border-2 border-secondary bg-transparent text-secondary hover:bg-secondary/5 hover:text-main active:bg-secondary/20 active:scale-[0.98]",
      secondary:
        "bg-secondary text-white hover:bg-secondary/80 active:bg-secondary/70 active:scale-[0.98]",
      ghost: "hover:bg-secondary/10 hover:text-main active:bg-secondary/20 active:scale-[0.98]",
      link: "text-primary underline-offset-4 hover:underline active:text-primary/80",
      success: "bg-green-500 text-white hover:bg-green-600 active:bg-green-700 active:scale-[0.98]",
    };

    const sizes = {
      default: "h-9 px-4 py-2",
      sm: "h-8 px-3 text-xs",
      md: "h-8 px-4 text-sm",
      lg: "h-10 px-8 text-md",
      icon: "h-9 w-9",
      link: "px-0 py-0",
    };

    const spinnerColors = {
      default: { spin: "#ffffff", ring: "rgba(255,255,255,0.3)" },
      destructive: { spin: "#ffffff", ring: "rgba(255,255,255,0.3)" },
      outline: { spin: "#1A1918", ring: "rgba(0,0,0,0.1)" },
      secondary: { spin: "#ffffff", ring: "rgba(255,255,255,0.3)" },
      ghost: { spin: "#1A1918", ring: "rgba(0,0,0,0.1)" },
      link: { spin: "#5700FE", ring: "rgba(87,0,254,0.2)" },
      success: { spin: "#ffffff", ring: "rgba(255,255,255,0.3)" },
    };

    const compiledClassName = cn(baseStyles, variants[variant], sizes[size], className);

    return (
      <button
        ref={ref}
        className={compiledClassName}
        {...props}
        disabled={loading || props.disabled}
      >
        {loading ? (
          <>
            <Spinner
              spinColor={spinnerColors[variant].spin}
              ringColor={spinnerColors[variant].ring}
              size={16}
            />
            {loadingText}
          </>
        ) : (
          children
        )}
      </button>
    );
  }
);
Button.displayName = "Button";
