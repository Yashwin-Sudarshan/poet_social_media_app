"use server";

import { GetPoemsByFilterParams } from "@/types/shared.types";
import { searchPoems } from "../utils";

export async function getFeaturedPoem() {
  const result = await fetch(
    process.env.POETVINE_API_URL + "/poems?filter=FEATURED",
    {
      method: "GET",
      // headers: {
      //     "Authorization": "Bearer <token>"
      // },
      //   cache: "no-store",
      next: { revalidate: 86400 },
    }
  )
    .then((response) => response.json())
    .catch((error) => console.log(error));

  return result.poems[0];
}

export async function getPoemsByFilter(params: GetPoemsByFilterParams) {
  const {
    searchQuery,
    filter = "TOP_THIS_WEEK",
    page = 1,
    pageSize = 6,
  } = params;

  const result = await fetch(
    process.env.POETVINE_API_URL + `/poems?filter=${filter}`,
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

  // TODO: Modify API to support pagination. Below code is a workaround in the meantime
  const numAllPoemsReturned = result.poems.length;

  const endIndex = (page - 1) * pageSize + pageSize;

  const resultsAfterSearch = searchPoems(result.poems, searchQuery);

  const paginatedPoems = resultsAfterSearch.slice(0, endIndex);

  return { paginatedPoems, numAllPoemsReturned };
}
