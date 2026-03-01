import { useNavigate, useParams } from "react-router";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";
import { FormField } from "../components/ui/FormField";
import { SubjectSelect } from "../components/ui/SubjectSelect";
import { classroomSchema, classroomUpdateSchema } from "/src/lib/schemas";
import { createClassroom, updateClassroom, getClassroomById } from "/src/api";
import { useState, useEffect } from "react";
import { PageLoader } from "../components/ui/PageLoader";
import { useClassroomContext } from "../hooks/useClassroomContext";

export default function CreateClassroom() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { fetchClassrooms } = useClassroomContext();
  const isEditMode = !!id;
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isLoading, setIsLoading] = useState(isEditMode);
  const [submitError, setSubmitError] = useState(null);

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm({
    resolver: zodResolver(isEditMode ? classroomUpdateSchema : classroomSchema),
    defaultValues: {
      title: "",
      description: "",
      note: "",
      joinCode: "",
      subjectId: null,
    },
  });

  useEffect(() => {
    if (!isEditMode) return;
    async function fetchClassroom() {
      setIsLoading(true);
      const response = await getClassroomById(id);
      if (response.success) {
        const c = response.data.data;
        reset({
          title: c.title || "",
          description: c.description || "",
          note: c.note || "",
        });
      }
      setIsLoading(false);
    }
    fetchClassroom();
  }, [id, isEditMode, reset]);

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    setSubmitError(null);
    try {
      if (isEditMode) {
        const payload = {
          title: data.title,
          description: data.description || null,
          note: data.note || null,
        };
        const response = await updateClassroom(id, payload);
        if (!response.success) {
          setSubmitError(response.error);
          return;
        }
        await fetchClassrooms(true);
        navigate(`/classrooms/${id}`);
      } else {
        const payload = {
          title: data.title,
          description: data.description || null,
          note: data.note || null,
          joinCode: data.joinCode || null,
          subjectId: data.subjectId,
        };
        const response = await createClassroom(payload);
        if (!response.success) {
          setSubmitError(response.error);
          return;
        }
        await fetchClassrooms(true);
        navigate(`/classrooms/${response.data.data.id}`);
      }
    } catch (error) {
      console.error("Failed to save classroom:", error);
      setSubmitError("Something went wrong. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) return <PageLoader />;

  return (
    <div className="max-w-5xl mx-auto py-8 px-4 md:px-0">
      <div className="mb-8 text-center md:text-left">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">
          {isEditMode ? "Edit Classroom" : "Create New Classroom"}
        </h1>
        <p className="text-secondary text-lg">
          {isEditMode ? "Update your classroom details" : "Set up a classroom for your students"}
        </p>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-secondary/10">
        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
          <div className="p-6 md:p-8 flex flex-col gap-6 border-b border-secondary/10">
            <h2 className="font-bold text-lg text-main">General Information</h2>

            <FormField label="Title" error={errors.title?.message}>
              <Input
                id="title"
                placeholder="Enter classroom title"
                hasError={!!errors.title}
                {...register("title")}
              />
            </FormField>

            <FormField label="Description" error={errors.description?.message}>
              <textarea
                id="description"
                {...register("description")}
                className="flex min-h-[120px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none"
                placeholder="Describe your classroom (optional)"
              />
            </FormField>

            {!isEditMode && (
              <FormField label="Subject" error={errors.subjectId?.message}>
                <Controller
                  name="subjectId"
                  control={control}
                  render={({ field }) => (
                    <SubjectSelect
                      value={field.value}
                      onChange={field.onChange}
                      valueKey="subjectId"
                      hasError={!!errors.subjectId}
                    />
                  )}
                />
              </FormField>
            )}
          </div>

          <div className="p-6 md:p-8 flex flex-col gap-6 border-b border-secondary/10">
            <h2 className="font-bold text-lg text-main">Settings</h2>

            <FormField label="Announcement" error={errors.note?.message}>
              <textarea
                id="note"
                {...register("note")}
                className="flex min-h-[80px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none"
                placeholder="Pin an announcement for your students (optional)"
              />
            </FormField>

            {!isEditMode && (
              <FormField
                label="Custom Join Code"
                error={errors.joinCode?.message}
                hint="At least 6 characters. Leave blank to auto-generate."
              >
                <Input
                  id="joinCode"
                  placeholder="e.g. BIO2026"
                  hasError={!!errors.joinCode}
                  {...register("joinCode")}
                />
              </FormField>
            )}
          </div>

          {submitError && (
            <div className="px-6 md:px-8 pt-4">
              <p className="text-sm text-destructive">{submitError}</p>
            </div>
          )}

          <div className="p-6 md:p-8 flex gap-4 bg-gray-50/50 rounded-b-2xl border-t border-secondary/10">
            <Button
              type="button"
              variant="outline"
              className="flex-1"
              onClick={() => navigate(isEditMode ? `/classrooms/${id}` : "/classrooms")}
            >
              Cancel
            </Button>
            <Button type="submit" className="flex-1" disabled={isSubmitting}>
              {isSubmitting ? "Saving..." : isEditMode ? "Save Changes" : "Create Classroom"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}

