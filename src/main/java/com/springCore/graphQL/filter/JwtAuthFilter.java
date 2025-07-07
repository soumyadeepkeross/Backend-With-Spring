package com.springCore.graphQL.filter;


import com.springCore.graphQL.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        System.out.println(authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
            System.out.println(username);
        }

        String SWPHeader = request.getHeader("Sec-WebSocket-Protocol");
        System.out.println(request.getHeaderNames());
        System.out.println(SWPHeader);
        /* if bearer token is not present then go for SWP */
        if (authHeader==null && SWPHeader != null) {

            token = SWPHeader.substring(21);
            username = jwtUtil.extractUsername(token);
            System.out.println(username);
        }


        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println("-inside-");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println(userDetails);
            System.out.println(jwtUtil.validateToken(token, userDetails));
            if (jwtUtil.validateToken(token, userDetails)) {
                System.out.println("inside");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
