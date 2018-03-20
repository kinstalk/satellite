package com.kinstalk.satellite.web;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kinstalk.satellite.common.page.Page;
import com.kinstalk.satellite.domain.Group;

import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.domain.MenuType;
import com.kinstalk.satellite.service.api.GroupService;
import com.kinstalk.satellite.service.api.MenuService;
import com.kinstalk.satellite.service.api.MenuTypeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/group", method = {RequestMethod.GET, RequestMethod.POST})
public class GroupController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Resource
    private GroupService groupService;

    @Resource
    private MenuService menuService;

    @Resource
    private MenuTypeService menuTypeService;

    private static Integer pageSize = 15;


    private JsonArray loopMenuJson(List<Menu> menuList) {

        JsonArray menuJsonArray = new JsonArray();
        for (Menu menu : menuList) {
            JsonObject menuJson = new JsonObject();
            menuJson.addProperty("id", menu.getId());
            menuJson.addProperty("name", menu.getName());
            menuJson.addProperty("open", true);

            // 设置是否选中，js赋值使用。
            if (menu.getIsChecked() != null
                    && menu.getIsChecked().intValue() == 1) {
                menuJson.addProperty("checked", true);
            } else {
                menuJson.addProperty("checked", false);
            }
            if (menu.getChildren() != null) {
                // 添加到子节点
                JsonArray childrenMenuJsonArray = loopMenuJson(menu
                        .getChildren());
                menuJson.add("children", childrenMenuJsonArray);
            }
            menuJsonArray.add(menuJson);
        }
        return menuJsonArray;
    }


    /**
     * 初始化用户菜单树.
     */
    public void initUserFormTree(Long groupId, Model model) throws Exception {
        List<Menu> menuListtemp = menuTypeService.queryAllMenu();
        MenuType menuType=new MenuType();
        menuType.setMenuList(menuListtemp);
        menuType.setId(1l);
        menuType.setName("admin后台管理");

        if (groupId != null && groupId.longValue() != 0) {
            Group group = groupService.queryGroup(groupId);
            model.addAttribute("group", group);
            // 循环分类下面的菜单

                List<Menu> menuList = menuType.getMenuList();
                if (CollectionUtils.isNotEmpty(menuList)) {
                    loopCheckedMenu(menuList, group);
                }

        } else {
            model.addAttribute("group", new Group());
        }

        List<MenuTypeTreeJson> menuTypeTreeJson = new ArrayList<MenuTypeTreeJson>();


            JsonObject menuTypeJson = new JsonObject();
            menuTypeJson.addProperty("id", "menuType_" + menuType.getId());
            menuTypeJson.addProperty("name", menuType.getName());
            menuTypeJson.addProperty("nocheck", true);
            menuTypeJson.addProperty("open", true);
            menuTypeJson.addProperty("icon", "/css/home.gif");

            JsonArray menuJsonArray = new JsonArray();
            List<Menu> menuList = menuType.getMenuList();
            if (CollectionUtils.isNotEmpty(menuList)) {
                menuJsonArray = loopMenuJson(menuList);
            }

            // 添加到数组里面
            menuTypeJson.add("children", menuJsonArray);
            // 保存数组变量.
            JsonArray menuTypeArray = new JsonArray();
            menuTypeArray.add(menuTypeJson);
            MenuTypeTreeJson json = new MenuTypeTreeJson();
            json.setId(menuType.getId());
            json.setJson(new Gson().toJson(menuTypeArray));
            // 生成多个树.
            menuTypeTreeJson.add(json);


        // 生成多个树.放到页面展示.
        model.addAttribute("menuTypeTreeJson", menuTypeTreeJson);
        // 所有菜单分类.
        model.addAttribute("menuTypeList", menuType);
    }


    //递归循环菜单。将isChecked赋值。
    protected void loopCheckedMenu(List<Menu> allRootMenuList, Group group) {
        if (group != null) {
            for (Menu menu : allRootMenuList) {
                List<Menu> menuList = group.getMenuList();
                if (menuList != null && CollectionUtils.isNotEmpty(menuList)) {
                    // 循环用户菜单看看是否已经选中
                    for (Menu menuTemp : menuList) {
                        if (menu.getId() != null && menuTemp.getId() != null
                                && menu.getId().longValue() == menuTemp.getId().longValue()) {
                            // 如果两个菜单Id相同
                            menu.setIsChecked(1);
                            break;
                        } else {
                            menu.setIsChecked(0);
                        }
                    }

                    List<Menu> childrenMenuList = menu.getChildren();
                    if (CollectionUtils.isNotEmpty(childrenMenuList)) {
                        loopCheckedMenu(childrenMenuList, group);
                    }
                }
            }
        }
    }

    public class MenuTypeTreeJson {

        private Long id;

        private String json;// menuType名称

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }
    }

    @RequestMapping(value = "/edit", method = {RequestMethod.PUT})
    public String edit(@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
                       Model view) {
        try {

//            if (id != null && id.longValue() > 0) {
//                Group group = groupService.queryGroup(id);
//                view.addAttribute("group", group);
//            }
            // 初始化用户菜单树.
            initUserFormTree(id, view);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "/group/edit";
    }


    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public String delete(@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
                         Model view) {
        try {

            long rows = groupService.deleteGroup(id);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "redirect:/group/list/";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public String save(Group group,
                       @RequestParam(value = "menuIds", required = false) Long[] menuIds,
                       Model view) {
        try {

            // 设置组菜单
            if (menuIds != null) {
                // 将数组翻转。展示顺序和数据库插入顺序相反。
                ArrayUtils.reverse(menuIds);
                List<Menu> menuList = new ArrayList<Menu>();
                for (Long menuId : menuIds) {
                    // 如果有数据将menu放到list.
                    System.out.println("menuId:" + menuId);
                    Menu menu = new Menu();
                    menu.setId(menuId);
                    menuList.add(menu);
                }
                // 更新用户菜单。
                group.setMenuList(menuList);
            } else {
                List<Menu> menuList = new ArrayList<Menu>();
                // 组菜单全部删除的情况.!!
                group.setMenuList(menuList);
            }


            long rows = groupService.saveGroup(group);
            view.addAttribute("group", group);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "redirect:/group/list/";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public String list(@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
                       Model view) {
        try {

            Page<Group> page = groupService.queryGroupPage(currentPage, pageSize);
            //放入page对象。
            view.addAttribute("page", page);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "/group/list";
    }

}
