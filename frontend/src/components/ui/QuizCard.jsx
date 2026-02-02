import { cn } from "/src/lib/utils";

/**
 * quizcard component
 * @param {string} title title
 * @param {number} cardCount count of cards
 * @param {object} author userfield
 * @param {string} authorRole role of the user
 * @param {function} onClick click handler
 * @param {string} className any extra classes
 */
export function QuizCard({
    title,
    cardCount,
    author,
    authorRole,
    onClick,
    className,
}) {
    return (
        <div
            onClick={onClick}
            className={cn(
                "flex flex-col bg-white rounded-xl border border-secondary/10 overflow-hidden cursor-pointer transition-all duration-200 hover:shadow-sm hover:-translate-y-1 min-h-[250px] min-w-[240px]",
                className
            )}
        >
            <div className="p-4 flex flex-col gap-2 flex-1 justify-center">
                <h3 className="font-inter font-bold text-main text-lg leading-tight">
                    {title}
                </h3>

                {cardCount !== undefined && (
                    <span className="inline-flex w-fit items-center px-2 py-0.5 rounded-full text-xs font-medium bg-primary/10 text-primary">
                        {cardCount} cards
                    </span>
                )}
            </div>
            <div className="mt-auto">
                <hr className="border-secondary/20 mx-4" />
                <div className="px-4 py-3 flex items-center justify-between">
                    <div className="flex items-center gap-2">
                        <div className="w-6 h-6 rounded-full bg-gray-300 flex items-center justify-center text-xs text-gray-500">
                            {author?.name?.charAt(0)?.toUpperCase()}
                        </div>
                        <span className="text-sm text-secondary">
                            {author?.name}
                        </span>
                    </div>
                    
                    {authorRole && (
                        <span className="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium bg-primary text-white">
                            {authorRole}
                        </span>
                    )}
                </div>
            </div>
        </div>
    );
}
