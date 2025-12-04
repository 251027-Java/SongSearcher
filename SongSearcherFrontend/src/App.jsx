import AppRoutes from "./AppRoutes";
import AppBar from "./Components/AppBar";
import AuthProvider from "./AuthProvider";

function App() {
  return (
    <div className="relative h-lvh min-w-full overflow-hidden">
      <AuthProvider>
        <AppBar />
        <AppRoutes />
      </AuthProvider>
    </div>
  );
}

export default App;
