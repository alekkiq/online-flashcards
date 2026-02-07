import { z } from "zod";

export const loginSchema = z.object({
  email: z.string().email("Enter a valid email"),
  password: z.string().min(1, "Password is required"),
});

export const registerSchema = z
  .object({
    firstName: z.string().min(1, "First name is required"),
    lastName: z.string().min(1, "Last name is required"),
    email: z.string().email("Enter a valid email"),
    password: z.string().min(6, "Password must be at least 6 characters"),
    repeatPassword: z.string().min(1, "Repeat password is required"),
    isTeacher: z.boolean().default(false),
    organizationName: z.string().optional(),
  })
  .superRefine((val, ctx) => {
    if (val.password !== val.repeatPassword) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        message: "Passwords do not match",
        path: ["repeatPassword"],
      });
    }

    if (
      val.isTeacher &&
      (!val.organizationName || val.organizationName.trim().length === 0)
    ) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        message: "Organization name is required for teachers",
        path: ["organizationName"],
      });
    }
  });
