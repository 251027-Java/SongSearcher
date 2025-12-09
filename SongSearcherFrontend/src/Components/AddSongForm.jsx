import { useArtistsApi } from "../ApiHooks/useArtistsApi";
import { useAlbumsApi } from "../ApiHooks/useAlbumsApi";
import { useState } from "react";
import Select from "react-select";

const AddSongForm = ({ onSubmit }) => {
  const { artistsQuery } = useArtistsApi();
  const { albumsQuery } = useAlbumsApi();
  const [selectedAlbum, setSelectedAlbum] = useState(null);

  const { data: artistsData, isLoading: isLoadingArtists } = artistsQuery;
  const { data: albumsData, isLoading: isLoadingAlbums } = albumsQuery;

  const artists = artistsData ?? [];
  const albums = albumsData ?? [];

  const albumOptions = albums.map((album) => ({
    value: album.title,
    label: album.title,
    album: album,
  }));

  const selectedArtistName = selectedAlbum?.artists?.[0]?.name;
  const additionalArtists = selectedArtistName
    ? artists.filter((a) => a.name !== selectedArtistName)
    : artists;

  const addArtistOptions = additionalArtists.map((artist) => ({
    value: artist.name,
    label: artist.name,
    artist: artist,
  }));

  const albumSelectHandler = (selectedOption) => {
    setSelectedAlbum(selectedOption);
  };

  const submitHandler = (event) => {
    event.preventDefault();

    const data = new FormData(event.currentTarget);
    const artists = [selectedAlbum?.artists[0],data.get("artists")];
    onSubmit({
      title: data.get("title"),
      length: Math.round(data.get("length")),
      lyrics: data.get("lyrics"),
      artists: artists,
      album: data.get("album"),
    });
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
              Length:
            </label>
            <input
              required
              className="border border-grey-200 rounded-sm px-1 flex-1 min-w-0 bg-white"
              type="number"
              step="1"
              min="0"
              id="length"
              name="length"
              placeholder="Song length..."
            />
          </div>
          <div className="flex w-full items-center">
            <label className="px-1 mx-1" for="album">
              Album:
            </label>
            <Select
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
          className="bg-mint-300 bottom-0 right-2 p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-400 hover:cursor-pointer"
        >
          Add Song
        </button>
      </div>
    </form>
  );
};

export default AddSongForm;
