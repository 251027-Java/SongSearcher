import { useState } from "react";
import { FileMusic, Search, Sparkles } from 'lucide-react';
import LoginBox from "../Components/LoginBox";
import SignUpBox from "../Components/SignUpBox";

const LoginPage = () => {
  const [login, setLogin] = useState(true);

  const toggleLogin = () => {
    setLogin((prev) => !prev);
  };

  return (
    <main className="flex h-full w-full justify-around items-center theme-background">
      <div className="w-full max-w-[50vw] m-5 p-10 text-center rounded-xl">
        <div className="max-w-[80%] justify-center text-left pl-20">
          <div className="mb-15">
            <h1 className="text-5xl font-bold text-mint-600 mb-4 text-left">
              SongSearcher
            </h1>
            <br></br>
            <p className="text-lg">
              Discover your next favorite song by exploring music through lyrical similarity
            </p>
          </div>
          <div className="space-y-6">
            <div className="flex items-start gap-4">
              <div className="bg-mint-300 backdrop-blur-sm p-2.5 rounded-xl shrink-0">
                <Search className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-lg mb-1">Lyric-Based Search</h3>
                <p className="text-md">
                  Find songs with similar themes, emotions, and storytelling through intelligent lyric analysis
                </p>
              </div>
            </div>
            <div className="flex items-start gap-4">
              <div className="bg-mint-300 backdrop-blur-sm p-2.5 rounded-xl shrink-0">
                <FileMusic className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-lg mb-1">Custom Favorites Playlist</h3>
                <p className="text-md">
                  Add songs to your favorites playlist for quick access
                </p>
              </div>
            </div>
            <div className="flex items-start gap-4">
              <div className="bg-mint-300 backdrop-blur-sm p-2.5 rounded-xl shrink-0">
                <Sparkles className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-lg mb-1">Smart Recommendations</h3>
                <p className="text-md">
                  Get personalized suggestions based on your own favorite songs
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      {login ? (
        <LoginBox toggle={toggleLogin} />
      ) : (
        <SignUpBox toggle={toggleLogin} />
      )}
    </main>
  );
};

export default LoginPage;
