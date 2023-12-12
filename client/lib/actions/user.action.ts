"use server";

import { CreateUserParams } from "@/types/shared.types";

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

export async function registerUser(params: CreateUserParams) {
  const { username, email, password } = params;

  const requestData = JSON.stringify({
    username,
    email,
    password,
  });

  try {
    const result = await fetch(
      process.env.POETVINE_API_URL + "/auth/register",
      {
        method: "POST",
        body: requestData,
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (result.ok) {
      const data = await result.json();

      return data.token;
    } else {
      const errorData = await result.json();

      if (result.status === 400) {
        throw new Error(errorData.error);
      } else {
        throw new Error("Unexpected error during registration");
      }
    }
  } catch (error: any) {
    console.error("Error during registration:", error.message);
    throw error;
  }
}
