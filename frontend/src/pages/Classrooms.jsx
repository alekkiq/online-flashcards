import { useState } from "react";
import { useNavigate, useSearchParams } from "react-router";
import { useAuth } from "../hooks/useAuth";
import { useClassroomSearch } from "../hooks/useClassroomSearch";
import { useSubjects } from "../hooks/useSubjects";
import { Searchbar } from "../components/ui/Searchbar";
import ClassroomCard from "../components/ui/ClassroomCard";
import { NewItemCard } from "../components/ui/NewItemCard";
import { Modal } from "../components/ui/Modal";
import JoinClassroomForm from "../components/classroom/JoinClassroomForm";
import { DoorOpen } from "lucide-react";
import { useTranslation } from "react-i18next";

export default function Classrooms() {
  const navigate = useNavigate();
  const { isTeacher } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  const [selectedSubject, setSelectedSubject] = useState(null);
  const [joinModalOpen, setJoinModalOpen] = useState(false);
  const { subjects } = useSubjects();
  const { t } = useTranslation();
  const { searchQuery, setSearchQuery, filteredClassrooms } = useClassroomSearch(
    searchParams,
    setSearchParams,
    selectedSubject
  );

  const subjectFilters = [
    { label: t("classrooms.all"), onClick: () => setSelectedSubject(null), active: !selectedSubject },
    ...subjects.map((subject) => ({
      label: subject.name,
      onClick: () => setSelectedSubject(subject.name),
      active: selectedSubject === subject.name,
    })),
  ];

  return (
    <div className="max-w-7xl mx-auto py-8 space-y-6 md:space-y-8">
      <div className="mt-8">
        <div className="mb-6">
          <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">
            {t("classrooms.title")}
          </h1>
          <p className="text-secondary">
            {isTeacher ? t("classrooms.subtitleTeacher") : t("classrooms.subtitleStudent")}
          </p>
        </div>

        <Searchbar
          value={searchQuery}
          onChange={setSearchQuery}
          placeholder={t("classrooms.searchPlaceholder")}
          filters={subjectFilters}
          filterTriggerLabel={t("classrooms.subject")}
          activeFilterLabel={selectedSubject}
        />

        {searchQuery && (
          <p className="text-main font-bold mb-6">{t("classrooms.searchResultsFor", { query: searchQuery })}</p>
        )}

        <div className="flex flex-col gap-4 sm:grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {isTeacher && (
            <NewItemCard
              onClick={() => navigate("/classrooms/create")}
              title={t("classrooms.newClassroom")}
              subtitle={t("classrooms.createNewClassroom")}
            />
          )}
          {!isTeacher && (
            <NewItemCard
              onClick={() => setJoinModalOpen(true)}
              title={t("classrooms.joinClassroom")}
              subtitle={t("classrooms.enterJoinCode")}
              icon={
                <DoorOpen className="w-12 h-12 text-gray-400 group-hover:text-gray-600 transition-colors" />
              }
            />
          )}
          {filteredClassrooms.map((classroom) => (
            <ClassroomCard
              key={classroom.id}
              classroom={classroom}
              onClick={() => navigate(`/classrooms/${classroom.id}`)}
            />
          ))}
        </div>

        {filteredClassrooms.length === 0 && searchQuery && (
          <p className="text-secondary text-center py-8">{t("classrooms.noClassroomsFound")}</p>
        )}
      </div>

      <Modal open={joinModalOpen} onClose={() => setJoinModalOpen(false)} title={t("classrooms.joinModalTitle")}>
        <p className="text-sm text-secondary mb-4">{t("classrooms.joinModalDescription")}</p>
        <JoinClassroomForm />
      </Modal>
    </div>
  );
}
