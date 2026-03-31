import { Link } from "react-router";
import { Badge } from "/src/components/ui/Badge";
import { DropdownMenu } from "/src/components/ui/DropdownMenu";
import { LogOut, Users } from "lucide-react";
import { useEffect } from "react";
import { useClassroomContext } from "/src/hooks/useClassroomContext";
import { leaveClassroom } from "/src/api";
import { useTranslation } from "react-i18next";

function ClassroomRow({ classroom, onLeave }) {
  const { t } = useTranslation();

  const handleLeave = async () => {
    const response = await leaveClassroom(classroom.id);
    if (response.success) {
      onLeave();
    }
  };

  const menuItems = [
    {
      label: t("classroomList.leaveClassroom"),
      icon: <LogOut size={14} />,
      variant: "destructive",
      onClick: handleLeave,
    },
  ];

  return (
    <div className="flex items-center gap-4 rounded-lg py-2 hover:bg-secondary/5 transition-colors">
      <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-primary/10 text-primary sm:ms-2">
        <Users size={22} />
      </div>

      <div className="min-w-0 flex-1">
        <div className="flex flex-nowrap items-center gap-2">
          <Link
            to={`/classrooms/${classroom.id}`}
            className="font-medium text-main truncate hover:text-primary hover:underline underline-offset-2 transition-colors"
          >
            {classroom.title}
          </Link>
          {classroom.isOwner && (
            <Badge bgColor="bg-green-100" textColor="text-green-700">
              {t("classroomList.owner")}
            </Badge>
          )}
        </div>
        <p className="text-xs text-secondary">{classroom.subjectName}</p>
      </div>

      {!classroom.isOwner && (
        <DropdownMenu items={menuItems} triggerLabel="Classroom options" className="sm:me-2" />
      )}
    </div>
  );
}

export default function ClassroomList() {
  const { classrooms, fetchClassrooms } = useClassroomContext();
  const { t } = useTranslation();

  useEffect(() => {
    fetchClassrooms();
  }, [fetchClassrooms]);

  return (
    <div>
      <h2 className="text-sm font-semibold text-secondary uppercase tracking-wide">
        {t("classroomList.title")}
      </h2>

      {classrooms.length === 0 ? (
        <p className="mt-3 text-sm text-secondary">{t("classroomList.empty")}</p>
      ) : (
        <div className="mt-2 flex flex-col divide-y divide-secondary/10">
          {classrooms.map((classroom) => (
            <ClassroomRow key={classroom.id} classroom={classroom} onLeave={() => fetchClassrooms(true)} />
          ))}
        </div>
      )}
    </div>
  );
}
