"use client";

import { FooterLinks } from "@/constants";
import { useTheme } from "@/context/ThemeProvider";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import React from "react";

const Footer = () => {
  const pathname = usePathname();
  const { mode } = useTheme();

  const isLinkActive = (link: string) => {
    return pathname.includes(link) || pathname === link;
  };

  return (
    <footer
      className="flex w-full flex-col items-center justify-evenly border-t-[1px]
        border-t-brown/30 bg-pale p-10 dark:border-t-dark-pale dark:bg-gray-dark sm:flex-row"
    >
      <div className="flex flex-col items-center">
        <Link href="/poems">
          {mode === "light" ? (
            <Image
              src="/assets/images/poetvine-logo.svg"
              alt="poetvine logo"
              width={94}
              height={94}
            />
          ) : (
            <Image
              src="/assets/images/poetvine-logo-dark.svg"
              alt="poetvine logo"
              width={94}
              height={94}
            />
          )}
        </Link>

        <p className="pt-3 text-base font-bold text-brown dark:text-pale sm:text-lg">
          © Copyright 2023 Poetvine
        </p>
      </div>
      <div className="mt-10 flex flex-col items-center sm:items-start">
        {FooterLinks.map((link) => (
          <Link
            key={link.label}
            href={link.route}
            className={`pb-5 text-base font-bold text-brown dark:text-pale sm:text-lg
            ${isLinkActive(link.label.toLowerCase()) ? "underline" : ""}`}
          >
            {link.label}
          </Link>
        ))}
      </div>
    </footer>
  );
};

export default Footer;
