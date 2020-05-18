package com.czkj.role.controller;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.exception.ExceptionHandleAdvice;
import com.czkj.permission.service.MenuService;
import com.czkj.res.Response;
import com.czkj.role.service.RoleService;
import com.czkj.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/4/24 12:52
 * @Modified By：
 */
@Api(description = "角色管理生产接口")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    private static final Logger log = LoggerFactory.getLogger(RoleController.class);

    @ApiOperation(value = "显示角色列表（分页显示）", notes = "显示角色列表（分页显示）")
    @ApiImplicitParams({@ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = " 角色名-用于模糊查询", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "available", value = " 是否可用标识", paramType = "query", required = false, dataType = "String")})
    @GetMapping("/getRoleList")
    public Response<PageResult> getRoleList(Integer currentPage, Integer size, String roleName, String available) {
        PageResult<TabRole> pageResult = roleService.queryRoleList(currentPage, size, roleName, available);
        return Response.success().data(pageResult);
    }

    /**
     * 检验角色信息是否存在
     *
     * @param name 角色名
     * @param code 角色编码
     * @return
     */
    private Response validateRole(String name, String code, String keyId, BindingResult bindingResult) {
        log.info("角色名:" + name + "," + "角色编码:" + code);
        if (null != bindingResult) {
            if (bindingResult.hasErrors()) {
                return Response.failure("2021", bindingResult.getFieldError().getDefaultMessage());
            }
        }
        if (StringUtils.isNotBlank(name)) {
            boolean result = roleService.validateRoleExit(name, null, keyId);
            if (!result) {
                return Response.failure("4015", "角色名已存在");
            }
        }
        if (StringUtils.isNotBlank(code)) {
            boolean result = roleService.validateRoleExit(null, code, keyId);
            if (!result) {
                return Response.failure("4016", "角色编码已存在");
            }
        }
        return Response.success();
    }

    @ApiOperation(value = "保存角色信息", notes = "保存角色信息")
    @RequestMapping(value = "/saveRole", produces = "application/json", method = RequestMethod.POST)
    public Response saveRole(@RequestBody @Validated TabRole tabRole, BindingResult bindingResult) {
        log.info("角色名:" + tabRole.getName(), "," + "角色编码:" + tabRole.getCode());
        log.info("权限id:" + tabRole.getTabPermissions());
        Response response = validateRole(tabRole.getName(), tabRole.getCode(), null, bindingResult);
        //验证成功则进行添加
        if ("0".equals(response.getCode())) {
            log.info("检验结果:" + response.getCode());
            boolean result = roleService.saveRole(tabRole);
            if (result) {
                return Response.success().message("保存成功");
            }
            return Response.failure("保存失败");
        }
        return response;
    }

    @ApiOperation(value = "获取对应角色信息", notes = "获取对应角色信息")
    @ApiImplicitParam(name = "roleId", value = "角色id", required = true, paramType = "query", dataType = "String")
    @GetMapping("/getRoleById")
    public Response getRoleById(String roleId) {
        TabRole tabRole = roleService.queryRoleById(roleId);
        return Response.success(tabRole);
    }

    @ApiOperation(value = "修改指定角色信息", notes = "修改指定角色信息")
    @RequestMapping(value = "/updateRole", produces = "application/json", method = RequestMethod.PUT)
    public Response updateRole(@RequestBody @Validated TabRole tabRole, BindingResult bindingResult) {
        System.out.println("角色id：" + tabRole.getId());
        if (null == tabRole.getId()) {
            return Response.failure("2565", "主键不能为空");
        } else {
            String available = roleService.queryRoleById(tabRole.getId()).getAvailable();
            if ("0".equals(available)) {
                return Response.failure("6236","不能操作废弃的角色");
            }
        }
        Response response = validateRole(tabRole.getName(), tabRole.getCode(), tabRole.getId(), bindingResult);
        //验证成功则进行添加
        if ("0".equals(response.getCode())) {
            log.info("权限id:" + tabRole.getTabPermissions());
            boolean result = roleService.updateRoleById(tabRole);
            if (result) {
                return Response.success().message("修改成功");
            }
            return Response.failure("修改失败");
        }
        return response;
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "roleId", value = "角色id，删除", paramType = "query", required = true, dataType = "String")
    @DeleteMapping("/deleteRole")
    public Response deleteRole(String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return Response.failure("2565", "主键不能为空");
        } else {
            String available = roleService.queryRoleById(roleId).getAvailable();
            if ("0".equals(available)) {
                return Response.failure("6237","此角色已是禁用状态");
            }
        }
        List<TabPermission> permissionList = roleService.queryPermissionListByRole(roleId);
        if (permissionList.size() > 0) {
            //初始化
            String perName = "";
            //遍历角色数据，字符串拼接
            for(int i =0;i<permissionList.size();i++){
                if (i>0){
                    perName+=",";
                }
                perName+=permissionList.get(i).getName();
            }
            return Response.failure("4014", "不能禁用该角色,有权限["+perName+"]绑定");
        }
        boolean result = roleService.deleteRole(roleId);
        if (result) {
            return Response.success().message("禁用成功");
        }
        return Response.failure("禁用失败,服务异常");

    }

    @ApiOperation(value = "获取对应权限id", notes = "获取对应权限id")
    @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "query", required = true, dataType = "String")
    @GetMapping("/getPerIdByRoleId")
    public List<String> getPerIdByRoleId(String roleId) {
        List<String> perIdList = new ArrayList<>();
        List<TabPermission> permissionList = roleService.queryPermissionListByRole(roleId);
        if (permissionList.size() > 0) {
            for (TabPermission tabPermission : permissionList) {
                String id = tabPermission.getId();
                perIdList.add(id);
            }
        }
        return perIdList;
    }

    @ApiOperation(value = "角色授权", notes = "角色授权")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleId", value = "角色id", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "perIds", value = "权限id数组", paramType = "query", required = true, allowMultiple = true, dataType = "String")
    })
    @PostMapping("/savePermission")
    public Response savePermission(String[] perIds, String roleId) {
        if (perIds.length < 0) {
            return Response.failure("5034", "请选择权限");
        }
        if (StringUtils.isBlank(roleId)) {
            return Response.failure("5033", "主键不能为空");
        }else {
            String available = roleService.queryRoleById(roleId).getAvailable();
            if ("0".equals(available)) {
                return Response.failure("6234","不能操作已经废弃的角色");
            }
        }
        boolean result = roleService.savePermissionAndRole(perIds, roleId);
        if (result) {
            return Response.success("绑定成功");
        }
        return Response.failure("绑定失败，请检查原因");
    }

    @ApiOperation(value = "获取所有权限信息", notes = "获取所有权限信息")
    @ApiImplicitParam(name = "perName", value = "权限名", paramType = "query", dataType = "String")
    @GetMapping("/getPermissionList")
    public List<TabPermission> getPermissionList(String perName) {
        PageResult<TabPermission> pageResult = menuService.getAllList("1", perName, null, null);
        List<TabPermission> items = pageResult.getItems();
        return items;
    }

    @ApiOperation(value = "启用角色", notes = "启用角色")
    @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "query", required = true, dataType = "String")
    @PutMapping("/enableRole")
    public Response enableRole(String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return Response.failure("2565", "主键不能为空");
        } else {
            String available = roleService.queryRoleById(roleId).getAvailable();
            if ("1".equals(available)) {
                return Response.failure("6235","此角色已是启用状态");
            }
        }
        boolean result = roleService.enableRole(roleId);
        if (result) {
            return Response.success("启用成功");
        }
        return Response.failure("启用失败,服务异常");
    }
}
