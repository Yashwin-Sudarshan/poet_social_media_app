"use client";

import Image from "next/image";
import Link from "next/link";
import React, { useEffect, useRef, useState } from "react";
import Theme from "./Theme";
import { Button } from "@/components/ui/button";
import { usePathname } from "next/navigation";

const DesktopAndTabletNavbar = () => {
  const pathname = usePathname();
  const isLinkActive = (link: string) => {
    return pathname.includes(link) || pathname === link;
  };

  return (
    <div className="mx-auto flex max-w-[1440px] items-center justify-between px-[40px] py-2 min-[1024px]:px-[80px] min-[1380px]:px-[140px]">
      <div className="flex items-center justify-between gap-10 min-[800px]:gap-14 min-[1180px]:gap-20">
        <Link
          href="/about"
          className={`${
            isLinkActive("about")
              ? "text-xl font-bold text-brown underline dark:text-pale min-[920px]:text-2xl"
              : "text-xl font-bold text-brown dark:text-pale min-[920px]:text-2xl"
          }`}
        >
          About
        </Link>
        <Link
          href="/poems"
          className={`${
            isLinkActive("poems")
              ? "text-xl font-bold text-brown underline dark:text-pale min-[920px]:text-2xl"
              : "text-xl font-bold text-brown dark:text-pale min-[920px]:text-2xl"
          }`}
        >
          Poems
        </Link>
        <Link
          href="/poets"
          className={`${
            isLinkActive("poets")
              ? "text-xl font-bold text-brown underline dark:text-pale min-[920px]:text-2xl"
              : "text-xl font-bold text-brown dark:text-pale min-[920px]:text-2xl"
          }`}
        >
          Poets
        </Link>
      </div>
      <Link href="/poems" className="absolute left-1/2 -translate-x-1/2">
        <Image
          src="/assets/images/poetvine-logo.svg"
          alt="poetvine logo"
          width={94}
          height={94}
        />
      </Link>
      <div className="flex items-center justify-between gap-6 max-[1280px]:gap-2">
        <div className="px-2 min-[800px]:px-6">
          <Theme />
        </div>
        <Button
          className="h-14 w-24 rounded-[10px] border-2 border-brown text-xl font-bold text-brown hover:border-brown hover:bg-brown
            hover:text-pale dark:border-pale dark:text-pale dark:hover:border-pale dark:hover:bg-pale dark:hover:text-gray-dark
             min-[920px]:w-32 min-[920px]:text-2xl min-[1180px]:w-[172px]"
        >
          Login
        </Button>
        <Button
          className="h-14 w-24 rounded-[10px] border-2 border-brown bg-brown text-xl font-bold text-pale 
          hover:shadow-default dark:border-dark-pale dark:bg-dark-pale min-[920px]:w-32 min-[920px]:text-2xl min-[1180px]:w-[172px]"
        >
          Sign Up
        </Button>
      </div>
    </div>
  );
};

const MobileNavBar = () => {
  const pathname = usePathname();
  const isLinkActive = (link: string) => {
    return pathname.includes(link) || pathname === link;
  };

  const [isMenuOpen, setIsMenuOpen] = useState(false);

  // Define the menuRef with the correct type
  const menuRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const handleOutsideClick = (e: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
        setIsMenuOpen(false);
      }
    };

    document.addEventListener("click", handleOutsideClick);

    return () => {
      document.removeEventListener("click", handleOutsideClick);
    };
  }, [isMenuOpen]);
  return (
    <>
      <div className="flex items-center justify-between px-[20px] py-2">
        <Link href="/poems">
          <Image
            src="/assets/icons/poetvine-book.svg"
            alt="poetvine logo"
            width={32}
            height={32}
          />
        </Link>
        <Theme />
        {isMenuOpen ? (
          <Image
            src="/assets/icons/close.svg"
            alt="poetvine logo"
            width={34}
            height={34}
            className="cursor-pointer"
            onClick={() => setIsMenuOpen(false)}
          />
        ) : (
          <Image
            src="/assets/icons/hamburger.svg"
            alt="poetvine logo"
            width={34}
            height={34}
            className="cursor-pointer"
            onClick={() => setIsMenuOpen(true)}
          />
        )}
      </div>

      {/* Menu */}
      {isMenuOpen && (
        <div
          ref={menuRef}
          className="absolute left-1/2 flex w-full -translate-x-1/2 flex-col gap-7 border-[1px] border-y-brown/30 bg-pale py-5 
          text-center dark:border-b-dark-pale dark:bg-gray-dark"
        >
          <Link
            href="/about"
            className={`${
              isLinkActive("about")
                ? "text-xl font-bold text-brown underline dark:text-pale min-[920px]:text-2xl"
                : "text-xl font-bold text-brown dark:text-pale min-[920px]:text-2xl"
            }`}
          >
            About
          </Link>
          <Link
            href="/poems"
            className={`${
              isLinkActive("poems")
                ? "text-xl font-bold text-brown underline dark:text-pale min-[920px]:text-2xl"
                : "text-xl font-bold text-brown dark:text-pale min-[920px]:text-2xl"
            }`}
          >
            Poems
          </Link>
          <Link
            href="/poets"
            className={`${
              isLinkActive("poets")
                ? "text-xl font-bold text-brown underline dark:text-pale min-[920px]:text-2xl"
                : "text-xl font-bold text-brown dark:text-pale min-[920px]:text-2xl"
            }`}
          >
            Poets
          </Link>
          <div className="flex flex-col items-center gap-2">
            <Button
              className="h-14 w-32 rounded-[10px] border-2 border-brown text-xl font-bold text-brown hover:border-brown hover:bg-brown
                hover:text-pale dark:border-pale dark:text-pale dark:hover:border-pale dark:hover:bg-pale dark:hover:text-gray-dark"
            >
              Login
            </Button>
            <Button
              className="h-14 w-32 rounded-[10px] border-2 border-brown bg-brown text-xl font-bold text-pale 
                hover:shadow-default dark:border-dark-pale dark:bg-dark-pale"
            >
              Sign Up
            </Button>
          </div>
        </div>
      )}
    </>
  );
};

const Navbar = () => {
  return (
    <>
      <nav className="w-full border-b-[1px] border-b-brown/30 bg-pale dark:border-b-dark-pale dark:bg-gray-dark max-[744px]:hidden">
        <DesktopAndTabletNavbar />
      </nav>
      <nav className="w-full border-b-[1px] border-b-brown/30 bg-pale dark:border-b-dark-pale dark:bg-gray-dark min-[744px]:hidden">
        <MobileNavBar />
      </nav>
    </>
  );
};

export default Navbar;
