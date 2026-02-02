import { useEffect, useState } from "react";
import { useDebounce } from "use-debounce";

const SAMPLE_QUIZZES = [
    {
        id: 1,
        title: "Asia Geography Quiz",
        cardCount: 32,
        author: { name: "Aleksput"},
        authorRole: "Teacher",
    },
    {
        id: 2,
        title: "Albania Quiz",
        cardCount: 5,
        author: { name: "BlendG"},
        authorRole: "Student",
    },
    {
        id: 3,
        title: "Live Like Teemu",
        cardCount: 392,
        author: { name: "TeemuLaasio"},
        authorRole: "User",
    },
    {
        id: 4,
        title: "Bugs 101",
        cardCount: 16,
        author: { name: "AlabbasA"},
        authorRole: "Bug",
    },
    {
        id: 5,
        title: "Homo123",
        cardCount: 16,
        author: { name: "Homo"},
        authorRole: "Bug",
    }
];

export function useQuiz(searchParams, setSearchParams) {
    const searchQuery = searchParams.get("q") || "";
    const [debouncedQuery] = useDebounce(searchQuery, 1000);
    const [quizzes, setQuizzes] = useState(SAMPLE_QUIZZES);
    const [isLoading, setIsLoading] = useState(false);

    const setSearchQuery = (query) => {
        if (query) {
            setSearchParams({ q: query });
        } else {
            setSearchParams({});
        }
    };

    useEffect(() => {
        //TODO: fetch from backend using debouncedQuery
        if (!debouncedQuery.trim()) {
            setQuizzes(SAMPLE_QUIZZES);
        } else {
            const query = debouncedQuery.toLowerCase();
            setQuizzes(
                SAMPLE_QUIZZES.filter(
                    (quiz) =>
                        quiz.title.toLowerCase().includes(query) ||
                        quiz.author.name.toLowerCase().includes(query)
                )
            );
        }
    }, [debouncedQuery]);

    return {
        searchQuery,
        setSearchQuery,
        filteredQuizzes: quizzes,
        isLoading,
    };
}
