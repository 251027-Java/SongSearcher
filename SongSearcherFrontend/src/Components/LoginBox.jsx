import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../AuthContext";

const LoginBox = ({ toggle }) => {
  const { setIsAuthenticated } = useContext(AuthContext);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const nav = useNavigate();

  const handlePassChange = (e) => {
    setPassword(e.target.value);
  };

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  };

  const handleLogin = () => {
    // Placeholder login logic
    setIsAuthenticated(true);
    console.log(
      "Login with username: " + username + " and password: " + password
    );
    nav("/dashboard");
    setPassword("");
    setUsername("");
  };

  return (
    <div className="flex flex-col items-center justify-center border border-slate-400 bg-slate-200 rounded-2xl m-5 py-10 px-15 gap-1">
      <div className="flex flex-col items-center justify-center">
        <h2 className="text-lg font-medium">Username</h2>
        <input
          id="username"
          className="bg-grey-200 rounded-sm p-1 border border-grey-500"
          type="text"
          onChange={handleUsernameChange}
          value={username}
        />
      </div>
      <div className="flex flex-col items-center justify-center">
        <h2 className="text-lg font-medium">Password</h2>
        <input
          id="password"
          className="bg-grey-200 mb-3 rounded-sm p-1 border border-grey-500"
          type="password"
          onChange={handlePassChange}
          value={password}
        />
      </div>
      <button
        className="bg-mint-300 p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-400 hover:cursor-pointer"
        type="submit"
        onClick={handleLogin}
      >
        Login
      </button>
      <div className="flex items-center gap-2">
        <p>Don't have an account?</p>
        <button className="text-blue-800 hover:text-blue-900 hover:cursor-pointer p-1" onClick={toggle}>
          Sign Up
        </button>
      </div>
    </div>
  );
};

export default LoginBox;
