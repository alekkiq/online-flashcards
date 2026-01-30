import { createContext, useState } from "react";
import { useNavigate, useLocation } from "react-router";

const AuthContext = createContext(null);

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  const handleRegister = async (credentials) => {
    //TODO: connect backend here
    return { success: true };
  };

  const handleLogin = async (credentials, redirectTo = null) => {
    //TODO: connect backend here
    if (redirectTo) {
      navigate(redirectTo);
      return { success: true };
    }

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
    if (!token) return;
    //TODO: connect backend here
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
