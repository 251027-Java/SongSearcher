import RecommendationsBox from "../Components/RecommendationsBox";
import SearchBox from "../Components/SearchBox";
import FavoritesBox from "../Components/FavoritesBox";
import AddToDatabaseBox from "../Components/AddToDatabaseBox";
import { usePlaylistsApi } from "../ApiHooks/usePlaylistsApi";

const DashboardPage = () => {
  const {userPlaylistsQuery} = usePlaylistsApi();

  const {data: playlists, isLoading} = userPlaylistsQuery;
  
  return (
    <>
      {/* Empty div is spacing for the AppBar */}
      <div className="h-[3rem]"/>
      <main className="grid grid-cols-3 gap-4 p-5 overflow-auto">
        <SearchBox playlists={playlists}/>
        <FavoritesBox playlists={playlists} isLoading={isLoading}/>
        <AddToDatabaseBox />
        <RecommendationsBox playlists={playlists}/>
      </main>
    </>
  );
};

export default DashboardPage;
