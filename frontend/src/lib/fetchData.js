const API_BASE = "/api/v1";

/**
 * Helper for API requests
 * @param {string} endpoint
 * @param {RequestInit} options
 * @returns {Promise<{success: boolean, data?: any, error?: Error}>}
 */
export async function fetchData(endpoint, options = {}) {
  try {
    const response = await fetch(`${API_BASE}/${endpoint}`, options);

    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }

    const data = await response.json();
    return { success: true, data };
  } catch (error) {
    console.error("fetchData error:", error);
    return { success: false, error };
  }
}
