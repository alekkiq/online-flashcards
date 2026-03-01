import { useContext } from "react";
import { ClassroomContext } from "../context/ClassroomContext";

/**
 * Access the classroom context.
 * Must be used within a ClassroomProvider.
 */
export const useClassroomContext = () => {
  const context = useContext(ClassroomContext);
  if (!context) {
    throw new Error("useClassroomContext must be used within a ClassroomProvider");
  }
  return context;
};

