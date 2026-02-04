//TODO: Implement fetchData
const API_BASE = "/api/v1";

/**
 * use this for fetching data, makes it easier
 * @param {*} endpoint without slash
 * @param {*} options options for fetch
 * @returns {response.json()}
 */

export async function fetchData(endpoint, options = {}) {
  try {
    const url = `${API_BASE}/${endpoint}`;
    const response = await fetch(url, options);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error("Error fetching data: ", error);
    throw error;
  }
}
