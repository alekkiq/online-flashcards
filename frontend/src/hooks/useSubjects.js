import { useContext, useEffect } from "react";
import { SubjectContext } from "../context/SubjectContext";

export function useSubjects() {
  const context = useContext(SubjectContext);
  if (!context) {
    throw new Error("useSubjects must be used within a SubjectProvider");
  }

  const { subjects, isLoading, error, fetchSubjects } = context;

  useEffect(() => {
    fetchSubjects();
  }, [fetchSubjects]);

  return { subjects, isLoading, error };
}

