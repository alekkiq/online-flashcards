export const navLinks = (user) => [
  {
    to: "/search",
    label: "Find Quizzes",
  },
  ...(user
    ? [
        {
          to: "/my-quizzes",
          label: "My Quizzes",
        },
        {
          to: "/classrooms",
          label: "Classrooms",
        },
      ]
    : []),
];
