import { MAX_LENGTH } from "../constants";
import { FcLikePlaceholder } from "react-icons/fc";
import { FaTrash } from "react-icons/fa";

const SearchSongItem = ({ song }) => {
  return (
    <div className="relative h-auto p-1 bg-mint-400 w-full rounded-xl">
      <div className="flex flex-col gap-1">
        <h2 className="font-bold text-md font-serif">{song.title}</h2>
        <div>
          {song.artists.map((artist) => (
            <h3 id={artist.id} className="text-sm font-bold font-serif">
              {artist.name}
            </h3>
          ))}
        </div>
        <p className="text-sm font-serif">{song.album.title}</p>
        <p className="text-sm font-serif">
          {song.lyrics.substring(0, MAX_LENGTH) + "..."}
        </p>
      </div>
      <button className="absolute right-5 top-0">
        <FcLikePlaceholder />
      </button>
      <button className="absolute right-5 bottom-0">
        <FaTrash />
      </button>
    </div>
  );
};

export default SearchSongItem;
