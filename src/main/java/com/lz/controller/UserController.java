package com.lz.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.component.RetryLimitHashedCredentialsMatcher;
import com.lz.realm.ShiroRealm;
import com.lz.entity.User;
import com.lz.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lz
 * @since 2020-12-05
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher;


    /**
     * 创建固定写死的用户
     * @param model
     * @return
     */
    @RequiresPermissions("userInfo:add")
    @RequiresRoles(value = {"admin","test"},logical = Logical.OR)
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    @ResponseBody
    public String add(Model model) {
        User user = new User();
        user.setUsername("lz");
        user.setPassword("123456");

        userService.save(user);
        return "创建用户成功";

    }

    /**
     * 删除固定写死的用户
     * @param model
     * @return
     */
    @RequiresPermissions("userInfo:view")
    @RequiresRoles("admin")
    @RequestMapping(value = "/del",method = RequestMethod.GET)
    @ResponseBody
    public String del(Model model) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username","lz");
        userService.remove(queryWrapper);
        return "删除用户名为lz用户成功";
    }

    @RequiresPermissions("userInfo:view")
    @RequestMapping(value = "/view",method = RequestMethod.GET)
    @ResponseBody
    public String view(Model model) {
        return "这是用户列表页";
    }

    @GetMapping("/unLock")
    public String unLock(Model model){

        retryLimitHashedCredentialsMatcher.unlockAccount("lz");
        model.addAttribute("msg","已解锁");

        return "login";
    }

    /**
     * 给admin用户添加 userInfo:del 权限
     * @param model
     * @return
     */
    @RequestMapping(value = "/dealPerms",method = RequestMethod.GET)
    @ResponseBody
    public String addPermission(Model model) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username","lz");
        User userInfo = userService.getOne(queryWrapper);

        userInfo.setPerms("userInfo:view");

        userService.updateById(userInfo);

        //添加成功之后 清除缓存
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager)SecurityUtils.getSecurityManager();
        ShiroRealm shiroRealm = (ShiroRealm) securityManager.getRealms().iterator().next();

        //清除权限 相关的缓存
        shiroRealm.clearAllCache();
        return "给admin修改权限为userInfo:view";

    }

//    /**
//     * 删除admin用户 userInfo:del 权限
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "/delPermission",method = RequestMethod.GET)
//    @ResponseBody
//    public String delPermission(Model model) {
//
//        //在sys_role_permission 表中  将 删除的权限 关联到admin用户所在的角色
//        roleMapper.delPermission(1,3);
//        //添加成功之后 清除缓存
//        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
//        ShiroRealm shiroRealm = (ShiroRealm) securityManager.getRealms().iterator().next();
//        //清除权限 相关的缓存
//        shiroRealm.clearAllCache();
//
//        return "删除admin用户userInfo:del 权限成功";
//
//    }

}

