import { fetchData } from "/src/lib/fetchData";

export async function getQuiz(id) {
  return fetchData(`quiz/${id}`);
}

export async function createQuiz(quiz) {
  return fetchData("quiz", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(quiz),
  });
}

export async function updateQuiz(id, quiz) {
  return fetchData(`quiz/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(quiz),
  });
}

export async function deleteQuiz(id) {
  return fetchData(`quiz/${id}`, {
    method: "DELETE",
  });
}
