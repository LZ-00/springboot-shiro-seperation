package com.lz;

import com.lz.entity.User;
import com.lz.jwt.JWTUtil;
import com.lz.service.UserService;
import com.lz.util.SystemUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.util.Collection;

@SpringBootTest
class SpringbootShiroPlusApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    SessionDAO sessionDAO;

    @Test
    void contextLoads() {

        User user = new User();
        String username = "qwer";
        String password = "123456";
        user.setUsername(username);

        password = SystemUtil.Md5(password,username);
        user.setPassword(password);

        user.setPerms("*:*");
        user.setRole("admin");

        userService.save(user);

    }

    @Test
    void subjectTest(){
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        Collection<Realm> realms = securityManager.getRealms();
        Realm next = realms.iterator().next();
        String name = next.getName();
        System.out.println(name);

    }

    @Test
    void onlineCountTest(){
        System.out.println("online count :"+sessionDAO.getActiveSessions().size());
    }

    @Test
    void JwtTest(){
        String token = JWTUtil.sign("lz");
        System.out.println(token);

        if(JWTUtil.verify(token)){
            System.out.println("success");
        }else {
            System.out.println("fail");
        }
    }
}
