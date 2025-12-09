import { useState } from "react";
import LoginBox from "../Components/LoginBox";
import SignUpBox from "../Components/SignUpBox";

const LoginPage = () => {
  const [login, setLogin] = useState(true);

  const toggleLogin = () => {
    setLogin((prev) => !prev);
  };

  return (
    <main className="flex h-full w-full items-center justify-center">
      <div className="w-xl m-5 p-10 text-center">
        <h1 className="text-4xl font-bold mb-4">
          Welcome to the Song Searcher website!
        </h1>
        <p className="text-lg">
          Song Searcher is the perfect place to find new songs that you will
          enjoy! Simply pick some favorite songs and get recommendations, or
          search for similar songs based on artist, album, or song.
        </p>
        <p className="text-lg">
          Please login or create a new account to get started!
        </p>
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
