import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { apiClient } from "./ApiClient";

export const usePlaylistsApi = () => {
  const queryClient = useQueryClient();

  const playlistsQuery = useQuery({
    queryKey: ["playlists"],
    queryFn: () => apiClient("/playlists"),
  })

  const useUserPlaylists = (userId) =>
    useQuery({
      queryKey: ["userPlaylists", userId],
      queryFn: () => apiClient(`/playlists/user/${userId}`),
      enabled: !!userId,
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
      queryClient.invalidateQueries(["userPlaylists"]);
      queryClient.invalidateQueries(["playlists"]);
    },
  });

  const updatePlaylist = useMutation({
    mutationFn: ({ id, data }) =>
      apiClient(`/playlists/${id}`, {
        method: "PUT",
        body: data,
      }),
    onSuccess: (_, { id }) => {
      queryClient.invalidateQueries(["playlist", id]);
      queryClient.invalidateQueries(["userPlaylists"]);
    },
  });

  const deletePlaylist = useMutation({
    mutationFn: (id) =>
      apiClient(`/playlists/${id}`, {
        method: "DELETE",
      }),
    onSuccess: () => {
      queryClient.invalidateQueries(["playlists"]);
      queryClient.invalidateQueries(["userPlaylists"]);
    },
  });

  const addSongToPlaylist = useMutation({
    mutationFn: async ({ playlist, song }) => {
      const updated = {
        ...playlist,
        songs: [...playlist.songs, song],
      };

      return apiClient(`/playlists/${playlist.id}`, {
        method: "PUT",
        body: updated,
      });
    },
    onSuccess: (_, { playlist }) => {
      queryClient.invalidateQueries(["playlist", playlist.id]);
      queryClient.invalidateQueries(["userPlaylists", playlist.userId]);
    },
  });

  const removeSongFromPlaylist = useMutation({
    mutationFn: async ({ playlist, songId }) => {
      const updated = {
        ...playlist,
        songs: playlist.songs.filter((s) => s.id !== songId),
      };

      return apiClient(`/playlists/${playlist.id}`, {
        method: "PUT",
        body: updated,
      });
    },
    onSuccess: (_, { playlist }) => {
      queryClient.invalidateQueries(["playlist", playlist.id]);
      queryClient.invalidateQueries(["userPlaylists", playlist.userId]);
    },
  });

  return {
    playlistsQuery,
    useUserPlaylists,
    usePlaylist,
    createPlaylist,
    updatePlaylist,
    deletePlaylist,
    addSongToPlaylist,
    removeSongFromPlaylist,
  };
};
