import { useMutation } from "@tanstack/react-query";
import { SEARCH_MODEL } from "../constants";

export function useSongsSearch({
  searchSongsByTitle,
  similarSongs,
  searchSongsByAlbum,
  searchSongsByArtist
}) {
  return useMutation({
    mutationFn: async ({ model, search }) => {
      switch (model) {
        case SEARCH_MODEL.SONG_TITLE:
          return searchSongsByTitle(search);
        case SEARCH_MODEL.SONG_LYRICS:
          return similarSongs({ lyrics: search });
        case SEARCH_MODEL.ALBUM:
          return searchSongsByAlbum(search);
        case SEARCH_MODEL.ARTIST:
          return searchSongsByArtist(search);
        default:
          throw new Error("Unknown search model: " + model);
      }
    }
  });
}