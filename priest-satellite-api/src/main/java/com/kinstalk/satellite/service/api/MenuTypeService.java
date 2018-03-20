package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.common.page.Page;
import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.domain.MenuType;

import java.util.List;


public interface MenuTypeService {

    public Page<MenuType> queryMenuTypePage(Integer currentPage, Integer pageSize);

    public MenuType queryMenuType(Long id);

    public long deleteMenuType(Long id);

    public long saveMenuType(MenuType menuType);

    public List<Menu> queryAllMenu();

    public MenuType queryMenuTypeByUidAndTypeId(String uid, Long menuTypeId);

    public boolean checkDevUid(String uid, Long menuTypeId);

    }

