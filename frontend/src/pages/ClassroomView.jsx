import { useParams, useNavigate } from "react-router";
import { Button } from "../components/ui/Button";
import { PageLoader } from "../components/ui/PageLoader";
import ClassroomHeader from "../components/classroom/ClassroomHeader";
import QuizzesSection from "../components/classroom/QuizzesSection";
import MaterialsSection from "../components/classroom/MaterialsSection";
import MembersSection from "../components/classroom/MembersSection";
import { BackLink } from "../components/ui/BackLink";
import { useEffect } from "react";
import { useClassroom } from "../hooks/useClassroom";
import { useAuth } from "../hooks/useAuth";
import { useClassroomContext } from "../hooks/useClassroomContext";
import { removeUserFromClassroom, leaveClassroom } from "/src/api";

export default function ClassroomView() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAdmin } = useAuth();
  const { classroom, isLoading, error, fetchClassroom } = useClassroom();
  const { fetchClassrooms } = useClassroomContext();

  useEffect(() => {
    fetchClassroom(id);
  }, [id, fetchClassroom]);

  const handleRemoveUser = async (userId) => {
    const response = await removeUserFromClassroom(id, userId);
    if (response.success) {
      fetchClassroom(id);
    }
  };

  const handleLeave = async () => {
    const response = await leaveClassroom(id);
    if (response.success) {
      await fetchClassrooms(true);
      navigate("/classrooms");
    }
  };

  if (isLoading) return <PageLoader />;

  if (error || !classroom) {
    return (
      <div className="max-w-7xl mx-auto py-8 md:px-0 text-center">
        <p className="text-destructive">{error || "Classroom not found."}</p>
        <Button variant="outline" className="mt-4" onClick={() => navigate("/classrooms")}>
          Back to Classrooms
        </Button>
      </div>
    );
  }

  const isOwner = classroom.isOwner;

  return (
    <div className="max-w-7xl mx-auto py-8 md:px-0">
      <BackLink label="Back to classrooms" />
      <ClassroomHeader
        classroom={classroom}
        isOwner={isOwner}
        onManage={() => navigate(`/classrooms/${id}/edit`)}
        onLeave={handleLeave}
      />

      <MaterialsSection
        materials={classroom.learningMaterials}
        isOwner={isOwner}
        onAddMaterial={() => navigate(`/create-material?classroomId=${classroom.id}`)}
      />

      <QuizzesSection
        quizzes={classroom.quizzes}
        isOwner={isOwner}
        onAddQuiz={() => navigate(`/my-quizzes/create?classroomId=${classroom.id}`)}
        onQuizClick={(quizId) => navigate(`/quiz-details/${quizId}`)}
      />

      {(isOwner || isAdmin) && (
        <MembersSection
          ownerUsername={classroom.ownerUsername}
          users={classroom.users}
          userCount={classroom.userCount}
          isOwner={isOwner}
          onRemoveUser={handleRemoveUser}
        />
      )}
    </div>
  );
}
