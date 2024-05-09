export function valueOrNull(
  value: string,
  defaultValue: string = ""
): string | null {
  if (value === defaultValue) return null;
  if (value === "") return null;
  return value;
}
