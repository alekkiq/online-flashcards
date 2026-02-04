import { lazy, Suspense } from "react";
import { BrowserRouter, Routes, Route } from "react-router";
import MainLayout from "/src/components/layout/MainLayout";
import { PageLoader } from "/src/components/ui/PageLoader";
import { AuthProvider } from "/src/context/AuthContext";

const Home = lazy(() => import("/src/pages/Home"));
const SearchQuizzes = lazy(() => import("/src/pages/SearchQuizzes"));
const Login = lazy(() => import("/src/pages/Login"));
const Profile = lazy(() => import("/src/pages/Profile"));
const MyQuizzes = lazy(() => import("/src/pages/MyQuizzes"));
const QuizDetails = lazy(() => import("/src/pages/QuizDetails"));
const QuizGame = lazy(() => import("/src/pages/QuizGame"));

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
                <Route path="profile" element={<Profile />} />
                <Route path="my-quizzes" element={<MyQuizzes />} />
                <Route path="quiz-details/:id" element={<QuizDetails />} />
                <Route path="quiz/:id" element={<QuizGame />} />
              </Route>
            </Routes>
          </Suspense>
        </AuthProvider>
      </BrowserRouter>
    </>
  );
}

export default App;
