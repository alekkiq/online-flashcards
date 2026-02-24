import { cn } from "/src/lib/utils";
import { Badge } from "./Badge";
import { Avatar } from "./Avatar";

export default function ClassroomCard(classroom, onClick, className) {
    const classroomCount = classroom?.classroomCount || classroom.users?.length || 0;
    const isPrivate = classroom?.isPrivate || classroom.joinCode != "" || false;

    return (
        <div>
            {classroom.title}
        </div>
    )
}