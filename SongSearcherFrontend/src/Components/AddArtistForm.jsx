const AddArtistForm = ({ onSubmit }) => {
  const submitHandler = (event) => {
    event.preventDefault();

    const data = new FormData(event.currentTarget);
    onSubmit({ name: data.get("name") });
  };

  return (
    <form className="p-2 flex flex-col gap-2" onSubmit={submitHandler}>
      <div className="flex">
        <label className="px-1 mx-1" for="name">
          Name:
        </label>
        <input
          required
          className="border border-grey-200 rounded-sm px-1 w-full bg-white"
          type="text"
          id="name"
          name="name"
          placeholder="Artist name..."
        />
      </div>
      <div>
        <button
          type="submit"
          className="bg-mint-300 bottom-0 right-2 p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-400 hover:cursor-pointer"
        >
          Add Artist
        </button>
      </div>
    </form>
  );
};

export default AddArtistForm;
