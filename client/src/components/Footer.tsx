const Footer = () => {
  return (
    <footer className="footer w-screen">
      <div className="container mx-auto">
        <div className="w-90 border-t border-brown mb-4"></div>
      </div>
      {/* SMALL SCREEN NAVIGATION */}
      <div className="sm:hidden container mx-auto">
        <div className="flex items-center justify-center mb-4">
          <img
            src="src/assets/poetvine-larger-screen-logo.png"
            alt="Logo"
            className="w-24"
          />
        </div>
        <div className="text-center text-brown mb-4">
          <p className="font-light">© Copyright 2023 Poetvine</p>
        </div>
        <div className="flex flex-col items-center space-y-2 mt-20">
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
          <a
            href="/privacy-policy"
            className="text-lg text-brown font-light hover:underline"
          >
            Privacy policy
          </a>
          <a
            href="/terms-and-conditions"
            className="text-lg text-brown font-light hover:underline"
          >
            Terms and conditions
          </a>
        </div>
      </div>

      {/* MEDIUM + LARGER SCREEN NAVIGATION */}
      <div className="hidden sm:flex container mx-auto">
        <div className="w-1/2 mt-20">
          <div className="flex items-center justify-center mb-4">
            <img
              src="src/assets/poetvine-larger-screen-logo.png"
              alt="Logo"
              className="w-24"
            />
          </div>
          <div className="text-center text-brown mb-4">
            <p className="font-light">© Copyright 2023 Poetvine</p>
          </div>
        </div>
        <div className="w-1/2 flex flex-col items-center space-y-2 mt-20">
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
          <a
            href="/privacy-policy"
            className="text-lg text-brown font-light hover:underline"
          >
            Privacy policy
          </a>
          <a
            href="/terms-and-conditions"
            className="text-lg text-brown font-light hover:underline"
          >
            Terms and conditions
          </a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
