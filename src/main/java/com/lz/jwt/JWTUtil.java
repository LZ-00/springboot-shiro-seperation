package com.lz.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 乐。
 */
@Slf4j
public class JWTUtil {

    /**
     * 过期时间为15分钟
     */
    private static final long EXPIRE_TIME= 7*24*60*60*1000;

    private static final String TOKEN_SECRET = "8a6ef7ff-1ab2-484f-b239-9a87e395ba4f";

    public static String sign(String userName){

        try{
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            //私钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>(2);
            header.put("typ","JWT");
            header.put("alg","HS256");
            //附带username,password信息，生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("username",userName)
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (UnsupportedEncodingException e){
            return null;
        }
    }

    /**
     * 校验token是否正确
     * @param token
     * @return
     */
    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            log.info("invoke======>jwt verify successfully!");
            return true;
        } catch (Exception e) {
            log.debug("invoke======>jwt verify failed!");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得token中的信息
     * @param token
     * @return token中包含的用户名
     */
    public static String getUserName(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
