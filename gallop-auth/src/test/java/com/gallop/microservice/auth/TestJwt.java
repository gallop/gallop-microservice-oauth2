package com.gallop.microservice.auth;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

/**
 * author gallop
 * date 2022-03-17 14:50
 * Description:
 * Modified By:
 */
public class TestJwt {
    public static void main(String[] args) {
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjN2shDFST07fCKCLQtjB+OgKcubjTH3utybqYc7XrB3rJH65Hu0RpR1CSf8A9FdyR30lcGN8jI/i25pFfhe4+f7EsIimtN77/m0thGpBwCeMvUybxQl5tfL2eUwRd7p1RjIXgSGniYkmk3EHCnd8pLZPV4XZ7LoDaQ1195s5jWfgYMu+SR/RliGRmb0QEgF8MdwyeNLmf8OAc6NunXPnzRPfwoSlp2U9gGSczxKmssqAnU3cXaNscLlC1tk36Ww9pOIVfTlxw3JLeMVVD9W2yxUhS5pfIUL5CuWavxjZ+L2q1AjGXzR7S3faMCEn0FqxrVUXIuk8lSczjbTZWFJk5QIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJnYWxsb3AiLCJzY29wZSI6WyJhbGwiXSwibmFtZSI6ImdhbGxvcCIsIm1vYmlsZSI6IjE1OTU5MjAyODExIiwiZXhwIjoxNjQ3NTA3MzcyLCJhdXRob3JpdGllcyI6WyJhZG1pbjpmaWxlTWFuYWdlcjpkb3dubG9hZCIsImFkbWluOnVzZXI6cmVzZXRQd2QiLCJhZG1pbjp1c2VyOmxpc3QiLCJhZG1pbjpzeXNSb2xlOmxpc3QiLCJhZG1pbjpmaWxlTWFuYWdlcjpkb1ByZXZpZXciLCJhZG1pbjp1c2VyOm93blJvbGUiLCJhZG1pbjp1c2VyOmVkaXQiLCJhZG1pbjp1c2VyOmRldGFpbCIsIuaIkeeahOaWh-ahoyIsImFkbWluOmZpbGVNYW5hZ2VyOmNyZWF0ZSIsImFkbWluOmZpbGVNYW5hZ2VyOnVwbG9hZE1pbm8iLCJhZG1pbjpmaWxlTWFuYWdlcjpvcGVyYXRlIiwiYWRtaW46ZmlsZU1hbmFnZXI6bGlzdCJdLCJqdGkiOiI2ZmRlMWU5OC02MTEwLTQ0YTktOWI1Zi1kM2QzNWJlYmE1Y2QiLCJjbGllbnRfaWQiOiJjbGllbnRfMDAxIn0.aGOyd6YDgrb7TkgaNuQ00c9M85zg_YTo9dnse6PjH1g_EUUNRwp1H-HtfZvRpHAA5KqV7_To4siucWJ33Wwloj_UZIeGnBNrR2rWkAiqXYhYXLG6jA3p3w2-0ToIYh3yk5fTTKShtYNXucdeQCc6rOMU8E0h2F7zCjfUrOI-wPOVaRhqrtIzvuV98Nr165ayychBUsBvs6bGdxU9qPyPlJ2DDDr86Hr46HIrDc35NLfrgYXPz9Pm2fTIvEhsYG3a9q5YrWwjo8cno4rbA1nxWQYFcCHFvjxN6caJeia0MCny32E77muFzy_aABPFgYTh0weq0wu_GACAm0q5Ef1t5Q";
        //校验jwt令牌
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        //拿到jwt令牌中自定义的内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
