export const navLinks = (user) => [
    {
        to: "/search",
        label: "Search Quizzes",
    },
    ...(user ? [{
        to: "/my-quizzes",
        label: "My Quizzes"
    }] : [])
];
