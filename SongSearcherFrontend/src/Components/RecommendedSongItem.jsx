import { HeartIcon } from "@heroicons/react/24/outline";
import { useState } from "react";
import SongModal from "./SongModal";

const RecommendedSongItem = ({ song, favoriteSong, showFavoriteButton }) => {

  const [modalOpen, setModalOpen] = useState(false);

  const openModalHandler = () => setModalOpen(true);
  const closeModalHandler = () => setModalOpen(false);

  return (
    <div className="h-auto w-full p-2 bg-mint-400 w-full rounded-xl"
        
      >
      <div className="flex justify-between items-center">
        <div className="flex flex-col gap-1"
            onClick={openModalHandler}
            role="button"
            tabIndex={0}>
          <h2 className="font-bold text-sm font-serif">{song.title}</h2>
          <div className="flex gap ml-2">
            {song.artists.map((artist, index) => (
              <h3 key={artist.id} className="text-xs font-bold font-serif">
                {index !== 0 && ", "}{artist.name}
              </h3>
            ))}
          </div>
          <p className="text-xs font-serif ml-2">{song.album.title}</p>
        </div>
        {showFavoriteButton && <div className="flex flex-col gap-5 mr-2">
          <button className="p-1 hover:bg-red-200 hover:cursor-pointer rounded-full" onClick={() => favoriteSong(song.id)}>
            <HeartIcon className="size-6 text-red-500" />
          </button>
        </div>}
      </div>
      <SongModal
        open={modalOpen}
        setOpen={setModalOpen}
        song={song}
      />
    </div>
  );
};

export default RecommendedSongItem;