import { MAX_LENGTH } from "../constants";
import {
  TrashIcon,
  HeartIcon as HeartOutlineIcon,
} from "@heroicons/react/24/outline";
import { HeartIcon as HeartSolidIcon } from "@heroicons/react/24/solid";
import DeleteConfirmDialog from "./Dialogs/DeleteConfirmDialog";
import SongModal from "./SongModal";
import { useState } from "react";
import { useSongsApi } from "../ApiHooks/useSongsApi";

const SongSearchItem = ({ song, resetSearch, toggleFavorite, isFav }) => {
  const [open, setOpen] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const { deleteSong } = useSongsApi();

  const openDialogHandler = () => {
    setOpen(true);
  };

  const openModalHandler = () => setModalOpen(true);
  const closeModalHandler = () => setModalOpen(false);

  const deleteHandler = async () => {
    const response = await deleteSong.mutateAsync(song.id);
    console.log(response);
    setOpen(false);
    resetSearch();
  };

  return (
    
      <div
        className="h-auto p-2 bg-mint-400 w-full rounded-xl"
      >
      <div className="flex justify-between items-center">
          <div className="flex flex-col gap-1 w-[90%]"
            onClick={openModalHandler}
            role="button"
            tabIndex={0}>
          <h2 className="font-bold text-sm font-serif">{song.title}</h2>
          <div className="flex ml-2">
            {song.artists.map((artist, index) => (
              <h3 key={artist.id} className="text-xs font-bold font-serif">
                {index !== 0 && ", "}{artist.name}
              </h3>
            ))}
          </div>
          <p className="text-xs font-serif ml-2">{song.album.title}</p>
          <p className="text-xs font-serif text-ash-grey-700 ml-2">
            {song.lyrics.substring(0, MAX_LENGTH) + "..."}
          </p>
        </div>
          <div className="flex flex-col gap-5 mr-2">
          <button
            className="px-1 hover:bg-red-200 hover:cursor-pointer rounded-full"
            onClick={(e) => { e.stopPropagation(); toggleFavorite(song.id); }}
          >
            {isFav ? (
              <HeartSolidIcon className="size-6 text-red-500" />
            ) : (
              <HeartOutlineIcon className="size-6 text-red-500" />
            )}
          </button>
          <button
            onClick={(e) => { e.stopPropagation(); openDialogHandler(); }}
            className="px-1 hover:bg-mint-200 hover:cursor-pointer rounded-full"
          >
            <TrashIcon className="size-6" />
          </button>
        </div>
      </div>
      <SongModal
        open={modalOpen}
        setOpen={setModalOpen}
        song={song}
      />
      <DeleteConfirmDialog
        open={open}
        setOpen={setOpen}
        onDelete={deleteHandler}
        song={song}
      />
    </div>
  );
};

export default SongSearchItem;
