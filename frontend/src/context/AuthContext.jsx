import { createContext, useState } from "react";
import { useNavigate, useLocation } from "react-router";
import { login, autoLogin, register } from "../api";

const AuthContext = createContext(null);

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  const handleRegister = async (credentials) => {
    setIsLoading(true);
    setError(null);
    const response = await register(credentials.username, credentials.password, credentials.email);
    setIsLoading(false);
    if (!response.success) {
      setError(response.error);
      return response;
    }
    const data = response.data.data;
    setUser({
      id: data.userId,
      username: data.username,
      role: data.role,
    });
    localStorage.setItem("token", data.token);
    navigate("/");
    return { success: true };
  };

  const handleLogin = async (credentials) => {
    setIsLoading(true);
    setError(null);
    const response = await login(credentials.username, credentials.password);
    setIsLoading(false);
    if (!response.success) {
      setError(response.error);
      return response;
    }

    const data = response.data.data;

    setUser({
      id: data.userId,
      username: data.username,
      role: data.role,
    });
    localStorage.setItem("token", data.token);

    navigate("/");
    return { success: true };
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    setUser(null);
    navigate("/");
  };

  const handleAutoLogin = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      handleLogout();
      return;
    }
    const response = await autoLogin(token);
    if (!response.success) {
      handleLogout();
      return;
    }
    const data = response.data.data;
    setUser({
      id: data.userId,
      username: data.username,
      role: data.role,
    });
    navigate(location.pathname);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        setUser,
        handleLogin,
        handleLogout,
        handleAutoLogin,
        handleRegister,
        isLoading,
        error,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthProvider, AuthContext };
