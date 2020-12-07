package com.lz.service;

import com.lz.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lz
 * @since 2020-12-05
 */
public interface UserService extends IService<User> {

    /**
     * @param userName 用户名
     * @return User对象
     */
    User getByUsername(String userName);

}
