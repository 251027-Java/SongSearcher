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

  const searchSongsByTitle = useMutation({
    mutationFn: (title) => apiClient(`/songs/search/title/${title}`),
  });

  const searchSongsByAlbum = useMutation({
    mutationFn: (title) => apiClient(`/songs/search/album/${title}`),
  });

  const searchSongsByArtist = useMutation({
    mutationFn: (name) => apiClient(`/songs/search/artist/${name}`),
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

  const similarSongs = useMutation({
    mutationFn: (lyrics) =>
      apiClient("/songs/similar", {
        method: "POST",
        body: lyrics,
      }),
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
