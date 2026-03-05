import { cn } from "/src/lib/utils";
import { Badge } from "./Badge";
import { Avatar } from "./Avatar";
import { Users } from "lucide-react";

export default function ClassroomCard({ classroom, onClick, className }) {
  const memberCount = classroom.userCount || 0;

  return (
    <div
      onClick={onClick}
      className={cn(
        "flex flex-col bg-white rounded-xl border border-secondary/10 overflow-hidden cursor-pointer transition-all duration-200 hover:shadow-sm hover:-translate-y-1 min-h-[220px] min-w-[240px] group relative",
        className
      )}
    >
      <div className="p-4 flex flex-col gap-2 flex-1 justify-center">
        <h3 className="font-inter font-bold text-main text-lg sm:text-2xl leading-tight">
          {classroom.title}
        </h3>

        {classroom.description && (
          <p className="text-sm text-secondary line-clamp-3 mb-2">{classroom.description}</p>
        )}

        <div className="flex items-center gap-2 flex-wrap">
          {classroom.subjectName && (
            <Badge textColor="text-primary" bgColor="bg-primary/10">
              {classroom.subjectName}
            </Badge>
          )}
          <Badge textColor="text-primary" bgColor="bg-primary/10">
            <Users size={12} className="inline mr-1" />
            {memberCount} {memberCount === 1 ? "member" : "members"}
          </Badge>
        </div>
      </div>

      <div className="mt-auto">
        <hr className="border-secondary/20 mx-4" />
        <div className="px-4 py-3 flex items-center gap-2">
          <Avatar name={classroom.ownerUsername || "?"} size="w-6 h-6" textSize="text-[10px]" />
          <span className="text-sm text-secondary truncate">{classroom.ownerUsername}</span>
        </div>
      </div>
    </div>
  );
}
