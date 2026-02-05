import { Search } from "lucide-react";
import { useSearchParams } from "react-router";
import { Input } from "../components/ui/Input";
import { Button } from "../components/ui/Button";
import { QuizCard } from "../components/ui/QuizCard";
import { useQuizSearch } from "../hooks/useQuizSearch";
import { useNavigate } from "react-router";

export default function SearchQuizzes() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const { searchQuery, setSearchQuery, filteredQuizzes } = useQuizSearch(searchParams, setSearchParams);

  return (
    <div className="py-8">
      <div className="mb-8">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">Search Quizzes</h1>
        <p className="text-secondary">Search for community made Quizzes.</p>
      </div>
      <div className="flex flex-col sm:flex-row gap-3 mb-8">
        <div className="flex-1">
          <Input
            type="text"
            placeholder="Search for quizzes...."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            startIcon={<Search size={18} />}
          />
        </div>
        <Button variant="default" className="px-6">
          Category
        </Button>
      </div>
      {searchQuery && (
        <p className="text-main font-bold mb-6">Search results for "{searchQuery}":</p>
      )}
      <div className="flex flex-col gap-4 sm:grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5">
        {filteredQuizzes.map((quiz) => (
          <QuizCard
            key={quiz.id}
            title={quiz.title}
            cardCount={quiz.cardCount}
            thumbnailUrl={quiz.thumbnailUrl}
            author={quiz.author}
            authorRole={quiz.authorRole}
            onClick={() => navigate(`/quiz-details/${quiz.id}`)}
          />
        ))}
      </div>
      {filteredQuizzes.length === 0 && (
        <div className="text-center py-12 text-secondary">
          No quizzes found matching "{searchQuery}"
        </div>
      )}
    </div>
  );
}
