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
    createSong,
    deleteSong
  }
};
