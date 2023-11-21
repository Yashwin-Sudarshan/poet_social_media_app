"use server"

export async function getFeaturedPoem() {
    const result = await fetch(process.env.POETVINE_API_URL + "/poems?filter=FEATURED", {
        method: "GET",
        // headers: {
        //     "Authorization": "Bearer <token>"
        // },
        cache: "no-store"
    }).then(response => response.json())
    .catch((error) => console.log(error))

    return result.poems[0];
}