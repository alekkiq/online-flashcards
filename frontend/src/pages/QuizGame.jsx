import { useParams } from "react-router";
import { useQuizContext } from "../hooks/useQuizContext";

export default function QuizGame() {
    const { currentQuiz } = useQuizContext();
    
    return (
        <div>
            <h1>Quiz Game {currentQuiz.id}</h1>
        </div>
    );
}