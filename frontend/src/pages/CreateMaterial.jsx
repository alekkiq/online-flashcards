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

export default function CreateMaterial() {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const classroomId = searchParams.get("classroomId");
  const {
    handleCreateQuiz,
    handleUpdateQuiz,
    quizzes,
    isLoading: isMaterialLoading,
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
    resolver: zodResolver(quizSchema),
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
    if (isEditMode && !isMaterialLoading) {
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
  }, [isEditMode, id, quizzes, isMaterialLoading, reset]);

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
      <BackLink label="Back to classroom" />
      <div className="mb-8 text-center md:text-left">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">
          {isEditMode ? "Edit Quiz" : classroomId ? "Add Learning Material to Classroom" : "Create New Quiz"}
        </h1>
        <p className="text-secondary text-lg">Build custom learning materials</p>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-secondary/10">
        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
          <div className="p-6 md:p-8 flex flex-col gap-6 border-b border-secondary/10">
            <h2 className="font-bold text-lg text-main">General Information</h2>

            <FormField label="Title" error={errors.title?.message}>
              <Input
                id="title"
                placeholder="Enter material title"
                hasError={!!errors.title}
                {...register("title")}
              />
            </FormField>

            <FormField label="Description" error={errors.description?.message}>
              <textarea
                id="description"
                {...register("description")}
                className="flex min-h-[120px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none"
                placeholder="Enter description (optional)"
              />
            </FormField>

            <FormField label="Subject" error={errors.subject?.message}>
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

        </form>
      </div>
    </div>
  );
}
