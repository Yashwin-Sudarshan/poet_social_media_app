import React from "react";
import Searchbar from "@/components/shared/search/Searchbar";
import FilterGroup from "@/components/shared/search/FilterGroup";
import PoemCard from "@/components/shared/PoemCard";

// Remove this once api is integrated
import { MockPoemData, MockTrendingPoetsData } from "@/constants";
import { Button } from "@/components/ui/button";
import UserCard from "@/components/shared/UserCard";
import Link from "next/link";

const Page = () => {
  return (
    <div
      className="mx-5 flex max-[1040px]:gap-0 max-[749px]:flex-col min-[500px]:mx-10
      md:max-[860px]:justify-center lg:justify-center lg:gap-4 xl:gap-x-[30px] min-[1330px]:mx-20
      min-[1440px]:mx-[140px]"
    >
      <div
        className="max-[749px]:w-full min-[750px]:max-w-[600px] min-[750px]:border-r min-[750px]:border-r-brown/30 
          min-[750px]:dark:border-r-dark-pale min-[750px]:max-[860px]:w-1/2 min-[860px]:max-lg:w-3/4 
          min-[1110px]:max-w-[680px] xl:max-w-[763px]"
      >
        <div className="max-w-[664px] pt-10 max-[430px]:pt-5 min-[750px]:mr-5">
          <h1 className="text-5xl font-bold text-brown dark:text-pale max-[430px]:text-2xl">
            Poems
          </h1>
          <Searchbar placeholder="Search by author, title, tags, or content..." />
          <FilterGroup />
        </div>
        <div
          className="min-xl:gap-7 mt-20 flex flex-col justify-center gap-5 max-[750px]:items-center
            min-[750px]:flex-row min-[750px]:flex-wrap lg:justify-start"
        >
          {MockPoemData.map((poem, index) => (
            <PoemCard
              key={index}
              id={poem.id}
              title={poem.title}
              content={poem.content}
              author_username={poem.author_username}
              created_at={poem.created_at}
              tags={poem.tags}
              number_of_likes={poem.number_of_likes}
              number_of_comments={poem.number_of_comments}
              hasLiked={false}
            />
          ))}
        </div>
        <div
          className="text-center max-[376px]:justify-center max-[360px]:flex
          min-[1024px]:mr-4 min-[1295px]:mr-9"
          // className="flex justify-center"
        >
          <Button
            className="mb-40 mt-20 h-14 w-[280px] rounded-[10px] border-2 border-brown text-2xl font-bold
          text-brown hover:border-brown hover:bg-brown hover:text-pale dark:border-pale dark:text-pale
          dark:hover:border-pale dark:hover:bg-pale dark:hover:text-gray-dark max-[430px]:mb-0 max-[430px]:mt-10
          max-[430px]:text-xl min-[500px]:max-[750px]:w-[320px] min-[749px]:max-lg:w-5/6 lg:w-[364px]"
          >
            See More
          </Button>
        </div>
      </div>
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
              id={MockPoemData[0].id}
              title={MockPoemData[0].title}
              content={MockPoemData[0].content}
              author_username={MockPoemData[0].author_username}
              created_at={MockPoemData[0].created_at}
              tags={MockPoemData[0].tags}
              number_of_likes={MockPoemData[0].number_of_likes}
              number_of_comments={MockPoemData[0].number_of_comments}
              hasLiked={false}
            />
          </div>
        </div>
        <div className="mt-20 sm:max-[750px]:mt-10">
          <h2 className="text-[32px] font-bold text-brown dark:text-pale max-[430px]:text-xl">
            Trending Poets
          </h2>
          <div className="mt-10 flex flex-col gap-5 max-[430px]:mt-5">
            {MockTrendingPoetsData.map((user, index) => (
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
