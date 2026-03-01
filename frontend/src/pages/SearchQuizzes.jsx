import { useState } from "react";
import { useSearchParams, useNavigate } from "react-router";
import { Searchbar } from "../components/ui/Searchbar";
import { QuizCard } from "../components/ui/QuizCard";
import { useQuizSearch } from "../hooks/useQuizSearch";
import { useSubjects } from "../hooks/useSubjects";

export default function SearchQuizzes() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [selectedSubject, setSelectedSubject] = useState(null);
  const { subjects } = useSubjects();
  const { searchQuery, setSearchQuery, filteredQuizzes } = useQuizSearch(searchParams, setSearchParams, selectedSubject);

  const subjectFilters = [
    { label: "All", onClick: () => setSelectedSubject(null), active: !selectedSubject },
    ...subjects.map((subject) => ({
      label: subject.name,
      onClick: () => setSelectedSubject(subject.name),
      active: selectedSubject === subject.name,
    })),
  ];

  return (
    <div className="max-w-7xl mx-auto py-8">
      <div className="mb-8">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">Search Quizzes</h1>
        <p className="text-secondary">Search for community made Quizzes.</p>
      </div>
      <Searchbar
        value={searchQuery}
        onChange={setSearchQuery}
        placeholder="Search for quizzes..."
        filters={subjectFilters}
        filterTriggerLabel="Subject"
        activeFilterLabel={selectedSubject}
      />
      {searchQuery && (
        <p className="text-main font-bold mb-6">Search results for "{searchQuery}":</p>
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
