import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { promotionRequestSchema } from "/src/lib/schemas";
import { Button } from "/src/components/ui/Button";
import { FormField } from "/src/components/ui/FormField";
import { usePromotionRequests } from "/src/hooks/usePromotionRequests";

export default function PromotionRequestForm({ isOpen, onClose }) {
    const { error, submitError, submitSuccess, submitRequest } = usePromotionRequests();

    const form = useForm({
        resolver: zodResolver(promotionRequestSchema),
        defaultValues: { message: "" },
    });

    const onSubmit = async ({ message }) => {
        const response = await submitRequest(message);
        if (response?.success) {
            form.reset();
            onClose();
        }
    };

    if (!isOpen) return null;

    return (
        <form onSubmit={form.handleSubmit(onSubmit)} className="mt-4 space-y-4">
            <FormField
                label="Message (optional)"
                error={form.formState.errors.message?.message}
            >
                <textarea
                    {...form.register("message")}
                    rows={3}
                    placeholder="Tell us why you'd like to become a teacher…"
                    className="w-full rounded-lg border border-secondary/30 px-3 py-2 text-sm text-main placeholder:text-secondary/60 focus:outline-none focus:ring-2 focus:ring-primary/40 resize-none"
                />
            </FormField>

            {submitError && (
                <p className="text-sm text-red-500">{submitError}</p>
            )}
            {submitSuccess && (
                <p className="text-sm text-green-600">{submitSuccess}</p>
            )}
            {error && (
                <p className="text-sm text-red-500">{error}</p>
            )}

            <Button
                type="submit"
                disabled={form.formState.isSubmitting}
                className="w-full sm:w-auto"
            >
                {form.formState.isSubmitting ? "Submitting…" : "Submit Request"}
            </Button>
        </form>
    );
}