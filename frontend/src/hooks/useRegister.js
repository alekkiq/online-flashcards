import { useState } from "react";
import { registerApi } from "@/api/authApi";

export function useRegister() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  async function submit(values) {
    setIsLoading(true);
    setError("");

    const result = await registerApi(values);

    if (!result.success) {
      setError(result.error?.message || "Register failed");
      setIsLoading(false);
      return null;
    }

    setIsLoading(false);
    return result.data;
  }

  return { submit, isLoading, error };
}
