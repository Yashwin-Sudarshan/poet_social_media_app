"use server";

interface User {
  profile_image_name: string;
  username: string;
  number_of_followers: number;
  number_of_poems_published: number;
  topics_written_about: string[];
}

export async function getTrendingPoets(limit: number) {
  const result = await fetch(
    process.env.POETVINE_API_URL + "/users?filter=TOP_THIS_WEEK",
    {
      method: "GET",
      // headers: {
      //     "Authorization": "Bearer <token>"
      // },
      cache: "no-store",
    }
  )
    .then((response) => response.json())
    .catch((error) => console.log(error));

  // Ensure that result.users is an array before trying to slice it
  const usersArray: User[] = Array.isArray(result.users) ? result.users : [];

  // Return the first `limit` elements of the array
  return usersArray.slice(0, limit);
}
