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

interface Props {
  type: string;
}

const SearchFilterGroup = ({ type }: Props) => {
  const [isTopFilterSelected, setIsTopFilterSelected] = useState(true);
  const [isRecentFilterSelected, setIsRecentFilterSelected] = useState(false);
  // const [specificTopFilter, setSpecificTopFilter] = useState("");

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
            }
          }}
        >
          {type === "poems" ? "Top Poems" : "Top Poets"}
        </h3>

        <Select disabled={isRecentFilterSelected} defaultValue="TOP_THIS_WEEK">
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
                value="TOP_TODAY"
                className="text-lg focus:bg-brown-textfield
                dark:focus:bg-gray-dark-textfield
                max-[430px]:text-sm"
              >
                Today
              </SelectItem>
              <SelectItem
                value="TOP_THIS_WEEK"
                className="text-lg focus:bg-brown-textfield
                dark:focus:bg-gray-dark-textfield
                max-[430px]:text-sm"
              >
                This week
              </SelectItem>
              <SelectItem
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
          }
        }}
      >
        Recent
      </h3>
    </>
  );
};

export default SearchFilterGroup;
