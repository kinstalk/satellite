package com.kinstalk.satellite.service.impl;

import com.kinstalk.satellite.common.exception.AppException;
import com.kinstalk.satellite.common.page.Page;
import com.kinstalk.satellite.dao.GroupMapper;
import com.kinstalk.satellite.dao.GroupMenuMapper;
import com.kinstalk.satellite.dao.MenuMapper;
import com.kinstalk.satellite.domain.Group;
import com.kinstalk.satellite.domain.GroupMenu;
import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.service.api.GroupService;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger logger = Logger.getLogger(GroupServiceImpl.class);

    @Resource
    private GroupMapper groupMapper;

    @Resource
    private GroupMenuMapper groupMenuMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public Page<Group> queryGroupPage(Integer currentPage, Integer pageSize) {

        Page<Group> page = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderField", "id");
        params.put("orderFieldType", "desc");

        try {
            //设置页数。
            page = new Page<Group>(currentPage, pageSize);
            Integer size = groupMapper.selectListCount(params);
            if (size == null || size <= 0) {
                return page;
            }
            page.setTotalCount(size);
            params.put("startIndex", page.getStartIndex());
            params.put("pageSize", page.getPageSize());
            page.setResult(groupMapper.selectList(params));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return page;
    }

    @Override
    public Group queryGroup(Long id) {
        Group group = null;
        try {

            group = groupMapper.selectById(id);
            //将group下面的MenuList设置。
            List<Menu> menuList = menuMapper.selectMenuListByGroupId(id);
            if (group != null && menuList != null) {
                group.setMenuList(menuList);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return group;
    }


    @Override
    public long deleteGroup(Long id) {
        long rows = 0;
        try {

            rows = groupMapper.delete(id);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return rows;
    }

    @Override
    public long saveGroup(Group group) {
        long rows = 0;
        try {
            if (group != null) {
                if (group.getId() != null && group.getId() != 0) {
                    //更新
                    rows = groupMapper.update(group);

                    List<GroupMenu> groupMenuList = queryGroupMenuByGroupId(group.getId());
                    if (groupMenuList != null) {
                        for (GroupMenu groupMenu : groupMenuList) {
                            groupMenuMapper.delete(groupMenu);
                        }
                    }
                } else {
                    //插入
                    rows = groupMapper.save(group);
                }
                //保存菜单列表数据。
                if (group.getMenuList() != null) {
                    for (Menu menu : group.getMenuList()) {
                        GroupMenu groupMenu = new GroupMenu();
                        groupMenu.setMenuId(menu.getId());
                        groupMenu.setGroupId(group.getId());
                        groupMenuMapper.save(groupMenu);
                    }
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException(e);
        }
        return rows;
    }

    @Override
    public List<Group> queryAllGroup() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderField", "order_id");
        params.put("orderFieldType", "desc");
        return groupMapper.selectList(params);
    }

    //查询子节点封装。
    private List<GroupMenu> queryGroupMenuByGroupId(Long groupId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("groupId", groupId);
        return groupMenuMapper.selectList(params);
    }


}