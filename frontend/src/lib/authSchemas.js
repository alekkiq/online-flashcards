import { z } from "zod";

export const loginSchema = z.object({
  username: z.string().min(1, "Username is required"),
  password: z.string().min(1, "Password is required"),
});

export const registerSchema = z
  .object({
    username: z.string().min(1, "Username is required"),
    email: z.email("Enter a valid email"),
    password: z.string().min(6, "Password must be at least 6 characters"),
    repeatPassword: z.string().min(1, "Repeat password is required"),
  })
  .refine((val) => val.password === val.repeatPassword, {
    message: "Passwords do not match",
    path: ["repeatPassword"],
  });
