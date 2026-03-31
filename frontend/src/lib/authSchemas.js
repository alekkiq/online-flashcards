import { z } from "zod";

export const loginSchema = (t) =>
  z.object({
    username: z.string().min(1, t("validation.usernameRequired")),
    password: z.string().min(1, t("validation.passwordRequired")),
  });

export const registerSchema = (t) =>
  z
    .object({
      username: z.string().min(1, t("validation.usernameRequired")),
      email: z.email(t("validation.emailInvalid")),
      password: z.string().min(6, t("validation.passwordMin6")),
      repeatPassword: z.string().min(1, t("validation.repeatPasswordRequired")),
    })
    .refine((val) => val.password === val.repeatPassword, {
      message: t("validation.passwordsMustMatch"),
      path: ["repeatPassword"],
    });

export const editEmailSchema = (t) =>
  z.object({
    email: z.string().email(t("validation.emailInvalid")),
  });

export const editPasswordSchema = (t) =>
  z
    .object({
      oldPassword: z.string().min(1, t("validation.currentPasswordRequired")),
      newPassword: z.string().min(8, t("validation.passwordMin8")),
      confirmPassword: z.string(),
    })
    .refine((data) => data.newPassword === data.confirmPassword, {
      message: t("validation.passwordsMustMatch"),
      path: ["confirmPassword"],
    });
