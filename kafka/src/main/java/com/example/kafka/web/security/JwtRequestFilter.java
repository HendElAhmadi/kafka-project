package com.example.kafka.web.security;



import com.example.kafka.exception.AuthException;
import com.example.kafka.model.constants.Constants;
import com.example.kafka.model.constants.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(JwtRequestFilter.class);


    @Autowired
    private TokenManager tokenManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {


            final String authorizationHeader = request.getHeader(Constants.AUTHORIZATION);

            if (authorizationHeader == null || !authorizationHeader.startsWith(Constants.BEARER)) {
                throw new AuthException(ErrorCode.TOKEN_IS_NEEDED.name(),
                        ErrorCode.TOKEN_IS_NEEDED.getErrorDesc());
            }

            String token = authorizationHeader.substring(7);

            tokenManager.validateToken(token);

            String issuer = tokenManager.getIssuerFromToken(token);

            if(Objects.isNull(issuer) || issuer.isEmpty()) {//empty
                LOGGER.error("Invalid token!! ");
                throw new AuthException(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getErrorDesc());

            } else {
                String username = tokenManager.getUsernameFromToken(token);

                if(Objects.isNull(username) || username.isEmpty()) {
                    LOGGER.error("Invalid token!! ");
                    throw new AuthException(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getErrorDesc());
                } else {


                    Collection<GrantedAuthority> authorities = tokenManager.getRolesFromToken(token)
                            .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                    SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(token,authorities));

                    chain.doFilter(request, response);
                }
            }


    }



    @Override protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<String> excludeUrlPatterns = new ArrayList<>();
        excludeUrlPatterns.add("/swagger-ui.html");
        excludeUrlPatterns.add("/swagger-ui/**");
        excludeUrlPatterns.add("/webjars/**");
        excludeUrlPatterns.add("/api/**");
        excludeUrlPatterns.add("/v3/api-docs");
        excludeUrlPatterns.add("/v3/api-docs/**");
        excludeUrlPatterns.add("/swagger-resources");
        excludeUrlPatterns.add("/configuration/ui");
        excludeUrlPatterns.add("/configuration/security");
        excludeUrlPatterns.add("/swagger-resources/**");
        excludeUrlPatterns.add("/authentication/login");
        excludeUrlPatterns.add("/books");
        excludeUrlPatterns.add("/authentication/login");
        excludeUrlPatterns.add("/file");
        excludeUrlPatterns.add("/file/**");

        return excludeUrlPatterns.stream() .anyMatch(p -> new AntPathMatcher().match(p, request.getServletPath())) ;
    }




}
