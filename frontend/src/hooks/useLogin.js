import { useState } from "react";
import { loginApi } from "@/api/authApi";

export function useLogin() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  async function submit(values) {
    setIsLoading(true);
    setError("");

    const result = await loginApi(values);

    if (!result.success) {
      setError(result.error?.message || "Login failed");
      setIsLoading(false);
      return null;
    }

    setIsLoading(false);
    return result.data;
  }

  return { submit, isLoading, error };
}
