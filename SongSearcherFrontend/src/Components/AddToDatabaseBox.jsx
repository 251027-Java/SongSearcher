import { useState } from "react";
import { MODEL_TYPE } from "../constants";
import AddSongForm from "./AddSongForm";
import AddAlbumForm from "./AddAlbumForm";
import AddArtistForm from "./AddArtistForm";
import { useSongsApi } from "../ApiHooks/useSongsApi";
import { useAlbumsApi } from "../ApiHooks/useAlbumsApi";
import { useArtistsApi } from "../ApiHooks/useArtistsApi";

const AddToDatabaseBox = () => {
  const [modelType, setModelType] = useState(MODEL_TYPE.SONG);
  const { createSong } = useSongsApi();
  const { createAlbum } = useAlbumsApi();
  const { createArtist } = useArtistsApi();

  const submitHandler = (data) => {
    if (modelType == MODEL_TYPE.SONG) {
      createSong.mutateAsync(data);
    } else if (modelType == MODEL_TYPE.ALBUM) {
      createAlbum.mutateAsync(data);
    } else if (modelType == MODEL_TYPE.ARTIST) {
      createArtist.mutateAsync(data);
    }
  };

  return (
    <div className="flex flex-col col-span-2 bg-slate-200 rounded-lg p-5 relative">
      <div className="flex gap-2 items-center">
        <h1 className="font-bold">Add to database</h1>
        <button
          className={`${
            modelType == MODEL_TYPE.SONG ? "bg-mint-300" : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => setModelType(MODEL_TYPE.SONG)}
        >
          Song
        </button>
        <button
          className={`${
            modelType == MODEL_TYPE.ALBUM ? "bg-mint-300" : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => setModelType(MODEL_TYPE.ALBUM)}
        >
          Album
        </button>
        <button
          className={`${
            modelType == MODEL_TYPE.ARTIST ? "bg-mint-300" : "bg-mint-500"
          } p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-300 hover:cursor-pointer`}
          onClick={() => setModelType(MODEL_TYPE.ARTIST)}
        >
          Artist
        </button>
      </div>
      {modelType == MODEL_TYPE.SONG && <AddSongForm onSubmit={submitHandler} />}
      {modelType == MODEL_TYPE.ARTIST && (
        <AddArtistForm onSubmit={submitHandler} />
      )}
      {modelType == MODEL_TYPE.ALBUM && (
        <AddAlbumForm onSubmit={submitHandler} />
      )}
    </div>
  );
};

export default AddToDatabaseBox;
