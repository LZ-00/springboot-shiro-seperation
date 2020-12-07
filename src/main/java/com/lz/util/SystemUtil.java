package com.lz.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author 乐。
 */
public class SystemUtil {

    public static String Md5(String password,String salt){
        String algorithm = "md5";
        Object source  =password;
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        int hashIterations = 2;
        return new SimpleHash(algorithm,source,salt,hashIterations).toBase64();
    }

}
