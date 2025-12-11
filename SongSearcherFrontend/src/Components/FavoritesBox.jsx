import { useMemo } from "react";
import FavoriteSongItem from "./FavoriteSongItem";
import { usePlaylistsApi } from "../ApiHooks/usePlaylistsApi";
import Spinner from "./Spinner";

const FavoritesBox = () => {
  const { userPlaylistsQuery, removeSongFromPlaylist } = usePlaylistsApi();
  const { data: playlists, isLoading } = userPlaylistsQuery;

  const favoritePlaylist = useMemo(() => {
    if (!playlists) return null;
    return playlists.find((p) => p.name === "Favorites") || null;
  }, [playlists]);

  const unFavoriteSong = (songId) => {
    removeSongFromPlaylist.mutateAsync({
      playlistId: favoritePlaylist.id,
      songId,
    });
  };

  return (
    <div className="flex flex-col items-center col-span-1 h-90 bg-slate-200 rounded-lg p-5">
      <h1 className="text-lg font-bold">Favorites</h1>
      {isLoading ? (
        <Spinner />
      ) : (
        favoritePlaylist && (
          <ul className="w-full overflow-auto">
            {favoritePlaylist.songs.map((song) => (
              <FavoriteSongItem
                key={song.id}
                song={song}
                unFavoriteSong={unFavoriteSong}
              />
            ))}
          </ul>
        )
      )}
    </div>
  );
};

export default FavoritesBox;
