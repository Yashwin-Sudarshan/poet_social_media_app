import Image from "next/image";
import Link from "next/link";
import React from "react";

interface Props {
  profile_image_name: string;
  username: string;
  number_of_followers: number;
  number_of_poems_published: number;
  topics_written_about: string[];
}

const UserCard = ({
  profile_image_name,
  username,
  number_of_followers,
  number_of_poems_published,
  topics_written_about,
}: Props) => {
  return (
    <div
      className="w-[280px] rounded-[10px] bg-brown px-8 py-5 text-pale shadow-default dark:bg-dark-pale dark:shadow-none 
       min-[500px]:max-[750px]:w-[353px] min-[500px]:max-[750px]:px-10 md:max-[860px]:w-[320px] md:max-[860px]:px-8 
       min-[860px]:max-lg:w-[353px] min-[860px]:max-lg:px-10 min-[1110px]:w-[320px] xl:w-[353px] xl:px-10"
    >
      <div className="flex items-center gap-5">
        <Image
          alt={`${username}'s profile picture`}
          src="assets/icons/profile.svg"
          height={53}
          width={53}
        />
        <h2 className="text-[32px] font-bold text-pale max-[430px]:text-2xl">
          {username}
        </h2>
      </div>
      <p className="mt-5 text-lg max-[430px]:text-base">
        <span className="text-[30px] font-bold max-[430px]:text-2xl">
          {number_of_poems_published}
        </span>{" "}
        poems published
      </p>
      <p className="mt-5 text-lg max-[430px]:text-base">
        <span className="text-[30px] font-bold max-[430px]:text-2xl">
          {number_of_followers}
        </span>{" "}
        followers
      </p>
      <div className="mt-5 text-lg max-[430px]:text-base">
        <p>
          Writes about{" "}
          {topics_written_about.map((topic, index) => (
            <Link
              key={index}
              href={`?tag=${topic}`}
              className="hover:underline"
            >
              {`#${topic} `}
            </Link>
          ))}
        </p>
      </div>
      <div className="mt-5 flex justify-end">
        <Link
          href={`/poems/user/${username}`}
          className="group flex items-center gap-[5px] text-lg font-bold transition hover:translate-x-2
          max-[430px]:text-base"
        >
          Read their poems
          <Image
            src="/assets/icons/right-arrow.svg"
            alt="arrow"
            width={40}
            height={40}
          />
        </Link>
      </div>
    </div>
  );
};

export default UserCard;
