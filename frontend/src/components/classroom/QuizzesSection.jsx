import { BookOpen } from "lucide-react";
import { Button } from "/src/components/ui/Button";
import { QuizCard } from "/src/components/ui/QuizCard";
import { useTranslation } from "react-i18next";

export default function QuizzesSection({ quizzes = [], isOwner, onAddQuiz, onQuizClick }) {
  const { t } = useTranslation();

  return (
    <div className="mt-6 rounded-xl bg-white p-5 md:p-8">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <BookOpen size={20} className="text-secondary" />
          <h2 className="font-semibold text-main">{t("quizzesSection.title")}</h2>
          <span className="text-sm text-secondary">({quizzes.length})</span>
        </div>
        {isOwner && (
          <Button size="sm" onClick={onAddQuiz} className="hidden sm:inline-flex">
            {t("quizzesSection.addQuiz")}
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
        <p className="text-sm text-secondary">{t("quizzesSection.noQuizzes")}</p>
      )}
      {isOwner && (
        <Button size="sm" onClick={onAddQuiz} className="inline-flex sm:hidden mt-4">
          {t("quizzesSection.addQuiz")}
        </Button>
      )}
    </div>
  );
}
