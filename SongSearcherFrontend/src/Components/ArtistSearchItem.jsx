import { FaTrash } from "react-icons/fa";

const ArtistSearchItem = ({ artist }) => {
  return (
    <div className="h-auto p-1 bg-mint-400 w-full rounded-xl">
      <div className="flex justify-between items-center">
        <h2 className="font-bold text-md font-serif">{artist.name}</h2>
        <button className="">
          <FaTrash />
        </button>
      </div>
    </div>
  );
};

export default ArtistSearchItem;
