import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { apiClient } from "./ApiClient";

export const useSongsApi = () => {
  const queryClient = useQueryClient();

  const songsQuery = useQuery({
    queryKey: ["songs"],
    queryFn: () => apiClient("/songs"),
  });

  const useSong = (id) =>
    useQuery({
      queryKey: ["song", id],
      queryFn: () => apiClient(`/songs/${id}`),
      enabled: !!id,
    });

  // The searches are not a mutation as I am using a single mutation to wrap them all with useSongsSearch
  const searchSongsByTitle = (title) =>
    apiClient(`/songs/search/title/${title}`);

  const searchSongsByAlbum = (album) =>
    apiClient(`/songs/search/album/${album}`);

  const searchSongsByArtist = (name) =>
    apiClient(`/songs/search/artist/${name}`);

  const similarSongs = (lyrics) =>
    apiClient("/songs/search/similar", {
      method: "POST",
      body: lyrics,
    });

  const createSong = useMutation({
    mutationFn: (song) =>
      apiClient("/songs", {
        method: "POST",
        body: song,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries(["songs"]);
    },
  });

  const deleteSong = useMutation({
    mutationFn: (id) =>
      apiClient(`/songs/${id}`, {
        method: "DELETE",
      }),
    onSuccess: () => {
      queryClient.invalidateQueries(["songs"]);
    },
  });

  return {
    songsQuery,
    useSong,
    searchSongsByTitle,
    searchSongsByAlbum,
    searchSongsByArtist,
    createSong,
    similarSongs,
    deleteSong,
  };
};
