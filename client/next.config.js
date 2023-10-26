/** @type {import('next').NextConfig} */
const nextConfig = {
  async redirects() {
    return [
      {
        source: "/",
        destination: "/poems",
        permanent: false,
      },
    ];
  },
};

module.exports = nextConfig;
