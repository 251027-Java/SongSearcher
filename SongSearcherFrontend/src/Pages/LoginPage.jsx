import { useState } from "react";

const LoginPage = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handlePassChange = (e) => {
        setPassword(e.target.value);
    }

    const handleUsernameChange = (e) => {
        setUsername(e.target.value);
    }

    const handleLogin = () => {
        console.log("Login attempted with username: " + username + " and password: " + password);
        setPassword("");
        setUsername("");
    }

  return (
    <div className="flex h-full w-full items-center justify-center">
      <div className="w-xl m-5 p-10 text-center">
        <h1 className="text-4xl font-bold mb-4">
          Welcome to the Song Searcher website!
        </h1>
        <p className="text-lg">
          Song Searcher is the perfect place to find new songs that you will
          enjoy! Simply pick some favorite songs and get recommendations, or
          search for similar songs based on artist, album, or song.
        </p>
        <p className="text-lg">Please login or create a new account to get started!</p>
      </div>
      <div className="flex flex-col items-center justify-center border border-slate-400 bg-slate-200 rounded-2xl m-5 py-10 px-15">
        <h2 className="text-lg font-medium mb-2">Username</h2>
        <input className="bg-grey-200 mb-2 rounded-sm p-1 border border-grey-500" type="text" onChange={handleUsernameChange} value={username}/>
        <h2 className="text-lg font-medium mb-2">Password</h2>
        <input className="bg-grey-200 mb-3 rounded-sm p-1 border border-grey-500" type="password" onChange={handlePassChange} value={password}/>
        <button className="bg-mint-300 p-1 px-2 border border-mint-500 rounded-md hover:bg-mint-400 hover:cursor-pointer" type="submit" onClick={handleLogin}>Login</button>
      </div>
    </div>
  );
};

export default LoginPage;
