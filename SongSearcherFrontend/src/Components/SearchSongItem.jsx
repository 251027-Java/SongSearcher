import { MAX_LENGTH } from "../constants";
import { FcLikePlaceholder } from "react-icons/fc";

const SearchSongItem = ({ song }) => {
  return (
    <div className="relative p-1 bg-mint-400 w-full rounded-xl">
      <div className="flex flex-col gap-1">
        <h2 className="font-bold text-md">{song.title}</h2>
        <div>
          {song.artists.map((artist) => (
            <h3 id={artist.id} className="text-sm font-bold">{artist.name}</h3>
          ))}
        </div>
        <p className="text-sm">{song.album.title}</p>
        <p className="text-sm">{song.lyrics.substring(0, MAX_LENGTH) + "..."}</p>
      </div>
      <button className="absolute right-5 top-10">
        <FcLikePlaceholder />
      </button>
    </div>
  );
};

export default SearchSongItem;
