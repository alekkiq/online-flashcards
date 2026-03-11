import { Outlet } from "react-router";
import NavBar from "./NavBar";
import { QuizProvider } from "/src/context/QuizContext";
import { ClassroomProvider } from "/src/context/ClassroomContext";
import { SubjectProvider } from "/src/context/SubjectContext";
import { useAuth } from "/src/hooks/useAuth";
import { useEffect } from "react";
import Footer from "./Footer";

export default function MainLayout() {
  const { handleAutoLogin } = useAuth();

  useEffect(() => {
    let isMounted = true;

    const login = async () => {
      try {
        await handleAutoLogin();
      } catch (error) {
        console.error(error);
      }
    };

    login();

    return () => { isMounted = false; };
  }, []);

  return (
    <SubjectProvider>
      <QuizProvider>
        <ClassroomProvider>
          <div className="min-h-screen">
            <NavBar />
            <main className="w-full min-h-[70vh] px-4 mb-[10vh] md:mb-0">
              <Outlet />
            </main>
            <Footer />
          </div>
        </ClassroomProvider>
      </QuizProvider>
    </SubjectProvider>
  );
}
