import { Input } from "./Input";
import { Label } from "./Label";
import { Badge } from "./Badge";

/**
 * FlashcardInput component for a single card in the quiz creation form
 * @param {number} index Index of the card (0-based)
 * @param {object} register React Hook Form register function
 * @param {object} errors React Hook Form errors object
 * @param {function} onRemove Callback to remove this card
 */
export function FlashcardInput({ index, register, errors, onRemove }) {
  const cardNumber = index + 1;

  const questionError = errors?.cards?.[index]?.question?.message;
  const answerError = errors?.cards?.[index]?.answer?.message;

  return (
    <div className="flex flex-col gap-4 py-6 border-b border-gray-100 last:border-0">
      <div className="flex items-center gap-3">
        <div className="flex items-center justify-center w-8 h-8 rounded-full bg-purple-100 text-purple-600 font-bold text-sm">
          {cardNumber}
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 ml-11">
        <div className="flex flex-col gap-2">
          <Label htmlFor={`card-${index}-question`} className="text-xs font-bold uppercase tracking-wider text-main/80">
            Front
          </Label>
          <textarea
            id={`card-${index}-question`}
            {...register(`cards.${index}.question`)}
            className={`flex min-h-[80px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none ${
              questionError ? "border-destructive focus-visible:ring-destructive" : ""
            }`}
            placeholder="Type the question..."
          />
          {questionError && <p className="text-xs font-medium text-destructive">{questionError}</p>}
        </div>

        <div className="flex flex-col gap-2">
          <Label htmlFor={`card-${index}-answer`} className="text-xs font-bold uppercase tracking-wider text-main/80">
            Back
          </Label>
          <textarea
            id={`card-${index}-answer`}
            {...register(`cards.${index}.answer`)}
            className={`flex min-h-[80px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none ${
              answerError ? "border-destructive focus-visible:ring-destructive" : ""
            }`}
            placeholder="Type the answer..."
          />
          {answerError && <p className="text-xs font-medium text-destructive">{answerError}</p>}
        </div>
      </div>
    </div>
  );
}
