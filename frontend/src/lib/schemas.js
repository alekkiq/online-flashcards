import * as z from "zod";

export const quizSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().optional(),
  cards: z.array(
    z.object({
      question: z.string().min(1, "Question is required"),
      answer: z.string().min(1, "Answer is required"),
    })
  ).min(1, "At least one card is required"),
});

export const promotionRequestSchema = z.object({
    message: z.string().max(500, "Message must be at most 500 characters").optional(),
});