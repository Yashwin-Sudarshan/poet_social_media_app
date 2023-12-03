"use client";

import React, { useState } from "react";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import Searchbar from "./search/Searchbar";
import PoemCard from "./PoemCard";
import { Button } from "../ui/button";
import { useSearchParams, useRouter } from "next/navigation";
import { formUrlQuery, formatTimestamp } from "@/lib/utils";

interface Props {
  filteredPoems: {
    id: number;
    title: string;
    content: string;
    author_username: string;
    created_at: Date;
    tags: string[];
    number_of_likes: number;
    number_of_comments: number;
  }[];
  numAllPoems: number;
}

const PoemsLayout = ({ filteredPoems, numAllPoems }: Props) => {
  const [columnPreference, setColumnPreference] = useState("double");
  const searchParams = useSearchParams();
  const router = useRouter();

  let [page, setPage] = useState(1);

  const handlePaginationParams = () => {
    setPage(++page);

    const newUrl = formUrlQuery({
      params: searchParams.toString(),
      key: "page",
      value: page.toString(),
    });

    router.push(newUrl, { scroll: false });
  };

  const rearrangedFilterdPoemsForDoubleColumn = () => {
    const rearrangedFilteredPoems = [];

    for (let i = 0; i < filteredPoems.length; i += 2) {
      rearrangedFilteredPoems.push(filteredPoems[i]);
    }

    for (let i = 1; i < filteredPoems.length; i += 2) {
      rearrangedFilteredPoems.push(filteredPoems[i]);
    }

    return rearrangedFilteredPoems;
  };

  const SearchFilterGroup = (type: string) => {
    const [isTopFilterSelected, setIsTopFilterSelected] = useState(true);
    const [isRecentFilterSelected, setIsRecentFilterSelected] = useState(false);
    // const [specificTopFilter, setSpecificTopFilter] = useState("");

    const handleSelectUpdateParams = (value: string) => {
      const newUrl = formUrlQuery({
        params: searchParams.toString(),
        key: "filter",
        value,
      });

      router.push(newUrl, { scroll: false });
    };

    const handleRecentUpdateParams = () => {
      const newUrl = formUrlQuery({
        params: searchParams.toString(),
        key: "filter",
        value: "RECENT",
      });

      router.push(newUrl, { scroll: false });
    };

    return (
      <>
        <div>
          <h3
            className={`cursor-pointer text-2xl font-bold max-[430px]:text-base ${
              isTopFilterSelected
                ? "text-brown underline dark:text-pale"
                : "text-brown/60 dark:text-pale/40"
            }`}
            onClick={() => {
              if (!isTopFilterSelected) {
                setIsTopFilterSelected(true);
                setIsRecentFilterSelected(false);

                handleSelectUpdateParams("TOP_THIS_WEEK");
              }
            }}
          >
            {type === "poems" ? "Top Poems" : "Top Poets"}
          </h3>

          <Select
            disabled={isRecentFilterSelected}
            onValueChange={handleSelectUpdateParams}
            defaultValue="TOP_THIS_WEEK"
            value={searchParams.get("filter")?.toString()}
          >
            <SelectTrigger
              className="absolute mt-2.5 flex w-36 justify-start gap-1 border-none pr-2 text-lg text-brown focus-visible:ring-0
                  dark:text-pale max-[430px]:text-sm"
            >
              <SelectValue placeholder="Select a timeperiod" />
            </SelectTrigger>
            <SelectContent
              className="w-36 border-brown-textfield bg-pale text-lg text-brown
                  dark:border-gray-dark-textfield dark:bg-gray-dark dark:text-pale
                  max-[430px]:text-sm"
            >
              <SelectGroup>
                <SelectItem
                  key="TOP_TODAY"
                  value="TOP_TODAY"
                  className="text-lg focus:bg-brown-textfield
                      dark:focus:bg-gray-dark-textfield
                      max-[430px]:text-sm"
                >
                  Today
                </SelectItem>
                <SelectItem
                  key="TOP_THIS_WEEK"
                  value="TOP_THIS_WEEK"
                  className="text-lg focus:bg-brown-textfield
                      dark:focus:bg-gray-dark-textfield
                      max-[430px]:text-sm"
                >
                  This week
                </SelectItem>
                <SelectItem
                  key="TOP_THIS_MONTH"
                  value="TOP_THIS_MONTH"
                  className="text-lg focus:bg-brown-textfield
                      dark:focus:bg-gray-dark-textfield
                      max-[430px]:text-sm"
                >
                  This month
                </SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
        <div className="mx-3 h-3 w-3 rounded-full bg-brown dark:bg-pale" />
        <h3
          className={`cursor-pointer text-2xl font-bold max-[430px]:text-base ${
            isRecentFilterSelected
              ? "text-brown underline dark:text-pale"
              : "text-brown/60 dark:text-pale/40"
          }`}
          onClick={() => {
            if (!isRecentFilterSelected) {
              setIsRecentFilterSelected(true);
              setIsTopFilterSelected(false);

              handleRecentUpdateParams();
            }
          }}
        >
          Recent
        </h3>
      </>
    );
  };

  const ColumnLayoutSelectorGroup = () => {
    return (
      <>
        <div
          className={`mx-10 flex h-[42px] w-[42px] items-center justify-center rounded-full border-2 ${
            columnPreference === "double"
              ? "border-brown dark:border-pale"
              : "border-brown/40 dark:border-pale/40"
          }
        hidden cursor-pointer p-0.5 lg:flex`}
          onClick={() => {
            if (columnPreference === "single") {
              setColumnPreference("double");
            }
          }}
        >
          <div className="flex w-1/2 flex-col gap-1">
            {Array(4)
              .fill(null)
              .map((_, i) => (
                <div
                  key={i}
                  className={`mx-0.5 h-[1px] ${
                    columnPreference === "double"
                      ? "bg-brown dark:bg-pale"
                      : "bg-brown/40 dark:bg-pale/40"
                  }`}
                />
              ))}
          </div>
          <div className="flex w-1/2 flex-col gap-1">
            {Array(4)
              .fill(null)
              .map((_, i) => (
                <div
                  key={i}
                  className={`mx-0.5 h-[1px] ${
                    columnPreference === "double"
                      ? "bg-brown dark:bg-pale"
                      : "bg-brown/40 dark:bg-pale/40"
                  }`}
                />
              ))}
          </div>
        </div>
        <div
          className={`flex h-[42px] w-[42px] items-center justify-center rounded-full border-2 ${
            columnPreference === "single"
              ? "border-brown dark:border-pale"
              : "border-brown/40 dark:border-pale/40"
          }
        hidden cursor-pointer p-0.5 lg:flex`}
          onClick={() => {
            if (columnPreference === "double") {
              setColumnPreference("single");
            }
          }}
        >
          <div className="flex w-1/2 flex-col gap-1">
            {Array(4)
              .fill(null)
              .map((_, i) => (
                <div
                  key={i}
                  className={`mx-0.5 h-[1px] ${
                    columnPreference === "single"
                      ? "bg-brown dark:bg-pale"
                      : "bg-brown/40 dark:bg-pale/40"
                  }`}
                />
              ))}
          </div>
        </div>
      </>
    );
  };

  return (
    <div
      className="max-[749px]:w-full min-[750px]:max-w-[600px] min-[750px]:border-r min-[750px]:border-r-brown/30 
          min-[750px]:dark:border-r-dark-pale min-[750px]:max-[860px]:w-1/2 min-[860px]:max-lg:w-3/4 
          min-[1110px]:max-w-[680px] xl:max-w-[763px]"
    >
      <div className="max-w-[664px] pt-10 max-[430px]:pt-5 min-[750px]:mr-5">
        <h1 className="text-5xl font-bold text-brown dark:text-pale max-[430px]:text-2xl">
          Poems
        </h1>

        <Searchbar
          placeholder="Search by author, title, tags, or content..."
          route="/poems"
        />

        <div className="mt-5 flex items-center">
          {SearchFilterGroup("poems")}
          {ColumnLayoutSelectorGroup()}
        </div>
      </div>

      {filteredPoems.length === 0 && (
        <div className="mt-20 min-[1030px]:max-[1125px]:w-[599px] min-[1110px]:w-[680px] xl:w-[763px]">
          <h2 className="max-w-[664px] text-center text-[32px] text-brown dark:text-pale max-[430px]:text-xl min-[750px]:mr-5">
            No poems found
          </h2>
        </div>
      )}

      {columnPreference === "double" && filteredPoems.length > 1 ? (
        <div
          className="min-xl:gap-7 my-20 flex flex-col justify-center gap-5 max-[750px]:items-center max-[430px]:mb-0 min-[750px]:flex-row
        min-[750px]:flex-wrap lg:justify-start
        min-[1024px]:pr-4 min-[1295px]:pr-[35px]"
        >
          <div className="flex flex-col gap-5">
            {rearrangedFilterdPoemsForDoubleColumn()
              .slice(0, Math.ceil(filteredPoems.length / 2))
              .map((poem, index) => (
                <PoemCard
                  key={index}
                  id={poem.id}
                  title={poem.title}
                  content={poem.content}
                  author_username={poem.author_username}
                  created_at={formatTimestamp(poem.created_at.toString())}
                  tags={poem.tags}
                  number_of_likes={poem.number_of_likes}
                  number_of_comments={poem.number_of_comments}
                  hasLiked={false}
                />
              ))}
          </div>
          <div className="flex flex-col gap-5">
            {rearrangedFilterdPoemsForDoubleColumn()
              .slice(Math.ceil(filteredPoems.length / 2))
              .map((poem, index) => (
                <PoemCard
                  key={index}
                  id={poem.id}
                  title={poem.title}
                  content={poem.content}
                  author_username={poem.author_username}
                  created_at={formatTimestamp(poem.created_at.toString())}
                  tags={poem.tags}
                  number_of_likes={poem.number_of_likes}
                  number_of_comments={poem.number_of_comments}
                  hasLiked={false}
                />
              ))}
          </div>
        </div>
      ) : (
        <div
          className="my-20 flex flex-col items-center justify-center gap-5
          max-[430px]:mb-0 min-[1024px]:pr-4 min-[1030px]:max-[1125px]:w-[599px] min-[1110px]:w-[680px]
          xl:w-[763px] min-[1295px]:pr-9"
        >
          {filteredPoems.map((poem, index) => (
            <PoemCard
              key={index}
              id={poem.id}
              title={poem.title}
              content={poem.content}
              author_username={poem.author_username}
              created_at={poem.created_at}
              tags={poem.tags}
              number_of_likes={poem.number_of_likes}
              number_of_comments={poem.number_of_comments}
              hasLiked={false}
            />
          ))}
        </div>
      )}

      {numAllPoems > page * 6 && filteredPoems.length === page * 6 && (
        <div
          className="text-center max-[376px]:justify-center max-[360px]:flex
          min-[1024px]:mr-4 min-[1295px]:mr-9"
          // className="flex justify-center"
        >
          <Button
            className="mb-40 mt-20 h-14 w-[280px] rounded-[10px] border-2 border-brown text-2xl font-bold
          text-brown hover:border-brown hover:bg-brown hover:text-pale dark:border-pale dark:text-pale
          dark:hover:border-pale dark:hover:bg-pale dark:hover:text-gray-dark max-[430px]:mb-0 max-[430px]:mt-10
          max-[430px]:text-xl min-[500px]:max-[750px]:w-[320px] min-[749px]:max-lg:w-5/6 lg:w-[364px]"
            onClick={handlePaginationParams}
          >
            See More
          </Button>
        </div>
      )}
    </div>
  );
};

export default PoemsLayout;
