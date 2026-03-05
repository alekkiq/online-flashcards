import { describe, it, expect, vi } from "vitest";
import { render, act } from "@testing-library/react";
import { useContext } from "react";
import { QuizContext, QuizProvider } from "../context/QuizContext";

vi.mock("../hooks/useAuth", () => ({
  useAuth: () => ({
    user: { id: 1, role: "USER" },
    token: "fake-token"
  })
}));

global.fetch = vi.fn(() => Promise.resolve({
  ok: true,
  json: () => Promise.resolve([])
}));

const TestConsumer = ({ onMount }) => {
  const context = useContext(QuizContext);
  onMount(context);
  return null;
};

describe("QuizContext Logic States", () => {
  it("should have correct initial state", () => {
    let contextValue;
    render(
      <QuizProvider>
        <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
      </QuizProvider>
    );

    expect(contextValue.currentQuiz).toBeNull();
    expect(contextValue.loading).toBe(false);
    expect(contextValue.error).toBeNull();
    expect(contextValue.currentCardIndex).toBe(0);
    expect(contextValue.score).toBe(0);
    expect(contextValue.progress).toBe(0);
    expect(contextValue.isAnswered).toBe(false);
    expect(contextValue.quizHistory).toEqual([]);
  });

  it("should reset game state back to 0", () => {
    let contextValue;
    render(
      <QuizProvider>
        <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
      </QuizProvider>
    );

    act(() => {
      contextValue.resetGameState();
    });

    expect(contextValue.currentCardIndex).toBe(0);
    expect(contextValue.score).toBe(0);
    expect(contextValue.progress).toBe(0);
    expect(contextValue.isAnswered).toBe(false);
  });

  it("should clear quiz completely", () => {
    let contextValue;
    render(
      <QuizProvider>
        <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
      </QuizProvider>
    );

    act(() => {
      contextValue.clearQuiz();
    });

    expect(contextValue.currentQuiz).toBeNull();
    expect(contextValue.quizHistory).toEqual([]);
    expect(contextValue.error).toBeNull();
  });

  it("should safely return null for current card if no quiz is loaded", () => {
    let contextValue;
    render(
      <QuizProvider>
        <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
      </QuizProvider>
    );

    expect(contextValue.getCurrentCard()).toBeNull();
  });
});
