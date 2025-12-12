import { useArtistsApi } from "../../ApiHooks/useArtistsApi";
import { useAlbumsApi } from "../../ApiHooks/useAlbumsApi";
import { useState } from "react";
import Select from "react-select";

const AddSongForm = ({ onSubmit }) => {
  const { artistsQuery } = useArtistsApi();
  const { albumsQuery } = useAlbumsApi();
  const [selectedAlbum, setSelectedAlbum] = useState(null);
  const [selectedArtists, setSelectedArtists] = useState(null);

  const { data: artistsData, isLoading: isLoadingArtists } = artistsQuery;
  const { data: albumsData, isLoading: isLoadingAlbums } = albumsQuery;

  const artists = artistsData ?? [];
  const albums = albumsData ?? [];

  const albumOptions = albums.map((album) => ({
    value: album,
    label: album.title,
  }));

  const selectedArtistName = selectedAlbum?.value.artists?.[0]?.name;
  const additionalArtists = selectedArtistName
    ? artists.filter((a) => a.name !== selectedArtistName)
    : artists;

  const addArtistOptions = additionalArtists.map((artist) => ({
    value: artist,
    label: artist.name,
  }));

  const albumSelectHandler = (selectedOption) => {
    setSelectedAlbum(selectedOption);
  };

  const addArtistsChangeHandler = (artist) => {
    setSelectedArtists(artist);
  };

  const submitHandler = (event) => {
    event.preventDefault();

    const data = new FormData(event.currentTarget);
    const addArtists = !selectedArtists ? [] : selectedArtists.map(
      (artistOption) => artistOption.value
    );
    onSubmit({
      title: data.get("title"),
      length: Math.round(data.get("length")),
      lyrics: data.get("lyrics"),
      album: selectedAlbum.value,
      artists: addArtists,
    });
    event.target.reset();
    setSelectedAlbum(null);
    setSelectedArtists(null);
  };

  return (
    <form className="p-2 w-full flex flex-col gap-2" onSubmit={submitHandler}>
      <div className="w-full flex gap-2">
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
              placeholder="Song name..."
            />
          </div>
          <div className="flex w-full min-w-0">
            <label className="px-1 mx-1" for="length">
              Length (seconds):
            </label>
            <input
              required
              className="border border-grey-200 rounded-sm px-1 flex-1 min-w-0 bg-white"
              type="number"
              step="1"
              min="0"
              id="length"
              name="length"
              placeholder="Song length in seconds..."
            />
          </div>
          <div className="flex w-full items-center">
            <label className="px-1 mx-1" for="album">
              Album:
            </label>
            <Select
              required
              className="w-full"
              name="album"
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
              value={selectedAlbum}
              onChange={albumSelectHandler}
              options={albumOptions}
              isLoading={isLoadingAlbums}
              isSearchable={true}
              classNamePrefix="select"
            />
          </div>
          <div className="flex w-full min-w-0">
            <label className="px-1 mx-1" for="primary-artist">
              Primary Artist:
            </label>
            <input
              required
              disabled
              value={selectedAlbum ? selectedAlbum.value.artists[0].name : ""}
              className="border border-grey-200 rounded-sm px-1 flex-1 min-w-0 bg-white text-grey-400"
              type="text"
              id="primary-artist"
              name="primary-artist"
            />
          </div>
          <div className="flex w-full items-center">
            <label className="px-1 mx-1" for="artists">
              Additional Artists:
            </label>
            <Select
              isMulti
              name="artists"
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
              value={selectedArtists}
              onChange={addArtistsChangeHandler}
              options={addArtistOptions}
              isLoading={isLoadingArtists}
              isClearable={true}
              isSearchable={true}
              classNamePrefix="select"
            />
          </div>
        </div>
        <div className="flex w-full flex-1">
          <label className="px-1 mx-1" for="lyrics">
            Lyrics:
          </label>
          <textarea
            required
            className="border border-grey-200 rounded-sm px-1 resize-none w-full min-w-20 bg-white"
            type="text"
            id="lyrics"
            name="lyrics"
            placeholder="Song lyrics..."
          />
        </div>
      </div>
      <div>
        <button
          type="submit"
          className="submit-button"
        >
          Add Song
        </button>
      </div>
    </form>
  );
};

export default AddSongForm;
