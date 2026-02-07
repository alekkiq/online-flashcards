import { fetchData } from "@/lib/fetchData";

/**
 * Login request
 */
export async function loginApi(payload) {
  return fetchData("auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });
}

/**
 * Register request
 */
export async function registerApi(payload) {
  return fetchData("auth/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });
}
