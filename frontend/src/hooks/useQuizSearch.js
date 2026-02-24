import { useEffect, useState } from "react";
import { useSearch } from "./useSearch";
import { useDebounce } from "use-debounce";
import { searchQuizzes } from "/src/api"

const quizFilterFn = (quiz, query) =>
    quiz.title.toLowerCase().includes(query) ||
    quiz?.author?.name.toLowerCase().includes(query);

export function useQuizSearch(searchParams, setSearchParams) {
  const [items, setItems] = useState([]);
  const { searchQuery, setSearchQuery, filteredItems } = useSearch(
    items,
    quizFilterFn,
    searchParams,
    setSearchParams
  );

  const fetchQuizzesByQuery = async () => {
      const response = await searchQuizzes();
      if (!response.success) {
        return [];
      }
      console.log(response.data.data);
      return response.data.data;
  }

  useEffect(() => {
    const fetchQuizzes = async () => {
      const quizzes = await fetchQuizzesByQuery();
      setItems(quizzes);
    };
    fetchQuizzes();
  }, []);

  return {
    searchQuery,
    setSearchQuery,
    filteredQuizzes: filteredItems,
    isLoading: false,
  };
}
