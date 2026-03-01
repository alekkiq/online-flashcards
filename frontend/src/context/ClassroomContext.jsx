import { createContext, useState, useCallback, useEffect } from "react";
import { getMyClassrooms } from "/src/api";
import { useAuth } from "../hooks/useAuth";

const ClassroomContext = createContext(null);

const ClassroomProvider = ({ children }) => {
  const { user } = useAuth();
  const [classrooms, setClassrooms] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [hasFetched, setHasFetched] = useState(false);
  const [error, setError] = useState(null);

  // reset cache when user changes (logout/login)
  useEffect(() => {
    setClassrooms([]);
    setHasFetched(false);
    setError(null);
  }, [user]);

  /**
   * Fetch the user's classrooms. Skips if already fetched or currently loading.
   * @param {boolean} [force=false] - force a re-fetch even if already fetched
   */
  const fetchClassrooms = useCallback(async (force = false) => {
    if ((!force && (hasFetched || isLoading)) || !user) return;
    setIsLoading(true);
    setError(null);
    try {
      const response = await getMyClassrooms();
      if (response.success) {
        setClassrooms(response.data.data);
        setHasFetched(true);
      } else {
        setError(response.error);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  }, [hasFetched, isLoading, user]);

  /**
   * Clear all classroom data (e.g. on logout).
   */
  const clearClassrooms = useCallback(() => {
    setClassrooms([]);
    setHasFetched(false);
    setError(null);
  }, []);

  return (
    <ClassroomContext.Provider
      value={{
        classrooms,
        setClassrooms,
        isLoading,
        hasFetched,
        error,
        fetchClassrooms,
        clearClassrooms,
      }}
    >
      {children}
    </ClassroomContext.Provider>
  );
};

export { ClassroomProvider, ClassroomContext };

