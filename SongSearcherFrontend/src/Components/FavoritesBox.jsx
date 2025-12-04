import { useState } from "react";
import FavoriteSongItem from "./FavoriteSongItem";

const FavoritesBox = () => {
  // Temporary hardcoded favorite songs
 const [favoriteSongs] = useState([
    {title: "Test Song 1", artist: "Artist 1"},
    {title: "Test Song 2", artist: "Artist 2"},
    {title: "Test Song 3", artist: "Artist 3"},
  ]);

  return (
    <div className="flex flex-col items-center col-span-1 h-75 bg-slate-200 rounded-lg p-5">
      <h1 className="text-lg font-bold">Favorites</h1>
      <ul>
        {favoriteSongs.map((song, index) => (
          <FavoriteSongItem key={index} song={song} />
        ))}
      </ul>
    </div>
  );
};

export default FavoritesBox;
