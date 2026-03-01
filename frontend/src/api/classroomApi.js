import { fetchData } from "/src/lib/fetchData";

function getToken() {
  return localStorage.getItem("token");
}

export async function getMyClassrooms() {
  return fetchData("classrooms/me", {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function getClassroomById(id) {
  return fetchData(`classrooms/${id}`, {
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function createClassroom(classroom) {
  return fetchData("classrooms", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    body: JSON.stringify(classroom),
  });
}

export async function updateClassroom(id, data) {
  return fetchData(`classrooms/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
    body: JSON.stringify(data),
  });
}

export async function joinClassroomByCode(code) {
  return fetchData(`classrooms/join?code=${encodeURIComponent(code)}`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function addQuizToClassroom(classroomId, quizId) {
  return fetchData(`classrooms/${classroomId}/quizzes/${quizId}`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function removeQuizFromClassroom(classroomId, quizId) {
  return fetchData(`classrooms/${classroomId}/quizzes/${quizId}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function removeUserFromClassroom(classroomId, userId) {
  return fetchData(`classrooms/${classroomId}/users/${userId}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function leaveClassroom(classroomId) {
  return fetchData(`classrooms/${classroomId}/leave`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

