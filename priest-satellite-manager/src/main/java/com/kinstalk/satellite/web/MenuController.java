package com.kinstalk.satellite.web;

import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.domain.MenuType;
import com.kinstalk.satellite.service.api.MenuService;
import com.kinstalk.satellite.service.api.MenuTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/menu")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Resource
    private MenuService menuService;

    @Resource
    private MenuTypeService menuTypeService;

    private static Integer pageSize = 15;

    @RequestMapping(value = "/edit", method = {RequestMethod.GET})
    public String edit(@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
                       Model view) {
        try {

            if (id != null && id.longValue() > 0) {
                Menu menu = menuService.queryMenu(id);
                view.addAttribute("menu", menu);
            }


            // 按照menuTypeId查询全部根节点。
            List<Menu> rootMenuList = menuService
                    .queryAllRootMenu();
            List<Menu> rootMenuListSort = new ArrayList<Menu>();
            loopMenu(rootMenuList, rootMenuListSort);

            List<Menu> rootMenuListTemp = new ArrayList<Menu>();
            //创建一个id=0的根节点。
            Menu rootMenu = new Menu();
            rootMenu.setId(0L);
            rootMenu.setName("Root");//设置根节点名称。
            rootMenuListTemp.add(rootMenu);
            // 直接将根节点加入到List
            rootMenuListTemp.addAll(rootMenuListSort);

            view.addAttribute("rootMenuList", rootMenuListTemp);


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "menu/edit";
    }


    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public String delete(@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
                         Model view) {
        try {

            long rows = menuService.deleteMenu(id);
            //删除缓存。
            menuService.removeMenuCache();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "redirect:/menu/list/";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public String save(Menu menu,
                       Model view) {
        try {

            if (menu != null) {
                if (menu.getId() != null && menu.getParentId() != null) {
                    if (menu.getId().longValue() == menu.getParentId().longValue()) {
                        view.addAttribute("errorMsg", "不能设置自己为父节点。");
                        //当数据出错，返回修改页面。
                        return edit(menu.getId(), view);
                    }
                }
            }

            long rows = menuService.saveMenu(menu);
            view.addAttribute("menu", menu);
            //删除缓存。
            menuService.removeMenuCache();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "redirect:/menu/list/";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public String list(@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
                       Model view) {
        try {

            //按照类型，查询全部菜单。
            List<Menu> menuList = menuTypeService.queryAllMenu();
            view.addAttribute("menuList", menuList);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "/menu/list";
    }


    /**
     * moveUpMenu
     */
    @RequestMapping(value = "/moveUp", method = {RequestMethod.GET})
    public String saveMoveUp(
            @RequestParam(value = "id", required = true) Long id) throws Exception {
        // save or update ItemContent.
        menuService.moveUpMenuById(id);

        //删除缓存。
        menuService.removeMenuCache();

        return "redirect:/menu/list";
    }

    /**
     * moveDownMenu
     */
    @RequestMapping(value = "/moveDown", method = {RequestMethod.GET})
    public String saveMoveDown(@RequestParam(value = "id", required = true) Long id) throws Exception {
        // save or update ItemContent.
        menuService.moveDownMenuById(id);

        //删除缓存。
        menuService.removeMenuCache();

        return "redirect:/admin/menu/list";
    }


    private void loopMenu(List<Menu> rootMenuList, List<Menu> rootMenuListSort) {
        for (Menu menu : rootMenuList) {
            int level = menu.getLevel().intValue();
            String space = "";
            while (level-- > 0) {
                space += "\u251C\u2500";// 线上下一级菜单。
            }
            String userName = menu.getName();
            userName = space + userName;
            menu.setName(userName);
            rootMenuListSort.add(menu);
            if (menu.getChildren() != null) {
                loopMenu(menu.getChildren(), rootMenuListSort);
            }
        }
    }

}
