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
    const mockData = { success: true, id: 1, title: "Test Quiz" };
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockData,
    });

    const result = await fetchData("quizzes/1", {});

    expect(fetch).toHaveBeenCalledWith("/api/v1/quizzes/1", {
      headers: {
        "Content-Type": "application/json",
      },
    });
    expect(result).toEqual({ success: true, data: mockData });
  });

  it("should pass options to fetch and merge headers", async () => {
    const mockData = { success: true, created: true };
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockData,
    });

    const options = {
      method: "POST",
      headers: { "Authorization": "Bearer token" },
      body: JSON.stringify({ title: "New Quiz" }),
    };

    await fetchData("quizzes", options);

    expect(fetch).toHaveBeenCalledWith("/api/v1/quizzes", {
      ...options,
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer token",
      },
    });
  });

  it("should return success: false on HTTP error", async () => {
    fetch.mockResolvedValueOnce({
      ok: false,
      status: 404,
      json: async () => ({ error: { message: "Quiz not found" } }),
    });

    const result = await fetchData("quizzes/999");

    expect(result.success).toBe(false);
    expect(result.error).toBe("Quiz not found");
    expect(result.status).toBe(404);
  });

  it("should return success: false on network error", async () => {
    fetch.mockRejectedValueOnce(new Error("Network error"));

    const result = await fetchData("quizzes");

    expect(result.success).toBe(false);
    expect(result.error).toBe("Network error");
  });

  it("should handle 500 server error", async () => {
    fetch.mockResolvedValueOnce({
      ok: false,
      status: 500,
      json: async () => ({ error: { message: "Internal Server Error" } }),
    });

    const result = await fetchData("quizzes");

    expect(result.success).toBe(false);
    expect(result.error).toBe("Internal Server Error");
  });
});
