import i18n from "../i18n";
import { LANGUAGES } from "../config";

const API_BASE = "/api/v1";

/**
 * use this for fetching data, makes it easier
 * @param {*} endpoint without slash
 * @param {*} options options for fetch
 * @returns {*} {success: boolean, data, error?: Error}
 */
export async function fetchData(endpoint, options = {}) {
  const currentLocale = LANGUAGES[i18n.language]?.locale || "en-UK";
  try {
    const url = `${API_BASE}/${endpoint}`;
    const response = await fetch(url, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        "Accept-Language": currentLocale,
        ...options.headers,
      },
    });
    const data = await response.json();
    if (!response.ok || !data.success) {
      return {
        success: false,
        error: data.error?.message || "Request failed",
        status: response.status,
      };
    }
    return { success: true, data };
  } catch (error) {
    console.error("Error fetching data: ", error);
    return { success: false, error: error.message };
  }
}

export function getToken() {
    return localStorage.getItem("token");
}