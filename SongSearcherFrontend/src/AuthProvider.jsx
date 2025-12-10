import { useState, useCallback, useEffect } from 'react';
import { AuthContext } from './AuthContext';
import { setAuthToken as setApiAuthToken, setLogoutCallback } from './ApiHooks/ApiClient';

const AuthProvider = ({ children }) => {
  // token is kept only in application memory (no localStorage)
  const [token, setToken] = useState(null);

  const isAuthenticated = !!token;

  const login = useCallback((newToken) => {
    setToken(newToken);
    // inform api client to include the header
    try {
      setApiAuthToken(newToken);
    } catch (e) {
      // ignore if api client not available
    }
  }, []);

  const logout = useCallback(() => {
    setToken(null);
    try {
      setApiAuthToken(null);
    } catch (e) {
      // ignore
    }
  }, []);

  // Wire logout callback into ApiClient so it can auto-logout on 401
  useEffect(() => {
    setLogoutCallback(logout);
  }, [logout]);

  return (
    <AuthContext.Provider value={{ isAuthenticated, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
