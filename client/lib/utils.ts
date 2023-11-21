import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"
 
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatTimestamp(timestamp: string): Date {
  const options: Intl.DateTimeFormatOptions = {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: true,
  };

  const formattedDate = new Date(timestamp);

  // Extract the relevant components and set them in a new Date object
  const formattedTimestamp = new Date(
    formattedDate.toLocaleString('en-US', options)
  );

  return formattedTimestamp;
}
