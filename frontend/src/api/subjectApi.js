import { fetchData } from "/src/lib/fetchData";

export async function getSubjects() {
  return fetchData("subjects");
}

