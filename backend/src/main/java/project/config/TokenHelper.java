package project.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import project.model.User;

import java.io.Serializable;
import java.util.*;

import static project.config.SecurityConfiguration.TOKEN_KEY;
import static project.config.SecurityConfiguration.TOKEN_VALIDITY;

@Component
public class TokenHelper implements Serializable {


    //Generate Token with userID,role,name

    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getLogin());
        claims.put("userId",user.getId());
        claims.put("role",user.getRole().getName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://localhost:8080")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_VALIDITY*1000))
                .signWith(SignatureAlgorithm.HS256,TOKEN_KEY)
                .compact();
    }

    public String refreshToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://localhost:8080")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+TOKEN_VALIDITY*1000))
                .signWith(SignatureAlgorithm.HS256,TOKEN_KEY)
                .compact();

    }

    public Claims getAllClaimsFromToken(String token){

        return Jwts.parser()
                .setSigningKey(TOKEN_KEY)
                .parseClaimsJws(token)
                .getBody();
    }



    public String getUsernameFromToken(String token){

        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    public String getAuthoritiesFromToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        String auth = (String)claims.get("role");
        return auth;
    }

    public boolean validateToken(String token,UserDetails userDetails){
        String userName = getUsernameFromToken(token);
        String tokenAuth = getAuthoritiesFromToken(token);
        String userDetailsAuth = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();

        if(userName.contentEquals(userDetails.getUsername())
                && !isTokenExpired(token)
                && tokenAuth.contentEquals(userDetailsAuth)){
            return true;
        }
        else{
            return false;
        }
    }

    private Date getExpirationDate(String token) {

        Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    public boolean isTokenExpired(String token){
        Date currentTime = new Date(System.currentTimeMillis());
        Date tokenTime = getExpirationDate(token);
        if(currentTime.after(tokenTime)){
            return true;
        }
        else return false;
    }


}
