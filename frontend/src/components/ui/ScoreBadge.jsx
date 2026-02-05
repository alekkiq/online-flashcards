import { Badge } from "./Badge";

/**
 * displays a color-coded badge based on percentage score
 * @param {Object} props
 * @param {number} props.percentage - Score percentage (0-100)
 * @param {string} props.date - Date of the score
 */
export function ScoreBadge({ percentage, date }) {
    const getBadgeColors = () => {
        if (percentage >= 80) return { bg: "bg-green-500/30", text: "text-green-900" };
        if (percentage >= 50) return { bg: "bg-yellow-500/30", text: "text-yellow-900" };
        return { bg: "bg-red-500/30", text: "text-red-900" };
    };

    const { bg, text } = getBadgeColors();

    return (
        <div className="flex flex-row items-center gap-2">
            <Badge textColor={text} bgColor={bg} className="px-2 py-3 md:px-4 md:py-5 text-sm md:text-md">
                {percentage}%
            </Badge>
            <div className="flex flex-col">
                <p className="font-serif text-lg font-bold text-main">Correct</p>
                <p className="font-serif text-md font-semibold text-secondary">{date}</p>
            </div>
        </div>
    );
}
