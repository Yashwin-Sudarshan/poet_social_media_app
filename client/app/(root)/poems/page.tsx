import React from "react";
import PoemCard from "@/components/shared/PoemCard";

import UserCard from "@/components/shared/UserCard";
import Link from "next/link";
import PoemsLayout from "@/components/shared/PoemsLayout";
import { getFeaturedPoem, getPoemsByFilter } from "@/lib/actions/poem.action";
import { formatTimestamp } from "@/lib/utils";
import { SearchParamsProps } from "@/types";
import { getTrendingPoets } from "@/lib/actions/user.action";

const Page = async ({ searchParams }: SearchParamsProps) => {
  const result = await getPoemsByFilter({
    searchQuery: searchParams.q,
    filter: searchParams.filter,
    page: searchParams.page ? +searchParams.page : 1,
  });
  const featuredPoem = await getFeaturedPoem();
  const trendingPoets = await getTrendingPoets(3);

  return (
    <div
      className="mx-5 flex min-h-screen max-[1040px]:gap-0 max-[749px]:flex-col
      min-[500px]:mx-10 md:max-[860px]:justify-center lg:justify-center lg:gap-4 xl:gap-x-[30px]
      min-[1330px]:mx-20 min-[1440px]:mx-[140px]"
    >
      <PoemsLayout
        filteredPoems={result.paginatedPoems}
        numAllPoems={result.numAllPoemsReturned}
      />
      <div
        className="max-[750px]:flex max-[750px]:flex-col max-[750px]:items-center
         max-[750px]:gap-y-[30px] max-[430px]:mt-20 min-[750px]:max-[1040px]:ml-5"
      >
        <div className="mt-10 max-[750px]:mt-0">
          <h2 className="text-[32px] font-bold text-brown dark:text-pale max-[430px]:text-xl">
            Featured Poem
          </h2>
          <div className="mt-10 max-[750px]:mt-5">
            <PoemCard
              id={featuredPoem.id}
              title={featuredPoem.title}
              content={featuredPoem.content}
              author_username={featuredPoem.author_username}
              created_at={formatTimestamp(featuredPoem.created_at)}
              tags={featuredPoem.tags}
              number_of_likes={featuredPoem.number_of_likes}
              number_of_comments={featuredPoem.number_of_comments}
              hasLiked={false}
            />
          </div>
        </div>
        <div className="mt-20 sm:max-[750px]:mt-10">
          <h2 className="text-[32px] font-bold text-brown dark:text-pale max-[430px]:text-xl">
            Trending Poets
          </h2>
          <div className="mt-10 flex flex-col gap-5 max-[430px]:mt-5">
            {trendingPoets.map((user, index) => (
              <UserCard
                key={index}
                profile_image_name={user.profile_image_name}
                username={user.username}
                number_of_followers={user.number_of_followers}
                number_of_poems_published={user.number_of_poems_published}
                topics_written_about={user.topics_written_about}
              />
            ))}
            <Link
              className="mb-40 mt-10 flex h-14 w-full items-center justify-center rounded-[10px] border-2 border-brown text-2xl
                font-bold text-brown transition-colors hover:border-brown hover:bg-brown hover:text-pale
              dark:border-pale dark:text-pale dark:hover:border-pale dark:hover:bg-pale dark:hover:text-gray-dark
                max-[430px]:text-xl"
              href="/poets"
            >
              Discover more poets
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Page;