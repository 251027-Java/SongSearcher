import RecommendationsBox from "../Components/RecommendationsBox";
import SearchBox from "../Components/SearchBox";
import FavoritesBox from "../Components/FavoritesBox";
import CRUDBox from "../Components/CRUDBox";

const DashboardPage = () => {
  return (
    <div className="grid grid-cols-3 gap-4 p-5">
      <SearchBox />
      <FavoritesBox />
      <CRUDBox />
      <RecommendationsBox />
    </div>
  );
};

export default DashboardPage;
