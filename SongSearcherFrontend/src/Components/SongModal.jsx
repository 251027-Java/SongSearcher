import { Dialog, DialogBackdrop, DialogPanel } from '@headlessui/react';
import { useState, useEffect } from 'react';
import { useSongsApi } from '../ApiHooks/useSongsApi';
import { SEARCH_MODEL } from "../constants";
import { usePlaylistsApi } from "../ApiHooks/usePlaylistsApi";
import Spinner from './Spinner';
import RecommendedSongItem from './RecommendedSongItem';

import { useSongsSearch } from "../ApiHooks/useSongsSearch";
import { HeartIcon as HeartOutlineIcon } from '@heroicons/react/24/outline';

const SongModal = ({ open, setOpen, song, isFav, favoritePlaylist }) => {
  const { useSong, similarSongsById } = useSongsApi();

  //const { data: fullSong, isLoading: songLoading } = useSong(song?.id);
  const [expanded, setExpanded] = useState(false);
  const [similarSongsData, setSimilarSongsData] = useState(null);
  const [loadingSimilar, setLoadingSimilar] = useState(false);
  const [errorSimilar, setErrorSimilar] = useState(null);

  const searchMutation = useSongsSearch({
      similarSongsById
    });
  
  const loadSimilar = async () => {
    if (!song?.id) return;
    setLoadingSimilar(true);
    setErrorSimilar(null);
    let search = song.id;
    try {
      const songs = await searchMutation.mutateAsync({
      model: SEARCH_MODEL.SONG_SIMILAR,
      search,
    });
      setSimilarSongsData(songs);
    } catch (err) {
      setErrorSimilar(err);
      console.error(err);
    } finally {
      setLoadingSimilar(false);
    }
  };

  const toggleLyrics = () => setExpanded((s) => !s);

  const onCloseHandler = () => {
    setOpen(false);
  };

  useEffect(() => {
    if (open) {
      loadSimilar();
    } else {
        setExpanded(false)
        setSimilarSongsData(null);
    }
  }, [open]);

  return (
    <Dialog open={open} onClose={onCloseHandler} className="relative z-10">
      <DialogBackdrop
        transition
        className="fixed inset-0 bg-ash-900/75 transition-opacity data-closed:opacity-0 data-enter:duration-300 data-enter:ease-out data-leave:duration-200 data-leave:ease-in"
      />

      <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
          <DialogPanel className="relative transform overflow-hidden rounded-lg bg-mint-800 text-left shadow-xl outline -outline-offset-1 outline-white/10 transition-all data-closed:translate-y-4 data-closed:opacity-0 data-enter:duration-300 data-enter:ease-out data-leave:duration-200 data-leave:ease-in sm:my-8 sm:w-full sm:max-w-3xl data-closed:sm:translate-y-0 data-closed:sm:scale-95">
            <div className="bg-mint-800 px-6 pt-5 pb-4 sm:p-6 sm:pb-4">
              <div className="flex flex-col sm:flex-row gap-4">
                <div className="flex-1">
                  <h2 className="text-2xl font-bold text-white">{song?.title || song.title}</h2>
                  <p className="text-md text-gray-400 mt-1">
                    {song?.artists?.map((a, idx) => (
                      <span key={a.id}>{idx !== 0 && ', '}{a.name}</span>
                    ))}
                  </p>
                  <p className="text-sm text-gray-400 mt-1">Album: {song?.album?.title || song.album.title}</p>

                  <div className="mt-4">
                    <div className="bg-mint-600/25 rounded-lg p-3">
                      <div className={`lyrics ${expanded ? 'expanded' : 'collapsed'} text-sm text-gray-200 whitespace-pre-wrap`}> 
                        {song?.lyrics || song.lyrics}
                      </div>
                      <div className="mt-2">
                        <button onClick={toggleLyrics} className="text-sm text-white/80 underline">
                          {expanded ? 'Show less' : 'Show more'}
                        </button>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="w-full sm:w-80">
                  <div className="bg-mint-700 rounded-lg p-3">
                    <div className="flex items-center justify-between">
                      <h3 className="text-white font-semibold">Similar Songs</h3>
                      <button onClick={loadSimilar} className="text-xs text-white/80 underline">Refresh</button>
                    </div>

                    <div className={`mt-2 overflow-auto ${expanded ? 'h-auto' : 'max-h-60'}`}>
                      {loadingSimilar && <Spinner />}
                      {errorSimilar && <p className="text-red-400">Failed to load similar songs</p>}
                      {!loadingSimilar && !errorSimilar && similarSongsData?.length === 0 && <p className="text-sm text-gray-400">No similar songs found</p>}

                      {similarSongsData?.map((s) => (
                        <div key={s.id} className="mb-2">
                          <RecommendedSongItem song={s} favoriteSong={() => {}} showFavoriteButton={false}/>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>

              </div>
            </div>
            <div className="bg-mint-600/25 px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6">
              <button onClick={onCloseHandler} className="mt-3 inline-flex w-full justify-center rounded-md bg-white/10 px-3 py-2 text-sm font-semibold text-white inset-ring inset-ring-white/5 hover:bg-white/20 sm:mt-0 sm:w-auto">Close</button>
            </div>
          </DialogPanel>
        </div>
      </div>
      <style>{`
        .lyrics.collapsed { max-height: 5.5rem; overflow: hidden; }
        .lyrics.expanded { max-height: none; }
      `}</style>
    </Dialog>
  );
};

export default SongModal;
