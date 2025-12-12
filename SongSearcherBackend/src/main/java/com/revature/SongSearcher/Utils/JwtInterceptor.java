package com.revature.SongSearcher.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    // Fields
    private final JwtUtil jwtUtil;

    // Constructor
    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil=jwtUtil;
    }

    // Methods
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // if is an options request, approve
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // is the auth header attached/included
        String authHeader = request.getHeader("Authorization");
        // is the token included?
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // is the token valid?
        String token = authHeader.substring(7);
        if (jwtUtil.validateToken(token)) {
            Long userId = Long.parseLong(jwtUtil.getUserIdFromToken(token));
            request.setAttribute("authenticatedUserId", userId);
            return true;
        }
        // if not,reject the request
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized: invalid token");
        return false;
    }
}
