package com.czkj.permission.controller;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabPermissionUrl;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.exception.ExceptionHandleAdvice;
import com.czkj.permission.service.MenuService;
import com.czkj.res.Response;
import com.czkj.utils.PageResult;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: SunMin
 * @Description:菜单管理web层
 * @Date:Create：in 2020/4/22 11:49
 * @Modified By：
 */
@Api(description = "权限管理API接口")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandleAdvice.class);

    @ApiOperation(value = "显示所有权限数据及URL", notes = "显示所有权限数据及URL")
    @ApiImplicitParams({@ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "name", value = "权限名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "available", value = " 是否可用标识", paramType = "query", required = false, dataType = "String")})
    @GetMapping("/getAllList")
    public Response<PageResult> getAllList(String available, String name, Integer currentPage, Integer size) {
        log.info("是否可用：" + available + ",当前页：" + currentPage + ",每页显示条数：" + size);
        PageResult<TabPermission> pageResult = menuService.getAllList(available, name, currentPage, size);
        return Response.success().data(pageResult);
    }

    @ApiOperation(value = "新增权限及添加对应URL", notes = "新增权限及添加对应URL")
    @RequestMapping(value = "/savePermission", produces = "application/json", method = RequestMethod.POST)
    public Response savePermission(@RequestBody TabPermission tabPermission) {
        Response response = vlidate(tabPermission);
        if ("0".equals(response.getCode())) {
            boolean result = menuService.savePermission(tabPermission);
            if (result) {
                return Response.success().message("新增成功");
            }
            return Response.failure("新增失败,请检查原因！");
        }
        return response;
    }

    @ApiOperation(value = "显示指定权限id的权限数据", notes = "显示指定权限id的权限数据")
    @ApiImplicitParam(name = "key", value = "主键id", required = true, paramType = "query", dataType = "String")
    @GetMapping("/getPermissionByKey")
    public Response getPermissionByKey(String key) {
        TabPermission permissionByKey = menuService.getPermissionByKey(key);
        return Response.success(permissionByKey);
    }

    @ApiOperation(value = "修改对应权限数据", notes = "修改对应权限数据")
    @RequestMapping(value = "/updatePerByKey", produces = "application/json", method = RequestMethod.PUT)
    public Response updatePerByKey(@RequestBody TabPermission tabPermission) {
        if (StringUtils.isBlank(tabPermission.getId())) {
            return Response.failure("5069", "主键不能为空");
        } else {
            String available = menuService.getPermissionByKey(tabPermission.getId()).getAvailable();
            if ("0".equals(available)) {
                return Response.failure("5488", "不能操作已经废弃的权限");
            }
        }
        Response response = vlidate(tabPermission);
        if ("0".equals(response.getCode())) {
            boolean result = menuService.updatePerByKey(tabPermission);
            if (result) {
                return Response.success().message("修改成功");
            }
            return Response.failure("修改失败");
        }
        return response;
    }

    private Response vlidate(TabPermission tabPermission) {

        //先进行表单的一些校验，校验成功进行用户注册
        if (StringUtils.isNotBlank(tabPermission.getName())) {
            boolean result = menuService.queryExit(tabPermission.getName(), null, tabPermission.getId());
            if (!result) {
                //权限已存在
                return Response.failure("5120", tabPermission.getName() + "权限已存在");
            }
        } else {
            return Response.failure("5026", "权限名不能为空");
        }
        if (null != tabPermission.getUrlList() && tabPermission.getUrlList().size() > 0) {
            for (int i = 0; i < tabPermission.getUrlList().size(); i++) {
                if (StringUtils.isBlank(tabPermission.getUrlList().get(i).getName())) {
                    return Response.failure("5023", "url不能为空");
                }
                log.info("URL=" + tabPermission.getUrlList().get(i).getName());
                boolean result = menuService.queryExit(null, tabPermission.getUrlList().get(i).getName(), tabPermission.getId());
                if (!result) {
                    //获取对应URL绑定的权限id
                    String perId = menuService.getPerUrlByUrl(tabPermission.getUrlList().get(i).getName(), tabPermission.getId()).getPerId();
                    //根据权限id获取权限名
                    String perName = menuService.getPermissionByKey(perId).getName();
                    //URL已存在
                    return Response.failure("5121", "URL:" + tabPermission.getUrlList().get(i).getName() + "已绑定权限[" + perName + "]，请先取消绑定");
                }
            }
        }
        return Response.success();
    }

    @ApiOperation(value = "删除对应权限", notes = "删除对应权限")
    @ApiImplicitParam(name = "key", value = "主键id", required = true, paramType = "query", dataType = "String")
    @PutMapping("/deleteTabPermission")
    public Response deleteTabPermission(String key) {
        if (StringUtils.isBlank(key)) {
            return Response.failure("5068", "主键不能为空");
        } else {
            String available = menuService.getPermissionByKey(key).getAvailable();
            if ("0".equals(available)) {
                return Response.failure("5489", "该权限已是禁用状态");
            }
            List<TabRole> roleList = menuService.getRoleList(key);
            if (roleList.size() > 0) {
                //初始化
                String roleName = "";
                //遍历角色数据，字符串拼接
                for (int i = 0; i < roleList.size(); i++) {
                    if (i > 0) {
                        roleName += ",";
                    }
                    roleName += roleList.get(i).getName();
                }
                return Response.failure("4008", "权限不能废弃，有角色[" + roleName + "]关联-_-");
            } else {
                boolean result = menuService.deleteTabpermission(key);
                if (result) {
                    return Response.success().message("废弃成功");
                }
            }
        }
        return Response.failure("废弃失败,请检查原因");
    }

    @ApiOperation(value = "启用权限", notes = "启用权限")
    @ApiImplicitParam(name = "key", value = "主键id", required = true, paramType = "query", dataType = "String")
    @PutMapping("/enablePerById")
    public Response enablePerById(String key) {
        if (StringUtils.isBlank(key)) {
            return Response.failure("5070", "主键不能为空");
        } else {
            String available = menuService.getPermissionByKey(key).getAvailable();
            if ("1".equals(available)) {
                return Response.failure("5487", "该权限已是启用状态");
            }
        }
        boolean b = menuService.enablePermission(key);
        if (b) {
            return Response.success().message("启用成功");
        }
        return Response.failure("启用失败-_-");
    }
}
