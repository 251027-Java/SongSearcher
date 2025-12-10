import { MAX_LENGTH } from "../constants";
import { FcLikePlaceholder } from "react-icons/fc";
import { FaTrash } from "react-icons/fa";

const SongSearchItem = ({ song }) => {
  return (
    <div className="h-auto p-1 bg-mint-400 w-full rounded-xl">
      <div className="flex justify-between items-center">
        <div className="flex flex-col gap-1">
          <h2 className="font-bold text-md font-serif">{song.title}</h2>
          <div className="flex gap-1">
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
        <div className="flex flex-col gap-6 mr-3">
          <button className="">
            <FcLikePlaceholder />
          </button>
          <button className="">
            <FaTrash />
          </button>
        </div>
      </div>
    </div>
  );
};

export default SongSearchItem;
