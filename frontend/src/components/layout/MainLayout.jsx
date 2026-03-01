import { Outlet } from "react-router";
import NavBar from "./NavBar";
import { QuizProvider } from "/src/context/QuizContext";
import { ClassroomProvider } from "/src/context/ClassroomContext";
import { SubjectProvider } from "/src/context/SubjectContext";
import { useAuth } from "/src/hooks/useAuth";
import { useEffect } from "react";

export default function MainLayout() {
  const { user, handleAutoLogin } = useAuth();

  useEffect(() => {
    handleAutoLogin();
  }, []);

  return (
    <SubjectProvider>
      <QuizProvider>
        <ClassroomProvider>
          <div className="min-h-screen">
            <NavBar />
            <main className="w-full mx-auto px-4 mb-[10vh] md:mb-0">
              <Outlet />
            </main>
          </div>
        </ClassroomProvider>
      </QuizProvider>
    </SubjectProvider>
  );
}
