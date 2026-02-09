import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { registerSchema } from "@/lib/authSchemas";
import { useRegister } from "@/hooks/useRegister";
import { FormField } from "@/components/ui/FormField";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";

export default function RegisterForm() {
  const { submit, isLoading, error } = useRegister();

  const form = useForm({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      repeatPassword: "",
      isTeacher: false,
      organizationName: "",
    },
    mode: "onSubmit",
  });

  const isTeacher = form.watch("isTeacher");

  // tyhjennä org-kenttä jos ei teacher
  useEffect(() => {
    if (!isTeacher) form.setValue("organizationName", "");
  }, [isTeacher, form]);

  async function onSubmit(values) {
    await submit(values);
  }

  return (
    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid grid-cols-2 gap-3">
        <FormField label="First name *">
          <Input placeholder="First name" {...form.register("firstName")} />
          {form.formState.errors.firstName && (
            <p className="text-xs text-red-500 mt-1">
              {form.formState.errors.firstName.message}
            </p>
          )}
        </FormField>

        <FormField label="Last name *">
          <Input placeholder="Last name" {...form.register("lastName")} />
          {form.formState.errors.lastName && (
            <p className="text-xs text-red-500 mt-1">
              {form.formState.errors.lastName.message}
            </p>
          )}
        </FormField>
      </div>

      <FormField label="Email address *">
        <Input type="email" placeholder="Email address" {...form.register("email")} />
        {form.formState.errors.email && (
          <p className="text-xs text-red-500 mt-1">
            {form.formState.errors.email.message}
          </p>
        )}
      </FormField>

      <FormField label="Password *">
        <Input type="password" placeholder="Password" {...form.register("password")} />
        {form.formState.errors.password && (
          <p className="text-xs text-red-500 mt-1">
            {form.formState.errors.password.message}
          </p>
        )}
      </FormField>

      <FormField label="Repeat password *">
        <Input
          type="password"
          placeholder="Repeat password"
          {...form.register("repeatPassword")}
        />
        {form.formState.errors.repeatPassword && (
          <p className="text-xs text-red-500 mt-1">
            {form.formState.errors.repeatPassword.message}
          </p>
        )}
      </FormField>

      <label className="flex items-center gap-2 text-xs text-[var(--color-secondary)]">
        <input type="checkbox" {...form.register("isTeacher")} />
        Are you a teacher?
      </label>

      {isTeacher && (
        <div className="space-y-2">
          <FormField label="Organization name *">
            <Input placeholder="Organization name" {...form.register("organizationName")} />
            {form.formState.errors.organizationName && (
              <p className="text-xs text-red-500 mt-1">
                {form.formState.errors.organizationName.message}
              </p>
            )}
          </FormField>

          <p className="text-[11px] text-gray-500">
            We review teacher accounts before first sign in.
          </p>
        </div>
      )}

      {error && <p className="text-sm text-red-600">{error}</p>}

      <Button type="submit" className="w-full h-11" disabled={isLoading}>
        {isLoading ? "Creating account..." : "Sign Up"}
      </Button>
    </form>
  );
}