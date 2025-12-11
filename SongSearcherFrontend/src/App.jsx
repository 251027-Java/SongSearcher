import AppRoutes from "./AppRoutes";
import AppBar from "./Components/AppBar";
import FootBar from "./Components/FootBar";
import AuthProvider from "./AuthProvider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const queryClient = new QueryClient();

function App() {
  return (
    <AuthProvider>
      <QueryClientProvider client={queryClient}>
        <div className="relative h-lvh min-w-full">
          <AppBar />
          <AppRoutes />
          <FootBar />
        </div>
      </QueryClientProvider>
    </AuthProvider>
  );
}

export default App;
