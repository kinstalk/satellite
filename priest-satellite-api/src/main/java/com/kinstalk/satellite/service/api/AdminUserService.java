package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.domain.AdminUser;
import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.domain.UserGroup;

import java.util.List;


public interface AdminUserService {

    public List<AdminUser> queryAdminUser();

    public AdminUser queryAdminUser(String id);

    public AdminUser queryAdminUserAndGroupList(String id);

    public AdminUser queryAdminUserAndMenuList(String id);

    public List<Menu> queryMenuByGroupId(List<Long> ids);

    public long deleteAdminUser(String id);

    public long saveAdminUser(AdminUser adminUser);

    public long login(AdminUser adminUser);

    public List<UserGroup> queryUserGroupByUserId(String userId);
}

