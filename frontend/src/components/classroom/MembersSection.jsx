import { Users } from "lucide-react";
import MemberCard from "./MemberCard";

/**
 * Members section for the classroom view.
 * @param {object} props
 * @param {string} props.ownerUsername - the classroom owner's username
 * @param {Array} props.users - array of { userId, username, role }
 * @param {number} props.userCount - total member count
 * @param {boolean} props.isOwner - whether the current user is the owner
 */
export default function MembersSection({ ownerUsername, users, userCount, isOwner, onRemoveUser }) {
  const nonOwnerUsers = users?.filter((u) => u.username !== ownerUsername) || [];

  return (
    <div className="mt-6 rounded-xl bg-white p-5 md:p-8">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Users size={20} className="text-secondary" />
          <h2 className="font-semibold text-main">Members</h2>
          <span className="text-sm text-secondary">({userCount || 0})</span>
        </div>
      </div>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
        <MemberCard username={ownerUsername} badge="Owner" />
        {nonOwnerUsers.map((user) => (
          <MemberCard
            key={user.userId}
            username={user.username}
            role={user.role}
            menuItems={
              isOwner
                ? [
                    {
                      label: "Remove",
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
