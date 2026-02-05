import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { fetchData } from "../lib/fetchData";

describe("fetchData", () => {
  beforeEach(() => {
    vi.stubGlobal("fetch", vi.fn());
  });

  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it("should return success and data on successful fetch", async () => {
    const mockData = { id: 1, title: "Test Quiz" };
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockData,
    });

    const result = await fetchData("quizzes/1");

    expect(fetch).toHaveBeenCalledWith("/api/v1/quizzes/1", {});
    expect(result).toEqual({ success: true, data: mockData });
  });

  it("should pass options to fetch", async () => {
    const mockData = { created: true };
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockData,
    });

    const options = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title: "New Quiz" }),
    };

    await fetchData("quizzes", options);

    expect(fetch).toHaveBeenCalledWith("/api/v1/quizzes", options);
  });

  it("should return success: false on HTTP error", async () => {
    fetch.mockResolvedValueOnce({
      ok: false,
      status: 404,
    });

    const result = await fetchData("quizzes/999");

    expect(result.success).toBe(false);
    expect(result.error).toBeInstanceOf(Error);
    expect(result.error.message).toBe("HTTP error! status: 404");
  });

  it("should return success: false on network error", async () => {
    fetch.mockRejectedValueOnce(new Error("Network error"));

    const result = await fetchData("quizzes");

    expect(result.success).toBe(false);
    expect(result.error.message).toBe("Network error");
  });

  it("should handle 500 server error", async () => {
    fetch.mockResolvedValueOnce({
      ok: false,
      status: 500,
    });

    const result = await fetchData("quizzes");

    expect(result.success).toBe(false);
    expect(result.error.message).toBe("HTTP error! status: 500");
  });
});
