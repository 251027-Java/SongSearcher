import { useState, useContext } from "react";
import { AuthContext } from "../AuthContext";
import { apiClient } from "../ApiHooks/ApiClient";
import { useNavigate } from "react-router-dom";

const SignUpBox = ({ toggle }) => {
  const nav = useNavigate();
  const { login } = useContext(AuthContext);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPass, setConfirmPass] = useState("");
  const [passError, setPassError] = useState(false);

  const handlePassChange = (e) => {
    setPassword(e.target.value);
    if (passError) {
      setPassError(false);
    }
  };

  const handleConfirmPassChange = (e) => {
    setConfirmPass(e.target.value);
    if (passError) {
      setPassError(false);
    }
  };

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  };

  const handleSignUp = async () => {
    if (password === confirmPass) {
      try {
        const data = await apiClient("/auth/register", {
          method: "POST",
          body: { username, password },
        });

        const token = data?.token || data?.accessToken || null;
        if (!token) {
          console.error("No token returned from register", data);
          return;
        }

        login(token);
        nav("/dashboard");
        setPassword("");
        setUsername("");
        setConfirmPass("");
      } catch (err) {
        console.error("Login failed", err.message);
      }
    } else {
      setPassError(true);
      setConfirmPass("");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center theme-main shadow-md rounded-2xl m-5 py-10 px-15 gap-1 duration-200 ease-in-out hover:shadow-lg">
      <div className="mb-5 mx-20 max-w-[20vw] text-center">
        <h1 className="text-4xl font-bold mb-1">Create Account</h1>
        <p className="text-gray-600">Sign up for a new account</p>
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
          className={`bg-grey-100 rounded-md p-1 border ${
            passError ? "border-red-500" : "border-black"
          }`}
          type="password"
          onChange={handlePassChange}
          value={password}
        />
      </div>
      <div className="flex flex-col items-center justify-center">
        <h2 className="text-lg font-medium">Confirm Password</h2>
        <input
          id="confirm-password"
          className={`bg-grey-100 rounded-md p-1 border ${
            passError ? "border-red-500" : "border-black mb-3"
          }`}
          type="password"
          onChange={handleConfirmPassChange}
          value={confirmPass}
        />
      </div>
      {passError && (
        <p className="text-red-500 mb-3 w-50 text-center">
          Passwords did not match
        </p>
      )}
      <button
        className="bg-mint-300 p-1 px-2 mb-2 border border-mint-500 rounded-md hover:bg-mint-400 hover:cursor-pointer"
        type="submit"
        onClick={handleSignUp}
      >
        Sign Up
      </button>
      <div className="flex items-center gap-2">
        <p>Already have an account?</p>
        <button
          className="text-blue-800 hover:text-blue-900 hover:cursor-pointer p-1"
          onClick={toggle}
        >
          Log In
        </button>
      </div>
    </div>
  );
};

export default SignUpBox;
