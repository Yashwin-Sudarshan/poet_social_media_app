import Footer from "@/components/shared/Footer";
import Navbar from "@/components/shared/navbar/Navbar";
import React, { ReactNode } from "react";

const Layout = ({ children }: { children: ReactNode }) => {
  return (
    <main>
      <Navbar />
      <section className="bg-pale dark:bg-gray-dark">
        <div>{children}</div>
      </section>
      <Footer />
    </main>
  );
};

export default Layout;
