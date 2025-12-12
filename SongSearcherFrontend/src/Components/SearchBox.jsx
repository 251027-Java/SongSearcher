import SongSearchItem from "./SongSearchItem";
import { useState } from "react";
import { SEARCH_MODEL } from "../constants";
import { useSongsApi } from "../ApiHooks/useSongsApi";
import { useSongsSearch } from "../ApiHooks/useSongsSearch";
import Spinner from "./Spinner";
import { usePlaylistsApi } from "../ApiHooks/usePlaylistsApi";
import { Search } from "lucide-react";

const SearchBox = ({ favoritePlaylist }) => {
  const [searchModel, setSearchModel] = useState(SEARCH_MODEL.SONG_TITLE);
  const [searchQuery, setSearchQuery] = useState(null);
  const [search, setSearch] = useState("");
  const {
    similarSongs,
    searchSongsByTitle,
    searchSongsByAlbum,
    searchSongsByArtist,
  } = useSongsApi();
  const { addSongToPlaylist, removeSongFromPlaylist } = usePlaylistsApi();

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
    if (search == "") {
      setSearchQuery(null);
      return;
    }
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

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      submitSearchHandler();
    }
  };

  return (
    <div className="flex flex-col theme-main gap-2 col-span-2 h-180 rounded-lg p-5">
      <div className="flex gap-1 items-center">
        <Search className="w-5 h-5" />
        <h1 className="font-bold text-2xl">Search</h1>
      </div>
      <div className="grid grid-cols-4 gap-1">
        <button
          className={`toggle-button ${
            searchModel == SEARCH_MODEL.SONG_TITLE
              ? "bg-mint-300"
              : "bg-mint-500"
          }`}
          onClick={() => modelButtonClickHandler(SEARCH_MODEL.SONG_TITLE)}
        >
          Song Title
        </button>
        <button
          className={`toggle-button ${
            searchModel == SEARCH_MODEL.SONG_LYRICS
              ? "bg-mint-300"
              : "bg-mint-500"
          }`}
          onClick={() => modelButtonClickHandler(SEARCH_MODEL.SONG_LYRICS)}
        >
          Song Lyrics
        </button>
        <button
          className={`toggle-button ${
            searchModel == SEARCH_MODEL.ALBUM ? "bg-mint-300" : "bg-mint-500"
          }`}
          onClick={() => modelButtonClickHandler(SEARCH_MODEL.ALBUM)}
        >
          Album
        </button>
        <button
          className={`toggle-button ${
            searchModel == SEARCH_MODEL.ARTIST ? "bg-mint-300" : "bg-mint-500"
          }`}
          onClick={() => modelButtonClickHandler(SEARCH_MODEL.ARTIST)}
        >
          Artist
        </button>
      </div>
      <div className="flex gap-1 mb-5">
        <input
          className="bg-white rounded-md w-full border border-grey-200 px-1"
          placeholder={placeholder}
          value={search}
          onKeyDown={handleKeyDown}
          onChange={searchChangeHandler}
        />
        <button className="submit-button" onClick={submitSearchHandler}>
          Search
        </button>
      </div>
      {searchMutation.isPending && <Spinner />}
      {searchMutation.isError && (
        <p className="text-red-500">Error: {searchMutation.error.message}</p>
      )}
      <div className="flex flex-col gap-2 px-2 overflow-auto">
        {searchQuery ? (
          searchQuery.map((song) => (
            <SongSearchItem
              key={song.id}
              song={song}
              isFav={favoritePlaylist?.songs.some((s) => s.id === song.id)}
              toggleFavorite={toggleFavoriteHandler}
              resetSearch={submitSearchHandler}
            />
          ))
        ) : (
          <p>Begin your search for songs!</p>
        )}
      </div>
    </div>
  );
};

export default SearchBox;
