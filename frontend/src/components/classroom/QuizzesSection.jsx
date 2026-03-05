import { BookOpen } from "lucide-react";
import { Button } from "/src/components/ui/Button";
import { QuizCard } from "/src/components/ui/QuizCard";

/**
 * Quizzes section for the classroom view.
 * @param {object} props
 * @param {Array} props.quizzes - array of quiz summary objects
 * @param {boolean} props.isOwner - whether the current user is the owner
 * @param {function} props.onAddQuiz - callback when "+ Add Quiz" is clicked
 * @param {function} props.onQuizClick - callback when a quiz card is clicked, receives quizId
 */
export default function QuizzesSection({ quizzes = [], isOwner, onAddQuiz, onQuizClick }) {
  return (
    <div className="mt-6 rounded-xl bg-white p-5 md:p-8">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <BookOpen size={20} className="text-secondary" />
          <h2 className="font-semibold text-main">Quizzes</h2>
          <span className="text-sm text-secondary">({quizzes.length})</span>
        </div>
        {isOwner && (
          <Button size="sm" onClick={onAddQuiz} className="hidden sm:inline-flex">
            + Add Quiz
          </Button>
        )}
      </div>
      {quizzes.length > 0 ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          {quizzes.map((quiz) => (
            <QuizCard key={quiz.quizId} quiz={quiz} onClick={() => onQuizClick(quiz.quizId)} />
          ))}
        </div>
      ) : (
        <p className="text-sm text-secondary">No quizzes added yet.</p>
      )}
      {isOwner && (
        <Button size="sm" onClick={onAddQuiz} className="inline-flex sm:hidden mt-4">
          + Add Quiz
        </Button>
      )}
    </div>
  );
}
