import { useEffect, useState, useRef, useCallback } from "react";
import { useParams, useNavigate } from "react-router";
import { useQuizContext } from "../hooks/useQuizContext";
import FlipCard from "../components/Quiz/FlipCard";
import { Button } from "../components/ui/Button";
import { ArrowLeft, ArrowRight } from "lucide-react";
import { PageLoader } from "/src/components/ui/PageLoader";
import { useTranslation } from "react-i18next";

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
    error,
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
  const { t } = useTranslation();

  // keep score ref in sync
  useEffect(() => {
    scoreRef.current = score;
  }, [score]);

  const handleFlip = useCallback(() => {
    if (!isNavigating) setIsFlipped((prev) => !prev);
  }, [isNavigating]);

  const navigateCard = useCallback(
    (direction) => {
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
    },
    [isFlipped, isNavigating, getNextCard, getPreviousCard]
  );

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

  useEffect(() => {
    const handleKeyDown = (e) => {
      if (e.target.tagName === "INPUT" || e.target.tagName === "TEXTAREA") return;
      switch (e.key) {
        case " ":
        case "Enter":
          e.preventDefault();
          handleFlip();
          break;
        case "ArrowLeft":
          e.preventDefault();
          navigateCard("prev");
          break;
        case "ArrowRight":
          e.preventDefault();
          navigateCard("next");
          break;
        case "1":
          if (!isAnswered && !isNavigating) advanceProgress(true);
          break;
        case "2":
          if (!isAnswered && !isNavigating) advanceProgress(false);
          break;
      }
    };
    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [handleFlip, navigateCard, advanceProgress, isAnswered, isNavigating]);

  const currentCard = getCurrentCard();
  const totalCards = currentQuiz?.flashcards?.length || 0;

  if (error) {
    return (
      <div className="max-w-7xl mx-auto mt-8 px-4">
        <div className="bg-red-50 border border-red-200 text-red-700 rounded-xl px-4 py-3 text-sm">
          {error}
        </div>
      </div>
    );
  }

  if (!currentQuiz || !currentCard || loading) {
    return <PageLoader />;
  }

  const slideClass =
    slideDirection === "next"
      ? "animate-slide-in-right"
      : slideDirection === "prev"
        ? "animate-slide-in-left"
        : "";

  return (
    <div className="max-w-7xl mx-auto">
      <div className="flex items-center mt-2 gap-2">
        <Button variant="link" onClick={() => navigate("/search")}>
          <ArrowLeft />
          {t("quizGame.backToSearch")}
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
        <div className="flex flex-col items-center md:max-w-[90vh] w-full mt-8 gap-8 md:gap-4">
          <div className="flex items-center gap-2">
            <Button
              size="lg"
              disabled={isAnswered || isNavigating}
              variant="destructive"
              className="text-black-50"
              onClick={() => advanceProgress(false)}
            >
              {t("quizGame.iDontKnow")}
            </Button>
            <Button
              size="lg"
              disabled={isAnswered || isNavigating}
              variant="success"
              className="text-black-50"
              onClick={() => advanceProgress(true)}
            >
              {t("quizGame.iKnow")}
            </Button>
          </div>
          <div className="flex justify-between w-full">
            <div className="flex items-center gap-2">
              <Button
                variant="outline"
                onClick={() => navigateCard("prev")}
                disabled={isNavigating || currentCardIndex === 0}
              >
                <ArrowLeft />
              </Button>
              <p className="font-inter font-bold text-sm md:text-base text-secondary">{t("quizGame.previous")}</p>
            </div>
            <div className="flex items-center gap-2">
              <p className="font-inter font-bold text-sm md:text-base text-secondary">{t("quizGame.next")}</p>
              <Button
                onClick={() => navigateCard("next")}
                disabled={isNavigating || currentCardIndex === totalCards - 1}
              >
                <ArrowRight />
              </Button>
            </div>
          </div>
          <div className="hidden md:flex flex-col items-center gap-2 text-xs text-secondary/60 mt-6">
            <div className="flex items-center gap-1.5">
              <kbd className="px-2 py-0.5 rounded border border-secondary/20 bg-secondary/5 font-mono text-secondary/70">Space</kbd>
              <kbd className="px-2 py-0.5 rounded border border-secondary/20 bg-secondary/5 font-mono text-secondary/70">Enter</kbd>
              <span>— {t("quizGame.keyboardFlip")}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <kbd className="px-2 py-0.5 rounded border border-secondary/20 bg-secondary/5 font-mono text-secondary/70">←</kbd>
              <kbd className="px-2 py-0.5 rounded border border-secondary/20 bg-secondary/5 font-mono text-secondary/70">→</kbd>
              <span>— {t("quizGame.keyboardNavigate")}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <kbd className="px-2 py-0.5 rounded border border-secondary/20 bg-secondary/5 font-mono text-secondary/70">1</kbd>
              <span>— {t("quizGame.iKnow")}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <kbd className="px-2 py-0.5 rounded border border-secondary/20 bg-secondary/5 font-mono text-secondary/70">2</kbd>
              <span>— {t("quizGame.iDontKnow")}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
