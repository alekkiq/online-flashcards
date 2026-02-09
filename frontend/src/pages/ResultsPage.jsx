import { useQuizContext } from "../hooks/useQuizContext";
import { useNavigate } from "react-router";
import { useEffect } from "react";
import { Button } from "../components/ui/Button";
import { ScoreBadge } from "../components/ui/ScoreBadge";
import { Trophy, RotateCcw, ArrowLeft } from "lucide-react";

export default function ResultsPage() {
    const { currentQuiz, score, resetGameState } = useQuizContext();
    const navigate = useNavigate();

    useEffect(() => {
        if (!currentQuiz) {
            navigate("/search");
        }
    }, []);

    if (!currentQuiz) {
        return null;
    }

    const totalCards = currentQuiz.flashcards?.length || 0;
    const percentage = totalCards > 0 ? Math.round((score / totalCards) * 100) : 0;

    const handlePlayAgain = () => {
        resetGameState();
        navigate(`/quiz/${currentQuiz.id}`);
    };

    return (
        <div className="max-w-7xl mx-auto flex flex-col gap-8 mt-[10vh]">
            <div className="flex flex-col gap-6 p-8 bg-white rounded-lg">
                <div className="flex flex-col items-center gap-4">
                    <div className="w-16 h-16 md:w-20 md:h-20 bg-primary/10 rounded-full flex items-center justify-center">
                        <Trophy className="w-8 h-8 md:w-10 md:h-10 text-primary" />
                    </div>
                    <h1 className="font-serif text-2xl md:text-4xl font-bold text-center">
                        Quiz Complete!
                    </h1>
                    <p className="font-serif font-semibold text-sm md:text-lg text-secondary">
                        {currentQuiz.title}
                    </p>
                </div>

                <hr className="w-full border-secondary/20 my-2" />
                <div className="flex flex-col items-center gap-4">
                    <p className="font-inter font-medium text-sm md:text-lg text-secondary">
                        Your Score
                    </p>
                    <div className="flex flex-row items-baseline gap-2">
                        <span className="font-serif font-bold text-5xl md:text-7xl text-main">
                            {score}
                        </span>
                        <span className="font-serif font-bold text-2xl md:text-3xl text-secondary">
                            / {totalCards}
                        </span>
                    </div>
                    <ScoreBadge percentage={percentage} />
                </div>

                <hr className="w-full border-secondary/20 my-2" />
                <div className="flex flex-row gap-8 justify-center">
                    <div className="flex flex-col items-center">
                        <p className="font-inter font-bold text-lg md:text-xl text-green-500">
                            {score}
                        </p>
                        <p className="font-serif font-medium text-sm md:text-base text-secondary">
                            Correct
                        </p>
                    </div>
                    <div className="w-px h-12 bg-secondary/20"></div>
                    <div className="flex flex-col items-center">
                        <p className="font-inter font-bold text-lg md:text-xl text-red-500">
                            {totalCards - score}
                        </p>
                        <p className="font-serif font-medium text-sm md:text-base text-secondary">
                            Incorrect
                        </p>
                    </div>
                </div>
            </div>
            <div className="flex flex-col md:flex-row gap-4 justify-center items-center">
                <Button
                    variant="outline"
                    size="lg"
                    className="w-full md:w-auto"
                    onClick={() => { navigate(`/quiz-details/${currentQuiz.id}`); resetGameState(); }}
                >
                    <ArrowLeft className="w-4 h-4 mr-2" />
                    Back to Details
                </Button>
                <Button
                    size="lg"
                    className="w-full md:w-auto"
                    onClick={handlePlayAgain}
                >
                    <RotateCcw className="w-4 h-4 mr-2" />
                    Play Again
                </Button>
            </div>
        </div>
    );
}