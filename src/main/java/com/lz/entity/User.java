package com.lz.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lz
 * @since 2020-12-05
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty(value = "主键")
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      @ApiModelProperty(value = "用户名")
      private String username;

      @ApiModelProperty(value = "密码")
      private String password;

      @ApiModelProperty(value = "锁定状态")
      private Integer lockState;

      @ApiModelProperty(value = "角色")
      private String role;

      @ApiModelProperty(value = "权限")
      private String perms;

      @ApiModelProperty(value = "乐观锁")
      @Version
    private Integer version;

      @ApiModelProperty(value = "创建时间")
      @TableField(fill = FieldFill.INSERT)
      private Date gmtCreate;

      @ApiModelProperty(value = " 更新时间")
      @TableField(fill = FieldFill.INSERT_UPDATE)
      private Date  gmtModify;


}
