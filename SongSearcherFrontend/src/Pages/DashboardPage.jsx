import RecommendationsBox from "../Components/RecommendationsBox";
import SearchBox from "../Components/SearchBox";
import FavoritesBox from "../Components/FavoritesBox";
import AddToDatabaseBox from "../Components/AddToDatabaseBox";

const DashboardPage = () => {
  return (
    <>
      {/* Empty div is spacing for the AppBar */}
      <div className="h-[3rem]"/>
      <main className="grid grid-cols-3 gap-4 p-5">
        <SearchBox />
        <FavoritesBox />
        <AddToDatabaseBox />
        <RecommendationsBox />
      </main>
    </>
  );
};

export default DashboardPage;
