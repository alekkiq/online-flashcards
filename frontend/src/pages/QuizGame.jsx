import { useEffect, useState, useRef, useCallback } from "react";
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
    currentCardIndex,
    isAnswered,
    progress,
    resetGameState,
    loading,
    isQuizFinished,
    saveQuizAttempt,
    score,
  } = useQuizContext();
  const { id } = useParams();
  const [isFlipped, setIsFlipped] = useState(false);
  const [isNavigating, setIsNavigating] = useState(false);
  const [slideDirection, setSlideDirection] = useState(null);
  const scoreRef = useRef(score);
  const navigate = useNavigate();

  // keep score ref in sync
  useEffect(() => {
    scoreRef.current = score;
  }, [score]);

  const handleFlip = () => {
    if (!isNavigating) setIsFlipped((prev) => !prev);
  };

  const navigateCard = useCallback((direction) => {
    if (isNavigating) return;

    const goTo = direction === "next" ? getNextCard : getPreviousCard;

    if (isFlipped) {
      setIsNavigating(true);
      setIsFlipped(false);
      setTimeout(() => {
        setSlideDirection(direction);
        goTo();
        setIsNavigating(false);
      }, 350);
    } else {
      setSlideDirection(direction);
      goTo();
    }
  }, [isFlipped, isNavigating, getNextCard, getPreviousCard]);

  useEffect(() => {
    resetGameState();
    fetchQuiz(id);
  }, [id]);

  useEffect(() => {
    if (isQuizFinished()) {
      saveQuizAttempt(id, scoreRef.current);
      navigate(`/quiz/results`);
    }
  }, [progress]);

  // clear slide direction after animation completes
  useEffect(() => {
    if (slideDirection) {
      const timer = setTimeout(() => setSlideDirection(null), 300);
      return () => clearTimeout(timer);
    }
  }, [slideDirection, currentCardIndex]);

  const currentCard = getCurrentCard();
  const totalCards = currentQuiz?.flashcards?.length || 0;

  if (!currentQuiz || !currentCard || loading) {
    return <PageLoader />;
  }

  const slideClass = slideDirection === "next"
    ? "animate-slide-in-right"
    : slideDirection === "prev"
      ? "animate-slide-in-left"
      : "";

  return (
    <div className="max-w-7xl mx-auto">
      <div className="flex items-center mt-2 gap-2">
        <Button variant="link" onClick={() => navigate("/search")}>
          <ArrowLeft />
          Back to Search
        </Button>
      </div>
      <div className="flex flex-col items-center justify-center min-h-[80vh] p-8 gap-8">
        <div className="flex flex-col items-center gap-2">
          <h1 className="font-serif md:text-5xl text-3xl font-bold">{currentQuiz.title}</h1>
          <p className="font-inter text-sm text-secondary">
            {currentCardIndex + 1} / {totalCards}
          </p>
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
        <div
          key={currentCardIndex}
          className={`md:w-[500px] w-[350px] h-[350px] font-serif md:text-2xl text-xl lg:text-3xl ${slideClass}`}
        >
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
              disabled={isAnswered || isNavigating}
              variant="destructive"
              className="text-black-50"
              onClick={() => advanceProgress(false)}
            >
              I don't know this
            </Button>
            <Button
              size="lg"
              disabled={isAnswered || isNavigating}
              variant="success"
              className="text-black-50"
              onClick={() => advanceProgress(true)}
            >
              I know this
            </Button>
          </div>
          <div className="flex justify-between w-full">
            <div className="flex items-center gap-2">
              <Button variant="outline" onClick={() => navigateCard("prev")} disabled={isNavigating || currentCardIndex === 0}>
                <ArrowLeft />
              </Button>
              <p className="font-inter font-bold text-sm md:text-base text-secondary">PREVIOUS</p>
            </div>
            <div className="flex items-center gap-2">
              <p className="font-inter font-bold text-sm md:text-base text-secondary">NEXT</p>
              <Button onClick={() => navigateCard("next")} disabled={isNavigating || currentCardIndex === totalCards - 1}>
                <ArrowRight />
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
