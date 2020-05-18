package com.czkj.user.controller;


import com.czkj.common.Validator;
import com.czkj.common.entity.TabRole;
import com.czkj.res.Response;

import com.czkj.common.entity.TabCustomer;
import com.czkj.common.entity.TabSubscriber;
import com.czkj.common.entity.TabUserRole;
import com.czkj.role.service.RoleService;
import com.czkj.user.service.UserService;
import com.czkj.utils.Base;
import com.czkj.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @author SunMin
 * @description 用户web层控制
 * @create 2020/4/9
 * @since 1.0.0
 */
@Api(description = "用户功能生产api")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @RequestMapping(value = "/userRegister", produces = "application/json", method = RequestMethod.POST)
    public Response UserRegister(@RequestBody @Validated TabSubscriber tabSubscriber, BindingResult bindingResult /*, String smsCode*/) {

        //检验
        Response response = validate(tabSubscriber.getId(), tabSubscriber.getMobile(), null, bindingResult);
        if ("0".equals(response.getCode())) {
            boolean result = userService.userRegister(tabSubscriber);
            if (result) {
                return Response.success().message("注册成功");
            }
            return Response.failure("注册失败");
        }
        return response;
    }

    /**
     * 校验手机号，登录账号是否存在
     *
     * @param id     登录账号
     * @param mobile 手机号
     * @return
     */
    private Response validate(String id, String mobile, String keyId, BindingResult bindingResult) {

        //检验非空，格式
        if (null != bindingResult) {
            //校验文本框
            if (bindingResult.hasErrors()) {
                return Response.failure("4022", bindingResult.getFieldError().getDefaultMessage());
            }
        }
        //检验数据是否存在
        if (StringUtils.isNotBlank(id)) {
            boolean result = userService.vUserExits(id, null, keyId);
            if (result) {
                //用户名已存在
                return Response.failure("5003", "用户名已注册");
            }
        }
        if (StringUtils.isNotBlank(mobile)) {
            boolean result = userService.vUserExits(null, mobile, keyId);
            if (result) {
                //电话已存在
                return Response.failure("5004", "电话已注册");
            }
        }
        return Response.success();
    }

    @ApiOperation(value = "显示用户注册时的手机号", notes = "显示用户注册时的手机号")
    @ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String")
    @GetMapping("/vPhoneExit")
    public Response vPhoneExit(String userName) {
        String mobile = userService.vPhoneExits(userName);
        if (StringUtils.isNotBlank(mobile)) {
            return Response.success(mobile);
        }
        return Response.failure("5014", "未绑定手机号");
    }

    @ApiOperation(value = "获取所有角色", notes = "显示所有角色列表，供用户选择角色")
    @ApiImplicitParam(name = "roleName", value = " 角色名-用于模糊查询", paramType = "query", required = false, dataType = "String")
    @GetMapping("/getRoleList")
    public List<TabRole> getRoleList(String roleName) {
        //传递空值，查询全部
        PageResult pageResult = roleService.queryRoleList(null, null, roleName, "1");
        List<TabRole> roleList = pageResult.getItems();
        return roleList;
    }

    @ApiOperation(value = "获取对应用户的角色id", notes = "获取用户对应的角色id，显示对应已绑定的角色")
    @ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String")
    @GetMapping("/getRoleIdListByUserId")
    public List<String> getRoleIdListByUserId(String userName) {
        log.info("登录账号：" + userName);
        List<String> roleIdList = new ArrayList<>();
        List<TabRole> roleListByUId = userService.getRoleListByUId(userName);
        if (roleListByUId.size() > 0) {
            for (TabRole tabRole : roleListByUId) {
                roleIdList.add(tabRole.getId());
            }
        }
        return roleIdList;
    }

    @ApiOperation(value = "修改用户手机号", notes = "修改用户手机号")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "新手机号", paramType = "query", required = true, dataType = "String")})
    @PutMapping("/updatePhone")
    public Response updatePhone(String phone, String userName) {
        if (StringUtils.isBlank(userName)) {
            return Response.failure("2003", "登录账号不能为空");
        }
        if (StringUtils.isBlank(phone)) {
            return Response.failure("2004", "手机号不能为空");
        } else {
            boolean result = Validator.isMobile(phone);
            if (!result) {
                return Response.failure("2011", "手机号码格式不正确");
            }
        }
        Response response = validate(userName, phone, userName, null);
        if ("0".equals(response.getCode())) {
            boolean b = userService.updateUserPhone(phone, userName);
            if (b) {
                return Response.success().message("手机号修改成功");
            }
            return Response.failure("手机号更换失败");
        }
        return response;
    }

    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "要修改的密码", paramType = "query", required = true, dataType = "String")})
    @PutMapping("/forgetPassword")
    public Response forgetPassword(String password, String userName) {
        log.info("修改密码为：" + password);
        if (StringUtils.isBlank(userName)) {
            return Response.failure("2005", "登录账号不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return Response.failure("2006", "密码不能为空");
        } else {
            boolean result = Validator.isPassword(password);
            if (!result) {
                return Response.failure("2012", "密码格式不正确，不能包含特殊字符并且长度6-16");
            }
        }
        boolean result = userService.forgetPassoword(password, userName);
        if (result) {
            return Response.success().message("密码修改成功");
        }
        return Response.failure("修改失败");
    }

    @ApiOperation(value = "头像上传", notes = "头像上传")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "file", value = "文件（头像）", required = true, dataType = "MultipartFile")})
    @PostMapping("/uploadFile")
    public Response uploadProfile(MultipartFile file, String userName) {
        //获取后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (StringUtils.isBlank(userName)) {
            return Response.failure("2008", "登录账号不能为空");
        }
        if (null == file) {
            return Response.failure("2009", "密码不能为空");
        }
        if (!"JPG".equals(suffix.toUpperCase()) || "JPEG".equals(suffix.toUpperCase()) || "PNG".equals(suffix.toUpperCase())) {
            return Response.failure("2010", "只能上传JPG，JPEG，PNG格式的头像");
        }
        //获取当前用户的登录账号
        boolean result = userService.updateHeadImg(file, userName);
        if (result) {
            //上传成功，获取头像路径，显示
            String headImg = userService.getUserById(userName).getHeadImg();
            return Response.success(headImg).message("上传成功");
        }
        return Response.failure("上传头像失败");
    }

    @ApiOperation(value = "根据登录账号获取对应用户信息", notes = "根据登录账号获取对应用户信息")
    @ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String")
    @GetMapping("/getUserById")
    public Response getUserById(String userName) {
        log.info("用户名:" + userName);
        TabSubscriber user = userService.getUserById(userName);
        log.info("用户数据:" + user);
        return Response.success(user);
    }

    @ApiOperation(value = "身份认证", notes = "身份认证")
    @ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String")
    @RequestMapping(value = "/authentication", produces = "application/json", method = RequestMethod.POST)
    public Response Authentication(@RequestBody @Validated TabCustomer tabCustomer, String userName, BindingResult bindingResult) {
        //检验非空，格式
        if (null != bindingResult) {
            //校验文本框
            if (bindingResult.hasErrors()) {
                for (FieldError fieldError : bindingResult.getFieldErrors()) {
                    String defaultMessage = fieldError.getDefaultMessage();
                    return Response.failure("4015", defaultMessage);
                }
            }
        }
        if (StringUtils.isBlank(userName)) {
            return Response.failure("2013", "登录账号不能为空");
        }
        String custid = userService.getUserById(userName).getCustid();
        //判断未实名状态下才可实名
        if (StringUtils.isBlank(custid)) {
            boolean result = userService.Authentication(tabCustomer, userName);
            if (result) {
                return Response.success().message("认证成功");
            }
        }
        return Response.failure("认证失败");

    }

    @ApiOperation(value = "显示用户列表（分页）", notes = "显示用户列表（分页）")
    @ApiImplicitParams({@ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", paramType = "query", required = true, dataType = "int")})
    @GetMapping("/showUserList")
    public Response<PageResult> showUserList(Integer currentPage, Integer size) {
        PageResult<TabSubscriber> pageResult = userService.getUserList(currentPage, size);
        return Response.success().data(pageResult);
    }

    @ApiOperation(value = "修改用户数据", notes = "修改用户数据")
    @RequestMapping(value = "/updateUser", produces = "application/json", method = RequestMethod.PUT)
    public Response updateUser(@RequestBody @Validated TabSubscriber tabSubscriber, BindingResult bindingResult) {
        log.info("用户数据==" + tabSubscriber);
        Response response = validate(tabSubscriber.getId(), tabSubscriber.getMobile(), tabSubscriber.getId(), bindingResult);
        if ("0".equals(response.getCode())) {
            boolean result = userService.updateUser(tabSubscriber);
            if (result) {
                return Response.success().message("修改成功");
            }
            return Response.failure().message("修改失败");
        }
        return response;
    }

    @ApiOperation(value = "新增用户数据", notes = "新增用户数据")
    @RequestMapping(value = "/saveUser", produces = "application/json", method = RequestMethod.POST)
    public Response saveUser(@RequestBody @Validated TabSubscriber tabSubscriber, BindingResult bindingResult) {
        Response response = validate(tabSubscriber.getId(), tabSubscriber.getMobile(), null, bindingResult);
        if ("0".equals(response.getCode())) {
            boolean b = userService.saveUser(tabSubscriber);
            if (b) {
                return Response.success().message("新增成功");
            }
            return Response.failure("新增失败");
        }
        return response;
    }

    @ApiOperation(value = "角色绑定", notes = "角色绑定")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "roleIds", value = "角色id数组", required = true, paramType = "query", allowMultiple = true, dataType = "String")})
    @PostMapping("/saveRoleByUser")
    public Response saveRoleByUser(String[] roleIds, String userName) {
        if (null == roleIds || roleIds.length < 0) {
            return Response.failure("2001", "请选择角色");
        }
        if (StringUtils.isBlank(userName)) {
            return Response.failure("2002", "用户登录账号为空");
        }
        boolean result = userService.saveRoleByUser(roleIds, userName);
        if (result) {
            return Response.success("绑定成功");
        }
        return Response.failure("绑定失败，请检查原因");
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParam(name = "userName", value = "登录账号", paramType = "query", required = true, dataType = "String")
    @DeleteMapping("/deleteUser")
    public Response deleteUser(String userName) {
        if (StringUtils.isBlank(userName)) {
            return Response.failure("2014", "登录账号不能为空");
        }
        log.info("登录账号：" + userName);
        //查询用户是否绑定角色
        List<TabRole> roleListByUId = userService.getRoleListByUId(userName);
        if (roleListByUId.size() > 0) {
            //初始化
            String roleName = "";
            //遍历角色数据，字符串拼接
            for(int i =0;i<roleListByUId.size();i++){
                if (i>0){
                    roleName+=",";
                }
                roleName+=roleListByUId.get(i).getName();
            }
            log.info("角色="+roleName);
            return Response.failure("4018", "抱歉不能删除该用户,有角色["+roleName+"]绑定");
        } else {
            boolean b = userService.deleteUser(userName);
            if (b) {
                return Response.success().message("删除成功");
            }
        }
        return Response.failure("删除失败");
    }

}
