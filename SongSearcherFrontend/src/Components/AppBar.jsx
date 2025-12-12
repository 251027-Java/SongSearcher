import { useContext, useState } from "react";
import { AuthContext } from "../AuthContext";
import LogoutConfirmDialog from "./LogoutConfirmDialog";

const AppBar = () => {
  const { logout, isAuthenticated } = useContext(AuthContext);
  const [open, setOpen] = useState(false);
  
  const handleLogoutClick = () => {
    setOpen(true);
  }
  const logoutHandler = () => {
    logout();
    setOpen(false);
  }

  return (
    <header className="fixed w-full theme-tertiary h-[3rem] shadow-md top-0">
      <div className="flex items-center justify-between px-2 h-full">
        <h1 className="text-2xl font-bold font-mono text-mint-800">
          SongSearcher
        </h1>
        <div className="flex items-center gap-2">
          {isAuthenticated && (
            <button
              className="text-mint-800 rounded-md p-1 px-2 hover:cursor-pointer hover:bg-mint-300"
              onClick={handleLogoutClick}
            >
              Logout
            </button>
          )}
        </div>
      </div>
      <LogoutConfirmDialog open={open} setOpen={setOpen} onLogout={logoutHandler}/>
    </header>
  );
};

export default AppBar;
