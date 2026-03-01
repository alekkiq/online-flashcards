import { Badge } from "./Badge";
import { Avatar } from "./Avatar";

/**
 * displays a color-coded badge based on percentage score
 * @param {Object} props
 * @param {number} props.percentage - Score percentage (0-100)
 * @param {string} [props.date] - ISO date string or parseable date
 */
export function ScoreBadge({ percentage, date }) {
    const getBadgeColors = () => {
        if (percentage >= 80) return { bg: "bg-green-500/30", text: "text-green-900" };
        if (percentage >= 50) return { bg: "bg-yellow-500/30", text: "text-yellow-900" };
        return { bg: "bg-red-500/30", text: "text-red-900" };
    };

    const formatDate = (dateStr) => {
        if (!dateStr) return null;
        const d = new Date(dateStr);
        const pad = (n) => String(n).padStart(2, "0");
        return `${d.getDate()}.${d.getMonth() + 1}.${d.getFullYear()}, ${pad(d.getHours())}:${pad(d.getMinutes())}`;
    };

    const { bg, text } = getBadgeColors();
    const formattedDate = formatDate(date);

    return (
        <div className="flex flex-row items-center gap-2">
            <Badge textColor={text} bgColor={bg} className="w-10 h-10 p-0 text-sm md:text-md relative">
                <span className="absolute m-auto text-center inset-0 h-fit">{percentage}%</span>
            </Badge>
            <div className="flex flex-col">
                <p className="font-serif text-lg font-bold text-main">Correct</p>
                <p className="font-serif text-md font-semibold text-secondary">{formattedDate}</p>
            </div>
        </div>
    );
}
