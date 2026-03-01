import * as z from "zod";

export const quizSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().optional(),
  subject: z.string().min(1, "Subject is required"),
  cards: z
    .array(
      z.object({
        question: z.string().min(1, "Question is required"),
        answer: z.string().min(1, "Answer is required"),
      })
    )
    .min(1, "At least one card is required"),
});

export const promotionRequestSchema = z.object({
  message: z.string().max(500, "Message must be at most 500 characters").optional(),
});

export const joinClassroomSchema = z.object({
  joinCode: z.string().min(1, "Join code is required"),
});

export const classroomSchema = z.object({
  title: z.string().min(1, "Title is required").max(255, "Title must be at most 255 characters"),
  description: z.string().max(255, "Description must be at most 255 characters").optional(),
  note: z.string().max(255, "Note must be at most 255 characters").optional(),
  joinCode: z.union([z.literal(""), z.string().min(6, "Join code must be at least 6 characters")]),
  subjectId: z.preprocess(
    (val) => (val === null || val === "" ? undefined : Number(val)),
    z
      .number({ required_error: "Subject is required", invalid_type_error: "Subject is required" })
      .min(1, "Subject is required")
  ),
});

export const classroomUpdateSchema = z.object({
  title: z.string().min(1, "Title is required").max(255, "Title must be at most 255 characters"),
  description: z.string().max(255, "Description must be at most 255 characters").optional(),
  note: z.string().max(255, "Note must be at most 255 characters").optional(),
});
