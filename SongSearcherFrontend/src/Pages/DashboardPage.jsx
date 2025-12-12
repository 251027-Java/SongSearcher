import { useMemo } from "react";
import RecommendationsBox from "../Components/RecommendationsBox";
import SearchBox from "../Components/SearchBox";
import FavoritesBox from "../Components/FavoritesBox";
import AddToDatabaseBox from "../Components/AddToDatabaseBox";
import { usePlaylistsApi } from "../ApiHooks/usePlaylistsApi";

const DashboardPage = () => {
  const { userPlaylistsQuery } = usePlaylistsApi();

  const { data: playlists, isLoading } = userPlaylistsQuery;

  const favoritePlaylist = useMemo(() => {
    if (!playlists) return null;
    return playlists.find((p) => p.name === "Favorites") || null;
  }, [playlists]);

  return (
    <div className="theme-background px-10">
      {/* Empty div is spacing for the AppBar */}
      <div className="h-[3rem]" />
      <main className="grid grid-cols-3 gap-4 p-5 overflow-auto">
        <SearchBox favoritePlaylist={favoritePlaylist} />
        <FavoritesBox favoritePlaylist={favoritePlaylist} isLoading={isLoading} />
        <AddToDatabaseBox />
        <RecommendationsBox favoritePlaylist={favoritePlaylist} />
      </main>
    </div>
  );
};

export default DashboardPage;
