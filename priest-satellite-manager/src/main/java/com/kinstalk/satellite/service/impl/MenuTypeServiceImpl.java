package com.kinstalk.satellite.service.impl;


import com.kinstalk.satellite.common.exception.AppException;
import com.kinstalk.satellite.common.page.Page;
import com.kinstalk.satellite.dao.MenuTypeMapper;
import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.domain.MenuType;
import com.kinstalk.satellite.service.api.MenuService;
import com.kinstalk.satellite.service.api.MenuTypeService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuTypeServiceImpl implements MenuTypeService {

    private static final Logger logger = Logger.getLogger(MenuTypeServiceImpl.class);

    @Resource
    private MenuTypeMapper menuTypeMapper;

    @Resource
    private MenuService menuService;

    @Override
    public Page<MenuType> queryMenuTypePage(Integer currentPage, Integer pageSize) {

        Page<MenuType> page = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderField", "id");
        params.put("orderFieldType", "desc");

        try {
            //设置页数。
            page = new Page<MenuType>(currentPage, pageSize);
            Integer size = menuTypeMapper.selectListCount(params);
            if (size == null || size <= 0) {
                return page;
            }
            page.setTotalCount(size);
            params.put("startIndex", page.getStartIndex());
            params.put("pageSize", page.getPageSize());
            page.setResult(menuTypeMapper.selectList(params));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return page;
    }

    @Override
    public MenuType queryMenuType(Long id) {
        MenuType menuType = null;
        try {

            menuType = menuTypeMapper.selectById(id);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return menuType;
    }

    @Override
    public long deleteMenuType(Long id) {
        long rows = 0;
        try {

            rows = menuTypeMapper.delete(id);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return rows;
    }

    @Override
    public long saveMenuType(MenuType menuType) {
        long rows = 0;
        try {
            if (menuType != null) {
                if (menuType.getId() != null && menuType.getId() != 0) {
                    //更新
                    rows = menuTypeMapper.update(menuType);
                } else {
                    //插入
                    rows = menuTypeMapper.save(menuType);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return rows;
    }

    @Override
    public List<Menu> queryAllMenu() {



            // 去掉module.parentId = 0 and 将所有菜单一次查出
            List<Menu> menuList = menuService.queryMenuByParentId(0L);


            // 递归查询数据。
            menuService.loopQueryMenu(menuList);




        // 返回菜单列表
        return menuList;

    }

    @Override
    public MenuType queryMenuTypeByUidAndTypeId(String uid, Long menuTypeId) {
        MenuType menuType = null;

        // 判断是否是开发邮箱.
//        boolean isDevUid = checkDevUid(uid, menuTypeId);

        logger.info("findMenuByEmailMenuTypeId,uid" + uid + "menuTypeId:"
                + menuTypeId);
        try {
            if (StringUtils.isBlank(uid) || menuTypeId == null) {
                // 两个参数都为空或 null 返回null.
                return null;
            }

            // 获得缓存管理.
            CacheManager singletonCacheManager = CacheManager.create();

            // 用户Emailcache.
            Cache menuTypeUidCache = null;

            if (singletonCacheManager.cacheExists("MenuTypeUidCache")) {
                // 如果存在直接获得.
                menuTypeUidCache = singletonCacheManager
                        .getCache("MenuTypeUidCache");
            } else {// 否则创建.
                // 可以存5000个数据.60 * 20 设置20分钟缓存.
                menuTypeUidCache = new Cache("MenuTypeUidCache", 5000, false, false, 60 * 20, 60 * 10);
                // 放入缓存.
                singletonCacheManager.addCache(menuTypeUidCache);
            }
            System.out.println(menuTypeUidCache);
            String cacheKey = uid + "_menuTypeId_" + menuTypeId.longValue();
            if (menuTypeUidCache.isKeyInCache(cacheKey)) {// 存在缓存 取到缓存.
                // 按照Email取得缓存.
                Element element = menuTypeUidCache.get(cacheKey);
                try {
                    menuType = (MenuType) element.getValue();
                } catch (Exception e) {// 有可能缓存会有问题.
                    System.out.println(e.getMessage());
                }
                System.out.println("使用缓存:" + cacheKey);
            }

            // 缓存没有查询到从数据库中查询.
            if (menuType == null) {

                menuType = menuTypeMapper.selectById(menuTypeId);

                // 判断是否是开发邮箱.

                    if (menuType != null) {
                        //直接调用service方法。
                        List<Menu> menuList = menuService.queryMenuByTypeIdAndParentIdAndUid(menuTypeId, 0L,uid);
                        // 递归查询数据。
                        menuService.loopQueryMenuAndUid(menuList,uid);

                        // 设置子菜单
                        menuType.setMenuList(menuList);

                        // 放入缓存.key 是Email.
                        Element element = new Element(cacheKey, menuType);
                        menuTypeUidCache.put(element);
                    }
                } else {
                    if (menuType != null) {
                        //直接调用service方法。
                        List<Menu> menuList = menuService.queryMenuByParentId( 0L);
                        // 递归查询数据。
                        menuService.loopQueryMenu(menuList);
                        // 设置子菜单
                        menuType.setMenuList(menuList);

                        // 放入缓存.key 是Email.
                        Element element = new Element(cacheKey, menuType);
                        menuTypeUidCache.put(element);
                    }
                }


        } catch (Exception e) {
            logger.info("查询Menu全部异常");
            e.printStackTrace();
        }
        // 返回..
        return menuType;
    }

    //查询全部菜单类型，并按orderId封装
    private List<MenuType> selectAllRootMenuByType() {

        Map<String, Object> params = new HashMap<String, Object>();
        //查询全部根节点数据。parentId = 0;
        params.put("orderField", "order_id");
        params.put("orderFieldType", "ASC");
        return menuTypeMapper.selectList(params);
    }

    /**
     * 验证是否是开发邮箱.
     */
    @Override
    public boolean checkDevUid(String uid, Long menuTypeId) {
        // 如果是开发邮箱则是test前缀开头的.
        String defaultUid = "dev_test_" + menuTypeId.longValue();
        if (uid != null && defaultUid.equals(uid)) {
            return true;
        }
        return false;
    }
}