import { HeartIcon } from "@heroicons/react/24/solid";

const SongSearchItem = ({ song, unFavoriteSong }) => {

  return (
    <div className="h-auto w-full p-1 my-1 bg-mint-400 w-full rounded-xl">
      <div className="flex justify-between items-center">
        <div className="flex flex-col gap-1">
          <h2 className="font-bold text-md font-serif">{song.title}</h2>
          <div className="flex gap-1">
            {song.artists.map((artist) => (
              <h3 id={artist.id} className="text-sm font-bold font-serif">
                {artist.name}
              </h3>
            ))}
          </div>
          <p className="text-sm font-serif">{song.album.title}</p>
        </div>
        <div className="flex flex-col gap-5 mr-3">
          <button className="p-1 hover:bg-red-200 hover:cursor-pointer rounded-full" onClick={() => unFavoriteSong(song.id)}>
            <HeartIcon className="size-6 text-red-500" />
          </button>
        </div>
      </div>
    </div>
  );
};

export default SongSearchItem;