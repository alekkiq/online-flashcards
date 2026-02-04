import { cn } from "/src/lib/utils";
import { Badge } from "./Badge";
import { Avatar } from "./Avatar";

/**
 * quizcard component
 * @param {string} title title
 * @param {number} cardCount count of cards
 * @param {object} author userfield
 * @param {string} authorRole role of the user
 * @param {function} onClick click handler
 * @param {string} className any extra classes
 */
export function QuizCard({ title, cardCount, author, authorRole, onClick, className }) {
  return (
    <div
      onClick={onClick}
      className={cn(
        "flex flex-col bg-white rounded-xl border border-secondary/10 overflow-hidden cursor-pointer transition-all duration-200 hover:shadow-sm hover:-translate-y-1 min-h-[250px] min-w-[240px]",
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
        <div className="px-4 py-3 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Avatar name={author?.name} />
            <span className="text-sm text-secondary">{author?.name}</span>
          </div>

          {authorRole && (
            <Badge textColor="text-white" bgColor="bg-primary">
              {authorRole}
            </Badge>
          )}
        </div>
      </div>
    </div>
  );
}
