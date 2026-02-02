import { useParams } from "react-router";
import { useEffect, useState } from "react";
import { PageLoader } from "../components/ui/PageLoader";
import { Badge } from "../components/ui/Badge";
import { Avatar } from "../components/ui/Avatar";
import { Button } from "../components/ui/Button";
import { useAuth } from "../hooks/useAuth";
import { useNavigate } from "react-router";
import { useQuizContext } from "../hooks/useQuizContext";

export default function QuizDetails() {
    const { id } = useParams();
    const { user } = useAuth();
    const navigate = useNavigate();
    const { fetchQuiz, quizHistory, currentQuiz } = useQuizContext();
    useEffect(() => {
        fetchQuiz(id);
    }, []);

    return (
        <>
            {currentQuiz ? (    
                <div className="flex flex-col gap-4 mt-[10vh] ml-[5vw] mr-[5vw] md:ml-[10vw] md:mr-[10vw]">
                    <div className="flex flex-col gap-4 p-8 bg-white rounded-lg">
                        <h1 className="font-serif text-2xl md:text-4xl font-bold">{currentQuiz.title}</h1>
                        <p className="font-serif font-semibold text-sm md:text-lg text-secondary max-w-[50vw]">{currentQuiz.description}</p>
                        <div className="flex flex-row items-center gap-2">
                            <Avatar name={currentQuiz.author.name} className="w-9 h-9 md:w-11 md:h-11" />
                            <div className="flex flex-col items-start">
                                    <p className="font-inter font-bold text-sm md:text-lg text-secondary"><span className="text-main font-medium">Created by </span>{currentQuiz.author.name}</p>
                                    {currentQuiz.authorRole && (
                                        <Badge textColor="text-white" bgColor="bg-primary">
                                            {currentQuiz.authorRole}
                                        </Badge>
                                    )}
                                </div>
                            </div>
                        <hr className="w-full border-secondary my-2" />  
                        {user ? (
                            <div className="flex flex-col gap-2">
                                <p className="font-inter font-bold text-sm md:text-lg text-secondary">History</p>
                                <div className="flex flex-col gap-2">
                                    {/* //TODO: fetch history from backend */}
                                    {quizHistory.map((h) => (
                                        <div key={h.id} className="flex flex-row items-center gap-2">
                                            <p className="font-inter font-bold text-sm md:text-lg text-secondary">{h.date}</p>
                                            <p className="font-inter font-bold text-sm md:text-lg text-secondary">{h.score}</p>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        ) : (
                            <p className="font-inter font-medium text-sm md:text-lg text-secondary">Login to save attempts</p>
                        )}
                    </div>
                    <div className="flex flex-col md:flex-row gap-4 justify-center md:justify-between items-center">
                        <div className="flex flex-row gap-8 md:ml-8">   
                            <div className="flex flex-col items-center">
                                <p className="font-inter font-bold text-sm md:text-lg text-main">{currentQuiz.tries}</p>
                                <p className="font-serif font-bold text-sm md:text-lg text-main">Tries</p>
                            </div>
                            <div className="w-px h-12 bg-secondary/20"></div>
                            <div className="flex flex-col items-center">
                                <p className="font-inter font-bold text-sm md:text-lg text-secondary">{currentQuiz.cardCount}</p>
                                <p className="font-serif font-bold text-sm md:text-lg text-main">Card Count</p>
                            </div>
                        </div>
                        <Button size="lg" className="md:w-1/3 w-full mt-2" onClick={() => navigate(`/quiz/${currentQuiz.id}`)}>Play Quiz</Button>
                    </div>
                </div>
            ) : (
                <PageLoader />
            )}
        </>
    );
}