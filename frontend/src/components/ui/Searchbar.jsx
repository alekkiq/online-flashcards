import { forwardRef } from "react";
import { Search, SlidersHorizontal } from "lucide-react";
import { Input } from "./Input";
import { Button } from "./Button";
import { DropdownMenu } from "./DropdownMenu";

export const Searchbar = forwardRef(({ value, onChange, placeholder = "Search", filters, children }, ref) => {
    return (
        <div className="flex flex-col sm:flex-row gap-3 mb-8">
            <div className="flex-1">
                <Input
                    ref={ref}
                    type="text"
                    placeholder="Search for quizzes...."
                    value={value}
                    onChange={(e) => onChange(e.target.value)}
                    startIcon={<Search size={18} />}
                />
            </div>
            {filters && filters.length > 0 && (
                <DropdownMenu
                    items={filters}
                    triggerLabel="Filter"
                    trigger={
                        <Button className="h-full">
                            <SlidersHorizontal size={16} />
                            <span className="text-sm">Filter</span>
                        </Button>
                    }
                />
            )}
            {children}
        </div>
    )
});
Searchbar.displayName = "Searchbar";