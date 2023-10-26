import type { Config } from "tailwindcss";

const config: Config = {
  darkMode: ["class"],
  content: [
    "./pages/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}",
    "./app/**/*.{ts,tsx}",
    "./src/**/*.{ts,tsx}",
  ],
  theme: {
    container: {
      center: true,
      padding: "2rem",
      screens: {
        "2xl": "1400px",
      },
    },
    extend: {
      colors: {
        brown: "#BA6535",
        "brown-textfield": "#E8CDAF",
        pale: "#F8F0D9",
        "gray-dark": "#1E1E1E",
        "gray-dark-textfield": "#75706B",
        "dark-pale": "#3A3A3A",
      },
      fontFamily: {
        ptSerif: ["var(--font-ptSerif)"],
      },
      boxShadow: {
        default: "0px 1px 10px 0px rgba(0, 0, 0, 0.35)",
      },
    },
  },
  plugins: [],
};
export default config;
