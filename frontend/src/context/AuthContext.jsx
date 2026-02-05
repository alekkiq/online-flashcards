import { createContext, useState } from "react";
import { useNavigate, useLocation } from "react-router";
import { login, autoLogin } from "../api";

const AuthContext = createContext(null);

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  const handleRegister = async (credentials) => {
    //TODO: do the same as login but for register
    return { success: true };
  };

  const handleLogin = async (credentials) => {
    const response = await login(credentials.username, credentials.password);
    if (!response.success) {
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
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthProvider, AuthContext };
