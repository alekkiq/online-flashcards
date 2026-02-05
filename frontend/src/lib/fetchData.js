//TODO: Implement fetchData
const API_BASE = "/api/v1";

/**
 * use this for fetching data, makes it easier
 * @param {*} endpoint without slash
 * @param {*} options options for fetch
 * @returns {*} {success: boolean, data, error?: Error}
 */
export async function fetchData(endpoint, options = {}) {
  try {
    const url = `${API_BASE}/${endpoint}`;
    const response = await fetch(url, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
    });
    const data = await response.json();
    if (!response.ok || !data.success) {
      return { 
        success: false, 
        error: data.error?.message || "Request failed", 
        status: response.status 
      };
    }
    return { success: true, data };
  } catch (error) {
    console.error("Error fetching data: ", error);
    return { success: false, error: error.message };
  }
}
