import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { registerSchema } from "/src/lib/authSchemas";
import { useAuth } from "/src/hooks/useAuth";
import { FormField } from "/src/components/ui/FormField";
import { Input } from "/src/components/ui/Input";
import { Button } from "/src/components/ui/Button";

export default function RegisterForm() {
  const { handleRegister, isLoading, error } = useAuth();

  const form = useForm({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      username: "",
      email: "",
      password: "",
      repeatPassword: "",
    },
    mode: "onSubmit",
  });

  async function onSubmit(values) {
    await handleRegister(values);
  }

  return (
    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
      <FormField label="Username *" error={form.formState.errors.username?.message}>
        <Input
          placeholder="Username"
          hasError={!!form.formState.errors.username}
          {...form.register("username")}
        />
      </FormField>

      <FormField label="Email address *" error={form.formState.errors.email?.message}>
        <Input
          type="email"
          placeholder="Email address"
          hasError={!!form.formState.errors.email}
          {...form.register("email")}
        />
      </FormField>

      <FormField label="Password *" error={form.formState.errors.password?.message}>
        <Input
          type="password"
          placeholder="Password"
          hasError={!!form.formState.errors.password}
          {...form.register("password")}
        />
      </FormField>

      <FormField label="Repeat password *" error={form.formState.errors.repeatPassword?.message}>
        <Input
          type="password"
          placeholder="Repeat password"
          hasError={!!form.formState.errors.repeatPassword}
          {...form.register("repeatPassword")}
        />
      </FormField>

      {error && <p className="text-sm text-red-600">{error}</p>}

      <Button type="submit" className="w-full! h-11" disabled={isLoading}>
        {isLoading ? "Creating account..." : "Sign Up"}
      </Button>
    </form>
  );
}