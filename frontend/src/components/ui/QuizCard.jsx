import { cn } from "/src/lib/utils";
import { Badge } from "./Badge";
import { Avatar } from "./Avatar";

/**
 * quizcard component
 * @param {object} quiz quiz object containing relevant quiz data
 * @param {function} onClick click handler
 * @param {string} className any extra classes
 */
export function QuizCard({ quiz, onClick, className = "" }) {
  const cardCount = quiz?.cardCount || quiz.flashcards?.length || 0;
  const creatorRole = quiz?.creatorRole ? String(quiz.creatorRole) : "STUDENT";

  return (
    <div
      onClick={onClick}
      className={cn(
        "flex flex-col bg-white rounded-xl border border-secondary/10 overflow-hidden cursor-pointer transition-all duration-200 hover:shadow-sm hover:-translate-y-1 min-h-[250px] min-w-[240px]",
        className
      )}
    >
      <div className="p-4 flex flex-col gap-2 flex-1 justify-center">
        <h3 className="font-inter font-bold text-main text-lg leading-tight">{quiz.title}</h3>

        {quiz.flashcards?.length !== undefined && (
          <Badge textColor="text-primary" bgColor="bg-primary/10">
            {cardCount} cards
          </Badge>
        )}
      </div>
      <div className="mt-auto">
        <hr className="border-secondary/20 mx-4" />
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
      </div>
    </div>
  );
}
