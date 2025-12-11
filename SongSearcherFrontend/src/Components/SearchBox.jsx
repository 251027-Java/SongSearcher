import SongSearchItem from "./SongSearchItem";
import { useState, useMemo } from "react";
import { SEARCH_MODEL } from "../constants";
import { useSongsApi } from "../ApiHooks/useSongsApi";
import { useSongsSearch } from "../ApiHooks/useSongsSearch";
import Spinner from "./Spinner";
import { usePlaylistsApi } from "../ApiHooks/usePlaylistsApi";

const SearchBox = () => {
  const [searchModel, setSearchModel] = useState(SEARCH_MODEL.SONG_TITLE);
  const [searchQuery, setSearchQuery] = useState(null);
  const [search, setSearch] = useState("");
  const {
    similarSongs,
    searchSongsByTitle,
    searchSongsByAlbum,
    searchSongsByArtist,
  } = useSongsApi();
  const { addSongToPlaylist, userPlaylistsQuery, removeSongFromPlaylist } =
    usePlaylistsApi();

  const { data: playlists } = userPlaylistsQuery;
  const favoritePlaylist = useMemo(() => {
    if (!playlists) return null;
    return playlists.find((p) => p.name === "Favorites") || null;
  }, [playlists]);

  let placeholder;
  if (searchModel == SEARCH_MODEL.SONG_TITLE) {
    placeholder = "Song Title...";
  } else if (searchModel == SEARCH_MODEL.SONG_LYRICS) {
    placeholder = "Song Lyrics...";
  } else if (searchModel == SEARCH_MODEL.ALBUM) {
    placeholder = "Album Title...";
  } else if (searchModel == SEARCH_MODEL.ARTIST) {
    placeholder = "Artist Name...";
  }

  const searchChangeHandler = (e) => {
    setSearch(e.target.value);
  };

  const searchMutation = useSongsSearch({
    searchSongsByTitle,
    similarSongs,
    searchSongsByAlbum,
    searchSongsByArtist,
  });

  const submitSearchHandler = async () => {
    const result = await searchMutation.mutateAsync({
      model: searchModel,
      search,
    });

    setSearchQuery(result);
  };

  const toggleFavoriteHandler = async (songId) => {
    const isFav = favoritePlaylist?.songs.some((s) => s.id === songId);
    if (!isFav) {
      await addSongToPlaylist.mutateAsync({
        playlistId: favoritePlaylist.id,
        songId,
      });
    } else {
      await removeSongFromPlaylist.mutateAsync({
        playlistId: favoritePlaylist.id,
        songId,
      });
    }
  };

  const modelButtonClickHandler = (model) => {
    setSearchModel(model);
    setSearch("");
    setSearchQuery("");
  };

  return (
    <div className="flex flex-col gap-2 col-span-2 h-90 bg-slate-200 rounded-lg p-5">
      <h1 className="font-bold text-2xl">Search</h1>
      <div className="grid grid-cols-4 gap-1">
        <button
          className={`${
            searchModel == SEARCH_MODEL.SONG_TITLE
              ? "bg-mint-300"
              : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => modelButtonClickHandler(SEARCH_MODEL.SONG_TITLE)}
        >
          Song Title
        </button>
        <button
          className={`${
            searchModel == SEARCH_MODEL.SONG_LYRICS
              ? "bg-mint-300"
              : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => modelButtonClickHandler(SEARCH_MODEL.SONG_LYRICS)}
        >
          Song Lyrics
        </button>
        <button
          className={`${
            searchModel == SEARCH_MODEL.ALBUM ? "bg-mint-300" : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => modelButtonClickHandler(SEARCH_MODEL.ALBUM)}
        >
          Album
        </button>
        <button
          className={`${
            searchModel == SEARCH_MODEL.ARTIST ? "bg-mint-300" : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => modelButtonClickHandler(SEARCH_MODEL.ARTIST)}
        >
          Artist
        </button>
      </div>
      <div className="flex gap-1">
        <input
          className="bg-white rounded-md w-full border border-grey-200 px-1"
          placeholder={placeholder}
          value={search}
          onChange={searchChangeHandler}
        />
        <button
          className="bg-mint-400 border border-mint-400 p-1 px-2 rounded-md hover:bg-mint-300 hover:cursor-pointer"
          onClick={submitSearchHandler}
        >
          Search
        </button>
      </div>
      {searchMutation.isPending && <Spinner />}
      {searchMutation.isError && (
        <p className="color-red">Error: {searchMutation.error.message}</p>
      )}
      <div className="flex flex-col gap-2 overflow-auto">
        {searchQuery &&
          searchQuery.map((song) => (
            <SongSearchItem
              id={song.id}
              song={song}
              isFav={favoritePlaylist?.songs.some((s) => s.id === song.id)}
              toggleFavorite={toggleFavoriteHandler}
              resetSearch={submitSearchHandler}
            />
          ))}
      </div>
    </div>
  );
};

export default SearchBox;
