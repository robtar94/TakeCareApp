package project.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.services.UserServiceImpl;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import static project.config.SecurityConfiguration.TOKEN_HEADER;
import static project.config.SecurityConfiguration.TOKEN_SCHEME;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserServiceImpl userDetailsService;

    @Autowired
    private TokenHelper tokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = httpServletRequest;
        HttpServletResponse response = httpServletResponse;

        String header = httpServletRequest.getHeader(TOKEN_HEADER);
        String name = null;
        String tokenAuth = null;

        if(header!=null && header.startsWith(TOKEN_SCHEME)){
            tokenAuth = header.replace(TOKEN_SCHEME,"");
            try{
                name = tokenHelper.getUsernameFromToken(tokenAuth);
            } catch (IllegalArgumentException e) {
                request.setAttribute("token","token is invalid");
                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                request.setAttribute("token","token is expired");
                logger.warn("the token is expired and not valid anymore", e);
            } catch(SignatureException e){
                request.setAttribute("token","token is invalid");
                logger.error("Authentication Failed. Username or Password not valid.");
            }

        }else{
            logger.warn("Couldn't find Bearer String");
        }
        if(name != null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails = userDetailsService.loadUserByUsername(name);

            if(this.tokenHelper.validateToken(tokenAuth,userDetails)){
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,SecurityContextHolder.getContext().getAuthentication(),userDetails.getAuthorities());
                String token = this.tokenHelper.refreshToken(tokenAuth);

                request.setAttribute("token",token);

                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.info("Authenticated user "+name);

            }
        }


        filterChain.doFilter(request,response);

    }
}
