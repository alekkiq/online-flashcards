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
import { BackLink } from "../components/ui/BackLink";
import { useClassroomContext } from "../hooks/useClassroomContext";
import { useTranslation } from "react-i18next";

export default function CreateClassroom() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { fetchClassrooms } = useClassroomContext();
  const isEditMode = !!id;
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isLoading, setIsLoading] = useState(isEditMode);
  const [submitError, setSubmitError] = useState(null);
  const { t } = useTranslation();

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
      setSubmitError(t("createClassroom.somethingWentWrong"));
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) return <PageLoader />;

  return (
    <div className="max-w-5xl mx-auto py-8 px-4 md:px-0">
      <BackLink label={t("createClassroom.backToClassrooms")} />
      <div className="mb-8 text-center md:text-left">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">
          {isEditMode ? t("createClassroom.editClassroom") : t("createClassroom.createNewClassroom")}
        </h1>
        <p className="text-secondary text-lg">
          {isEditMode ? t("createClassroom.subtitleEdit") : t("createClassroom.subtitleCreate")}
        </p>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-secondary/10">
        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
          <div className="p-6 md:p-8 flex flex-col gap-6 border-b border-secondary/10">
            <h2 className="font-bold text-lg text-main">{t("createClassroom.generalInfo")}</h2>

            <FormField label={t("createClassroom.title")} error={errors.title?.message}>
              <Input
                id="title"
                placeholder={t("createClassroom.titlePlaceholder")}
                hasError={!!errors.title}
                {...register("title")}
              />
            </FormField>

            <FormField label={t("createClassroom.description")} error={errors.description?.message}>
              <textarea
                id="description"
                {...register("description")}
                className="flex min-h-[120px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none"
                placeholder={t("createClassroom.descriptionPlaceholder")}
              />
            </FormField>

            {!isEditMode && (
              <FormField label={t("createClassroom.subject")} error={errors.subjectId?.message}>
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
            <h2 className="font-bold text-lg text-main">{t("createClassroom.settings")}</h2>

            <FormField label={t("createClassroom.announcement")} error={errors.note?.message}>
              <textarea
                id="note"
                {...register("note")}
                className="flex min-h-[80px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none"
                placeholder={t("createClassroom.announcementPlaceholder")}
              />
            </FormField>

            {!isEditMode && (
              <FormField
                label={t("createClassroom.customJoinCode")}
                error={errors.joinCode?.message}
                hint={t("createClassroom.joinCodeHint")}
              >
                <Input
                  id="joinCode"
                  placeholder={t("createClassroom.joinCodePlaceholder")}
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
              {t("createClassroom.cancel")}
            </Button>
            <Button type="submit" className="flex-1" disabled={isSubmitting}>
              {isSubmitting ? t("createClassroom.saving") : isEditMode ? t("createClassroom.saveChanges") : t("createClassroom.createClassroom")}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
