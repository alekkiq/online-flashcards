import { createContext, useState, useEffect } from "react";
import { useNavigate } from "react-router";
import { login, autoLogin, register, updateEmail, updatePassword } from "../api";

const AuthContext = createContext(null);

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(!!localStorage.getItem("token"));
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleAutoLogin = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      setUser(null);
      setIsLoading(false);
      return;
    }

    setIsLoading(true);
    try {
      const response = await autoLogin(token);
      if (response.success) {
        const data = response.data.data;
        setUser({
          id: data.userId,
          username: data.username,
          role: data.role,
          email: data.email,
        });
      } else {
        setUser(null);
        localStorage.removeItem("token");
      }
    } catch (err) {
      console.error("Auto-login error:", err);
      setUser(null);
      localStorage.removeItem("token");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    handleAutoLogin();
  }, []);

  const handleRegister = async (credentials) => {
    setIsLoading(true);
    setError(null);
    try {
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
      return { success: true };
    } catch (err) {
      setError("Registration failed");
      setIsLoading(false);
      return { success: false, error: "Registration failed" };
    }
  };

  const handleLogin = async (credentials) => {
    setIsLoading(true);
    setError(null);
    try {
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
      return { success: true };
    } catch (err) {
      setError("Login failed");
      setIsLoading(false);
      return { success: false, error: "Login failed" };
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    setUser(null);
    navigate("/");
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
      ...prev,
      email: data.email ?? prev.email,
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
  };

  const isTeacher = user?.role === "TEACHER" || user?.role === "ADMIN";
  const isAdmin = user?.role === "ADMIN";

  return (
    <AuthContext.Provider
      value={{
        user,
        setUser,
        isTeacher,
        isAdmin,
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
