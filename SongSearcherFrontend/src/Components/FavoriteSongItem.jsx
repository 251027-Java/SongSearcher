const FavoriteSongItem = ({ song }) => {
    return (
        <li className="p-2 border border-gray-300 bg-mint-300 rounded-lg my-2 w-full">
            {song.title} by {song.artist}
        </li>   
    )
}

export default FavoriteSongItem;