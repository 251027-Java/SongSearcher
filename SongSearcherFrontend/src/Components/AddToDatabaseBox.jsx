import { useState } from "react";
import { MODEL_TYPE } from "../constants";
import AddSongForm from "./Forms/AddSongForm";
import AddAlbumForm from "./Forms/AddAlbumForm";
import AddArtistForm from "./Forms/AddArtistForm";
import { useSongsApi } from "../ApiHooks/useSongsApi";
import { useAlbumsApi } from "../ApiHooks/useAlbumsApi";
import { useArtistsApi } from "../ApiHooks/useArtistsApi";
import { toast, ToastContainer } from "react-toastify";
import { Plus } from "lucide-react";

const AddToDatabaseBox = () => {
  const [modelType, setModelType] = useState(MODEL_TYPE.SONG);
  const { createSong } = useSongsApi();
  const { createAlbum } = useAlbumsApi();
  const { createArtist } = useArtistsApi();

  const submitHandler = (data) => {
    if (modelType == MODEL_TYPE.SONG) {
      createSong.mutateAsync(data);
      toast(`Successfully added a ${modelType}!`);
    } else if (modelType == MODEL_TYPE.ALBUM) {
      createAlbum.mutateAsync(data);
      toast(`Successfully added an ${modelType}!`);
    } else if (modelType == MODEL_TYPE.ARTIST) {
      createArtist.mutateAsync(data);
      toast(`Successfully added an ${modelType}!`);
    }
  };

  return (
    <div className="flex flex-col gap-2 theme-main col-span-3 h-90 rounded-lg p-5 relative">
      <div className="flex gap-2">
        <div className="flex items-center gap-1">
          <Plus className="h-5 w-5" />
          <h1 className="font-bold text-2xl">Add to database</h1>
        </div>
        <button
          className={`toggle-button ${
            modelType == MODEL_TYPE.SONG ? "bg-mint-300" : "bg-mint-500"
          }`}
          onClick={() => setModelType(MODEL_TYPE.SONG)}
        >
          Song
        </button>
        <button
          className={`toggle-button ${
            modelType == MODEL_TYPE.ALBUM ? "bg-mint-300" : "bg-mint-500"
          }`}
          onClick={() => setModelType(MODEL_TYPE.ALBUM)}
        >
          Album
        </button>
        <button
          className={`toggle-button ${
            modelType == MODEL_TYPE.ARTIST ? "bg-mint-300" : "bg-mint-500"
          }`}
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
      <ToastContainer
        position="bottom-center"
        autoClose={2000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick={false}
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
    </div>
  );
};

export default AddToDatabaseBox;
