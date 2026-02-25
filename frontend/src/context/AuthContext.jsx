import { createContext, useState } from "react";
import { useNavigate, useLocation } from "react-router";
import { login, autoLogin, register, updateEmail, updatePassword } from "../api";

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
      email: data.email,
    });
    localStorage.setItem("token", data.token);
    window.location.href = "/"; 
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
      email: data.email,
    });
    localStorage.setItem("token", data.token);
    window.location.href = "/";
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
      setUser(null);
      localStorage.removeItem("token");
      return;
    }
    const response = await autoLogin(token);
    if (!response.success) {
      setUser(null);
      localStorage.removeItem("token");
      return;
    }
    const data = response.data.data;
    setUser({
      id: data.userId,
      username: data.username,
      role: data.role,
      email: data.email,
    });
    navigate(location.pathname);
  };

  const handleUpdateEmail = async (email) => {
    setIsLoading(true);
    setError(null);

    const token = localStorage.getItem("token");
    const response = await updateEmail(token, email);

    setIsLoading(false);

    if (!response.success) {
        setError(response.error);
        return response;
    }

    const data = response.data.data;
    setUser((prev) => ({
      ...prev, email: data.email ?? prev.email
    }));

    return { success: true };
  };

  const handleUpdatePassword = async (oldPassword, newPassword) => {
    setIsLoading(true);
    setError(null);

    const token = localStorage.getItem("token");
    const response = await updatePassword(token, oldPassword, newPassword);

    setIsLoading(false);

    if (!response.success) {
        setError(response.error);
        return response;
    }

    return { success: true };
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        setUser,
        handleLogin,
        handleLogout,
        handleAutoLogin,
        handleRegister,
        handleUpdateEmail,
        handleUpdatePassword,
        isLoading,
        error,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthProvider, AuthContext };
