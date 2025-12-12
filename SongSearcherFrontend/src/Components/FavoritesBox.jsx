import FavoriteSongItem from "./FavoriteSongItem";
import { usePlaylistsApi } from "../ApiHooks/usePlaylistsApi";
import Spinner from "./Spinner";

const FavoritesBox = ({favoritePlaylist, isLoading}) => {
  const { removeSongFromPlaylist } = usePlaylistsApi();

  const unFavoriteSong = (songId) => {
    removeSongFromPlaylist.mutateAsync({
      playlistId: favoritePlaylist.id,
      songId,
    });
  };

  return (
    <div className="flex flex-col items-center col-span-1 h-90 bg-slate-200 rounded-lg p-5">
      <h1 className="text-2xl font-bold">Favorites</h1>
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
