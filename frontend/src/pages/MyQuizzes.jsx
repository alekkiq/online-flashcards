import { useNavigate } from "react-router";
import { QuizCard } from "../components/ui/QuizCard";
import { NewItemCard } from "../components/ui/NewItemCard";
import { useMyQuizzes } from "../hooks/useMyQuizzes";
import { PageLoader } from "../components/ui/PageLoader";
import { useEffect, useCallback } from "react";
import { useTranslation } from "react-i18next";

export default function MyQuizzes() {
  const navigate = useNavigate();
  const { quizzes, isLoading, fetchUserQuizzes } = useMyQuizzes();
  const { t } = useTranslation();

  useEffect(() => {
    fetchUserQuizzes();
  }, []);

  if (isLoading) {
    return <PageLoader />;
  }

  return (
    <div className="max-w-7xl mx-auto py-8">
      <div className="my-8 md:px-0">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">{t("myQuizzes.title")}</h1>
        <p className="text-secondary text-lg">{t("myQuizzes.subtitle")}</p>
      </div>

      <div className="flex flex-col gap-4 sm:grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 md:px-0">
        <NewItemCard
          onClick={() => navigate("/my-quizzes/create")}
          title={t("myQuizzes.newQuiz")}
          subtitle={t("myQuizzes.createNewQuiz")}
        />

        {quizzes.map((quiz) => (
          <QuizCard
            key={quiz.quizId}
            quiz={quiz}
            showLanguage={true}
            onClick={() => navigate(`/quiz-details/${quiz.quizId}`)}
            onEdit={() => navigate(`/my-quizzes/details/${quiz.quizId}`)}
          />
        ))}
      </div>
    </div>
  );
}
