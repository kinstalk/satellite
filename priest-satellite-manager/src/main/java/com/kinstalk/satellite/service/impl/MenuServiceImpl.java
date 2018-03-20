package com.kinstalk.satellite.service.impl;


import com.kinstalk.satellite.common.exception.AppException;
import com.kinstalk.satellite.dao.MenuMapper;
import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.filter.AdminLoginFilter;
import com.kinstalk.satellite.utils.CacheUtils;
import com.kinstalk.satellite.service.api.MenuService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger logger = Logger.getLogger(MenuServiceImpl.class);

    @Resource
    private MenuMapper menuMapper;

    @Override
    public Menu queryMenu(Long id) {
        Menu menu = null;
        try {

            menu = menuMapper.selectById(id);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return menu;
    }

    @Override
    public long deleteMenu(Long id) {
        long rows = 0;
        try {

            rows = menuMapper.delete(id);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return rows;
    }

    @Override
    public long saveMenu(Menu menu) {
        long rows = 0;
        try {
            if (menu != null) {

                if (menu.getParentId() != null
                        && menu.getParentId().intValue() != 0) {
                    // 查询父节点
                    Menu parentMenu = queryMenu(menu.getParentId());
                    // 子菜单加 1
                    menu.setLevel(parentMenu.getLevel() + 1);
                }
                if (menu.getParentId() != null
                        && menu.getParentId().intValue() == 0) {
                    // 根节点 level = 1
                    menu.setLevel(1L);
                }

                if (menu.getId() != null && menu.getId() != 0) {
                    //更新
                    menu.setOrderId(null);
                    rows = menuMapper.update(menu);
                } else {
                    //插入
                    rows = menuMapper.save(menu);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return rows;
    }

    @Override
    public void removeMenuCache() {
        //清除key的缓存。
        CacheUtils.removeAllElementByKey(AdminLoginFilter.USER_MENU_FILTER_KEY);
    }

    @Override
    public void moveUpMenuById(Long id) {
        try {
            Menu menu = menuMapper.selectById(id);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("parentId", menu.getParentId());
            params.put("orderId", menu.getOrderId());
            Menu menuTemp = menuMapper.selectPrevMenuByOrderId(params);
            if (menu != null && menuTemp != null) {
                // 交换weight.
                Long orderId = menu.getOrderId();
               // System.out.println("orderId:" + orderId);
               // System.out.println("orderId:" + menuTemp.getOrderId());
                menu.setOrderId(menuTemp.getOrderId());
                menuTemp.setOrderId(orderId);

                menuMapper.update(menuTemp);
                menuMapper.update(menu);
            }
        } catch (Exception e) {
            logger.info("查询Menu全部异常");
            e.printStackTrace();
        }
    }

    @Override
    public void moveDownMenuById(Long id) {
        try {
            Menu menu = menuMapper.selectById(id);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("parentId", menu.getParentId());
            params.put("orderId", menu.getOrderId());
            Menu menuTemp = menuMapper.selectNextMenuByOrderId(params);
            if (menu != null && menuTemp != null) {
                // 交换weight.
                Long orderId = menu.getOrderId();
              //  System.out.println("menu.orderId:" + orderId);
              //  System.out.println("menuTemp.orderId:" + menuTemp.getOrderId());
                menu.setOrderId(menuTemp.getOrderId());
                menuTemp.setOrderId(orderId);

                menuMapper.update(menuTemp);
                menuMapper.update(menu);
            }
        } catch (Exception e) {
            logger.info("查询Menu全部异常");
            e.printStackTrace();
        }
    }


    @Override
    public List<Menu> queryAllRootMenu() {
        try {
            //按照类型，查询根节点菜单。
            List<Menu> menuList = queryMenuByParentId(0L);
            // 递归查询数据。
            loopQueryMenu(menuList);
            return menuList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 递归查询
    @Override
    public void loopQueryMenu(List<Menu> menuList) {
        if (menuList != null) {
           // System.out.println(menuList.size());
            for (Menu menu : menuList) {
               // System.out.println("Menu:" + menu.getId() + ":"+ menu.getName() + menu.getParentId()+ +menuList.size());

                List<Menu> childrenMenuList = queryMenuByParentId(menu.getId());

                if (childrenMenuList != null && childrenMenuList.size() > 0) {
                    // 设置子节点
                    menu.setChildren(childrenMenuList);
                    // 循环子节点
                    loopQueryMenu(childrenMenuList);
                } else {
                    // 解决session关闭问题
                    menu.setChildren(null);
                }
            }
        }
    }

    @Override
    //查询子节点封装。
    public List<Menu> queryMenuByParentId(Long parentId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("parentId", parentId);
        //按照orderId进行排序。
        params.put("orderField", "order_id");
        params.put("orderFieldType", "ASC");
        return menuMapper.selectList(params);
    }

    @Override
    public List<Menu> queryMenuByTypeIdAndParentIdAndUid(Long typeId, long parentId, String uid) {

        //String hql = " select module from Menu module , UserMenuMap userMenuMap , UserInfo userInfo
        // where module.parentId = ? and module.menuType.id = ? and userInfo.email = ? and userMenuMap.userId = userInfo.id
        // and userMenuMap.menuId = module.id order by module.orderId asc ";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("typeId", typeId);
        params.put("parentId", parentId);
        params.put("uid", uid);
        //按照orderId进行排序。
        return menuMapper.queryMenuByTypeIdAndParentIdAndUid(params);
    }


    //递归查询用户菜单。
    @Override
    public void loopQueryMenuAndUid(List<Menu> menuList, String uid) {
        if (menuList != null) {
          //  System.out.println(menuList.size());
            for (Menu menu : menuList) {
               // System.out.println("Menu:" + menu.getId() + ":" + menu.getName() + menu.getParentId() + +menuList.size());

                List<Menu> childrenMenuList = queryMenuByTypeIdAndParentIdAndUid(menu.getTypeId(), menu.getId(), uid);

                if (childrenMenuList != null && childrenMenuList.size() > 0) {
                    // 设置子节点
                    menu.setChildren(childrenMenuList);
                    // 循环子节点
                    loopQueryMenu(childrenMenuList);
                } else {
                    // 解决session关闭问题
                    menu.setChildren(null);
                }
            }
        }
    }

}