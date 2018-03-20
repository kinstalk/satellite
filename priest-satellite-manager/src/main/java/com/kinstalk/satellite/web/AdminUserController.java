package com.kinstalk.satellite.web;

import com.google.common.base.Strings;
import com.kinstalk.satellite.domain.AdminUser;
import com.kinstalk.satellite.domain.Group;
import com.kinstalk.satellite.domain.UserGroup;
import com.kinstalk.satellite.filter.AdminLoginFilter;
import com.kinstalk.satellite.utils.CacheUtils;
import com.kinstalk.satellite.service.api.AdminUserService;
import com.kinstalk.satellite.service.api.GroupService;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin", method = {RequestMethod.GET, RequestMethod.POST})
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private GroupService groupService;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static Integer pageSize = 15;

    @RequestMapping(value = "/edit", method = {RequestMethod.PUT})
    public String edit(@RequestParam(value = "id", required = false, defaultValue = "") String id,
                       Model view) {
        try {
            AdminUser adminUser = null;
            if (!Strings.isNullOrEmpty(id)) {
                adminUser = adminUserService.queryAdminUserAndGroupList(id);
                //进入修改页面的时候将密码设置为空。
                adminUser.setPassword("");
                view.addAttribute("adminUser", adminUser);
            }

            //查询全部数据。
            List<Group> groupList = groupService.queryAllGroup();
            loopGroup(groupList, adminUser);
            view.addAttribute("groupList", groupList);


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "adminUser/edit";
    }


    //循环并赋值isChecked。
    private void loopGroup(List<Group> groupList, AdminUser adminUser) {
        if (adminUser != null && adminUser.getUserGroupList() != null && groupList != null) {

            //使用hashmap存储，避免多次循环。
            Map<Long, Long> userGroupMap = new HashMap<Long, Long>();
            for (UserGroup userGroup : adminUser.getUserGroupList()) {
                if (userGroup != null && userGroup.getGroupId() != null) {
                    userGroupMap.put(userGroup.getGroupId(), 0L);
                }
            }
            //循环判断。
            for (Group group : groupList) {
                if (group != null && group.getId() != null && userGroupMap.containsKey(group.getId())) {
                    //设置成已经选择。
                    group.setIsChecked(1);
                }
            }
        }
    }

    //刷新Cache，删除cache。
    @RequestMapping(value = "/refreshCache", method = {RequestMethod.DELETE})
    public String refreshCache(Model view) {
        try {

            CacheUtils.removeAllElementByKey(AdminLoginFilter.USER_MENU_FILTER_KEY);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "redirect:/admin/list";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public String save(@RequestParam(value = "groupIds", required = false) Long[] groupIds,
                      AdminUser adminUser,
                       Model view) {
        try {
//            if (Strings.isNullOrEmpty(adminUser.getEmail())) {
//                view.addAttribute("errorMsg", "email不能为空。");
//                view.addAttribute("adminUser", adminUser);
//                return "admin/adminUser/edit";
//            }
//
//            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//            Matcher matcher = pattern.matcher(adminUser.getEmail());
//            if (!matcher.matches()) {
//                view.addAttribute("errorMsg", "email格式不正确。");
//                view.addAttribute("adminUser", adminUser);
//                return "admin/adminUser/edit";
//            }
//            if (Strings.isNullOrEmpty(adminUser.getPassword())) {
//                view.addAttribute("errorMsg", "密码不能为空。");
//                view.addAttribute("adminUser", adminUser);
//                return "admin/adminUser/edit";
//            }
//            if (adminUser.getPassword().indexOf("{SHA}") == 0) {
//                view.addAttribute("errorMsg", "密码格式不对，请重新输入。");
//                view.addAttribute("adminUser", adminUser);
//                return "admin/adminUser/login";
//            }
            //将Email的前面的字母数字作为ID。
            //String emailName = StringUtils.substringBefore(adminUser.getEmail(), "@");

            //查下数据。
//            AdminUser adminUser = adminUserService.queryAdminUser(id);
//            if (adminUser != null) {

                // 设置组菜单
                if (groupIds != null) {
                    // 将数组翻转。展示顺序和数据库插入顺序相反。
                    ArrayUtils.reverse(groupIds);
                    List<UserGroup> userGroupList = new ArrayList<UserGroup>();
                    for (Long groupId : groupIds) {
                        // 如果有数据将menu放到list.
                        System.out.println("groupId:" + groupId);
                        UserGroup userGroup = new UserGroup();
                        userGroup.setGroupId(groupId);
                        userGroup.setUserId(adminUser.getId());
                        userGroupList.add(userGroup);
                    }
                    // 更新用户菜单。
                    adminUser.setUserGroupList(userGroupList);
                } else {
                    List<UserGroup> userGroupList = new ArrayList<UserGroup>();
                    // 组菜单全部删除的情况.!!
                    adminUser.setUserGroupList(userGroupList);
                }
                //只按照email前面的用户名判断，保证增加的时候
//            if (Strings.isNullOrEmpty(adminUser.getId()) && adminUserTmp != null) {
//                view.addAttribute("errorMsg", "该用户名：【" + emailName + "】已经存在。");
//                view.addAttribute("adminUser", adminUser);
//                return "admin/adminUser/edit";
//            }
//            //验证完数据唯一性之后在setId。
//            adminUser.setId(emailName);
                long rows = adminUserService.saveAdminUser(adminUser);
//            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "redirect:/admin/list";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public String list(@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
                       Model view) {
        try {

            List<AdminUser> adminList = adminUserService.queryAdminUser();
            //放入page对象。
            view.addAttribute("adminList", adminList);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "/adminUser/list";
    }

}
