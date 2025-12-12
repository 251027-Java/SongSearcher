import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { apiClient } from "./ApiClient";

export const usePlaylistsApi = () => {
  const queryClient = useQueryClient();

  const userPlaylistsQuery = useQuery({
    queryKey: ["playlists"],
    queryFn: () => apiClient(`/playlists/user`),
  });

  const usePlaylist = (id) =>
    useQuery({
      queryKey: ["playlist", id],
      queryFn: () => apiClient(`/playlists/${id}`),
      enabled: !!id,
    });

  const createPlaylist = useMutation({
    mutationFn: (playlist) =>
      apiClient("/playlists", {
        method: "POST",
        body: playlist,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries(["playlists"]);
    },
  });

  const updatePlaylist = useMutation({
    mutationFn: ({ id, data }) =>
      apiClient(`/playlists/${id}`, {
        method: "PUT",
        body: data,
      }),
    onSuccess: (updatedPlaylist) => {
      queryClient.invalidateQueries(["playlist", updatedPlaylist.id]);
    },
  });

  const deletePlaylist = useMutation({
    mutationFn: (id) =>
      apiClient(`/playlists/${id}`, {
        method: "DELETE",
      }),
    onSuccess: () => {
      queryClient.invalidateQueries(["playlists"]);
    },
  });

  const addSongToPlaylist = useMutation({
    mutationFn: async ({ playlistId, songId }) => {
      return apiClient(`/playlists/addSong/${playlistId}`, {
        method: "POST",
        body: { song_id: songId },
      });
    },
    onSuccess: (updatedPlaylist) => {
      queryClient.invalidateQueries(["playlist", updatedPlaylist.id]);
      queryClient.invalidateQueries(["playlists"]);
    },
  });

  const removeSongFromPlaylist = useMutation({
    mutationFn: async ({ playlistId, songId }) => {
      return apiClient(`/playlists/removeSong/${playlistId}`, {
        method: "POST",
        body: { song_id: songId },
      });
    },
    onSuccess: (updatedPlaylist) => {
      queryClient.invalidateQueries(["playlist", updatedPlaylist.id]);
      queryClient.invalidateQueries(["playlists"]);
    },
  });

  return {
    userPlaylistsQuery,
    usePlaylist,
    createPlaylist,
    updatePlaylist,
    deletePlaylist,
    addSongToPlaylist,
    removeSongFromPlaylist,
  };
};
