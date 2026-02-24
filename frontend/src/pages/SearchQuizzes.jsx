import { useState } from "react";
import { Search } from "lucide-react";
import { useSearchParams } from "react-router";
import { Input } from "../components/ui/Input";
import { Button } from "../components/ui/Button";
import { Searchbar } from "../components/ui/Searchbar";
import { QuizCard } from "../components/ui/QuizCard";
import { useQuizSearch } from "../hooks/useQuizSearch";
import { useNavigate } from "react-router";

export default function SearchQuizzes() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [selectedSubject, setSelectedSubject] = useState(null);
  const { searchQuery, setSearchQuery, filteredQuizzes, isLoading } = useQuizSearch(searchParams, setSearchParams);

    const subjectFilters = [
        { label: "All", onClick: () => setSelectedSubject(null) },
        { label: "Math", onClick: () => setSelectedSubject("math") },
        { label: "Science", onClick: () => setSelectedSubject("science") },
        // add more subjects as needed
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
      />
      {searchQuery && (
        <p className="text-main font-bold mb-6">Search results for "{searchQuery}":</p>
      )}

      {isLoading ? (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-main"></div>
        </div>
      ) : (
        <>
          <div className="flex flex-col gap-4 sm:grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5">
            {filteredQuizzes.map((quiz) => (
              <QuizCard
                key={quiz.quizId}
                quiz={quiz}
                onClick={() => navigate(`/quiz-details/${quiz.quizId}`)}
              />
            ))}
          </div>
          {filteredQuizzes.length === 0 && (
            <div className="text-center py-12 text-secondary">
              No quizzes found matching "{searchQuery}"
            </div>
          )}
        </>
      )}
    </div>
  );
}
