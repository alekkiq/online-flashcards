import { Plus } from "lucide-react";
import { cn } from "/src/lib/utils";

/**
 * Card for creating a new quiz
 * @param {function} onClick click handler
 * @param {string} className custom classes
 */
export function NewQuizCard({ onClick, className }) {
  return (
    <div
      onClick={onClick}
      className={cn(
        "flex flex-col items-center justify-center bg-gray-100/50 rounded-xl border-2 border-dashed border-gray-300 overflow-hidden cursor-pointer transition-all duration-200 hover:bg-gray-100 hover:border-gray-400 min-h-[250px] min-w-[240px] gap-4 group",
        className
      )}
    >
      <Plus className="w-12 h-12 text-gray-400 group-hover:text-gray-600 transition-colors" />
      <div className="text-center">
        <h3 className="font-inter font-bold text-lg text-main">New Quiz</h3>
        <p className="text-sm text-secondary">Create a new quiz</p>
      </div>
    </div>
  );
}
