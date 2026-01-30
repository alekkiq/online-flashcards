import { BrowserRouter, Routes, Route } from "react-router";
import MainLayout from "/src/components/layout/MainLayout";
import Home from "/src/pages/Home";
import SearchQuizzes from "/src/pages/SearchQuizzes";

function App() {
  return <>
    <BrowserRouter>
      <Routes>
        {/* public routes */}
        <Route path="/" element={<MainLayout />}>
          <Route index element={<Home />} />
          <Route path="search" element={<SearchQuizzes />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </>;
}

export default App;
