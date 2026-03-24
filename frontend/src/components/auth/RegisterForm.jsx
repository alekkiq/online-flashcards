import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { registerSchema } from "/src/lib/authSchemas";
import { useAuth } from "/src/hooks/useAuth";
import { FormField } from "/src/components/ui/FormField";
import { Input } from "/src/components/ui/Input";
import { Button } from "/src/components/ui/Button";
import { useTranslation } from "react-i18next";

export default function RegisterForm() {
  const { handleRegister, isLoading, error } = useAuth();
  const { t } = useTranslation();

  const form = useForm({
    resolver: zodResolver(registerSchema(t)),
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
      <FormField label={t("registerForm.username")} error={form.formState.errors.username?.message}>
        <Input
          placeholder={t("registerForm.usernamePlaceholder")}
          hasError={!!form.formState.errors.username}
          {...form.register("username")}
        />
      </FormField>

      <FormField label={t("registerForm.email")} error={form.formState.errors.email?.message}>
        <Input
          type="email"
          placeholder={t("registerForm.emailPlaceholder")}
          hasError={!!form.formState.errors.email}
          {...form.register("email")}
        />
      </FormField>

      <FormField label={t("registerForm.password")} error={form.formState.errors.password?.message}>
        <Input
          type="password"
          placeholder={t("registerForm.passwordPlaceholder")}
          hasError={!!form.formState.errors.password}
          {...form.register("password")}
        />
      </FormField>

      <FormField label={t("registerForm.repeatPassword")} error={form.formState.errors.repeatPassword?.message}>
        <Input
          type="password"
          placeholder={t("registerForm.repeatPasswordPlaceholder")}
          hasError={!!form.formState.errors.repeatPassword}
          {...form.register("repeatPassword")}
        />
      </FormField>

      {error && <p className="text-sm text-red-600">{error}</p>}

      <Button type="submit" className="w-full! h-11" disabled={isLoading}>
        {isLoading ? t("registerForm.creatingAccount") : t("registerForm.signUp")}
      </Button>
    </form>
  );
}
