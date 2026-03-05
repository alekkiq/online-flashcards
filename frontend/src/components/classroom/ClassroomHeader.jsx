import { Badge } from "/src/components/ui/Badge";
import { Avatar } from "/src/components/ui/Avatar";
import { Button } from "/src/components/ui/Button";
import { Copy, Check, StickyNote, LogOut, Settings } from "lucide-react";
import { useState } from "react";

/**
 * Header section for the classroom view — title, description, owner, join code, announcement.
 * @param {object} props
 * @param {object} props.classroom - the classroom response object
 * @param {boolean} props.isOwner - whether the current user is the owner
 */
export default function ClassroomHeader({ classroom, isOwner, onManage, onLeave }) {
  const [copiedCode, setCopiedCode] = useState(false);

  const handleCopyCode = () => {
    navigator.clipboard.writeText(classroom.joinCode);
    setCopiedCode(true);
    setTimeout(() => setCopiedCode(false), 2000);
  };

  return (
    <div className="rounded-xl bg-white p-5 md:p-8 flex flex-col gap-y-4">
      <div className="flex flex-col md:flex-row md:items-start md:justify-between gap-4">
        <div className="space-y-3">
          <div className="flex items-center gap-3 flex-wrap">
            <h1 className="font-serif text-3xl md:text-4xl lg:text-5xl font-bold text-main">
              {classroom.title}
            </h1>
            <Badge size="md">{classroom.subjectName}</Badge>
          </div>
          {classroom.description && (
            <p className="text-secondary max-w-2xl">{classroom.description}</p>
          )}
          <div className="flex items-center gap-2">
            <Avatar name={classroom.ownerUsername} />
            <span className="text-sm text-secondary">
              <span className="text-main font-medium">{classroom.ownerUsername}</span>
              {" · "}Owner
            </span>
          </div>
        </div>

        <div className="gap-2 shrink-0 hidden sm:flex">
          {isOwner ? (
            <Button variant="outline" size="sm" onClick={onManage}>
              <Settings size={16} />
              Manage
            </Button>
          ) : (
            <Button variant="outline" size="sm" onClick={onLeave}>
              <LogOut size={16} />
              Leave
            </Button>
          )}
        </div>
      </div>

      {isOwner && classroom.joinCode && (
        <>
          <hr className="border-secondary/20" />
          <div className="flex items-center justify-between sm:justify-start gap-3">
            <span className="text-sm font-semibold text-secondary uppercase tracking-wide">
              Join Code
            </span>
            <div className="flex items-center gap-2">
              <code className="px-3 py-1.5 rounded-lg bg-secondary/5 border border-secondary/20 text-sm font-mono font-semibold text-main">
                {classroom.joinCode}
              </code>
              <Button variant="ghost" size="sm" onClick={handleCopyCode}>
                {copiedCode ? <Check size={16} className="text-green-500" /> : <Copy size={16} />}
              </Button>
            </div>
          </div>
        </>
      )}

      {classroom.note && (
        <>
          <hr className="border-secondary/20" />
          <div className="flex gap-3 rounded-xl bg-primary/5 border border-primary/10 p-4">
            <StickyNote size={20} className="text-primary shrink-0 mt-0.5" />
            <div>
              <p className="text-sm font-semibold text-primary mb-1">Announcement</p>
              <p className="text-sm text-main">{classroom.note}</p>
            </div>
          </div>
        </>
      )}

      <div className="gap-2 shrink-0 flex sm:hidden">
        {isOwner ? (
          <Button variant="outline" size="sm" onClick={onManage}>
            <Settings size={16} />
            Manage
          </Button>
        ) : (
          <Button variant="outline" size="sm" onClick={onLeave}>
            <LogOut size={16} />
            Leave
          </Button>
        )}
      </div>
    </div>
  );
}
