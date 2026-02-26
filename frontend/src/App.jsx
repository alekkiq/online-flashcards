import { lazy, Suspense } from "react";
import { BrowserRouter, Routes, Route } from "react-router";
import MainLayout from "/src/components/layout/MainLayout";
import { PageLoader } from "/src/components/ui/PageLoader";
import { AuthProvider } from "/src/context/AuthContext";
import ProtectedRoute from "/src/components/auth/ProtectedRoute";

const Home = lazy(() => import("/src/pages/Home"));
const SearchQuizzes = lazy(() => import("/src/pages/SearchQuizzes"));
const Login = lazy(() => import("/src/pages/Login"));
const Profile = lazy(() => import("/src/pages/Profile"));
const MyQuizzes = lazy(() => import("/src/pages/MyQuizzes"));
const QuizDetails = lazy(() => import("/src/pages/QuizDetails"));
const QuizGame = lazy(() => import("/src/pages/QuizGame"));
const ResultsPage = lazy(() => import("/src/pages/ResultsPage"));
const CreateQuiz = lazy(() => import("/src/pages/CreateQuiz"));
const SearchClassrooms = lazy(() => import("/src/pages/SearchClassrooms"));

function App() {
  return (
    <>
      <BrowserRouter>
        <AuthProvider>
          <Suspense fallback={<PageLoader />}>
            <Routes>
              <Route path="/" element={<MainLayout />}>
                <Route index element={<Home />} />
                <Route path="search" element={<SearchQuizzes />} />
                <Route path="login" element={<Login />} />
                <Route path="profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
                <Route path="my-quizzes" element={<ProtectedRoute><MyQuizzes /></ProtectedRoute>} />
                <Route path="my-quizzes/create" element={<ProtectedRoute><CreateQuiz /></ProtectedRoute>} />
                <Route path="my-quizzes/details/:id" element={<ProtectedRoute><CreateQuiz /></ProtectedRoute>} />
                <Route path="quiz-details/:id" element={<QuizDetails />} />
                <Route path="quiz/:id" element={<QuizGame />} />
                <Route path="quiz/results" element={<ResultsPage />} />
                <Route path="classrooms" element={<SearchClassrooms />} />
              </Route>
            </Routes>
          </Suspense>
        </AuthProvider>
      </BrowserRouter>
    </>
  );
}

export default App;