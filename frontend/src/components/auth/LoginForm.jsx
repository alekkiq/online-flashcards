import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { loginSchema } from "@/lib/authSchemas";
import { useLogin } from "@/hooks/useLogin";
import { FormField } from "@/components/ui/FormField";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";

export default function LoginForm() {
  const { submit, isLoading, error } = useLogin();

  const form = useForm({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: "", password: "" },
    mode: "onSubmit",
  });

  async function onSubmit(values) {
    await submit(values);
  }

  return (
    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
      <FormField label="Email address *">
        <Input
          type="email"
          placeholder="Email address"
          autoComplete="email"
          {...form.register("email")}
        />
        {form.formState.errors.email && (
          <p className="text-xs text-red-500 mt-1">
            {form.formState.errors.email.message}
          </p>
        )}
      </FormField>

      <FormField label="Password *">
        <Input
          type="password"
          placeholder="Password"
          autoComplete="current-password"
          {...form.register("password")}
        />
        {form.formState.errors.password && (
          <p className="text-xs text-red-500 mt-1">
            {form.formState.errors.password.message}
          </p>
        )}
      </FormField>

      {error && <p className="text-sm text-red-600">{error}</p>}

      <Button type="submit" className="w-full h-11" disabled={isLoading}>
        {isLoading ? "Logging in..." : "Log In"}
      </Button>
    </form>
  );
}