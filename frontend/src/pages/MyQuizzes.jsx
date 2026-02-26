import { useNavigate } from "react-router";
import { MyQuizCard } from "../components/ui/MyQuizCard";
import { NewQuizCard } from "../components/ui/NewQuizCard";
import { useMyQuizzes } from "../hooks/useMyQuizzes";
import { PageLoader } from "../components/ui/PageLoader";
import { useEffect, useCallback } from "react";

export default function MyQuizzes() {
  const navigate = useNavigate();
  const { quizzes, isLoading, fetchUserQuizzes } = useMyQuizzes();

  useEffect(() => {
    fetchUserQuizzes();
  }, []);

  if (isLoading) {
    return <PageLoader />;
  }

  return (
    <div className="max-w-7xl mx-auto py-8">
      <div className="mb-8 px-4 md:px-0">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">My Quizzes</h1>
        <p className="text-secondary text-lg">View, edit and create quizzes</p>
      </div>
      
      <div className="flex flex-col gap-4 sm:grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 px-4 md:px-0">
        <NewQuizCard onClick={() => navigate("/my-quizzes/create")} />
        
        {quizzes.map((quiz) => (
          <MyQuizCard
            key={quiz.quizId}
            title={quiz.title}
            cardCount={quiz.cardCount}
            onClick={() => navigate(`/quiz-details/${quiz.quizId}`)}
            onEdit={() => navigate(`/my-quizzes/details/${quiz.quizId}`)}
          />
        ))}
      </div>
    </div>
  );
}
