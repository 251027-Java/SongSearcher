import { createContext } from 'react';

// In-memory auth context (no localStorage usage)
export const AuthContext = createContext({
	isAuthenticated: false,
	token: null,
	login: (token) => {},
	logout: () => {},
});