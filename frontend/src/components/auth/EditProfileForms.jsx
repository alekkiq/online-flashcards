import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { editEmailSchema, editPasswordSchema } from "/src/lib/authSchemas";
import { FormField } from "/src/components/ui/FormField";
import { Input } from "/src/components/ui/Input";
import { Button } from "/src/components/ui/Button";
import { useAuth } from "/src/hooks/useAuth";
import { useTranslation } from "react-i18next";

function EditEmailForm({ user }) {
  const { handleUpdateEmail } = useAuth();
  const { t } = useTranslation();

  const form = useForm({
    resolver: zodResolver(editEmailSchema(t)),
    defaultValues: { email: user?.email ?? "" },
    mode: "onSubmit",
  });

  const onSubmit = async ({ email }) => {
    const response = await handleUpdateEmail(email);
    if (!response?.success) {
      form.setError("email", { message: response?.error ?? "Failed to update email" });
    }
  };

  return (
    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
      <h3 className="text-sm font-semibold text-secondary uppercase tracking-wide">{t("editProfile.updateEmail")}</h3>
      <FormField label={t("editProfile.email")} error={form.formState.errors.email?.message}>
        <Input
          type="email"
          autoComplete="email"
          hasError={!!form.formState.errors.email}
          {...form.register("email")}
        />
      </FormField>
      {form.formState.errors.root && (
        <p className="text-sm text-red-600">{form.formState.errors.root.message}</p>
      )}
      {form.formState.isSubmitSuccessful && (
        <p className="text-sm text-green-600">{t("editProfile.emailUpdated")}</p>
      )}
      <Button type="submit" disabled={form.formState.isSubmitting}>
        {form.formState.isSubmitting ? t("editProfile.saving") : t("editProfile.updateEmailBtn")}
      </Button>
    </form>
  );
}

function EditPasswordForm() {
  const { handleUpdatePassword } = useAuth();
  const { t } = useTranslation();

  const form = useForm({
    resolver: zodResolver(editPasswordSchema(t)),
    defaultValues: { oldPassword: "", newPassword: "", confirmPassword: "" },
  });

  const onSubmit = async ({ oldPassword, newPassword }) => {
    const response = await handleUpdatePassword(oldPassword, newPassword);
    if (!response?.success) {
      form.setError("oldPassword", { message: response?.error ?? "Failed to update password" });
      return;
    }
    form.reset();
  };

  return (
    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
      <h3 className="text-sm font-semibold text-secondary uppercase tracking-wide">
        {t("editProfile.changePassword")}
      </h3>
      <FormField label={t("editProfile.currentPassword")} error={form.formState.errors.oldPassword?.message}>
        <Input
          type="password"
          placeholder="••••••••"
          autoComplete="current-password"
          hasError={!!form.formState.errors.oldPassword}
          {...form.register("oldPassword")}
        />
      </FormField>
      <FormField label={t("editProfile.newPassword")} error={form.formState.errors.newPassword?.message}>
        <Input
          type="password"
          placeholder="••••••••"
          autoComplete="new-password"
          hasError={!!form.formState.errors.newPassword}
          {...form.register("newPassword")}
        />
      </FormField>
      <FormField
        label={t("editProfile.confirmNewPassword")}
        error={form.formState.errors.confirmPassword?.message}
      >
        <Input
          type="password"
          placeholder="••••••••"
          autoComplete="new-password"
          hasError={!!form.formState.errors.confirmPassword}
          {...form.register("confirmPassword")}
        />
      </FormField>
      {form.formState.errors.root && (
        <p className="text-sm text-red-600">{form.formState.errors.root.message}</p>
      )}
      {form.formState.isSubmitSuccessful && (
        <p className="text-sm text-green-600">{t("editProfile.passwordUpdated")}</p>
      )}
      <Button type="submit" disabled={form.formState.isSubmitting}>
        {form.formState.isSubmitting ? t("editProfile.saving") : t("editProfile.updatePasswordBtn")}
      </Button>
    </form>
  );
}

export default function EditProfileForm({ user, onCancel }) {
  const { t } = useTranslation();

  return (
    <div className="mt-4 space-y-6">
      <EditEmailForm user={user} />
      <hr className="border-secondary/20" />
      <EditPasswordForm />
      <Button type="button" variant="outline" onClick={onCancel}>
        {t("editProfile.close")}
      </Button>
    </div>
  );
}
