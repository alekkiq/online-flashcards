import { useNavigate, useParams, useSearchParams } from "react-router";
import { useForm, useFieldArray, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";
import { FormField } from "../components/ui/FormField";
import { FlashcardInput } from "../components/ui/FlashcardInput";
import { SubjectSelect } from "../components/ui/SubjectSelect";
import { useMyQuizzes } from "../hooks/useMyQuizzes";
import { useState, useEffect } from "react";
import { PageLoader } from "../components/ui/PageLoader";
import { BackLink } from "../components/ui/BackLink";
import { quizSchema } from "/src/lib/schemas";
import { addQuizToClassroom } from "/src/api";
import { useTranslation } from "react-i18next";

export default function CreateQuiz() {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const classroomId = searchParams.get("classroomId");
  const { t } = useTranslation();
  const {
    handleCreateQuiz,
    handleUpdateQuiz,
    quizzes,
    isLoading: isQuizzesLoading,
  } = useMyQuizzes();
  const isEditMode = !!id;
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm({
    resolver: zodResolver(quizSchema(t)),
    defaultValues: {
      title: "",
      description: "",
      subject: "",
      cards: [
        { question: "", answer: "" },
        { question: "", answer: "" },
      ],
    },
  });

  const { fields, append, remove } = useFieldArray({
    control,
    name: "cards",
  });

  useEffect(() => {
    if (isEditMode && !isQuizzesLoading) {
      const quizToEdit = quizzes.find((q) => q.quizId === parseInt(id));
      console.log(quizToEdit);
      if (quizToEdit) {
        reset({
          title: quizToEdit.title,
          description: quizToEdit.description || "",
          subject: quizToEdit.subjectName || "",
          cards: quizToEdit.flashcards || [{ question: "", answer: "" }],
        });
      }
    }
  }, [isEditMode, id, quizzes, isQuizzesLoading, reset]);

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      if (isEditMode) {
        await handleUpdateQuiz(id, data);
        navigate("/my-quizzes");
      } else {
        const created = await handleCreateQuiz(data);
        if (classroomId && created?.quizId) {
          await addQuizToClassroom(classroomId, created.quizId);
          navigate(`/classrooms/${classroomId}`);
        } else {
          navigate("/my-quizzes");
        }
      }
    } catch (error) {
      console.error("Failed to save quiz:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isEditMode && isQuizzesLoading) {
    return <PageLoader />;
  }

  return (
    <div className="max-w-5xl mx-auto py-8 px-4 md:px-0">
      <BackLink label={classroomId ? t("createQuiz.backToClassroom") : t("createQuiz.backToMyQuizzes")}/>
      <div className="mb-8 text-center md:text-start">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">
          {isEditMode ? t("createQuiz.editQuiz") : classroomId ? t("createQuiz.addQuizToClassroom") : t("createQuiz.createNewQuiz")}
        </h1>
        <p className="text-secondary text-lg">{t("createQuiz.subtitle")}</p>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-secondary/10">
        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
          <div className="p-6 md:p-8 flex flex-col gap-6 border-b border-secondary/10">
            <h2 className="font-bold text-lg text-main">{t("createQuiz.generalInfo")}</h2>

            <FormField label={t("createQuiz.title")} error={errors.title?.message}>
              <Input
                id="title"
                placeholder={t("createQuiz.titlePlaceholder")}
                hasError={!!errors.title}
                {...register("title")}
              />
            </FormField>

            <FormField label={t("createQuiz.description")} error={errors.description?.message}>
              <textarea
                id="description"
                {...register("description")}
                className="flex min-h-[120px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none"
                placeholder={t("createQuiz.descriptionPlaceholder")}
              />
            </FormField>

            <FormField label={t("createQuiz.subject")} error={errors.subject?.message}>
              <Controller
                name="subject"
                control={control}
                render={({ field }) => (
                  <SubjectSelect
                    value={field.value}
                    onChange={field.onChange}
                    hasError={!!errors.subject}
                  />
                )}
              />
            </FormField>
          </div>

          <div className="p-6 md:p-8 flex flex-col gap-6">
            <h2 className="font-bold text-lg text-main">{t("createQuiz.cards")}</h2>

            <div className="flex flex-col">
              {fields.map((field, index) => (
                <FlashcardInput
                  key={field.id}
                  index={index}
                  register={register}
                  errors={errors}
                  onRemove={() => remove(index)}
                />
              ))}
            </div>

            <Button
              type="button"
              variant="outline"
              onClick={() => append({ question: "", answer: "" })}
              className="w-full py-4 border-2 border-dashed border-gray-200 text-gray-400 hover:border-primary hover:text-primary hover:bg-primary/5 transition-all"
            >
              {t("createQuiz.addCard")}
            </Button>
          </div>

          <div className="p-6 md:p-8 flex gap-4 bg-gray-50/50 rounded-b-2xl border-t border-secondary/10">
            <Button
              type="button"
              variant="outline"
              className="flex-1"
              onClick={() => navigate(classroomId ? `/classrooms/${classroomId}` : "/my-quizzes")}
            >
              {t("createQuiz.cancel")}
            </Button>
            <Button type="submit" className="flex-1" disabled={isSubmitting}>
              {isSubmitting ? t("createQuiz.saving") : t("createQuiz.saveQuiz")}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
