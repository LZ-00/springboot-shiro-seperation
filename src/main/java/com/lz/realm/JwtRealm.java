package com.lz.realm;//package com.example.shirojwt.shiro;
 
import com.lz.entity.User;
import com.lz.jwt.JWTToken;
import com.lz.jwt.JWTUtil;
import com.lz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
/**
 * MyRealm:自定义一个授权
 *
 * @author zhangxiaoxiang
 * @date: 2019/07/12
 */
 
@Component
@Slf4j
public class JwtRealm extends AuthorizingRealm {
 
    @Autowired
    private UserService userService;
 
    /**
     * 必须重写此方法，不然Shiro会报错
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }
 
    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = (String) principals.getPrimaryPrincipal();
        String username = JWTUtil.getUserName(token);

        User userInfo = userService.getByUsername(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        if(userInfo!=null){

            //添加角色
            authorizationInfo.addRole(userInfo.getRole());

            //添加用户权限
            authorizationInfo.addStringPermission(userInfo.getPerms());
        }

        return authorizationInfo;
    }
 
    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = null;
        try {
            //这里工具类没有处理空指针等异常这里处理一下(这里处理科学一些)
            username = JWTUtil.getUserName(token);
        } catch (Exception e) {
            throw new AuthenticationException("heard的token拼写错误或者值为空");
        }
        if (username == null) {
          log.error("token无效(空''或者null都不行!)");
            throw new AuthenticationException("token无效");
        }
        User userBean = userService.getByUsername(username);
        if (userBean == null) {
            log.error("用户不存在!)");
            throw new AuthenticationException("用户不存在!");
        }
        if (!JWTUtil.verify(token)) {
            log.error("用户名或密码错误(token无效或者与登录者不匹配)!)");
            throw new AuthenticationException("用户名或密码错误(token无效或者与登录者不匹配)!");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}