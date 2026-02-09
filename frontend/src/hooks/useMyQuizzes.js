import { useEffect, useState } from "react";

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

  useEffect(() => {
    const fetchQuizzes = async () => {
      setIsLoading(true);
      try {
        // const data = await getMyQuizzes();
        // setQuizzes(data);
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
    console.log("Creating quiz:", quizData);
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ success: true, id: Math.floor(Math.random() * 1000) });
      }, 1000);
    });
  };

  const handleUpdateQuiz = async (id, quizData) => {
    console.log("Updating quiz:", id, quizData);
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ success: true });
      }, 1000);
    });
  };

  return {
    quizzes,
    isLoading,
    error,
    handleCreateQuiz,
    handleUpdateQuiz,
  };
}
