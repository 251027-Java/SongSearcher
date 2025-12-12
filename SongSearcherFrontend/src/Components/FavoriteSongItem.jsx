import { HeartIcon } from "@heroicons/react/24/solid";

const FavoriteSongItem = ({ song, unFavoriteSong }) => {

  return (
    <div className="h-auto w-full p-2 my-1 bg-mint-400 w-full rounded-xl">
      <div className="flex justify-between items-center">
        <div className="flex flex-col gap-1">
          <h2 className="font-bold text-sm font-serif">{song.title}</h2>
          <div className="flex ml-2">
            {song.artists.map((artist, index) => (
              <h3 id={artist.id} className="text-xs font-bold font-serif">
                {index !== 0 && ", "}{artist.name}
              </h3>
            ))}
          </div>
          <p className="text-xs font-serif ml-2">{song.album.title}</p>
        </div>
        <div className="flex flex-col gap-5 mr-1">
          <button className="p-1 hover:bg-red-200 hover:cursor-pointer rounded-full" onClick={() => unFavoriteSong(song.id)}>
            <HeartIcon className="size-6 text-red-500" />
          </button>
        </div>
      </div>
    </div>
  );
};

export default FavoriteSongItem;