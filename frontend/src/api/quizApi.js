import { fetchData, getToken } from "/src/lib/fetchData";

export async function getQuiz(id) {
  return fetchData(`quizzes/${id}`);
}

export async function createQuiz(quiz) {
  return fetchData("quizzes", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${getToken()}`,
    },
    body: JSON.stringify(quiz),
  });
}

export async function getQuizzes() {
  return fetchData("quizzes/search");
}

export async function searchQuizzes(query) {
  return fetchData(`quizzes/search`);
}

export async function updateQuiz(id, quiz) {
  return fetchData(`quizzes/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${getToken()}`,
    },
    body: JSON.stringify(quiz),
  });
}

export async function deleteQuiz(id) {
  return fetchData(`quizzes/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function getMyQuizzes() {
  return fetchData("quizzes/me", {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function getQuizHistory(quizId) {
  return fetchData(`quiz-results/me/quiz/${quizId}`, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function saveQuizResult(quizId, score) {
  return fetchData(`quiz-results`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${getToken()}`,
    },
    body: JSON.stringify({
      quizId: quizId,
      scorePercentage: score,
    }),
  });
}
