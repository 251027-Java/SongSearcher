import AppRoutes from "./AppRoutes";
import AppBar from "./Components/AppBar";
import AuthProvider from "./AuthProvider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const queryClient = new QueryClient();

function App() {
  return (
    <AuthProvider>
      <QueryClientProvider client={queryClient}>
        <div className="relative h-lvh min-w-full overflow-hidden">
          <AppBar />
          <AppRoutes />
        </div>
      </QueryClientProvider>
    </AuthProvider>
  );
}

export default App;
