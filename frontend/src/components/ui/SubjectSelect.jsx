import * as React from "react";
import { cn } from "/src/lib/utils";
import { useSubjects } from "/src/hooks/useSubjects";
import { useTranslation } from "react-i18next";

/**
 * Reusable subject select dropdown.
 * Controlled component — pass value and onChange.
 * @param {string|number} value - currently selected subject value
 * @param {function} onChange - callback when selection changes
 * @param {boolean} hasError - highlights border red when true
 * @param {"name"|"subjectId"} valueKey - which field to use as option value (default: "name")
 * @param {string} className - additional class names
 */
export const SubjectSelect = React.forwardRef(
  ({ value, onChange, hasError, valueKey = "code", className, ...props }, ref) => {
    const { subjects, isLoading } = useSubjects();
    const { t } = useTranslation();

    const handleChange = (e) => {
      const val = e.target.value;
      onChange(valueKey === "subjectId" ? Number(val) : val);
    };

    return (
      <select
        ref={ref}
        value={value != null ? String(value) : ""}
        onChange={handleChange}
        className={cn(
          "flex h-10 w-full rounded-xl border border-secondary/30 bg-white px-3 py-1 text-sm text-main transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-primary disabled:cursor-not-allowed disabled:opacity-50",
          hasError && "border-destructive focus-visible:ring-destructive",
          !value && "text-secondary/60",
          className
        )}
        disabled={isLoading}
        {...props}
      >
        <option value="" disabled>
          {isLoading ? t("createQuiz.loadingSubjects") : t("createQuiz.selectSubject")}
        </option>
        {subjects.map((subject) => (
          <option key={subject.subjectId ?? subject.name} value={String(subject[valueKey])}>
            {subject.name}
          </option>
        ))}
      </select>
    );
  }
);
SubjectSelect.displayName = "SubjectSelect";
