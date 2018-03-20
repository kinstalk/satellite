package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.domain.Menu;

import java.util.List;


public interface MenuService {

    public Menu queryMenu(Long id);

    public long deleteMenu(Long id);

    public long saveMenu(Menu menu);

    public void removeMenuCache();

    public void moveUpMenuById( Long id);

    public void moveDownMenuById(Long id);

    public List<Menu> queryAllRootMenu();

    public void loopQueryMenu(List<Menu> menuList);

    public List<Menu> queryMenuByParentId(Long parentId);

    //按照用户查询菜单。
    public List<Menu> queryMenuByTypeIdAndParentIdAndUid(Long menuTypeId, long l, String uid);

    //按照用户递归查询菜单。
    public void loopQueryMenuAndUid(List<Menu> menuList, String uid);
}

