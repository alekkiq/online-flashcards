import { createContext, useState, useEffect } from "react";
import { getSubjects } from "/src/api";
import { useTranslation } from "react-i18next";

const SubjectContext = createContext(null);

const SubjectProvider = ({ children }) => {
  const {i18n} = useTranslation();
  const [subjects, setSubjects] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  /**
   * Fetch all subjects. Skips if already fetched or currently loading.
   */
  useEffect(() => {
     let cancelled = false;

     const loadSubjects = async () => {
         setIsLoading(true);
         setError(null);

         try {
             const response = await getSubjects();

             if (cancelled) return;

             if (response.success) {
                 setSubjects(response.data.data);
             } else {
                 setError(response.error);
             }
         } catch (err) {
             if (!cancelled) setError(err.message);
         } finally {
             if (!cancelled) setIsLoading(false);
         }
     };

     loadSubjects();

     return () => {
         cancelled = true;
     }
  }, [i18n.language]);

  return (
    <SubjectContext.Provider
      value={{
        subjects,
        isLoading,
        error,
      }}
    >
      {children}
    </SubjectContext.Provider>
  );
};

export { SubjectProvider, SubjectContext };
