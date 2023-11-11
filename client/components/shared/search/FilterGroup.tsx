import React from "react";
import SearchFilterGroup from "./SearchFilterGroup";
import ColumnLayoutSelectorGroup from "./ColumnLayoutSelectorGroup";

const FilterGroup = () => {
  return (
    <div className="mt-5 flex items-center">
      <SearchFilterGroup type="poems" />
      <ColumnLayoutSelectorGroup />
    </div>
  );
};

export default FilterGroup;
