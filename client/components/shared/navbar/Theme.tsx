import Image from "next/image";
import React from "react";
import {
  Menubar,
  MenubarContent,
  MenubarItem,
  MenubarMenu,
  MenubarTrigger,
} from "@/components/ui/menubar";
import { useTheme } from "@/context/ThemeProvider";

const Theme = () => {
  const { mode, setMode } = useTheme();

  return (
    <Menubar className="border-none">
      <MenubarMenu>
        <MenubarTrigger className="cursor-pointer p-0">
          {mode === "light" ? (
            <Image
              src="/assets/icons/sun.svg"
              alt="sun icon"
              width={32}
              height={32}
              className="min-[744px]:w-[40px]"
            />
          ) : (
            <Image
              src="/assets/icons/moon.svg"
              alt="moon icon"
              width={20}
              height={20}
              className="min-[744px]:w-[20px]"
            />
          )}
        </MenubarTrigger>
        <MenubarContent className="absolute right-[-5.8rem] mt-4 min-w-[150px] rounded bg-pale dark:border-gray-dark dark:bg-gray-dark">
          <MenubarItem
            className={`flex cursor-pointer items-center gap-4 py-3 text-brown
            focus:bg-brown-textfield dark:text-pale dark:focus:bg-gray-dark-textfield
              ${mode === "light" ? "bg-brown-textfield" : "bg-gray-dark"}`}
            onClick={() => {
              setMode("light");
              localStorage.theme = "light";
            }}
          >
            {mode === "light" ? (
              <Image
                src="/assets/icons/sun.svg"
                alt="sun icon"
                width={25}
                height={25}
                className="min-[744px]:w-[30px]"
              />
            ) : (
              <Image
                src="/assets/icons/sun-dark.svg"
                alt="sun icon"
                width={25}
                height={25}
                className="min-[744px]:w-[30px]"
              />
            )}
            <p className="text-base font-bold">Light</p>
          </MenubarItem>
          <MenubarItem
            className={`flex cursor-pointer items-center gap-4 py-3 text-brown
           focus:bg-brown-textfield dark:text-pale dark:focus:bg-gray-dark-textfield
             ${mode === "dark" ? "bg-gray-dark-textfield" : "bg-pale"}`}
            onClick={() => {
              setMode("dark");
              localStorage.theme = "dark";
            }}
          >
            {mode === "light" ? (
              <Image
                src="/assets/icons/moon-light.svg"
                alt="moon icon"
                width={16}
                height={16}
                className="min-[744px]:w-[20px] min-[744px]:pl-0.5"
              />
            ) : (
              <Image
                src="/assets/icons/moon.svg"
                alt="moon icon"
                width={16}
                height={16}
                className="w-[18px] pl-0.5 min-[744px]:w-[20px] min-[744px]:pl-0.5"
              />
            )}
            <p className="pl-2 text-base font-bold min-[744px]:pl-2.5">Dark</p>
          </MenubarItem>
          <MenubarItem
            className="flex cursor-pointer items-center gap-4 py-3 text-brown
          focus:bg-brown-textfield dark:text-pale dark:focus:bg-gray-dark-textfield"
            onClick={() => {
              setMode("system");
              localStorage.removeItem("theme");
            }}
          >
            {mode === "light" ? (
              <Image
                src="/assets/icons/system-dark.svg"
                alt="system icon"
                width={25}
                height={25}
                className="min-[744px]:w-[30px]"
              />
            ) : (
              <Image
                src="/assets/icons/system-light.svg"
                alt="system icon"
                width={25}
                height={25}
                className="min-[744px]:w-[30px]"
              />
            )}
            <p className="text-base font-bold">System</p>
          </MenubarItem>
        </MenubarContent>
      </MenubarMenu>
    </Menubar>
  );
};

export default Theme;
