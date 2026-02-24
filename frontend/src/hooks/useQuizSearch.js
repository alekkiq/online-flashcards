import { useEffect, useState } from "react";
import { useSearch } from "./useSearch";
import { useDebounce } from "use-debounce";

const quizFilterFn = (quiz, query) =>
    quiz.title.toLowerCase().includes(query) ||
    quiz?.author?.name.toLowerCase().includes(query);

const SAMPLE_QUIZZES = [
  {
    quizId: 1,
    title: "Asia Geography Quiz",
    description: "Asia Geography Quiz",
    subject: "Geography",
    tries: 1,
    flashcards: [],
    creator: { username: "Aleksput", role: "Teacher" },
  },
];

export function useQuizSearch(searchParams, setSearchParams) {
  const { searchQuery, setSearchQuery, filteredItems } = useSearch(
    SAMPLE_QUIZZES,
    quizFilterFn,
    searchParams,
    setSearchParams
  );

  return {
    searchQuery,
    setSearchQuery,
    filteredQuizzes: filteredItems,
    isLoading: false,
  };
}
