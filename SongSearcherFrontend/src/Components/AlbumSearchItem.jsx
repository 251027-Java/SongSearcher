import { FaTrash } from "react-icons/fa";

const AlbumSearchItem = ({ album }) => {
  return (
    <div className="h-auto p-1 bg-mint-400 w-full rounded-xl">
      <div className="flex justify-between items-center">
        <div className="flex flex-col gap-1">
          <h2 className="font-bold text-md font-serif">{album.title}</h2>
          <div className="flex gap-1">
            {album.artists.map((artist) => (
              <h3 id={artist.id} className="text-sm font-bold font-serif">
                {artist.name}
              </h3>
            ))}
          </div>
          <p className="text-sm font-serif">{album.releaseDate}</p>
        </div>
        <button className="">
          <FaTrash />
        </button>
      </div>
    </div>
  );
};

export default AlbumSearchItem;
