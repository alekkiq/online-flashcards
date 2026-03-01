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

export default function Classrooms() {
  const navigate = useNavigate();
  const { isTeacher } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  const [selectedSubject, setSelectedSubject] = useState(null);
  const [joinModalOpen, setJoinModalOpen] = useState(false);
  const { subjects } = useSubjects();
  const { searchQuery, setSearchQuery, filteredClassrooms } = useClassroomSearch(
    searchParams,
    setSearchParams,
    selectedSubject
  );

  const subjectFilters = [
    { label: "All", onClick: () => setSelectedSubject(null), active: !selectedSubject },
    ...subjects.map((subject) => ({
      label: subject.name,
      onClick: () => setSelectedSubject(subject.name),
      active: selectedSubject === subject.name,
    })),
  ];

  return (
    <div className="max-w-5xl mx-auto py-8 space-y-6 md:space-y-8">
      <div className="mt-8">
        <div className="mb-6">
          <h1 className="font-serif text-4xl md:text-5xl font-bold text-main mb-2">
            My Classrooms
          </h1>
          <p className="text-secondary">
            {isTeacher ? "Manage your classrooms." : "Classrooms you are a member of."}
          </p>
        </div>

        <Searchbar
          value={searchQuery}
          onChange={setSearchQuery}
          placeholder="Search your classrooms..."
          filters={subjectFilters}
          filterTriggerLabel="Subject"
          activeFilterLabel={selectedSubject}
        />

        {searchQuery && (
          <p className="text-main font-bold mb-6">Search results for "{searchQuery}":</p>
        )}

        <div className="flex flex-col gap-4 sm:grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {isTeacher && (
            <NewItemCard
              onClick={() => navigate("/classrooms/create")}
              title="New Classroom"
              subtitle="Create a new classroom"
            />
          )}
          {!isTeacher && (
            <NewItemCard
              onClick={() => setJoinModalOpen(true)}
              title="Join Classroom"
              subtitle="Enter a join code"
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
          <p className="text-secondary text-center py-8">No classrooms found.</p>
        )}
      </div>

      <Modal open={joinModalOpen} onClose={() => setJoinModalOpen(false)} title="Join a Classroom">
        <p className="text-sm text-secondary mb-4">Enter the join code your teacher provided.</p>
        <JoinClassroomForm />
      </Modal>
    </div>
  );
}
