package com.kinstalk.satellite.service.impl;

import com.google.common.base.Strings;
import com.kinstalk.satellite.dao.GroupMenuMapper;
import com.kinstalk.satellite.dao.UserGroupMapper;
import com.kinstalk.satellite.domain.AdminUser;
import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.domain.UserGroup;
import com.kinstalk.satellite.service.api.AdminUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {

    private static final Logger logger = Logger.getLogger(AdminUserServiceImpl.class);



    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private GroupMenuMapper groupMenuMapper;

    private static final String USER_OU1_NAME = "Users";
    //private static final String USER_OU2_NAME = "SecurityTest";
    //线上正式环境。product。
    private static final String USER_OU2_NAME = "product";
    private static final String USER_TREE_NAME = String.format("ou=%s,ou=%s", USER_OU1_NAME, USER_OU2_NAME);

    /**
     * http://docs.spring.io/spring-ldap/docs/1.3.x/reference/html/dirobjectfactory.html
     * CRUD 参考文档，查询不带分页。
     * http://docs.spring.io/spring-ldap/docs/2.0.2.RELEASE/reference/
     * 配置地址：
     * http://redmine.kinstalk.com/projects/dev_env_support/wiki/LDAP服务器描述
     * 资料：
     * http://blog.csdn.net/icechenbing/article/details/7645329
     * http://www.mzone.cc/article/621.html
     * ldap错误信息：
     * http://mxdxm.iteye.com/blog/728251
     * <p/>
     * LDAP目录的条目（entry）由属性（attribute）的一个聚集组成，并由一个唯一性的名字引用，即专有名称（distinguished name，DN）。
     * 目录中所有项目都务须拥有一个objectclass属性
     */


    @Override
    public List<AdminUser> queryAdminUser() {
        List<AdminUser> adminList = null;
        try {

            //没有找到分页方法，直接查下全部数据然后显示。
            //数据量不大，么有关系。
            //
//            ContainerCriteria ldapQueryBuilder = query().where("objectclass").is("inetOrgPerson");
//            adminList = ldapTemplate.search(ldapQueryBuilder, new AdminUserContextMapper());
            adminList=userGroupMapper.selectAdminUser();
//            if (adminList != null) {
//                List<AdminUser> adminListNew = new ArrayList<AdminUser>();
//
//                for (AdminUser adminUser : adminList) {
//                    if (!Strings.isNullOrEmpty(adminUser.getDn())&& adminUser.getDn().contains(USER_TREE_NAME)) {
//                        adminListNew.add(adminUser);
//                    }
//                }
//                adminList = adminListNew;
//            }

//            adminList = new ArrayList<AdminUser>();
//            AdminUser adminUser = new AdminUser();
//            adminUser.setId("zhangsan");
//            adminUser.setEmail("zhangsan@home.com");
//            adminList.add(adminUser);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<AdminUser>();
        }
        return adminList;
    }
//    @Override
//    public List<AdminUser> queryAdminUser() {
//        List<AdminUser> adminList = null;
//        try {
//
//            //没有找到分页方法，直接查下全部数据然后显示。
//            //数据量不大，么有关系。
//            //
//            ContainerCriteria ldapQueryBuilder = query().where("objectclass").is("inetOrgPerson");
//            adminList = ldapTemplate.search(ldapQueryBuilder
//                    , new AdminUserContextMapper());
//            if (adminList != null) {
//                List<AdminUser> adminListNew = new ArrayList<AdminUser>();
//
//                for (AdminUser adminUser : adminList) {
//                    if (!Strings.isNullOrEmpty(adminUser.getDn())
//                            && adminUser.getDn().contains(USER_TREE_NAME)) {
//                        adminListNew.add(adminUser);
//                    }
//                }
//                adminList = adminListNew;
//            }
//
////            adminList = new ArrayList<AdminUser>();
////            AdminUser adminUser = new AdminUser();
////            adminUser.setId("zhangsan");
////            adminUser.setEmail("zhangsan@home.com");
////            adminList.add(adminUser);
//
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return new ArrayList<AdminUser>();
//        }
//        return adminList;
//    }

    @Override
    public AdminUser queryAdminUser(String id) {
        AdminUser adminUser = null;
        try {
//            Name dn = buildDn(id);
//            adminUser = (AdminUser) ldapTemplate.lookup(dn, new AdminUserContextMapper());

            //edit查询有问题，目前根据list查询。
            List<AdminUser> adminList = queryAdminUser();
            //放入page对象。
            if (!Strings.isNullOrEmpty(id)) {
                for (AdminUser adminUserTmp : adminList) {
                    if (adminUserTmp != null && !Strings.isNullOrEmpty(adminUserTmp.getId())) {
                        if (adminUserTmp.getId().equals(id)) {
                            adminUser = adminUserTmp;
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return adminUser;
    }

    @Override
    public AdminUser queryAdminUserAndGroupList(String id) {
        AdminUser adminUser = null;
        try {
//            Name dn = buildDn(id);
//            adminUser = (AdminUser) ldapTemplate.lookup(dn, new AdminUserContextMapper());

            //edit查询有问题，目前根据list查询。
            List<AdminUser> adminList = queryAdminUser();
            //放入page对象。
            if (!Strings.isNullOrEmpty(id)) {
                for (AdminUser adminUserTmp : adminList) {
                    if (adminUserTmp != null && !Strings.isNullOrEmpty(adminUserTmp.getId())) {
                        if (adminUserTmp.getId().equals(id)) {
                            adminUser = adminUserTmp;
                            break;
                        }
                    }
                }
            }
            if (adminUser != null) {
                //查询用户组。
                List<UserGroup> userGroupList = queryUserGroupByUserId(adminUser.getId());
                adminUser.setUserGroupList(userGroupList);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return adminUser;
    }

    @Override
    public AdminUser queryAdminUserAndMenuList(String id) {
        AdminUser adminUser = null;
        try {
//            Name dn = buildDn(id);
//            adminUser = (AdminUser) ldapTemplate.lookup(dn, new AdminUserContextMapper());

            //edit查询有问题，目前根据list查询。
            List<AdminUser> adminList = queryAdminUser();
            //放入page对象。
            if (!Strings.isNullOrEmpty(id)) {
                for (AdminUser adminUserTmp : adminList) {
                    if (adminUserTmp != null && !Strings.isNullOrEmpty(adminUserTmp.getId())) {
                        if (adminUserTmp.getId().equals(id)) {
                            adminUser = adminUserTmp;
                            break;
                        }
                    }
                }
            }
            if (adminUser != null) {
                List<Menu> listAllUserList=new ArrayList<Menu>();
                //查询用户组。
                List<UserGroup> userGroupList = queryUserGroupByUserId(adminUser.getId());
                List<Long> musicIds = new ArrayList<>();
                if(userGroupList!=null&&userGroupList.size()>0){

                    for(UserGroup userGroup:userGroupList){
                        musicIds.add(userGroup.getGroupId());
                    }
                }

                listAllUserList.addAll(queryMenuByGroupId(musicIds));


                adminUser.setMenuList(listAllUserList);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return adminUser;
    }

    @Override
    public List<UserGroup> queryUserGroupByUserId(String userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        return userGroupMapper.selectList(params);
    }

    @Override
    public List<Menu> queryMenuByGroupId(List<Long> ids){


        return groupMenuMapper.selectMenuList(ids);

    }

    @Override
    public long deleteAdminUser(String id) {
        long rows = 0;
        try {
            //ldapTemplate.unbind(buildDn(id));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return rows;
    }

    @Override
    public long saveAdminUser(AdminUser adminUser) {
        try {
            if (adminUser != null) {
                //userGroupMapper
                if (adminUser.getId() != null) {
                    //更新
                    List<UserGroup> userGroupList = queryUserGroupByUserId(adminUser.getId());
                    if (userGroupList != null) {
                        for (UserGroup userGroup : userGroupList) {
                            userGroupMapper.delete(userGroup);
                        }
                    }
                }
                //保存菜单列表数据。
                if (adminUser.getUserGroupList() != null) {
                    for (UserGroup userGroup : adminUser.getUserGroupList()) {
                        //保存数据。
                        userGroupMapper.save(userGroup);

                    }
                    if(queryAdminUser(adminUser.getId())!=null){
                        userGroupMapper.updateUser(adminUser);
                    }else{
                        userGroupMapper.saveUser(adminUser);
                    }

                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }


//    public long saveAdminUserLdap(AdminUser adminUser) {
//        long rows = 0;
//        try {
//            if (adminUser != null) {
//                //context = ldapTemplate.lookupContext("cn=zhang san,ou=Users,ou=SecurityTest")
//                //查询数据。
//                Name dn = buildDn(adminUser.getId());
//                DirContextOperations context = null;
//                try {
//                    //按照dn进行数据查下，然后再做更新操作。
//                    context = ldapTemplate.lookupContext(dn);
//                } catch (Exception e) {
//                    logger.info(e.getMessage());
//                }
//                if (context != null) {
//                    mapToContext(adminUser, context);
//                    //ldapTemplate.modifyAttributes(context);
//                    //更新
//                } else {
//                    //插入
//                    context = new DirContextAdapter(dn);
//                    mapToContext(adminUser, context);
//                    //cn必须填写，并且是正确的节点名称
//                    context.setAttributeValue("cn", buildDn(adminUser.getId()));
//                    ldapTemplate.bind(context);
//                }
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//        return rows;
//    }

    @Override
    public long login(AdminUser adminUser) {
        long rows = -1;
        try {
            if (adminUser != null && !Strings.isNullOrEmpty(adminUser.getId())) {
                //查询数据。查询ldap数据。

                List<AdminUser> allAdminUserList = queryAdminUser();
                AdminUser adminUserTmp = null;
                for (AdminUser user : allAdminUserList) {
                    //循环全部用户，找到Id相同的。
                    if (user != null && user.getId() != null && user.getId().equals(adminUser.getId())) {
                        adminUserTmp = user;
                        break;
                    }
                }
				if(adminUserTmp == null) {
					return -1;
				}

                //判断用户的邮箱是否相等。如果不相等返回错误。
                /***if (Strings.isNullOrEmpty(adminUser.getEmail())
                 || Strings.isNullOrEmpty(adminUserTmp.getEmail())
                 || !adminUserTmp.getEmail().equals(adminUser.getEmail())) {
                 return -1;
                 }*/

                //生成密码。
                String passwordOld = adminUserTmp.getPassword();
//                String password = getLdapPassword(adminUser.getPassword());
                String password = adminUser.getPassword();

                //判断两个密码相等。
                if (!Strings.isNullOrEmpty(password)
                        && !Strings.isNullOrEmpty(passwordOld)) {
                    //临时修改bug，ldap修改密码之后增加了一个大写的{MD5}，造成密码不一致。
                    passwordOld = passwordOld.replace("{md5}", "").replace("{MD5}", "");
                    if (password.equals(passwordOld)) {
                        return 1;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return rows;
    }


//    private static Name buildDn(String name) {
//        //绑定姓名是有顺序的，从右往左的顺序。⬅，先插入的数据在最右边。这里要反过来，否则查下不到数据。
//        DistinguishedName dn = new DistinguishedName();
//        dn.add("ou", USER_OU2_NAME);
//        dn.add("ou", USER_OU1_NAME);
//        //uid放在第一个。
//        dn.add("uid", name);
//        System.out.println(dn);
//        return dn;
//    }


    private static String getLdapPassword(String password) {
        MessageDigest md = null;
        try {
            if (Strings.isNullOrEmpty(password)) {
                return "";
            }
            //md = MessageDigest.getInstance("SHA");
            md = MessageDigest.getInstance("md5");
            byte[] digest = md.digest(password.getBytes());
            //将二进制转换成字符串。
            String hexBinary = new HexBinaryAdapter().marshal(digest);
            //做一次base64转换。保证和ldap里面的数据一致。
            String result = (new BASE64Encoder()).encodeBuffer(digest);
            //bug，多了一个换行。
            result = result.replace("\n", "");
            //Ldap 服务器使用SHA进行加密，必须增加一个前缀。
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

//    private static void mapToContext(AdminUser adminUser, DirContextOperations context) {
//        //增加objectClass类型，必须填写。
//        context.setAttributeValues("objectclass", new String[]{"top", "inetOrgPerson"});
//
//        //uid和sn使用Id填写。
//        context.setAttributeValue("uid", adminUser.getId());
//        context.setAttributeValue("sn", adminUser.getId());
//        //email 和 userPassword 必须填写。
//        context.setAttributeValue("mail", adminUser.getEmail());
//        context.setAttributeValue("userpassword", getLdapPassword(adminUser.getPassword()));
//        //ou 的两个节点必须填写。
//        context.setAttributeValue("ou", USER_OU1_NAME);
//        context.setAttributeValue("ou", USER_OU2_NAME);
//    }
//
//    //http://docs.spring.io/spring-ldap/docs/1.3.x/reference/html/dirobjectfactory.html
//    //对ContextMapper 可以获得更多参数。
//    //将数据转换成对象，类似mybatis的mapper。
//    private class AdminUserContextMapper implements ContextMapper<AdminUser> {
//
//        @Override
//        public AdminUser mapFromContext(Object ctx) throws javax.naming.NamingException {
//            DirContextAdapter context = (DirContextAdapter) ctx;
//            //System.out.println("#############"+context.getDn());
//            //System.out.println("#############"+context.getReferralUrl());
//            //System.out.println("#############"+context.getNameInNamespace());
//
//            AdminUser adminUser = new AdminUser();
//            //最重要的获得DN的名字，可以获得节点信息。
//            String dn = context.getDn().toString();
//            if (!Strings.isNullOrEmpty(dn)) {
//                adminUser.setDn(dn);
//            }
//            //获得email信息。
//            String mail = context.getStringAttribute("mail");
//            if (!Strings.isNullOrEmpty(mail)) {
//                adminUser.setEmail(mail);
//            }
//            //获得uid信息，放到id里面。
//            String uid = context.getStringAttribute("uid");
//            if (!Strings.isNullOrEmpty(uid)) {
//                adminUser.setId(uid);
//            }
//            //查询需要使用密码。
//            Object password = context.getObjectAttribute("userpassword");
//            if (password != null && password instanceof byte[]) {
//                //只有密码类型比较特殊是byte数组，特殊处理。
//                adminUser.setPassword(new String((byte[]) password));
//            }
//
//
//            return adminUser;
//        }
//    }
}