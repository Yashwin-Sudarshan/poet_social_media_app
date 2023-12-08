import * as z from "zod";

export const SignUpSchema = z
  .object({
    email: z.string().email({ message: "Invalid email format." }),
    username: z.string().min(1, {
      message: "Username cannot be empty.",
    }),
    password: z.string().min(8, {
      message: "Password must be at least 8 characters.",
    }),
    confirmPassword: z.string().min(8, {
      message: "Password must be at least 8 characters.",
    }),
    termsAndConditions: z.literal(true, {
      errorMap: () => ({
        message: "You must accept the terms and conditions.",
      }),
    }),
    privacyPolicy: z.literal(true, {
      errorMap: () => ({
        message: "You must accept the privacy policy.",
      }),
    }),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ["confirmPassword"],
  });
