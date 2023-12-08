import SignUp from "@/components/forms/SignUp";
import Link from "next/link";
import React from "react";

const Page = () => {
  return (
    <div className="mx-auto block min-h-screen max-w-xl justify-center py-20 max-[620px]:px-5 max-[430px]:pt-10">
      <h1
        className="pb-5 text-center text-5xl font-bold text-brown dark:text-pale max-[430px]:pb-[10px]
        max-[430px]:text-2xl"
      >
        Sign up
      </h1>
      <p
        className="pb-10 text-center text-lg text-brown dark:text-pale max-[430px]:pb-5
        max-[430px]:text-base"
      >
        Already have an account?{" "}
        <Link className="font-bold underline" href="/login">
          Log in
        </Link>
        .
      </p>
      <SignUp />
    </div>
  );
};

export default Page;
