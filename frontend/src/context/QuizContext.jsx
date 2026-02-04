import { createContext, useState, useEffect } from "react";

const QuizContext = createContext(null);

const QuizProvider = ({ children }) => {
    const [currentQuiz, setCurrentQuiz] = useState(null);
    const [quizHistory, setQuizHistory] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // game state
    const [currentCardIndex, setCurrentCardIndex] = useState(0);
    const [score, setScore] = useState(0);
    const [progress, setProgress] = useState(0);
    const [answeredCards, setAnsweredCards] = useState([]);
    const [isAnswered, setIsAnswered] = useState(false);



    /** 
     * fetch a specific quiz by ID
     * @param {number} quizId the ID of the quiz to fetch
     */
    const fetchQuiz = async (quizId) => {
        setLoading(true);
        setError(null);
        try {
            //TODO: fetch from backend
            const SAMPLE_QUIZZES = [
                { 
                    id: 1, 
                    title: "Asia Geography Quiz", 
                    description: "This Quiz teaches you about Asia and Geography to learn all the fundamental bs about amazing asia blalbalbalbla", 
                    tries: 1, 
                    cardCount: 16, 
                    author: { name: "Aleksput" }, 
                    authorRole: "Teacher",
                    history: [
                        { id: 1, date: "2026-01-15", score: 28 },
                        { id: 2, date: "2026-01-20", score: 30 },
                    ],
                    flashcards: [
                        { id: 1, question: "What is the capital of Japan?", answer: "Tokyo" },
                        { id: 2, question: "What is the capital of China?", answer: "Beijing" },
                        { id: 3, question: "What is the capital of South Korea?", answer: "Seoul" },
                        { id: 4, question: "What is the largest country in Asia by area?", answer: "Russia (Asian part) / China" },
                        { id: 5, question: "What is the longest river in Asia?", answer: "Yangtze River" },
                        { id: 6, question: "What is the highest mountain in the world?", answer: "Mount Everest" },
                        { id: 7, question: "What ocean borders Asia to the east?", answer: "Pacific Ocean" },
                        { id: 8, question: "What is the capital of Thailand?", answer: "Bangkok" },
                        { id: 9, question: "What is the capital of Vietnam?", answer: "Hanoi" },
                        { id: 10, question: "What is the capital of India?", answer: "New Delhi" },
                        { id: 11, question: "What sea separates Japan from South Korea?", answer: "Sea of Japan / East Sea" },
                        { id: 12, question: "What is the capital of Indonesia?", answer: "Jakarta" },
                        { id: 13, question: "What desert is located in Mongolia and China?", answer: "Gobi Desert" },
                        { id: 14, question: "What is the capital of the Philippines?", answer: "Manila" },
                        { id: 15, question: "What mountain range separates India from Tibet?", answer: "Himalayas" },
                        { id: 16, question: "What is the capital of Malaysia?", answer: "Kuala Lumpur" },
                    ]
                },
                { 
                    id: 2, 
                    title: "Albania Quiz", 
                    description: "This Quiz teaches you about Albania", 
                    tries: 1, 
                    cardCount: 5, 
                    author: { name: "BlendG" }, 
                    authorRole: "Student",
                    history: [],
                    flashcards: [
                        { id: 1, question: "What is the capital of Albania?", answer: "Tirana" },
                        { id: 2, question: "What currency is used in Albania?", answer: "Albanian Lek (ALL)" },
                        { id: 3, question: "What sea borders Albania to the west?", answer: "Adriatic Sea" },
                        { id: 4, question: "What is the official language of Albania?", answer: "Albanian" },
                        { id: 5, question: "What is the highest mountain in Albania?", answer: "Mount Korab (2,764m)" },
                    ]
                },
                { 
                    id: 3, 
                    title: "Live Like Teemu", 
                    description: "Live Like Teemu", 
                    tries: 1, 
                    cardCount: 8, 
                    author: { name: "TeemuLaasio" }, 
                    authorRole: "User",
                    history: [],
                    flashcards: [
                        { id: 1, question: "How many hours of sleep does Teemu get?", answer: "8 hours minimum" },
                        { id: 2, question: "What does Teemu drink in the morning?", answer: "Black coffee" },
                        { id: 3, question: "How often does Teemu exercise?", answer: "Every day" },
                        { id: 4, question: "What is Teemu's favorite programming language?", answer: "JavaScript" },
                        { id: 5, question: "What time does Teemu wake up?", answer: "6:00 AM" },
                        { id: 6, question: "What is Teemu's life motto?", answer: "Ship it!" },
                        { id: 7, question: "How does Teemu handle bugs?", answer: "With patience and console.log" },
                        { id: 8, question: "What does Teemu eat for lunch?", answer: "Healthy salad" },
                    ]
                },
                { 
                    id: 4, 
                    title: "Bugs 101", 
                    description: "morjens tää quiz opettaa sua vältellä bugeja terveisin abbas", 
                    tries: 1, 
                    cardCount: 10, 
                    author: { name: "AlabbasA" }, 
                    authorRole: "Bug",
                    history: [],
                    flashcards: [
                        { id: 1, question: "What is a null pointer exception?", answer: "Error when accessing an object that points to nothing" },
                        { id: 2, question: "What is an off-by-one error?", answer: "Loop iterating one too many or too few times" },
                        { id: 3, question: "What is a race condition?", answer: "Bug where outcome depends on timing of events" },
                        { id: 4, question: "What is a memory leak?", answer: "When memory is allocated but never freed" },
                        { id: 5, question: "What is an infinite loop?", answer: "Loop that never terminates" },
                        { id: 6, question: "What is a syntax error?", answer: "Code that doesn't follow language rules" },
                        { id: 7, question: "What is a logic error?", answer: "Code runs but produces wrong results" },
                        { id: 8, question: "What is undefined behavior?", answer: "Code with unpredictable results" },
                        { id: 9, question: "What is a stack overflow?", answer: "Too many function calls exceeding stack memory" },
                        { id: 10, question: "What is a deadlock?", answer: "Two processes waiting for each other forever" },
                    ]
                },
                { 
                    id: 5, 
                    title: "Finnish Slang", 
                    description: "Learn some Finnish slang words!", 
                    tries: 1, 
                    cardCount: 6, 
                    author: { name: "Suomalainen" }, 
                    authorRole: "Native",
                    history: [],
                    flashcards: [
                        { id: 1, question: "What does 'Moi' mean?", answer: "Hi / Hello" },
                        { id: 2, question: "What does 'Kiitos' mean?", answer: "Thank you" },
                        { id: 3, question: "What does 'Hyvää päivää' mean?", answer: "Good day" },
                        { id: 4, question: "What does 'Anteeksi' mean?", answer: "Sorry / Excuse me" },
                        { id: 5, question: "What does 'Kippis' mean?", answer: "Cheers!" },
                        { id: 6, question: "What does 'Näkemiin' mean?", answer: "Goodbye" },
                    ]
                },
            ];

            const quizData = SAMPLE_QUIZZES.find((q) => q.id === Number(quizId));
            
            if (!quizData) {
                throw new Error("Quiz not found");
            }

            setCurrentQuiz(quizData);
            setQuizHistory(quizData.history || []);
            return quizData;
        } catch (err) {
            setError(err.message);
            console.error("Error fetching quiz:", err);
            return null;
        } finally {
            setLoading(false);
        }
    };

    /** 
     * save quiz attempt
     * @param {number} quizId The ID of the quiz to save attempt for
     * @param {number} score The score of the quiz attempt
     */
    const saveQuizAttempt = async (quizId, score) => {
        try {
            //TODO: fetch from backend
            const newAttempt = {
                id: Date.now(),
                date: new Date().toISOString().split('T')[0],
                score: score
            };

            setQuizHistory((prev) => [...prev, newAttempt]);
            return { success: true };
        } catch (err) {
            console.error("Error saving quiz attempt:", err);
            return { success: false, error: err.message };
        }
    };

    /** 
     * get current card
     * @returns {object|null} The current card
     */
    const getCurrentCard = () => {
        if (!currentQuiz) return null;
        return currentQuiz.flashcards[currentCardIndex];
    }

    /** 
     * get next card
     * @returns {object|null} The next card
     */
    const getNextCard = () => {
        if (!currentQuiz) return null;
        if (currentCardIndex === currentQuiz.flashcards.length - 1) return null;
        const nextIndex = currentCardIndex + 1;
        setCurrentCardIndex(nextIndex);
        setIsAnswered(answeredCards.includes(nextIndex));
        return currentQuiz.flashcards[nextIndex];
    }

    /** 
     * get previous card
     * @returns {object|null} The previous card
     */
    const getPreviousCard = () => {
        if (!currentQuiz) return null;
        if (currentCardIndex === 0) return null;
        const prevIndex = currentCardIndex - 1;
        setCurrentCardIndex(prevIndex);
        setIsAnswered(answeredCards.includes(prevIndex));
        return currentQuiz.flashcards[prevIndex];
    }

    /** 
     * calculate progress bar
     * @returns {number} The progress bar value
     */
    const progressBar = () => {
        if (!currentQuiz) return 0;
        setProgress((currentCardIndex + 1) / currentQuiz.flashcards.length * 100);
    }

    const advanceProgress = (isCorrect) => {
        if (isAnswered) return; // prevent double answering
        if (isCorrect) {
            setScore((prev) => prev + 1);
        } 
        setAnsweredCards((prev) => [...prev, currentCardIndex]);
        setIsAnswered(true);
        progressBar();
    }

    /** 
     * clear quiz data
     */
    const clearQuiz = () => {
        setCurrentQuiz(null);
        setQuizHistory([]);
        setError(null);
    };

    /**
     * reset game state (call when leaving quiz page)
     */
    const resetGameState = () => {
        setCurrentCardIndex(0);
        setScore(0);
        setProgress(0);
        setAnsweredCards([]);
        setIsAnswered(false);
    };

    return (
        <QuizContext.Provider
            value={{
                currentQuiz,
                quizHistory,
                loading,
                error,
                fetchQuiz,
                saveQuizAttempt,
                clearQuiz,
                setCurrentQuiz,
                // game state
                currentCardIndex,
                score,
                progress,
                getCurrentCard,
                getNextCard,
                getPreviousCard,
                progressBar,
                advanceProgress,
                isAnswered,
                setIsAnswered,
                resetGameState,
            }}
        >
            {children}
        </QuizContext.Provider>
    );
};

export { QuizProvider, QuizContext };
