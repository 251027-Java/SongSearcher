import SearchSongItem from "./SearchSongItem";
import { useState } from "react";
import { SEARCH_MODEL } from "../constants";

const SearchBox = () => {
  const [searchModel, setSearchModel] = useState(SEARCH_MODEL.SONG_TITLE);
  const [search, setSearch] = useState("");

  const searchChangeHandler = (e) => {
    setSearch(e.target.value);
  };

  const submitSearchHandler = () => {
    if (searchModel == SEARCH_MODEL.SONG_TITLE) {
      //Query for song by title
    } else if (searchModel == SEARCH_MODEL.SONG_LYRICS) {
      // Query similar by lyrics
    } else if (searchModel == SEARCH_MODEL.ALBUM) {
      // Query for album
    } else if (searchModel == SEARCH_MODEL.ARTIST) {
      // Query for artist
    }
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
          onClick={() => setSearchModel(SEARCH_MODEL.SONG_TITLE)}
        >
          Song Title
        </button>
        <button
          className={`${
            searchModel == SEARCH_MODEL.SONG_LYRICS
              ? "bg-mint-300"
              : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => setSearchModel(SEARCH_MODEL.SONG_LYRICS)}
        >
          Song Lyrics
        </button>
        <button
          className={`${
            searchModel == SEARCH_MODEL.ALBUM ? "bg-mint-300" : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => setSearchModel(SEARCH_MODEL.ALBUM)}
        >
          Album
        </button>
        <button
          className={`${
            searchModel == SEARCH_MODEL.ARTIST ? "bg-mint-300" : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => setSearchModel(SEARCH_MODEL.ARTIST)}
        >
          Artist
        </button>
      </div>
      <div className="flex gap-1">
        <input
          className="bg-white rounded-md w-full border border-grey-200 px-1"
          value={search}
          onChange={searchChangeHandler}
        />
        <button
          className="bg-mint-400 border border-mint-400 p-1 px-2 rounded-md hover:bg-mint-300"
          onClick={submitSearchHandler}
        >
          Search
        </button>
      </div>
    </div>
  );
};

export default SearchBox;
