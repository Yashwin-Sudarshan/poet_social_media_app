import { Input } from "@/components/ui/input";
import React from "react";

interface Props {
  placeholder: string;
}

const Searchbar = ({ placeholder }: Props) => {
  return (
    <>
      <Input
        type="text"
        placeholder={placeholder}
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
