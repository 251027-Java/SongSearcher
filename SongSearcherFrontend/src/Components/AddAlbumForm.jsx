const AddAlbumForm = ({ onSubmit }) => {
  const submitHandler = (event) => {
    event.preventDefault();

    const data = new FormData(event.currentTarget);
    const artists = data.get("artists").split(",");
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
            className="border rounded-sm px-1 flex-1 min-w-0"
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
            className="border rounded-sm px-1 flex-1 min-w-0"
            type="number"
            step="1"
            min="0"
            id="release-year"
            name="release-year"
            placeholder="Album release year..."
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
      </div>
      <div>
        <button
          type="submit"
          className="bg-mint-300 bottom-0 right-2 p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-400 hover:cursor-pointer"
        >
          Add Album
        </button>
      </div>
    </form>
  );
};

export default AddAlbumForm;
