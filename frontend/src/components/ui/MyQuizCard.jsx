import { Button } from "./Button";
import { Pencil } from "lucide-react";
import { cn } from "/src/lib/utils";
import { Badge } from "./Badge";

/**
 * @param {string} title Quiz title
 * @param {number} cardCount Number of cards
 * @param {function} onEdit Callback when edit button is clicked
 * @param {function} onClick Callback when card is clicked
 * @param {string} className Additional classes
 */
export function MyQuizCard({ title, cardCount, onEdit, onClick, className }) {
  return (
    <div
      onClick={onClick}
      className={cn(
        "flex flex-col bg-white rounded-xl border border-secondary/10 overflow-hidden cursor-pointer transition-all duration-200 hover:shadow-sm hover:-translate-y-1 min-h-[250px] min-w-[240px] group relative",
        className
      )}
    >
      <div className="p-4 flex flex-col gap-2 flex-1 justify-center">
        <h3 className="font-inter font-bold text-main text-lg leading-tight">{title}</h3>

        {cardCount !== undefined && (
          <Badge textColor="text-primary" bgColor="bg-primary/10">
            {cardCount} cards
          </Badge>
        )}
      </div>
      <div className="mt-auto">
        <hr className="border-secondary/20 mx-4" />
        <div className="px-4 py-3 flex items-center justify-end">
          <Button
            size="sm"
            variant="ghost"
            className="text-primary hover:text-primary hover:bg-primary/5 w-full justify-center"
            onClick={(e) => {
              e.stopPropagation();
              onEdit && onEdit();
            }}
          >
            <Pencil className="w-4 h-4 mr-2" />
            Edit
          </Button>
        </div>
      </div>
    </div>
  );
}
