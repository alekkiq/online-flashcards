import { createContext, useState, useCallback, useEffect } from "react";
import { getSubjects } from "/src/api";
import i18n from "../i18n";

const SubjectContext = createContext(null);

const SubjectProvider = ({ children }) => {
  const [subjects, setSubjects] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [hasFetched, setHasFetched] = useState(false);
  const [error, setError] = useState(null);

  /**
   * Fetch all subjects. Skips if already fetched or currently loading.
   */
  const fetchSubjects = useCallback(async () => {
    if (isLoading) return;
    setIsLoading(true);
    setError(null);
    try {
      const response = await getSubjects();
      if (response.success) {
        setSubjects(response.data.data);
        setHasFetched(true);
      } else {
        setError(response.error);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  }, [isLoading]);

  useEffect(() => {
    setHasFetched(false);
    setSubjects([]);
    fetchSubjects();
  }, [i18n.language, fetchSubjects]);

  return (
    <SubjectContext.Provider
      value={{
        subjects,
        isLoading,
        error,
        fetchSubjects,
      }}
    >
      {children}
    </SubjectContext.Provider>
  );
};

export { SubjectProvider, SubjectContext };
