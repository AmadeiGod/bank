package bank.operation.test.Jwt;

import bank.operation.test.Service.PersonDetailsImpl;
import bank.operation.test.Service.PersonServ;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory
            .getLogger(JwtUtils.class);


    private int jwtExpirationMs = 123123123;

    private String jwtSecret = "2eff2464e39b1e3b77bb340b818c6035d8e244e214afc4a31be657b40ace1386694cc9f7f48098e218bd0cafe95f43d918b4088d8f5f0fe1f0eabcdc2011c90f1d16c50ce6bd64bda5e21e5a55f9fa1d1d4a3016f6e4c72d133cfe16148782d0054952c6885cf99a526b9cd2f13d55ab5ce6f45b605d9d95ce9b3c90db4e94e499029c573bc27d21729f4e6a75ae7b9f29a9024e51a09213fc42d47431d6eb62d52a99738237a58054d7a6a335747eb5ff684b55a9dd1d75b0d3d0f7ab1a6068d8f7036e4eb60899f49b8746e66ddb6bf8874105cccce394f43742d260976a149a9f4d5599a66d35b6c647c0fae57033199eb661017259cba54f72b55c1ab36c";

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateJwtToken(Authentication authentication) {

        PersonServ personServ =
                (PersonServ) authentication.getPrincipal();

        return Jwts.builder().setSubject((personServ
                        .getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()
                        + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String getEmployeeNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody().getSubject();
    }

}