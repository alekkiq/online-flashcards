import { Avatar } from "/src/components/ui/Avatar";
import { Badge } from "/src/components/ui/Badge";
import { DropdownMenu } from "/src/components/ui/DropdownMenu";

/**
 * A single member row used in the classroom members grid.
 * @param {object} props
 * @param {string} props.username
 * @param {string} [props.role] - role label shown as small text (e.g. "STUDENT")
 * @param {string} [props.badge] - highlighted badge text (e.g. "Owner")
 * @param {Array} [props.menuItems] - dropdown menu items (e.g. remove action)
 */
export default function MemberCard({ username, role, badge, menuItems }) {
  return (
    <div className="flex items-center gap-3 p-3 rounded-xl border border-secondary/10">
      <Avatar name={username} />
      <div className="flex-1 min-w-0 flex gap-2 items-center">
        <p className="text-sm font-medium text-main truncate">{username}</p>
        {badge && (
          <Badge textColor="text-primary" bgColor="bg-primary/10" className="text-xs">
            {badge}
          </Badge>
        )}
        {!badge && role && (
          <p className="text-xs text-secondary">{role}</p>
        )}
      </div>
      {menuItems && menuItems.length > 0 && (
        <DropdownMenu
          items={menuItems}
          triggerLabel={`Options for ${username}`}
        />
      )}
    </div>
  );
}

