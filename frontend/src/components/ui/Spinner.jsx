/**
 * this is a spinner component that can be used to show a loading state
 * @param {number} size the size of the spinner
 * @param {string} ringColor the color of the ring
 * @param {string} spinColor the color of the spinnaaja
 */
export function Spinner({
  size = 24,
  ringColor = "rgba(0, 0, 0, 0.1)",
  spinColor = "#3b82f6",
}) {
  return (
    <div
      className="animate-spin"
      style={{
        width: size,
        height: size,
        border: `${Math.max(2, size / 8)}px solid ${ringColor}`,
        borderTopColor: spinColor,
        borderRadius: "50%",
      }}
    />
  );
}
