import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../AuthContext";
import { apiClient } from "../ApiHooks/ApiClient";

const LoginBox = ({ toggle }) => {
  const { login } = useContext(AuthContext);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [invalidMessage, setInvalidMessage] = useState("");
  const nav = useNavigate();

  const handlePassChange = (e) => {
    setPassword(e.target.value);
    setInvalidMessage("");
  };

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
    setInvalidMessage("");
  };

  const handleLogin = async () => {
    try {
      const data = await apiClient("/auth/login", {
        method: "POST",
        body: { username, password },
      });

      const token = data?.token || data?.accessToken || null;
      if (!token) {
        console.error("No token returned from login", data);
        return;
      }

      login(token);
      nav("/dashboard");
      setPassword("");
      setUsername("");
    } catch (err) {
      if (err.message.startsWith("401")) {
        setInvalidMessage("Invalid username or password");
      } else {
        console.error("Login failed", err.message);
      }
    }
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      handleLogin();
    }
  };

  return (
    <div className="flex flex-col items-center justify-center theme-main shadow-md rounded-2xl m-5 py-10 px-15 gap-1 duration-200 ease-in-out hover:shadow-lg">
      <div className="mb-5 mx-22 max-w-[20vw] text-center">
        <h1 className="text-4xl font-bold mb-1">Welcome Back</h1>
        <p className="text-gray-600">Login to your dashboard</p>
      </div>
      
      <div className="flex flex-col items-center justify-center">
        <h2 className="text-lg font-medium">Username</h2>
        <input
          id="username"
          className="bg-grey-100 rounded-md p-1 border"
          type="text"
          onChange={handleUsernameChange}
          value={username}
        />
      </div>
      <div className="flex flex-col items-center justify-center">
        <h2 className="text-lg font-medium">Password</h2>
        <input
          id="password"
          className="bg-grey-100 mb-3 rounded-md p-1 border"
          type="password"
          onChange={handlePassChange}
          onKeyDown={handleKeyDown}
          value={password}
        />
        {<p className="text-red-500">{invalidMessage}</p>}
      </div>
      <button
        className="submit-button"
        type="submit"
        onClick={handleLogin}
      >
        Login
      </button>
      <div className="flex items-center gap-2">
        <p>Don't have an account?</p>
        <button
          className="text-blue-800 hover:text-blue-900 hover:cursor-pointer p-1"
          onClick={toggle}
        >
          Sign Up
        </button>
      </div>
    </div>
  );
};

export default LoginBox;
