import { useContext, useEffect } from "react";
import { SubjectContext } from "../context/SubjectContext";
import i18n from "../i18n";

export function useSubjects() {
  const context = useContext(SubjectContext);
  if (!context) {
    throw new Error("useSubjects must be used within a SubjectProvider");
  }

  const { subjects, isLoading, error, fetchSubjects } = context;

  useEffect(() => {
    fetchSubjects();
  }, [i18n.language, fetchSubjects]);

  return { subjects, isLoading, error };
}
