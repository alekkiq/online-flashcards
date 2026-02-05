import { useState } from "react";
import { useSearchParams } from "react-router";
import LoginForm from "../components/auth/LoginForm";

export default function Login() {
  const [searchParams] = useSearchParams();
  const [isLogin, setIsLogin] = useState(true);
  const isSignup = searchParams.get("signup");

  //TODO: add form for signup and login, use the ui componetns from components/ui folder. Try to make it look 1:1 with figma and add all the functionality. Use state to toggle between login and signup. To the left add grid tailwind class so you get the nice look (fouund in index.css).
  return (
    <div>
      <h1>{isSignup || !isLogin ? "Sign Up" : "Login"}</h1>
      <LoginForm />
    </div>
  );
}
