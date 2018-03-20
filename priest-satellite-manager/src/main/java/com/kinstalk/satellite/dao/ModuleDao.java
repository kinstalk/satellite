package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.Module;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午12:08
 */
public interface ModuleDao {

    String SELECT_ALL_FIELD = "module_id as moduleId,module_url as moduleUrl,create_time as createTime";

    String INSERT_ALL_FIELD = "module_url,create_time,app_id";

    String TABLE_MODULE = " `module` ";


    /**
     * 根据接口id查询接口信息
     *
     * @param moduleId 模块id
     * @return 模块信息
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_MODULE + " where module_id = #{moduleId}")
    @Options(useCache = true)
    Module getById(@Param("moduleId") Long moduleId);

    /**
     * 新增一条模块信息
     *
     * @param module 模块信息对象
     * @return
     */
    @Insert(
            "insert ignore into " + TABLE_MODULE + " (" + INSERT_ALL_FIELD + ") " +
                    "values (#{moduleUrl},#{createTime})")
    @Options(useCache = true)
    int insertModule(Module module);


    /**
     * 批量插入记录
     *
     * @param modules
     * @return
     */
    @Options(useCache = true)
    @Insert("<script>" +
            "insert ignore into " + TABLE_MODULE + " (" + INSERT_ALL_FIELD + ") values" +
            "<foreach item=\"item\" index=\"index\" collection=\"modules\" open=\"(\" separator=\"),(\" close=\")\">\n" +
            "\t#{item.moduleUrl},#{item.createTime},#{item.appId}\n" +
            "\t</foreach> </script>")
    int batchInsertModule(@Param("modules") List<Module> modules);

    /**
     * 根据创建时间批量查询模块
     *
     * @param times 时间列表
     * @return 模块信息列表
     */
    @Select("<script> \n" +
            "select " + SELECT_ALL_FIELD + "\n" +
            "from " + TABLE_MODULE + "\n" +
            "where create_time in \n" +
            "<foreach item=\"item\" index=\"index\" collection=\"times\" open=\"(\" separator=\",\" close=\")\">\n" +
            "\t#{item}\n" +
            "\t</foreach>\n" +
            "</script>")
    @Options(useCache = true)
    List<Module> batchFindByTime(@Param("times") List<Long> times);

    /**
     * 查询所有模块信息
     *
     * @return
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_MODULE)
    @Options(useCache = true)
    List<Module> findAll();
}
