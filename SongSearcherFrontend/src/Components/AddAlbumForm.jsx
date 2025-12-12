import { useArtistsApi } from "../ApiHooks/useArtistsApi";
import Select from "react-select";
import { useState } from "react";

const AddAlbumForm = ({ onSubmit }) => {
  const { artistsQuery } = useArtistsApi();
  const { data, isLoading } = artistsQuery;
  const [mainArtists, setMainArtists] = useState([]);

  const artists = data ?? [];

  const artistOptions = artists.map((artist) => ({
    value: artist,
    label: artist.name,
  }));

  const mainArtistsChangeHandler = (artists) => {
    setMainArtists(artists);
  };

  const submitHandler = (event) => {
    event.preventDefault();

    const data = new FormData(event.currentTarget);
    const artists = mainArtists.map((artistOption) => artistOption.value);
    onSubmit({
      title: data.get("title"),
      releaseYear: Math.round(data.get("release-year")),
      artists: artists,
    });
  };

  return (
    <form className="p-2 w-full flex flex-col gap-2" onSubmit={submitHandler}>
      <div className="flex flex-col gap-2 flex-1 min-w-40">
        <div className="flex w-full min-w-0">
          <label className="px-1 mx-1" for="title">
            Name:
          </label>
          <input
            required
            className="border border-grey-200 rounded-sm px-1 flex-1 min-w-0 bg-white"
            type="text"
            id="title"
            name="title"
            placeholder="Album name..."
          />
        </div>
        <div className="flex w-full min-w-0">
          <label className="px-1 mx-1" for="release-year">
            Release Year:
          </label>
          <input
            required
            className="border border-grey-200 rounded-sm px-1 flex-1 min-w-0 bg-white"
            type="number"
            step="1"
            min="0"
            id="release-year"
            name="release-year"
            placeholder="Album release year..."
          />
        </div>
        <div className="flex w-full items-center">
          <label className="px-1 mx-1" for="main-artists">
            Main Artist:
          </label>
          <Select
            isMulti
            required
            name="main-artists"
            styles={{
              container: (base) => ({
                ...base,
                width: "100%",
              }),
              valueContainer: (base) => ({
                ...base,
                padding: "0 4px",
                maxHeight: "100px",
                overflowY: "auto",
              }),
            }}
            value={mainArtists}
            onChange={mainArtistsChangeHandler}
            options={artistOptions}
            isLoading={isLoading}
            isSearchable={true}
            classNamePrefix="select"
          />
        </div>
      </div>
      <div>
        <button
          type="submit"
          className="submit-button"
        >
          Add Album
        </button>
      </div>
    </form>
  );
};

export default AddAlbumForm;
