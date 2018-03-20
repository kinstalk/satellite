package com.kinstalk.satellite.web;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import com.kinstalk.satellite.domain.AdminUser;
import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.filter.AdminLoginFilter;
import com.kinstalk.satellite.utils.EncryptUtil;
import com.kinstalk.satellite.service.api.AdminUserService;
import com.kinstalk.satellite.service.api.MenuService;
import com.kinstalk.satellite.service.api.MenuTypeService;

/**
 * Created by zhangchuanqi on 16/9/29.
 */
@Controller
@RequestMapping(value = "/passport", method = {RequestMethod.GET, RequestMethod.POST})
public class PassportLoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PassportLoginController.class);

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private MenuService menuService;

    @Resource
    private MenuTypeService menuTypeService;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String login(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "returnUrl", required = false, defaultValue = "") String returnUrl, Model view) {
        view.addAttribute("returnUrl", returnUrl);
        try {
            Cookie cookie = new Cookie("cookie_test", "123456");
            //设定有效时间，以s为单位，设置20天超时。
            cookie.setMaxAge(60 * 60 * 24 * 20);
            //设置Cookie路径和域名
            cookie.setPath("/");
//            cookie.setDomain("127.0.0.1:9088");  //域名要以“.”开头
            //发送Cookie文件
            response.addCookie(cookie);
            System.out.println("returnUrl:" + returnUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/passport/index";
    }


    @RequestMapping(value = "/login/save", method = {RequestMethod.POST})
    public String loginSave(AdminUser adminUser, HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam(value = "returnUrl", required = false, defaultValue = "") String returnUrl,
                            Model view) {
        try {
            if (Strings.isNullOrEmpty(adminUser.getId())) {
                view.addAttribute("errorMsg", "用户名不能为空。");
                view.addAttribute("adminUser", adminUser);
                view.addAttribute("returnUrl", returnUrl);
                return "/passport/index";
            }
            if (Strings.isNullOrEmpty(adminUser.getPassword())) {
                view.addAttribute("errorMsg", "密码不能为空。");
                view.addAttribute("adminUser", adminUser);
                view.addAttribute("returnUrl", returnUrl);
                return "/passport/index";
            }
            //将Email的前面的字母数字作为ID。
            //setId，主要还是按照用户ID进行登陆/
            //查下数据。
            long flag = adminUserService.login(adminUser);

            //只按照uid前面的用户名判断，保证增加的时候
            if (flag < 0) {
                view.addAttribute("errorMsg", "该用户名密码错误。");
                view.addAttribute("adminUser", adminUser);
                view.addAttribute("returnUrl", returnUrl);
                return "/passport/index";
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            view.addAttribute("errorMsg", "用户登陆失败。");
            view.addAttribute("adminUser", adminUser);
            return "/passport/index";
        }
        view.addAttribute("successMsg", "用户登陆成功。");
        view.addAttribute("adminUser", adminUser);
        //################# 登陆成功增加cookie。 #################
        try {

            String userId = AdminLoginFilter.passportEncoder(adminUser.getId());
            System.out.println(userId);
            Cookie cookieCode = new Cookie("passport_code", userId);
            //设定有效时间，以s为单位，设置20天超时。
            cookieCode.setMaxAge(60 * 60 * 24 * 20);
            //设置Cookie路径和域名
            cookieCode.setPath("/");


            //生成校验字符串。
            String userKey = EncryptUtil.sha(userId);

            Cookie cookieKey = new Cookie("passport_key", userKey);


            //设定有效时间，以s为单位，设置20天超时。
            cookieKey.setMaxAge(60 * 60 * 24 * 20);

            //设置Cookie路径和域名
            cookieKey.setPath("/");


            //发送Cookie文件
            response.addCookie(cookieKey);
            response.addCookie(cookieCode);


            //System.out.println("returnUrl:"+returnUrl);
            if (Strings.isNullOrEmpty(returnUrl)) {
                //如果没有跳转到首页。
                returnUrl = "/";
            }
            response.sendRedirect(returnUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    public String logout(AdminUser adminUser, HttpServletRequest request,
                         HttpServletResponse response,
                         Model view) {
        try {

//            Cookie[] cookies = request.getCookies();
//            if (cookies != null) { //循环删除所有cookie。
//                for (Cookie cookie : cookies) {
//                    cookie.setValue("");
//                    cookie.setMaxAge(0);
//                    cookie.setDomain(".devops.shuzijiayuan.com");
//                    cookie.setPath("/");
//                    response.addCookie(cookie);
//                }
//            }

            Cookie cookieCode = new Cookie("passport_code", "");
            //设定有效时间，以s为单位，设置20天超时。
            cookieCode.setMaxAge(0);
            //设置Cookie路径和域名
            cookieCode.setPath("/");
            //cookieCode.setDomain(".devops.shuzijiayuan.com");  //域名要以“.”开头

            Cookie cookieKey = new Cookie("passport_key", "");
            //设定有效时间，以s为单位，设置20天超时。
            cookieKey.setMaxAge(0);
            //设置Cookie路径和域名
            cookieKey.setPath("/");
            //cookieKey.setDomain(".devops.shuzijiayuan.com");  //域名要以“.”开头

            //发送Cookie文件
            response.addCookie(cookieCode);
            response.addCookie(cookieKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //退出之后直接跳转到首页，然后交给passport进行判断。
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/logoutHtml", method = {RequestMethod.GET})
    public void logoutHtml(AdminUser adminUser, HttpServletRequest request,
                           HttpServletResponse response,
                           Model view) {
        StringBuffer buffer = new StringBuffer();
        try {

            Cookie cookieCode = new Cookie("passport_code", "");
            //设定有效时间，以s为单位，设置20天超时。
            cookieCode.setMaxAge(0);
            //设置Cookie路径和域名
            cookieCode.setPath("/");
            cookieCode.setDomain(".devops.shuzijiayuan.com");  //域名要以“.”开头

            Cookie cookieKey = new Cookie("passport_key", "");
            //设定有效时间，以s为单位，设置20天超时。
            cookieKey.setMaxAge(0);
            //设置Cookie路径和域名
            cookieKey.setPath("/");
            cookieKey.setDomain(".devops.shuzijiayuan.com");  //域名要以“.”开头

            //发送Cookie文件
            response.addCookie(cookieCode);
            response.addCookie(cookieKey);

            Cookie[] cookies = request.getCookies();
            if (cookies != null) { //循环删除所有cookie。
                for (Cookie cookie : cookies) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setDomain(".devops.shuzijiayuan.com");
                    cookie.setPath("/");
                    buffer.append(cookie.getName());
                    buffer.append("</br>");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //退出之后直接跳转到首页，然后交给passport进行判断。
        writeHtmlToPage(response, buffer.toString());

    }


    /**
     * getMenuList
     */
    @RequestMapping(value = "/userMenuJson")
    public void userMenuJson(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestParam(value = "uid", required = true) String uid)
            throws Exception {

        // 返回类
        JsonObject tmpJson = new JsonObject();
        /* 用户菜单json. */
        JsonArray menuJsonArray = new JsonArray();


        AdminUser adminUser = adminUserService.queryAdminUserAndMenuList(uid);

        if (adminUser != null && adminUser.getMenuList() != null) {
            // 查询用户权限菜单.
            for (Menu menu : adminUser.getMenuList()) {
                // set json属性。

                    // 得到当前系统菜单.
                    JsonObject menuJson = new JsonObject();
                    menuJson.addProperty("id", menu.getId());
                    menuJson.addProperty("name", menu.getName());
                    menuJson.addProperty("url", menu.getUrl());
                    menuJson.addProperty("parentId", menu.getParentId());
                    menuJson.addProperty("level", menu.getLevel());
                    menuJson.addProperty("orderId", menu.getOrderId());
                    menuJsonArray.add(menuJson);


            }

            // 返回菜单权限
            tmpJson.addProperty("userName", adminUser.getName());

            tmpJson.add("menuList", menuJsonArray);

        }

        writeToPage(response, new Gson().toJson(tmpJson));
    }





}

