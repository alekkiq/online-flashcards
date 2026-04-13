import { fetchData, getToken } from "/src/lib/fetchData";

export async function addLearningMaterial(classroomId, data) {
  return fetchData(`classrooms/${classroomId}/learning-materials`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${getToken()}`,
    },
    body: JSON.stringify(data),
  });
}

export async function removeLearningMaterial(classroomId, materialId) {
  return fetchData(`classrooms/${classroomId}/learning-materials/${materialId}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });
}

export async function editLearningMaterial(classroomId, materialId, data) {
  return fetchData(`classrooms/${classroomId}/learning-materials/${materialId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${getToken()}`,
    },
    body: JSON.stringify(data),
  });
}
