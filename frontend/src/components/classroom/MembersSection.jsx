import { Users } from "lucide-react";
import MemberCard from "./MemberCard";
import { useTranslation } from "react-i18next";

export default function MembersSection({ ownerUsername, users, userCount, isOwner, onRemoveUser }) {
  const nonOwnerUsers = users?.filter((u) => u.username !== ownerUsername) || [];
  const { t } = useTranslation();

  return (
    <div className="mt-6 rounded-xl bg-white p-5 md:p-8">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Users size={20} className="text-secondary" />
          <h2 className="font-semibold text-main">{t("membersSection.title")}</h2>
          <span className="text-sm text-secondary">({userCount || 0})</span>
        </div>
      </div>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
        <MemberCard username={ownerUsername} badge={t("membersSection.owner")} />
        {nonOwnerUsers.map((user) => (
          <MemberCard
            key={user.userId}
            username={user.username}
            role={user.role}
            menuItems={
              isOwner
                ? [
                    {
                      label: t("membersSection.remove"),
                      variant: "destructive",
                      onClick: () => onRemoveUser?.(user.userId),
                    },
                  ]
                : undefined
            }
          />
        ))}
      </div>
    </div>
  );
}
