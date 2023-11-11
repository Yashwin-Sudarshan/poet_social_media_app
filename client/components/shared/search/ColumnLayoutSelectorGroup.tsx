"use client";

import React, { useState } from "react";

const ColumnLayoutSelectorGroup = () => {
  const [doubleColumnIsActive, setDoubleColumnIsActive] = useState(true);
  const [singleColumnIsActive, setSingleColumnIsActive] = useState(false);

  return (
    <>
      <div
        className={`mx-10 flex h-[42px] w-[42px] items-center justify-center rounded-full border-2 ${
          doubleColumnIsActive
            ? "border-brown dark:border-pale"
            : "border-brown/40 dark:border-pale/40"
        }
        hidden cursor-pointer p-0.5 lg:flex`}
        onClick={() => {
          if (!doubleColumnIsActive) {
            setDoubleColumnIsActive(true);
            setSingleColumnIsActive(false);
          }
        }}
      >
        <div className="flex w-1/2 flex-col gap-1">
          {Array(4)
            .fill(null)
            .map((_, i) => (
              <div
                key={i}
                className={`mx-0.5 h-[1px] ${
                  doubleColumnIsActive
                    ? "bg-brown dark:bg-pale"
                    : "bg-brown/40 dark:bg-pale/40"
                }`}
              />
            ))}
        </div>
        <div className="flex w-1/2 flex-col gap-1">
          {Array(4)
            .fill(null)
            .map((_, i) => (
              <div
                key={i}
                className={`mx-0.5 h-[1px] ${
                  doubleColumnIsActive
                    ? "bg-brown dark:bg-pale"
                    : "bg-brown/40 dark:bg-pale/40"
                }`}
              />
            ))}
        </div>
      </div>
      <div
        className={`flex h-[42px] w-[42px] items-center justify-center rounded-full border-2 ${
          singleColumnIsActive
            ? "border-brown dark:border-pale"
            : "border-brown/40 dark:border-pale/40"
        }
        hidden cursor-pointer p-0.5 lg:flex`}
        onClick={() => {
          if (!singleColumnIsActive) {
            setSingleColumnIsActive(true);
            setDoubleColumnIsActive(false);
          }
        }}
      >
        <div className="flex w-1/2 flex-col gap-1">
          {Array(4)
            .fill(null)
            .map((_, i) => (
              <div
                key={i}
                className={`mx-0.5 h-[1px] ${
                  singleColumnIsActive
                    ? "bg-brown dark:bg-pale"
                    : "bg-brown/40 dark:bg-pale/40"
                }`}
              />
            ))}
        </div>
      </div>
    </>
  );
};

export default ColumnLayoutSelectorGroup;
