package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.AdminUser;
import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.domain.UserGroup;

import java.util.List;
import java.util.Map;

public interface UserGroupMapper {

    public long save(UserGroup userGroup);

    public long saveUser(AdminUser adminUser);

    public long update(UserGroup userGroup);

    public long delete(UserGroup userGroup);

    public List<UserGroup> selectList(Map<String, Object> params);

    public Integer selectListCount(Map<String, Object> params);

    public UserGroup selectById(Long id);

    public List<AdminUser> selectAdminUser();

    public long updateUser(AdminUser adminUser);

}
