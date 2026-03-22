export const navLinks = (user, t) => [
  {
    to: "/search",
    label: t ? t("nav.findQuizzes") : "Find Quizzes",
  },
  ...(user
    ? [
        {
          to: "/my-quizzes",
          label: t ? t("nav.myQuizzes") : "My Quizzes",
        },
        {
          to: "/classrooms",
          label: t ? t("nav.classrooms") : "Classrooms",
        },
      ]
    : []),
];
