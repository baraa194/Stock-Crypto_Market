package com.myProject.demo;

import com.myProject.demo.Services.JWTService;
import com.myProject.demo.Services.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    JWTService jwtservice;
    @Autowired
    ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //Bearer l;wjeflhiweghfgbwuebujgweug
        String authHeader=request.getHeader("Authorization");
        String token=null;
        String username=null;
        if(authHeader!=null&&authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            username=jwtservice.extractUserName(token);
        }
        // check if usernmae not null and user not authenticated
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userdetails=context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

            if(jwtservice.validateToken(token,userdetails))
            {
                UsernamePasswordAuthenticationToken authtoken= new UsernamePasswordAuthenticationToken
                        (userdetails,null,userdetails.getAuthorities());
                authtoken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authtoken);
            }
        }
        filterChain.doFilter(request, response);

    }

}
