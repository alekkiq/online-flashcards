import { useEffect, useState, useMemo } from "react";
import { useSearch } from "./useSearch";
import { searchQuizzes } from "/src/api";
import {useTranslation} from "react-i18next";

const quizFilterFn = (quiz, query) =>
  quiz.title.toLowerCase().includes(query) || quiz?.creatorUsername?.toLowerCase().includes(query);

export function useQuizSearch(searchParams, setSearchParams, selectedSubject) {
  const { i18n } = useTranslation();

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
    return response.data.data;
  };

  useEffect(() => {
    let cancelled = false;

    const fetchQuizzes = async () => {
      const quizzes = await fetchQuizzesByQuery();
      if (!cancelled) {
        setItems(quizzes);
      }
    };

    fetchQuizzes();

    return () => {
      cancelled = true;
    };
  }, [i18n.resolvedLanguage]);

  const filteredQuizzes = useMemo(
    () =>
      selectedSubject
        ? filteredItems.filter((quiz) => quiz.subjectName === selectedSubject)
        : filteredItems,
    [filteredItems, selectedSubject]
  );

  return {
    searchQuery,
    setSearchQuery,
    filteredQuizzes,
    isLoading: false,
  };
}
