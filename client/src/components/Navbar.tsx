import { useState, useEffect } from "react";
import { Button } from "./ui/button";

const Navbar = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const handleMenuClick = (event: React.MouseEvent<HTMLDivElement>) => {
    event.preventDefault();

    setIsMenuOpen((prev) => !prev);
  };

  const handleMenuOverlayClick = (event: React.MouseEvent<HTMLDivElement>) => {
    event.preventDefault();

    setIsMenuOpen(false);
  };

  useEffect(() => {}, [isMenuOpen]);

  return (
    <header className="header w-screen">
      {/* SMALL SCREEN NAVIGATION */}
      <nav className="sm:hidden flex items-center justify-between px-2 py-2 relative">
        <div className="flex items-center">
          <img
            src="src/assets/poetvine-mobile-logo.png"
            alt="The poetvine logo"
          />
        </div>
        <div className="flex items-center space-x-4">
          <Button className="text-sm rounded-full bg-cream border-brown border-2 text-brown font-normal hover:bg-dark-brown hover:border-dark-brown hover:text-cream">
            Login
          </Button>
          <Button className="text-sm rounded-full bg-brown border-brown border-2 text-cream font-normal hover:bg-dark-brown hover:border-dark-brown">
            Signup
          </Button>
        </div>
        <div
          className="flex items-center cursor-pointer"
          onClick={handleMenuClick}
        >
          {isMenuOpen ? (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              className="w-12 h-12 text-brown"
            >
              <path
                fillRule="evenodd"
                d="M5.47 5.47a.75.75 0 011.06 0L12 10.94l5.47-5.47a.75.75 0 111.06 1.06L13.06 12l5.47 5.47a.75.75 0 11-1.06 1.06L12 13.06l-5.47 5.47a.75.75 0 01-1.06-1.06L10.94 12 5.47 6.53a.75.75 0 010-1.06z"
                clipRule="evenodd"
              />
            </svg>
          ) : (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              className="w-12 h-12 text-brown"
            >
              <path
                fillRule="evenodd"
                d="M3 6.75A.75.75 0 013.75 6h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 6.75zM3 12a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 12zm0 5.25a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75a.75.75 0 01-.75-.75z"
                clipRule="evenodd"
              />
            </svg>
          )}
        </div>
        {isMenuOpen && (
          <div
            className="absolute top-full left-0 w-full bg-cream"
            onClick={handleMenuOverlayClick}
          >
            <div className="flex flex-col items-center space-y-4 py-4">
              <a
                href="/poems"
                className="text-lg text-brown font-light hover:underline"
              >
                Poems
              </a>
              <a
                href="/poets"
                className="text-lg text-brown font-light hover:underline"
              >
                Poets
              </a>
              <a
                href="/about"
                className="text-lg text-brown font-light hover:underline"
              >
                About
              </a>
            </div>
          </div>
        )}
      </nav>

      {/* MEDIUM + LARGER SCREEN NAVIGATION */}
      <nav className="hidden sm:flex flex-row  items-center justify-center px-2 py-2 relative">
        <div className="w-1/3 text-right mx-2">
          <a
            href="/about"
            className="text-lg md:text-xl lg:text-2xl text-brown font-light hover:underline px-2 md:px-4 lg:px-8"
          >
            About
          </a>
          <a
            href="/poems"
            className="text-lg md:text-xl lg:text-2xl text-brown font-light hover:underline px-2 md:px-4 lg:px-8 xl:px-16"
          >
            Poems
          </a>
          <a
            href="/poets"
            className="text-lg md:text-xl lg:text-2xl text-brown font-light hover:underline px-2 md:px-4 lg:px-8 xl:px-16"
          >
            Poets
          </a>
        </div>
        <div className="flex items-center justify-center w-1/3">
          <img
            src="src/assets/poetvine-larger-screen-logo.png"
            alt="The larger poetvine logo"
            className="sm:w-24 md:w-32 lg:w-40 xl:w-48"
          />
        </div>
        <div className="w-1/3">
          <Button className="w-5/12 md:h-12 lg:h-14  xl:h-16  2xl:h-18 text-lg md:text-xl lg:text-2xl rounded-[20px] font-normal bg-cream border-brown border-2 text-brown hover:bg-dark-brown hover:border-dark-brown hover:text-cream mx-2">
            Login
          </Button>
          <Button className="w-5/12 md:h-12 lg:h-14 xl:h-16 2xl:h-18 text-lg md:text-xl lg:text-2xl rounded-[20px] font-normal bg-brown border-brown border-2 text-cream hover:bg-dark-brown hover:border-dark-brown mx-2">
            Signup
          </Button>
        </div>
      </nav>
    </header>
  );
};

export default Navbar;
