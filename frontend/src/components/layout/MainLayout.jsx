import { Outlet } from "react-router";
import NavBar from "./NavBar";
import { QuizProvider } from "../../context/QuizContext";

export default function MainLayout() {
  return (
    <QuizProvider>
      <div className="min-h-screen">
        <NavBar />
        <main className="w-full max-w-7xl mx-auto px-4">
          <Outlet />
        </main>
      </div>
    </QuizProvider>
  );
}
