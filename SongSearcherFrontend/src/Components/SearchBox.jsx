import SearchSongItem from "./SearchSongItem";

const SearchBox = () => {
  return (
    <div className="flex col-span-2 h-75 bg-slate-200 rounded-lg p-5">
      <SearchSongItem
        song={{
          id: "fake",
          title: "test",
          length: 0,
          lyrics:
            "We the people in order to form a more perfect union establish justice and provide something like houses.",
          album: { id: "fake", title: "Yas Queen", releaseYear: 1997 },
          artists: [{ id: "fake", name: "Lady Gaga" }],
        }}
      />
    </div>
  );
};

export default SearchBox;
