"use client";

import { Input } from "@/components/ui/input";
import { formUrlQuery, removeKeysFromQuery } from "@/lib/utils";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import React, { useEffect, useState } from "react";

interface Props {
  route: string;
  placeholder: string;
}

const Searchbar = ({ placeholder, route }: Props) => {
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();

  const query = searchParams.get("q");
  const [search, setSearch] = useState(query || "");

  // form and push new url when typing in search bar. use debouncing
  useEffect(() => {
    const debounceSearch = setTimeout(() => {
      if (search) {
        const newUrl = formUrlQuery({
          params: searchParams.toString(),
          key: "q",
          value: search,
        });

        router.push(newUrl, { scroll: false });
      } else {
        if (pathname === route) {
          const newUrl = removeKeysFromQuery({
            params: searchParams.toString(),
            keysToRemove: ["q"],
          });

          router.push(newUrl, { scroll: false });
        }
      }
    }, 300);

    return () => clearTimeout(debounceSearch);
  }, [search, route, pathname, router, searchParams, query]);

  return (
    <>
      <Input
        type="text"
        placeholder={placeholder}
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="mt-5 h-14 w-full rounded-[5px] border-none bg-brown-textfield
        text-lg text-gray-dark-textfield outline-none ring-pale
        placeholder:text-gray-dark-textfield dark:bg-gray-dark-textfield
        dark:text-pale dark:placeholder:text-pale max-[430px]:h-10
        max-[430px]:text-sm"
      />
    </>
  );
};

export default Searchbar;
