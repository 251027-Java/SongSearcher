import { BASE_URL } from "../constants";

// Keep the token in this module's memory — no localStorage
let authToken = null;
let logoutCallback = null;

export const setAuthToken = (token) => {
  authToken = token;
};

export const setLogoutCallback = (callback) => {
  logoutCallback = callback;
};

export const apiClient = async (endpoint, { method = "GET", body } = {}) => {
  const headers = { "Content-Type": "application/json" };
  if (authToken) headers.Authorization = `Bearer ${authToken}`;

  const config = {
    method,
    headers,
  };

  if (body) config.body = JSON.stringify(body);

  const response = await fetch(`${BASE_URL}${endpoint}`, config);

  // Auto-logout on 401 Unauthorized
  if (response.status === 401) {
    if (logoutCallback) logoutCallback();
    const message = await response.text();
    throw new Error(`${response.status}: ${message}`);
  }

  if (!response.ok) {
    const message = await response.text();
    throw new Error(`${response.status}: ${message}`);
  }

  if (response.status === 204) return null;
  return response.json();
};
