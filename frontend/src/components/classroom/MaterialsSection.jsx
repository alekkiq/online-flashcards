import { FileText } from "lucide-react";
import { Button } from "/src/components/ui/Button";

/**
 * Learning materials section for the classroom view.
 * @param {object} props
 * @param {Array} props.materials - array of { id, title, content, creatorUsername }
 * @param {boolean} props.isOwner - whether the current user is the owner
 * @param {function} [props.onAddMaterial] - callback when "+ Add Material" is clicked
 */
export default function MaterialsSection({ materials = [], isOwner, onAddMaterial }) {
  return (
    <div className="mt-6 rounded-2xl bg-white p-6 md:p-8">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <FileText size={20} className="text-secondary" />
          <h2 className="font-semibold text-main">Learning Materials</h2>
          <span className="text-sm text-secondary">({materials.length})</span>
        </div>
        {isOwner && (
          <Button size="sm" onClick={onAddMaterial}>+ Add Material</Button>
        )}
      </div>
      {materials.length > 0 ? (
        <div className="flex flex-col gap-3">
          {materials.map((material) => (
            <div
              key={material.id}
              className="flex items-start gap-3 p-4 rounded-xl border border-secondary/10 hover:bg-secondary/5 transition-colors cursor-pointer"
            >
              <FileText size={18} className="text-primary shrink-0 mt-0.5" />
              <div className="flex-1 min-w-0">
                <p className="text-sm font-semibold text-main">{material.title}</p>
                <p className="text-sm text-secondary line-clamp-2 mt-1">{material.content}</p>
                <p className="text-xs text-muted mt-2">
                  Added by {material.creatorUsername}
                </p>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <p className="text-sm text-secondary">No learning materials added yet.</p>
      )}
    </div>
  );
}

