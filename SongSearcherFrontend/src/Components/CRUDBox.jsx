import { useState } from "react";
import { MODEL_TYPE } from "../constants";
import AddSongForm from "./AddSongForm";
import AddAlbumForm from "./AddAlbumForm";
import AddArtistForm from "./AddArtistForm";
import { useSongsApi } from "../ApiHooks/useSongsApi";
import { useAlbumsApi } from "../ApiHooks/useAlbumsApi";
import { useArtistsApi } from "../ApiHooks/useArtistsApi";

const CRUDBox = () => {
  const [modelType, setModelType] = useState(MODEL_TYPE.SONG);
  const { createSong } = useSongsApi();
  const { createAlbum } = useAlbumsApi();
  const { createArtist } = useArtistsApi();
  const title = modelType == MODEL_TYPE.SONG ? "Add a:" : "Add an:";

  const submitHandler = (data) => {
    if (modelType == MODEL_TYPE.SONG) {
      createSong.mutateAsync(JSON.stringify(data));
    } else if (modelType == MODEL_TYPE.ALBUM) {
      createAlbum.mutateAsync(JSON.stringify(data));
    } else if (modelType == MODEL_TYPE.ARTIST) {
      createArtist.mutateAsync(JSON.stringify(data));
    }
  };

  return (
    <div className="flex flex-col col-span-2 h-75 bg-slate-200 rounded-lg p-5 relative">
      <div className="flex gap-2">
        <h1 className="font-bold">{title}</h1>
        <select
          className="font-bold"
          id="model-type"
          value={modelType}
          onChange={(e) => setModelType(e.target.value)}
        >
          <option value={MODEL_TYPE.SONG} className="font-bold">
            Song
          </option>
          <option value={MODEL_TYPE.ALBUM} className="font-bold">
            Album
          </option>
          <option value={MODEL_TYPE.ARTIST} className="font-bold">
            Artist
          </option>
        </select>
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

export default CRUDBox;
