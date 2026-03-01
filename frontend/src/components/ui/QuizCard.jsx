import { cn } from "/src/lib/utils";
import { Badge } from "./Badge";
import { Avatar } from "./Avatar";
import { Button } from "./Button";
import { Pencil, BookCopy } from "lucide-react";

/**
 * Unified quiz card component.
 * Shows an edit button in the footer when `onEdit` is provided,
 * otherwise shows creator info with avatar and role badge.
 *
 * @param {object} quiz - quiz object containing title, cardCount, creatorUsername, creatorRole, etc.
 * @param {function} onClick - click handler for the card
 * @param {function} [onEdit] - when provided, renders an edit button in the footer instead of creator info
 * @param {string} [className] - any extra classes
 */
export function QuizCard({ quiz, onClick, onEdit, className = "" }) {
  const cardCount = quiz?.cardCount;
  const creatorRole = quiz?.creatorRole ? String(quiz.creatorRole) : "STUDENT";

  return (
    <div
      onClick={onClick}
      className={cn(
        "flex flex-col bg-white rounded-xl border border-secondary/10 overflow-hidden cursor-pointer transition-all duration-200 hover:shadow-sm hover:-translate-y-1 min-h-[250px] min-w-[240px] group relative",
        className
      )}
    >
      <div className="p-4 flex flex-col gap-2 flex-1 justify-center">
        <h3 className="font-inter font-bold text-main text-lg sm:text-2xl leading-tight">{quiz.title}</h3>

        {quiz.description && (
            <p className="text-sm text-secondary line-clamp-3 mb-2">{quiz.description}</p>
        )}

        <div className="flex items-center gap-2 flex-wrap">
          {quiz.subjectName && (
              <Badge textColor="text-primary" bgColor="bg-primary/10">
                {quiz.subjectName}
              </Badge>
          )}
          {cardCount !== undefined && (
            <Badge textColor="text-primary" bgColor="bg-primary/10">
              <BookCopy size={12} className="inline mr-1" />
              {cardCount} cards
            </Badge>
          )}
        </div>
      </div>
      <div className="mt-auto">
        <hr className="border-secondary/20 mx-4" />
        {onEdit ? (
          <div className="px-4 py-3 flex items-center justify-end">
            <Button
              size="sm"
              variant="ghost"
              className="text-primary hover:text-primary hover:bg-primary/5 w-full justify-center"
              onClick={(e) => {
                e.stopPropagation();
                onEdit();
              }}
            >
              <Pencil className="w-4 h-4 mr-2" />
              Edit
            </Button>
          </div>
        ) : (
          <div className="px-4 py-3 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <Avatar name={quiz?.creatorUsername} />
              <span className="text-sm text-secondary">{quiz?.creatorUsername}</span>
            </div>
            {creatorRole && (
              <Badge textColor="text-white" bgColor="bg-primary">
                {creatorRole}
              </Badge>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
