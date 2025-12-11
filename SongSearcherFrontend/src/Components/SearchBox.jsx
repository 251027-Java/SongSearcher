import SongSearchItem from "./SongSearchItem";
import { useState } from "react";
import { SEARCH_MODEL } from "../constants";
import { useSongsApi } from "../ApiHooks/useSongsApi";

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

  const submitSearchHandler = async () => {
    if (searchModel == SEARCH_MODEL.SONG_TITLE) {
      setSearchQuery(await searchSongsByTitle.mutateAsync(search));
    } else if (searchModel == SEARCH_MODEL.SONG_LYRICS) {
      setSearchQuery(await similarSongs.mutateAsync({ lyrics: search }));
    } else if (searchModel == SEARCH_MODEL.ALBUM) {
      setSearchQuery(await searchSongsByAlbum.mutateAsync(search));
    } else if (searchModel == SEARCH_MODEL.ARTIST) {
      setSearchQuery(await searchSongsByArtist.mutateAsync(search));
    }
  };

  const modelButtonClickHandler = (model) => {
    setSearchModel(model);
    setSearch("");
    setSearchQuery("");
  }

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
      <div className="flex flex-col gap-2 overflow-auto">
        {searchQuery &&
          searchQuery.map((song) => (
            <SongSearchItem id={song.id} song={song} />
          ))}
      </div>
    </div>
  );
};

export default SearchBox;
