import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";
import qs from "query-string";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatTimestamp(timestamp: string): Date {
  const options: Intl.DateTimeFormatOptions = {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    hour12: true,
  };

  const formattedDate = new Date(timestamp);

  // Extract the relevant components and set them in a new Date object
  const formattedTimestamp = new Date(
    formattedDate.toLocaleString("en-US", options)
  );

  return formattedTimestamp;
}

interface UrlQueryParams {
  params: string;
  key: string;
  value: string | null;
}

export const formUrlQuery = ({ params, key, value }: UrlQueryParams) => {
  const currentUrl = qs.parse(params);

  currentUrl[key] = value;

  return qs.stringifyUrl(
    {
      url: window.location.pathname,
      query: currentUrl,
    },
    { skipNull: true }
  );
};

interface RemoveUrlQueryParams {
  params: string;
  keysToRemove: string[];
}

export const removeKeysFromQuery = ({
  params,
  keysToRemove,
}: RemoveUrlQueryParams) => {
  const currentUrl = qs.parse(params);

  keysToRemove.forEach((key) => {
    delete currentUrl[key];
  });

  return qs.stringifyUrl(
    {
      url: window.location.pathname,
      query: currentUrl,
    },
    { skipNull: true }
  );
};

interface Poem {
  id: number;
  title: string;
  content: string;
  author_username: string;
  created_at: Date;
  tags: string[];
  number_of_likes: number;
  number_of_comments: number;
}

export function searchPoems(
  collection: Poem[],
  searchQuery: string | undefined
): Poem[] {
  if (!searchQuery || searchQuery.trim() === "") {
    return collection;
  }

  const lowerCaseSearchQuery = searchQuery.toLowerCase();

  const filteredPoems = collection.filter((poem) => {
    // eslint-disable-next-line camelcase
    const { author_username, title, tags, content } = poem;

    // eslint-disable-next-line camelcase
    const lowerCaseAuthor = author_username.toLowerCase();
    const lowerCaseTitle = title.toLowerCase();
    const lowerCaseTags = tags.map((tag) => tag.toLowerCase());
    const lowerCaseContent = content.toLowerCase();

    return (
      lowerCaseAuthor.includes(lowerCaseSearchQuery) ||
      lowerCaseTitle.includes(lowerCaseSearchQuery) ||
      lowerCaseTags.some((tag) => tag.includes(lowerCaseSearchQuery)) ||
      lowerCaseContent.includes(lowerCaseSearchQuery)
    );
  });

  return filteredPoems;
}
