import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import { useQuizContext } from "../hooks/useQuizContext";
import FlipCard from "../components/Quiz/FlipCard";
import { Button } from "../components/ui/Button";
import { ArrowLeft, ArrowRight } from "lucide-react";
import { PageLoader } from "/src/components/ui/PageLoader";

export default function QuizGame() {
  const {
    currentQuiz,
    fetchQuiz,
    getCurrentCard,
    getNextCard,
    getPreviousCard,
    advanceProgress,
    isAnswered,
    progress,
    setIsAnswered,
    resetGameState,
    loading,
  } = useQuizContext();
  const { id } = useParams();
  const [isFlipped, setIsFlipped] = useState(false);
  const navigate = useNavigate();

  const handleFlip = () => {
    setIsFlipped(!isFlipped);
  };

  const handleNextCard = () => {
    setIsFlipped(false);
    isFlipped ? setTimeout(() => getNextCard(), 260) : getNextCard();
  };

  const handlePrevCard = () => {
    setIsFlipped(false);
    isFlipped ? setTimeout(() => getPreviousCard(), 260) : getPreviousCard();
  };

  useEffect(() => {
    resetGameState();
    fetchQuiz(id);
  }, [id]);

  useEffect(() => {
    if (isFlipped) {
      setIsAnswered(true);
      advanceProgress(false);
    }
  }, [isFlipped]);

  useEffect(() => {
    if (progress === 100) {
      navigate(`/quiz/results`);
    }
  }, [progress]);

  const currentCard = getCurrentCard();

  if (!currentQuiz || !currentCard || loading) {
    return <PageLoader />;
  }

  return (
    <div>
      <div className="flex items-center mt-2 gap-2">
        <Button variant="link" onClick={() => navigate("/search")}>
          <ArrowLeft />
          Back to Search
        </Button>
      </div>
      <div className="flex flex-col items-center justify-center min-h-[80vh] p-8 gap-8">
        <div className="flex flex-col items-center gap-2">
          <h1 className="font-serif md:text-5xl text-3xl font-bold">{currentQuiz.title}</h1>
        </div>
        <div className="w-full max-w-[500px]">
          <div
            className="relative w-full h-3 rounded-full overflow-hidden"
            style={{ backgroundColor: "#E5E7EB" }}
          >
            <div
              className="absolute top-0 left-0 h-full rounded-full transition-all duration-300"
              style={{
                width: `${progress}%`,
                backgroundColor: "#5700FE",
              }}
            />
          </div>
        </div>
        <div className="md:w-[500px] w-[350px] h-[350px] font-serif md:text-2xl text-xl lg:text-3xl">
          <FlipCard
            front={currentCard.question}
            back={currentCard.answer}
            isFlipped={isFlipped}
            handleFlip={handleFlip}
          />
        </div>
        <div className="flex flex-col items-center md:max-w-[90vh] w-full mt-8 gap-8 md:gap-0">
          <div className="flex items-center gap-2">
            <Button
              size="lg"
              disabled={isAnswered}
              variant="destructive"
              className="text-red-900"
              onClick={() => {
                advanceProgress(false);
              }}
            >
              I don't know this
            </Button>
            <Button
              size="lg"
              disabled={isAnswered}
              variant="success"
              className="text-green-900"
              onClick={() => {
                advanceProgress(true);
              }}
            >
              I know this
            </Button>
          </div>
          <div className="flex justify-between w-full">
            <div className="flex items-center gap-2">
              <Button variant="outline" onClick={handlePrevCard}>
                <ArrowLeft />
              </Button>
              <p className="font-inter font-bold text-sm md:text-base text-secondary">PREVIOUS</p>
            </div>
            <div className="flex items-center gap-2">
              <p className="font-inter font-bold text-sm md:text-base text-secondary">NEXT</p>
              <Button onClick={handleNextCard}>
                <ArrowRight />
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
