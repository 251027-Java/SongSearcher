import { Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./Pages/LoginPage";
import DashboardPage from "./Pages/DashboardPage";
import ProtectedRoute from "./Pages/ProtectedRoute";

const AppRoutes = () => {

  return (
    <Routes>
      <Route path="/">
        <Route index element={<Navigate to="/login" replace />} />
        <Route index path="login" element={<LoginPage />} />
        <Route element={<ProtectedRoute />}>
          <Route path="dashboard" element={<DashboardPage />} />
        </Route>
      </Route>
    </Routes>
  );
};

export default AppRoutes;
