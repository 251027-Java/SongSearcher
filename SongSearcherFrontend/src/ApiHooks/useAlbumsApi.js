import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { apiClient } from "./ApiClient";

export const useAlbumsApi = () => {
  const queryClient = useQueryClient();

  const albumsQuery = useQuery({
    queryKey: ["albums"],
    queryFn: () => apiClient("/albums"),
  });

  const useAlbum = (id) =>
    useQuery({
      queryKey: ["album", id],
      queryFn: () => apiClient(`/albums/${id}`),
      enabled: !!id,
    });

  const createAlbum = useMutation({
    mutationFn: (album) =>
      apiClient("/albums", {
        method: "POST",
        body: album,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries(["albums"]);
    },
  });

  const deleteAlbum = useMutation({
    mutationFn: (id) =>
      apiClient(`/albums/${id}`, {
        method: "DELETE",
      }),
    onSuccess: () => {
      queryClient.invalidateQueries(["albums"]);
    },
  });

  return {
    albumsQuery,
    useAlbum,
    createAlbum,
    deleteAlbum
  }
};
