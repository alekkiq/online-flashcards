import { useParams } from "react-router";
import { useEffect, useState } from "react";
import { PageLoader } from "../components/ui/PageLoader";

// TODO: fetch this from backend
const SAMPLE_QUIZZES = [
    { id: 1, title: "Asia Geography Quiz", description: "This Quiz teaches you about Asia", tries: 1, cardCount: 32, author: { name: "Aleksput" }, authorRole: "Teacher" },
    { id: 2, title: "Albania Quiz", description: "This Quiz teaches you about Albania", tries: 1, cardCount: 5, author: { name: "BlendG" }, authorRole: "Student" },
    { id: 3, title: "Live Like Teemu", description: "Live Like Teemu", tries: 1, cardCount: 392, author: { name: "TeemuLaasio" }, authorRole: "User" },
    { id: 4, title: "Bugs 101", description: "Bugs 101", tries: 1, cardCount: 16, author: { name: "AlabbasA" }, authorRole: "Bug" },
    { id: 5, title: "Perse", description: "Persettä", tries: 1, cardCount: 16, author: { name: "Homo" }, authorRole: "Bug" },
];

export default function QuizDetails() {
    const { id } = useParams();
    const [quiz, setQuiz] = useState(null);

    useEffect(() => {
        // Convert id to number since useParams returns strings
        const quizData = SAMPLE_QUIZZES.find((q) => q.id === Number(id));
        setQuiz(quizData);
    }, [id]);

    return (
        <div>
            {quiz ? (
                <div className="flex flex-col gap-4 p-8 bg-white mt-[10vh] rounded-lg">
                    <h1 className="font-serif text-4xl font-bold">{quiz.title}</h1>
                    <p className="font-serif font-semibold text-lg text-secondary">{quiz.description}</p>
                    <p>{quiz.cardCount}</p>
                    <p>{quiz.author.name}</p>
                    <p>{quiz.authorRole}</p>
                </div>
            ) : (
                <PageLoader />
            )}
        </div>
    );
}