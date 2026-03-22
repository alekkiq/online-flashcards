import { useParams, useNavigate } from "react-router";
import { useEffect, useState } from "react";
import { getClassroomById } from "/src/api";
import { PageLoader } from "../components/ui/PageLoader";
import { BackLink } from "../components/ui/BackLink";
import { Button } from "../components/ui/Button";
import { FileText, User } from "lucide-react";
import { useTranslation } from "react-i18next";

export default function MaterialView() {
  const { classroomId, materialId } = useParams();
  const navigate = useNavigate();
  const [material, setMaterial] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const { t } = useTranslation();

  useEffect(() => {
    const fetchMaterial = async () => {
      setIsLoading(true);
      try {
        const response = await getClassroomById(classroomId);
        if (response.success) {
          const classroom = response.data.data;
          const found = classroom.learningMaterials?.find(
            (m) => m.id === parseInt(materialId)
          );
          if (found) {
            setMaterial(found);
          } else {
            setError(t("materialView.materialNotFound"));
          }
        } else {
          setError(response.error || "Failed to load classroom.");
        }
      } catch (err) {
        console.error("Failed to fetch material:", err);
        setError(t("createMaterial.unexpectedError"));
      } finally {
        setIsLoading(false);
      }
    };

    fetchMaterial();
  }, [classroomId, materialId]);

  if (isLoading) return <PageLoader />;

  if (error || !material) {
    return (
      <div className="max-w-5xl mx-auto py-8 px-4 md:px-0 text-center">
        <p className="text-destructive">{error || t("materialView.materialNotFound")}</p>
        <Button variant="outline" className="mt-4" onClick={() => navigate(`/classrooms/${classroomId}`)}>
          {t("materialView.backToClassroomBtn")}
        </Button>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto py-8 px-4 md:px-0">
      <BackLink label={t("materialView.backToClassroom")} to={`/classrooms/${classroomId}`} />

      <article className="bg-white rounded-2xl mt-4">
        <div className="p-6 md:p-10">
          <div className="flex items-center gap-2 text-primary mb-4">
            <FileText size={20} />
            <span className="text-sm font-medium uppercase tracking-wide">{t("materialView.learningMaterial")}</span>
          </div>

          <h1 className="font-serif text-3xl md:text-4xl font-bold text-main mb-4">
            {material.title}
          </h1>

          <div className="flex items-center gap-2 text-sm text-secondary mb-8 pb-6 border-b border-secondary/10">
            <User size={14} />
            <span>{t("materialView.createdBy")} <span className="font-medium text-main">{material.creatorUsername}</span></span>
          </div>

          <div className="prose prose-sm max-w-none text-main leading-relaxed whitespace-pre-wrap">
            {material.content}
          </div>
        </div>
      </article>
    </div>
  );
}
