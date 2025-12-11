const AppBar = () => {
    return (
        <header className="fixed w-full theme-tertiary h-[3rem] shadow-md top-0">
            <div className="flex items-center justify-between px-2 h-full">
                <h1 className="text-2xl font-bold font-mono text-mint-800">SongSearcher</h1>
                <div className="flex items-center gap-2">
                    <button className="text-mint-800">Dashboard</button>
                    <button className="text-mint-800">Logout</button>
                </div>
            </div>
        </header>
    )
}

export default AppBar;