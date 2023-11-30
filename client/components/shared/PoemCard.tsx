/* eslint-disable camelcase */
import Image from "next/image";
import Link from "next/link";
import React from "react";

interface Props {
  id: number;
  title: string;
  content: string;
  author_username: string;
  created_at: Date;
  tags: string[];
  number_of_likes: number;
  number_of_comments: number;
  hasLiked: boolean;
}

const PoemCard = ({
  id,
  title,
  content,
  author_username,
  created_at,
  tags,
  number_of_likes,
  number_of_comments,
  hasLiked,
}: Props) => {
  return (
    <>
      <div
        className="relative w-[280px] rounded-[10px] bg-brown px-6 
          text-pale shadow-default dark:bg-dark-pale dark:shadow-none
          min-[500px]:max-[750px]:w-[353px] min-[500px]:max-[750px]:px-10 md:max-[860px]:w-[320px] 
          md:max-[860px]:px-8 min-[860px]:max-lg:w-[353px] min-[860px]:max-lg:px-10 
          lg:w-[280px] min-[1110px]:w-[320px] min-[1110px]:px-8 xl:w-[353px] xl:px-10"
      >
        <h2 className="py-5 text-center text-[32px] font-bold max-[430px]:text-2xl">
          {title}
        </h2>
        <p className="text-lg max-[430px]:text-base">{content}</p>

        <div className="mb-5 mt-10 flex items-center justify-between">
          <div className="flex gap-3 min-[500px]:max-[750px]:gap-5 min-[860px]:max-lg:gap-5 xl:gap-5">
            <div className="flex gap-2 min-[500px]:max-[750px]:gap-2.5 min-[860px]:max-lg:gap-2.5 xl:gap-2.5">
              <Image
                src={
                  hasLiked
                    ? "/assets/icons/heart-filled.svg"
                    : "/assets/icons/heart-outline.svg"
                }
                width={25}
                height={25}
                alt="like icon"
                className="cursor-pointer"
              />
              <span>{number_of_likes}</span>
            </div>
            <div className="flex gap-2 min-[500px]:max-[750px]:gap-2.5 min-[860px]:max-lg:gap-2.5 xl:gap-2.5">
              <Image
                src="/assets/icons/comment.svg"
                width={25}
                height={25}
                alt="comment icon"
                className="cursor-pointer"
              />
              <span>{number_of_comments}</span>
            </div>
          </div>
          <div className="flex flex-col items-center gap-y-1">
            <p className="text-lg font-bold">{author_username}</p>
            <p className="text-center text-xs">
              {created_at.toLocaleString("en-US")}
            </p>
          </div>
        </div>
        <div className="pb-10 text-center text-lg max-[430px]:text-base">
          {tags.map((tag, index) => (
            <Link key={index} href={`?tag=${tag}`} className="hover:underline">
              {`#${tag} `}
            </Link>
          ))}
        </div>
      </div>
    </>
  );
};

export default PoemCard;
