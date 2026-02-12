import { useEffect, useState } from "react";
import { useDebounce } from "use-debounce";

const SAMPLE_QUIZZES = [
  {
    id: 1,
    title: "Asia Geography Quiz",
    description: "Asia Geography Quiz",
    tries: 1,
    cardCount: 32,
    author: { name: "Aleksput" },
    authorRole: "Teacher",
  },
  {
    id: 2,
    title: "Albania Quiz",
    description: "Albania Quiz",
    tries: 1,
    cardCount: 5,
    author: { name: "BlendG" },
    authorRole: "Student",
  },
  {
    id: 3,
    title: "Live Like Teemu",
    description: "Live Like Teemu",
    tries: 1,
    cardCount: 392,
    author: { name: "TeemuLaasio" },
    authorRole: "User",
  },
  {
    id: 4,
    title: "Bugs 101",
    description: "Bugs 101",
    tries: 1,
    cardCount: 16,
    author: { name: "AlabbasA" },
    authorRole: "Bug",
  },
  {
    id: 5,
    title: "Finnish Slang",
    description: "Finnish Slang",
    tries: 1,
    cardCount: 16,
    author: { name: "BlendG" },
    authorRole: "Student",
  },
];

export function useQuizSearch(searchParams, setSearchParams) {
  const searchQuery = searchParams.get("q") || "";
  const [quizzes, setQuizzes] = useState(SAMPLE_QUIZZES);
  const [isLoading, setIsLoading] = useState(false);

  const setSearchQuery = (query) => {
    setSearchParams((prev) => {
      const newParams = new URLSearchParams(prev);
      if (query) {
        newParams.set("q", query);
      } else {
        newParams.delete("q");
      }
      return newParams;
    });
  };

  useEffect(() => {
    const query = searchQuery.toLowerCase();
    setQuizzes(
      SAMPLE_QUIZZES.filter(
        (quiz) =>
          quiz.title.toLowerCase().includes(query) ||
            quiz.author.name.toLowerCase().includes(query)
        )
      );
  }, [searchQuery]);


  return {
    searchQuery,
    setSearchQuery,
    filteredQuizzes: quizzes,
    isLoading,
  };
}
