import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { joinClassroomSchema } from "/src/lib/schemas";
import { FormField } from "/src/components/ui/FormField";
import { Input } from "/src/components/ui/Input";
import { Button } from "/src/components/ui/Button";
import { DoorOpen, LockKeyholeOpen } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router";
import { joinClassroomByCode } from "/src/api";
import { useClassroomContext } from "/src/hooks/useClassroomContext";

export default function JoinClassroomForm() {
  const navigate = useNavigate();
  const { fetchClassrooms } = useClassroomContext();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const form = useForm({
    resolver: zodResolver(joinClassroomSchema),
    defaultValues: { joinCode: "" },
    mode: "onSubmit",
  });

  async function onSubmit(values) {
    setIsLoading(true);
    setError(null);
    try {
      const response = await joinClassroomByCode(values.joinCode);
      if (!response.success) {
        setError(response.error || "Failed to join classroom");
        return;
      }
      await fetchClassrooms(true);
      navigate(`/classrooms/${response.data.data.id}`);
    } catch (err) {
      setError(err.message || "Failed to join classroom");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <div className="flex gap-2 items-center flex-wrap">
          <FormField error={form.formState.errors.joinCode?.message} className="flex-1">
            <Input
              type="text"
              placeholder="Enter classroom join code"
              hasError={!!form.formState.errors.joinCode}
              startIcon={<DoorOpen size={18} />}
              className="h-11"
              {...form.register("joinCode")}
            />
          </FormField>

          <Button type="submit" className="h-11" disabled={isLoading}>
              <LockKeyholeOpen size={16} />
            {isLoading ? "Joining..." : "Join Classroom"}
          </Button>
        </div>

      {error && <p className="text-sm text-destructive">{error}</p>}
    </form>
  );
}

