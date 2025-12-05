import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { apiClient } from "./ApiClient";

export const useArtistsApi = () => {
  const queryClient = useQueryClient();

  const artistsQuery = useQuery({
    queryKey: ["artists"],
    queryFn: () => apiClient("/artists"),
  });

  const useArtist = (id) =>
    useQuery({
      queryKey: ["artist", id],
      queryFn: () => apiClient(`/artists/${id}`),
      enabled: !!id,
    });

  const createArtist = useMutation({
    mutationFn: (artist) =>
      apiClient("/artists", {
        method: "POST",
        body: artist,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries(["artists"]);
    },
  });

  const deleteArtist = useMutation({
    mutationFn: (id) =>
      apiClient(`/artists/${id}`, {
        method: "DELETE",
      }),
    onSuccess: () => {
      queryClient.invalidateQueries(["artists"]);
    },
  });

  return {
    artistsQuery,
    useArtist,
    createArtist,
    deleteArtist
  }
};
