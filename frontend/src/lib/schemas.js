import * as z from "zod";

export const quizSchema = (t) =>
  z.object({
    title: z
      .string()
      .min(1, t("validation.titleRequired"))
      .max(40, t("validation.titleMax40")),
    description: z
      .string()
      .max(255, t("validation.descriptionMax255"))
      .optional(),
    subjectCode: z.string().min(1, t("validation.subjectRequired")),
    cards: z
      .array(
        z.object({
          question: z
            .string()
            .min(1, t("validation.questionRequired"))
            .max(255, t("validation.questionMax255")),
          answer: z
            .string()
            .min(1, t("validation.answerRequired"))
            .max(255, t("validation.answerMax255")),
        })
      )
      .min(1, t("validation.atLeastOneCard")),
  });

export const promotionRequestSchema = (t) =>
  z.object({
    message: z.string().max(500, t("validation.messageMax500")).optional(),
  });

export const joinClassroomSchema = (t) =>
  z.object({
    joinCode: z.string().min(1, t("validation.joinCodeRequired")),
  });

export const classroomSchema = (t) =>
  z.object({
    title: z
      .string()
      .min(1, t("validation.titleRequired"))
      .max(255, t("validation.titleMax255")),
    description: z
      .string()
      .max(255, t("validation.descriptionMax255"))
      .optional(),
    note: z.string().max(255, t("validation.noteMax255")).optional(),
    joinCode: z.union([
      z.literal(""),
      z.string().min(6, t("validation.joinCodeMin6")),
    ]),
    subjectId: z.preprocess(
      (val) => (val === null || val === "" ? undefined : Number(val)),
      z
        .number({
          required_error: t("validation.subjectRequired"),
          invalid_type_error: t("validation.subjectRequired"),
        })
        .min(1, t("validation.subjectRequired"))
    ),
  });

export const classroomUpdateSchema = (t) =>
  z.object({
    title: z
      .string()
      .min(1, t("validation.titleRequired"))
      .max(255, t("validation.titleMax255")),
    description: z
      .string()
      .max(255, t("validation.descriptionMax255"))
      .optional(),
    note: z.string().max(255, t("validation.noteMax255")).optional(),
  });
