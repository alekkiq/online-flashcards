import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { loginSchema } from "/src/lib/authSchemas";
import { FormField } from "/src/components/ui/FormField";
import { Input } from "/src/components/ui/Input";
import { Button } from "/src/components/ui/Button";
import { useAuth } from "/src/hooks/useAuth";
import { useTranslation } from "react-i18next";

export default function LoginForm() {
  const { handleLogin, isLoading, error } = useAuth();
  const { t } = useTranslation();

  const form = useForm({
    resolver: zodResolver(loginSchema),
    defaultValues: { username: "", password: "" },
    mode: "onSubmit",
  });

  async function onSubmit(values) {
    await handleLogin(values);
  }

  return (
    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
      <FormField label={t("loginForm.username")} error={form.formState.errors.username?.message}>
        <Input
          type="text"
          placeholder={t("loginForm.usernamePlaceholder")}
          autoComplete="username"
          hasError={!!form.formState.errors.username}
          {...form.register("username")}
        />
      </FormField>

      <FormField label={t("loginForm.password")} error={form.formState.errors.password?.message}>
        <Input
          type="password"
          placeholder={t("loginForm.passwordPlaceholder")}
          autoComplete="current-password"
          hasError={!!form.formState.errors.password}
          {...form.register("password")}
        />
      </FormField>

      {error && <p className="text-sm text-red-600">{error}</p>}

      <Button type="submit" className="w-full! h-11" disabled={isLoading}>
        {isLoading ? t("loginForm.loggingIn") : t("loginForm.logIn")}
      </Button>
    </form>
  );
}
