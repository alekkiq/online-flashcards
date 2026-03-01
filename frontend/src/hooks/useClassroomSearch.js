import { useEffect, useMemo } from "react";
import { useSearch } from "./useSearch";
import { useClassroomContext } from "./useClassroomContext";

const classroomFilterFn = (classroom, query) =>
    classroom.title.toLowerCase().includes(query) ||
    (classroom.description || "").toLowerCase().includes(query) ||
    (classroom.subjectName || "").toLowerCase().includes(query) ||
    (classroom.ownerUsername || "").toLowerCase().includes(query);

export function useClassroomSearch(searchParams, setSearchParams, selectedSubject) {
    const { classrooms, isLoading, fetchClassrooms } = useClassroomContext();

    useEffect(() => {
        fetchClassrooms();
    }, [fetchClassrooms]);

    const { searchQuery, setSearchQuery, filteredItems } = useSearch(
        classrooms,
        classroomFilterFn,
        searchParams,
        setSearchParams
    );


    const filteredClassrooms = useMemo(() =>
        selectedSubject
            ? filteredItems.filter((c) => c.subjectName === selectedSubject)
            : filteredItems,
        [filteredItems, selectedSubject]
    );

    return {
        searchQuery,
        setSearchQuery,
        filteredClassrooms,
        isLoading,
    }
}