import { useState, useCallback } from "react";
import { getClassroomById } from "/src/api";

export function useClassroom() {
  const [classroom, setClassroom] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchClassroom = useCallback(async (classroomId) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await getClassroomById(classroomId);
      if (response.success) {
        setClassroom(response.data.data);
      } else {
        setError(response.error);
      }
      return response;
    } catch (err) {
      setError(err.message);
      return null;
    } finally {
      setIsLoading(false);
    }
  }, []);

  return {
    classroom,
    isLoading,
    error,
    fetchClassroom,
  };
}
