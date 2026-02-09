import { useNavigate, useParams } from "react-router";
import { useForm, useFieldArray } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";
import { Label } from "../components/ui/Label";
import { FlashcardInput } from "../components/ui/FlashcardInput";
import { useMyQuizzes } from "../hooks/useMyQuizzes";
import { useState, useEffect } from "react";
import { PageLoader } from "../components/ui/PageLoader";
import { quizSchema } from "/src/lib/schemas";

export default function CreateQuiz() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { handleCreateQuiz, handleUpdateQuiz, quizzes, isLoading: isQuizzesLoading } = useMyQuizzes();
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
      cards: [{ question: "", answer: "" }, { question: "", answer: "" }],
    },
  });

  const { fields, append, remove } = useFieldArray({
    control,
    name: "cards",
  });

  useEffect(() => {
    if (isEditMode && !isQuizzesLoading) {
      const quizToEdit = quizzes.find((q) => q.id === parseInt(id));
      if (quizToEdit) {
        reset({
          title: quizToEdit.title,
          description: quizToEdit.description || "",
          cards: quizToEdit.cards || [{ question: "", answer: "" }],
        });
      }
    }
  }, [isEditMode, id, quizzes, isQuizzesLoading, reset]);


  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      if (isEditMode) {
        await handleUpdateQuiz(id, data);
      } else {
        await handleCreateQuiz(data);
      }
      navigate("/my-quizzes");
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
        <div className="mb-8 text-center md:text-left">
            <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">
            {isEditMode ? "Edit Quiz" : "Create New Quiz"}
            </h1>
            <p className="text-secondary text-lg">Build a custom flashcard set</p>
        </div>
        
        <div className="bg-white rounded-2xl shadow-sm border border-secondary/10">
            <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
                <div className="p-6 md:p-8 flex flex-col gap-6 border-b border-secondary/10">
                    <h2 className="font-bold text-lg text-main">General Information</h2>
                    
                    <div className="flex flex-col gap-2">
                        <Label htmlFor="title">Title</Label>
                        <Input
                        id="title"
                        {...register("title")}
                        placeholder="Enter quiz title"
                        hasError={!!errors.title}
                        />
                        {errors.title && <p className="text-sm font-medium text-destructive">{errors.title.message}</p>}
                    </div>

                    <div className="flex flex-col gap-2">
                        <Label htmlFor="description">Description</Label>
                        <textarea
                        id="description"
                        {...register("description")}
                        className="flex min-h-[120px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none"
                        placeholder="Enter description (optional)"
                        />
                    </div>
                </div>

                <div className="p-6 md:p-8 flex flex-col gap-6">
                    <h2 className="font-bold text-lg text-primary">Cards</h2>
                    
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
                        + Add Card
                    </Button>
                </div>

                <div className="p-6 md:p-8 flex gap-4 bg-gray-50/50 rounded-b-2xl border-t border-secondary/10">
                    <Button
                        type="button"
                        variant="outline"
                        className="flex-1"
                        onClick={() => navigate("/my-quizzes")}
                    >
                        Cancel
                    </Button>
                    <Button type="submit" className="flex-1" disabled={isSubmitting}>
                        {isSubmitting ? "Saving..." : "Save Quiz"}
                    </Button>
                </div>
            </form>
        </div>
    </div>
  );
}
