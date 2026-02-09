import { useEffect, useState } from "react";
import { useAuth } from "/src/hooks/useAuth";

const SAMPLE_MY_QUIZZES = [
  {
    id: 101,
    title: "Perse",
    description: "Persettä",
    cardCount: 12,
  },
  {
    id: 102,
    title: "Ppppu",
    description: "Persettä",
    cardCount: 5,
  },
];

export function useMyQuizzes() {
  const [quizzes, setQuizzes] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useAuth();

  useEffect(() => {
    const fetchQuizzes = async () => {
      setIsLoading(true);
      try {
        setTimeout(() => {
          setQuizzes(SAMPLE_MY_QUIZZES);
          setIsLoading(false);
        }, 500);
      } catch (err) {
        setError(err);
        setIsLoading(false);
      }
    };

    fetchQuizzes();
  }, []);

  const handleCreateQuiz = async (quizData) => {
    setIsLoading(true);
    const data = {
      title: quizData.title,
      description: quizData.description,
      flashcards: quizData.cards,
      userId: user.id,
    };

    // TODO: Implement quiz creation
  };

  const handleUpdateQuiz = async (id, quizData) => {
    setIsLoading(true);
    const data = {
      title: quizData.title,
      description: quizData.description,
      flashcards: quizData.cards,
      userId: user.id,
    };

    // TODO: Implement quiz update
  };

  return {
    quizzes,
    isLoading,
    error,
    handleCreateQuiz,
    handleUpdateQuiz,
  };
}
