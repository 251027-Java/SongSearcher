const BASE_URL = "http://localhost:8080/api";

export const apiClient = async (endpoint, { method = "GET", body } = {}) => {
  const config = {
    method,
    headers: { "Content-Type": "application/json" },
  };

  if (body) config.body = JSON.stringify(body);

  const response = await fetch(`${BASE_URL}${endpoint}`, config);

  if (!response.ok) {
    const message = await response.text();
    throw new Error(`${response.status}: ${message}`);
  }

  if (response.status === 204) return null; // Spring Boot empty response
  return response.json();
};
