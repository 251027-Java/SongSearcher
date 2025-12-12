import { MAX_LENGTH } from "../constants";
import {
  TrashIcon,
  HeartIcon as HeartOutlineIcon,
} from "@heroicons/react/24/outline";
import { HeartIcon as HeartSolidIcon } from "@heroicons/react/24/solid";
import DeleteConfirmDialog from "./DeleteConfirmDialog";
import { useState } from "react";
import { useSongsApi } from "../ApiHooks/useSongsApi";

const SongSearchItem = ({ song, resetSearch, toggleFavorite, isFav }) => {
  const [open, setOpen] = useState(false);
  const { deleteSong } = useSongsApi();

  const openDialogHandler = () => {
    setOpen(true);
  };

  const deleteHandler = async () => {
    const response = await deleteSong.mutateAsync(song.id);
    console.log(response);
    setOpen(false);
    resetSearch();
  };

  return (
    <div className="h-auto p-3 bg-mint-400 w-full rounded-xl">
      <div className="flex justify-between items-center">
        <div className="flex flex-col gap-1">
          <h2 className="font-bold text-md font-serif">{song.title}</h2>
          <div className="flex ml-2">
            {song.artists.map((artist, index) => (
              <h3 key={artist.id} className="text-sm font-bold font-serif">
                {index !== 0 && ", "}{artist.name}
              </h3>
            ))}
          </div>
          <p className="text-sm font-serif ml-2">{song.album.title}</p>
          <p className="text-sm font-serif text-ash-grey-700 ml-2">
            {song.lyrics.substring(0, MAX_LENGTH) + "..."}
          </p>
        </div>
        <div className="flex flex-col gap-5 mr-1">
          <button
            className="p-1 hover:bg-red-200 hover:cursor-pointer rounded-full"
            onClick={() => toggleFavorite(song.id)}
          >
            {isFav ? (
              <HeartSolidIcon className="size-6 text-red-500" />
            ) : (
              <HeartOutlineIcon className="size-6 text-red-500" />
            )}
          </button>
          <button
            onClick={openDialogHandler}
            className="p-1 hover:bg-mint-200 hover:cursor-pointer rounded-full"
          >
            <TrashIcon className="size-6" />
          </button>
        </div>
      </div>
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
