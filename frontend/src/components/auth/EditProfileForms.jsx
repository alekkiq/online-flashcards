import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { editEmailSchema, editPasswordSchema } from "/src/lib/authSchemas";
import { FormField } from "/src/components/ui/FormField";
import { Input } from "/src/components/ui/Input";
import { Button } from "/src/components/ui/Button";
import { useAuth } from "/src/hooks/useAuth";

function EditEmailForm({ user }) {
    const { handleUpdateEmail } = useAuth();

    const form = useForm({
        resolver: zodResolver(editEmailSchema),
        defaultValues: { email: user?.email ?? "" },
        mode: "onSubmit",
    });

    const onSubmit = async ({ email }) => {
        const response = await handleUpdateEmail(email);
        if (!response?.success) {
            form.setError("email", { message: response?.error ?? "Failed to update email" });
        }
    }

    return (
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <h3 className="text-sm font-semibold text-secondary uppercase tracking-wide">Update Email</h3>
            <FormField label="Email" error={form.formState.errors.email?.message}>
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
                <p className="text-sm text-green-600">Email updated successfully.</p>
            )}
            <Button type="submit" disabled={form.formState.isSubmitting}>
                {form.formState.isSubmitting ? "Saving..." : "Update Email"}
            </Button>
        </form>
    );
}

function EditPasswordForm() {
    const { handleUpdatePassword } = useAuth();

    const form = useForm({
        resolver: zodResolver(editPasswordSchema),
        defaultValues: { oldPassword: "", newPassword: "", confirmPassword: "" },
    });

    const onSubmit = async ({ oldPassword, newPassword }) => {
        const response = await handleUpdatePassword(oldPassword, newPassword);
        if (!response?.success) {
            form.setError("oldPassword", { message: response?.error ?? "Failed to update password" });
            return;
        }
        form.reset();
    }

    return (
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <h3 className="text-sm font-semibold text-secondary uppercase tracking-wide">Change Password</h3>
            <FormField label="Current Password" error={form.formState.errors.oldPassword?.message}>
                <Input
                    type="password"
                    placeholder="••••••••"
                    autoComplete="current-password"
                    hasError={!!form.formState.errors.oldPassword}
                    {...form.register("oldPassword")}
                />
            </FormField>
            <FormField label="New Password" error={form.formState.errors.newPassword?.message}>
                <Input
                    type="password"
                    placeholder="••••••••"
                    autoComplete="new-password"
                    hasError={!!form.formState.errors.newPassword}
                    {...form.register("newPassword")}
                />
            </FormField>
            <FormField label="Confirm New Password" error={form.formState.errors.confirmPassword?.message}>
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
                <p className="text-sm text-green-600">Password updated successfully.</p>
            )}
            <Button type="submit" disabled={form.formState.isSubmitting}>
                {form.formState.isSubmitting ? "Saving..." : "Update Password"}
            </Button>
        </form>
    );
}

export default function EditProfileForm({ user, onCancel }) {
    return (
        <div className="mt-6 border-t border-gray-100 pt-4 space-y-8">
            <EditEmailForm user={user} />
            <div className="border-t border-gray-100 pt-4">
                <EditPasswordForm />
            </div>
            <Button type="button" variant="outline" onClick={onCancel}>
                Close
            </Button>
        </div>
    );
}