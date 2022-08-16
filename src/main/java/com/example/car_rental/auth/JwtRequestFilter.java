package com.example.car_rental.auth;

import com.example.car_rental.model.User;
import com.example.car_rental.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
//@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

//    public JwtRequestFilter(@Lazy UserService userService, JwtTokenUtil jwtTokenUtil) {
//        this.userService = userService;
//        this.jwtTokenUtil = jwtTokenUtil;
//    }

    public JwtRequestFilter(UserService userService, JwtTokenUtil jwtTokenUtil) {

        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String email = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                email = jwtTokenUtil.getSubjectFromToken(jwtToken);
            } catch (IllegalArgumentException ex) {
                System.out.println("Unable to get JWT Token!");
            } catch (ExpiredJwtException ex) {
                System.out.println("JWT Token has expired!");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String!");
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //MyUserDetails userDetails = (UserDetails) userService.readUserByEmail(email);
            //MyUserDetails userDetails = new MyUserDetails(userService.readUserByEmail(email));
            User user = userService.readUserByEmail(email);
            String role = user.getRole();
            List<SimpleGrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(role));
            if (jwtTokenUtil.validateToken(jwtToken, user.getEmail())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, roles);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
