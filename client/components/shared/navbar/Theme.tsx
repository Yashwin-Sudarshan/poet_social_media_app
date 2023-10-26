import Image from "next/image";
import React from "react";
import {
  Menubar,
  MenubarContent,
  MenubarItem,
  MenubarMenu,
  MenubarTrigger,
} from "@/components/ui/menubar";

const Theme = () => {
  return (
    <Menubar className="border-none">
      <MenubarMenu>
        <MenubarTrigger className="cursor-pointer p-0">
          <Image
            src="/assets/icons/sun.svg"
            alt="sun icon"
            width={32}
            height={32}
            className="min-[744px]:w-[40px]"
          />
        </MenubarTrigger>
        <MenubarContent className="absolute right-[-5.8rem] mt-3 min-w-[150px] rounded bg-pale">
          <MenubarItem className="cursor-pointer focus:bg-brown-textfield">
            Sun Icon - Light
          </MenubarItem>
          <MenubarItem className="cursor-pointer focus:bg-brown-textfield">
            Moon Icon - Dark
          </MenubarItem>
          <MenubarItem className="cursor-pointer focus:bg-brown-textfield">
            System Icon - System
          </MenubarItem>
        </MenubarContent>
      </MenubarMenu>
    </Menubar>
  );
};

export default Theme;
