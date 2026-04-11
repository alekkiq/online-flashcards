import { useNavigate, useSearchParams } from "react-router";
import { useState } from "react";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";
import { FormField } from "../components/ui/FormField";
import { BackLink } from "../components/ui/BackLink";
import { useAuth } from "../hooks/useAuth";
import { addLearningMaterial } from "/src/api";
import { useTranslation } from "react-i18next";

export default function CreateMaterial() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const classroomId = searchParams.get("classroomId");
  const { isTeacher } = useAuth();
  const { t } = useTranslation();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);

  if (!isTeacher) {
    return (
      <div className="max-w-5xl mx-auto py-8 px-4 md:px-0 text-center">
        <h1 className="font-serif text-3xl font-bold text-main mb-4">{t("createMaterial.unauthorized")}</h1>
        <p className="text-secondary">{t("createMaterial.onlyTeachers")}</p>
        <Button variant="outline" className="mt-4" onClick={() => navigate(-1)}>
          {t("createMaterial.goBack")}
        </Button>
      </div>
    );
  }

  if (!classroomId) {
    return (
      <div className="max-w-5xl mx-auto py-8 px-4 md:px-0 text-center">
        <h1 className="font-serif text-3xl font-bold text-main mb-4">{t("createMaterial.missingClassroom")}</h1>
        <p className="text-secondary">{t("createMaterial.classroomRequired")}</p>
        <Button variant="outline" className="mt-4" onClick={() => navigate("/classrooms")}>
          {t("createMaterial.goToClassrooms")}
        </Button>
      </div>
    );
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!title.trim()) {
      setError(t("createMaterial.titleRequired"));
      return;
    }
    if (!content.trim()) {
      setError(t("createMaterial.contentRequired"));
      return;
    }

    setIsSubmitting(true);
    setError(null);

    try {
      const response = await addLearningMaterial(classroomId, {
        title: title.trim(),
        content: content.trim(),
      });

      if (response.success) {
        navigate(`/classrooms/${classroomId}`);
      } else {
        setError(response.error || t("createMaterial.failedToCreate"));
      }
    } catch (err) {
      console.error("Failed to create learning material:", err);
      setError(t("createMaterial.unexpectedError"));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="max-w-5xl mx-auto py-8 px-4 md:px-0">
      <BackLink label={t("createMaterial.backToClassroom")} to={`/classrooms/${classroomId}`} />
      <div className="mb-8 text-center md:text-start">
        <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">
          {t("createMaterial.title")}
        </h1>
        <p className="text-secondary text-lg">{t("createMaterial.subtitle")}</p>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-secondary/10">
        <form onSubmit={handleSubmit} className="flex flex-col">
          <div className="p-6 md:p-8 flex flex-col gap-6">
            <h2 className="font-bold text-lg text-main">{t("createMaterial.materialDetails")}</h2>

            {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 rounded-xl px-4 py-3 text-sm">
                {error}
              </div>
            )}

            <FormField label={t("createMaterial.titleLabel")}>
              <Input
                id="title"
                placeholder={t("createMaterial.titlePlaceholder")}
                value={title}
                onChange={(e) => setTitle(e.target.value)}
              />
            </FormField>

            <FormField label={t("createMaterial.contentLabel")}>
              <textarea
                id="content"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                className="flex min-h-[250px] w-full rounded-xl border border-secondary/30 bg-white p-3 text-sm text-main transition-colors placeholder:text-secondary/60 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50 resize-none"
                placeholder={t("createMaterial.contentPlaceholder")}
              />
            </FormField>
          </div>

          <div className="flex justify-end gap-3 p-6 md:p-8 border-t border-secondary/10">
            <Button
              type="button"
              variant="outline"
              onClick={() => navigate(`/classrooms/${classroomId}`)}
            >
              {t("createMaterial.cancel")}
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? t("createMaterial.creating") : t("createMaterial.createMaterial")}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
