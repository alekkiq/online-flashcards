import { useState } from "react";
import { useSearchParams, useNavigate } from "react-router";
import { Searchbar } from "../components/ui/Searchbar";
import { QuizCard } from "../components/ui/QuizCard";
import { useQuizSearch } from "../hooks/useQuizSearch";
import { useSubjects } from "../hooks/useSubjects";
import { useTranslation } from "react-i18next";

export default function SearchQuizzes() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [selectedSubject, setSelectedSubject] = useState(null);
  const { subjects } = useSubjects();
  const { t } = useTranslation();
  const { searchQuery, setSearchQuery, filteredQuizzes } = useQuizSearch(
    searchParams,
    setSearchParams,
    selectedSubject
  );

  const subjectFilters = [
    { label: t("searchQuizzes.all"), onClick: () => setSelectedSubject(null), active: !selectedSubject },
    ...subjects.map((subject) => ({
      label: subject.name,
      onClick: () => setSelectedSubject(subject.name),
      active: selectedSubject === subject.name,
    })),
  ];

  return (
    <div className="max-w-7xl mx-auto py-8">
      <div className="my-8">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">{t("searchQuizzes.title")}</h1>
        <p className="text-secondary">{t("searchQuizzes.subtitle")}</p>
      </div>
      <Searchbar
        value={searchQuery}
        onChange={setSearchQuery}
        placeholder={t("searchQuizzes.placeholder")}
        filters={subjectFilters}
        filterTriggerLabel={t("searchQuizzes.subject")}
        activeFilterLabel={selectedSubject}
      />
      {searchQuery && (
        <p className="text-main font-bold mb-6">{t("searchQuizzes.searchResultsFor", { query: searchQuery })}</p>
      )}
      {searchQuery && filteredQuizzes.length === 0 && (
        <p className="text-secondary mt-4">{t("searchQuizzes.noQuizzesFound", { query: searchQuery })}</p>
      )}
      <div className="flex flex-col gap-4 sm:grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5">
        {filteredQuizzes.map((quiz) => (
          <QuizCard
            key={quiz.quizId}
            quiz={quiz}
            onClick={() => navigate(`/quiz-details/${quiz.quizId}`)}
          />
        ))}
      </div>
    </div>
  );
}
