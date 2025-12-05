package com.revature.SongSearcher;

import com.revature.SongSearcher.Model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class BasicAuthInterceptor implements HandlerInterceptor {
    // Fields
    private final UserRepository repo;

    // Constructor
    public BasicAuthInterceptor(UserRepository repo) {
        this.repo = repo;
    }

    // Methods
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        // is the header there, is it the right kind?
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // decode the header to a base 64 string
            String b64c = authHeader.substring(6);
            byte[] decoded = Base64.getDecoder().decode(b64c);
            String creds = new String(decoded, StandardCharsets.UTF_8);

            // split the "username:pass"
            String[] parts = creds.split(":", 2);
            if (parts.length == 2) {
                String username = parts[0];
                String password = parts[1];
                // check if the user is in the db
                Optional<User> user = repo.findByUsername(username);

                // check if the pass is correct
                if (user.isPresent() && user.get().getPassword().equals(password)) {
                    return true;
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized: invalid credentials");
        return false;
    }
}

