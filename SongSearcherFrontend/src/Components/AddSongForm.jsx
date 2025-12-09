const AddSongForm = ({ onSubmit }) => {
  const submitHandler = (event) => {
    event.preventDefault();

    const data = new FormData(event.currentTarget);
    const artists = data.get("artists").split(",");
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
              className="border rounded-sm px-1 flex-1 min-w-0"
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
              className="border rounded-sm px-1 flex-1 min-w-0"
              type="number"
              step="1"
              min="0"
              id="length"
              name="length"
              placeholder="Song length..."
            />
          </div>
          <div className="flex w-full min-w-0">
            <label className="px-1 mx-1" for="artists">
              Artist(s):
            </label>
            <input
              required
              className="border rounded-sm px-1 flex-1 min-w-0"
              type="text"
              id="artists"
              name="artists"
              placeholder="Artist name(s)..."
            />
          </div>
          <div className="flex w-full min-w-0">
            <label className="px-1 mx-1" for="album">
              Album:
            </label>
            <input
              required
              className="border rounded-sm px-1 flex-1 min-w-0"
              type="text"
              id="album"
              name="album"
              placeholder="Album name..."
            />
          </div>
        </div>
        <div className="flex w-full flex-2">
          <label className="px-1 mx-1" for="lyrics">
            Lyrics:
          </label>
          <textarea
            required
            className="border rounded-sm px-1 resize-none w-full min-w-20"
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
