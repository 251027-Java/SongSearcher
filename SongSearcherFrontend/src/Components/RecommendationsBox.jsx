import { useMemo } from "react";
import { useSongsApi } from "../ApiHooks/useSongsApi";
import { usePlaylistsApi } from "../ApiHooks/usePlaylistsApi";
import Spinner from "./Spinner";
import RecommendedSongItem from "./RecommendedSongItem";

const RecommendationsBox = ({playlists}) => {
  const { recommendedSongsQuery } = useSongsApi();

  const { addSongToPlaylist } = usePlaylistsApi();

  const {
    data: songs,
    isLoading: isRecLoading,
    isError,
  } = recommendedSongsQuery;

  const favoritePlaylist = useMemo(() => {
    if (!playlists) return null;
    return playlists.find((p) => p.name === "Favorites") || null;
  }, [playlists]);

  const favoriteSong = (songId) => {
    addSongToPlaylist.mutateAsync({
      playlistId: favoritePlaylist.id,
      songId,
    });
  };

  return (
    <div className="flex flex-col gap-1 col-span-1 h-90 bg-slate-200 rounded-lg p-5 items-center">
      <h1 className="font-bold text-2xl">Recommendations</h1>
      <div className="flex flex-col gap-2 overflow-auto">
        {isError ? (
          <p className="text-red-500">Something went wrong!</p>
        ) : isRecLoading ? (
          <Spinner />
        ) : (
          songs &&
          songs.map((song) => (
            <RecommendedSongItem
              key={song.id}
              song={song}
              favoriteSong={favoriteSong}
            />
          ))
        )}
      </div>
    </div>
  );
};

export default RecommendationsBox;
