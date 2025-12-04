import AppRoutes from "./AppRoutes";
import AppBar from "./Components/AppBar";
import AuthProvider from "./AuthProvider";

function App() {

  return (
    <div className="relative min-h-screen min-w-full overflow-hidden">
      <AuthProvider>
        <AppBar />
        <AppRoutes />
      </AuthProvider>
    </div>
  );
}

export default App;
