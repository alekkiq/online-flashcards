import { describe, it, expect, beforeEach } from "vitest";
import { render, screen, act } from "@testing-library/react";
import { useContext } from "react";
import { QuizContext, QuizProvider } from "../context/QuizContext";

const TestConsumer = ({ onMount }) => {
  const context = useContext(QuizContext);
  onMount(context);
  return <div data-testid="test-consumer">Consumer</div>;
};

describe("QuizContext", () => {
  describe("Initial State", () => {
    it("should have null currentQuiz initially", () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      expect(contextValue.currentQuiz).toBeNull();
      expect(contextValue.loading).toBe(false);
      expect(contextValue.error).toBeNull();
    });

    it("should have initial game state values", () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      expect(contextValue.currentCardIndex).toBe(0);
      expect(contextValue.score).toBe(0);
      expect(contextValue.progress).toBe(0);
      expect(contextValue.isAnswered).toBe(false);
    });
  });

  describe("fetchQuiz", () => {
    it("should fetch a quiz by ID", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(1);
      });

      expect(contextValue.currentQuiz).not.toBeNull();
      expect(contextValue.currentQuiz.id).toBe(1);
      expect(contextValue.currentQuiz.title).toBe("Asia Geography Quiz");
    });

    it("should return null and set error for non-existent quiz", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        const result = await contextValue.fetchQuiz(999);
        expect(result).toBeNull();
      });

      expect(contextValue.error).toBe("Quiz not found");
    });

    it("should load quiz history when fetching quiz", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(1);
      });

      expect(contextValue.quizHistory).toHaveLength(2);
      expect(contextValue.quizHistory[0].score).toBe(28);
    });
  });

  describe("Score Tracking", () => {
    it("should increment score on correct answer", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      expect(contextValue.score).toBe(0);

      act(() => {
        contextValue.advanceProgress(true);
      });

      expect(contextValue.score).toBe(1);
    });

    it("should not increment score on incorrect answer", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      act(() => {
        contextValue.advanceProgress(false);
      });

      expect(contextValue.score).toBe(0);
    });

    it("should prevent double scoring on same card", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      act(() => {
        contextValue.advanceProgress(true);
      });

      act(() => {
        contextValue.advanceProgress(true);
      });

      expect(contextValue.score).toBe(1);
    });

    it("should accumulate score across multiple cards", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      act(() => {
        contextValue.advanceProgress(true);
      });
      act(() => {
        contextValue.getNextCard();
      });

      act(() => {
        contextValue.advanceProgress(false);
      });
      act(() => {
        contextValue.getNextCard();
      });

      act(() => {
        contextValue.advanceProgress(true);
      });

      expect(contextValue.score).toBe(2);
    });
  });

  describe("Card Navigation", () => {
    it("should return current card", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      const card = contextValue.getCurrentCard();
      expect(card).not.toBeNull();
      expect(card.question).toBe("What is the capital of Albania?");
    });

    it("should navigate to next card", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      let nextCard;
      act(() => {
        nextCard = contextValue.getNextCard();
      });

      expect(nextCard.question).toBe("What currency is used in Albania?");
      expect(contextValue.currentCardIndex).toBe(1);
    });

    it("should navigate to previous card", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      act(() => {
        contextValue.getNextCard();
      });
      act(() => {
        contextValue.getNextCard();
      });

      let prevCard;
      act(() => {
        prevCard = contextValue.getPreviousCard();
      });

      expect(prevCard.question).toBe("What currency is used in Albania?");
      expect(contextValue.currentCardIndex).toBe(1);
    });

    it("should return null when navigating past first card", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      let prevCard;
      act(() => {
        prevCard = contextValue.getPreviousCard();
      });

      expect(prevCard).toBeNull();
      expect(contextValue.currentCardIndex).toBe(0);
    });

    it("should return null when navigating past last card", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      act(() => {
        contextValue.getNextCard();
      });
      act(() => {
        contextValue.getNextCard();
      });
      act(() => {
        contextValue.getNextCard();
      });
      act(() => {
        contextValue.getNextCard();
      });

      let nextCard;
      act(() => {
        nextCard = contextValue.getNextCard();
      });

      expect(nextCard).toBeNull();
      expect(contextValue.currentCardIndex).toBe(4);
    });

    it("should return null for getCurrentCard when no quiz loaded", () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      expect(contextValue.getCurrentCard()).toBeNull();
    });
  });

  describe("Reset and Clear", () => {
    it("should reset game state", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      act(() => {
        contextValue.advanceProgress(true);
        contextValue.getNextCard();
        contextValue.advanceProgress(true);
      });

      act(() => {
        contextValue.resetGameState();
      });

      expect(contextValue.currentCardIndex).toBe(0);
      expect(contextValue.score).toBe(0);
      expect(contextValue.progress).toBe(0);
      expect(contextValue.isAnswered).toBe(false);
    });

    it("should clear quiz data", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      act(() => {
        contextValue.clearQuiz();
      });

      expect(contextValue.currentQuiz).toBeNull();
      expect(contextValue.quizHistory).toEqual([]);
      expect(contextValue.error).toBeNull();
    });
  });

  describe("Save Quiz Attempt", () => {
    it("should save quiz attempt with score", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      const initialHistoryLength = contextValue.quizHistory.length;

      await act(async () => {
        const result = await contextValue.saveQuizAttempt(2, 4);
        expect(result.success).toBe(true);
      });

      expect(contextValue.quizHistory).toHaveLength(initialHistoryLength + 1);
      expect(contextValue.quizHistory[contextValue.quizHistory.length - 1].score).toBe(4);
    });
  });
  
  describe("Answered Cards Tracking", () => {
    it("should track answered cards correctly when navigating", async () => {
      let contextValue;
      render(
        <QuizProvider>
          <TestConsumer onMount={(ctx) => (contextValue = ctx)} />
        </QuizProvider>
      );

      await act(async () => {
        await contextValue.fetchQuiz(2);
      });

      act(() => {
        contextValue.advanceProgress(true);
      });

      expect(contextValue.isAnswered).toBe(true);

      act(() => {
        contextValue.getNextCard();
      });

      expect(contextValue.isAnswered).toBe(false);

      act(() => {
        contextValue.getPreviousCard();
      });

      expect(contextValue.isAnswered).toBe(true);
    });
  });
});
