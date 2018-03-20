package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.Api;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 上午11:39
 */
public interface ApiDao {

    String SELECT_ALL_FIELD = "api_id as apiId,api_url as apiUrl,module_id as moduleId,create_time as createTime";

    String INSERT_ALL_FIELD = "api_url,module_id,create_time,app_id";

    String TABLE_API = " `api` ";


    /**
     * 根据接口id查询接口信息
     *
     * @param apiId 接口id
     * @return 接口信息
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_API + " where api_id = #{apiId}")
    @Options(useCache = true)
    Api getById(@Param("apiId") Long apiId);

    /**
     * 新增一条接口信息
     *
     * @param Api 接口信息对象
     * @return
     */
    @Insert(
            "insert ignore into " + TABLE_API + " (" + INSERT_ALL_FIELD + ") " +
                    "values (#{apiUrl},#{moduleId},#{createTime})")
    int insertApi(Api Api);

    /**
     * 批量插入记录
     *
     * @param apis 接口列表信息
     * @return 影响的行数
     */
    @Options(useCache = true)
    @Insert("<script>" +
            "insert ignore into " + TABLE_API + " (" + INSERT_ALL_FIELD + ") values" +
            "<foreach item=\"item\" index=\"index\" collection=\"apis\" open=\"(\" separator=\"),(\" close=\")\">\n" +
            "\t#{item.apiUrl},#{item.moduleId},#{item.createTime},#{item.appId}\n" +
            "\t</foreach> </script>")
    int batchInsertApi(@Param("apis") List<Api> apis);

    /**
     * 根据模块id查询模块的接口列表
     *
     * @param moduleId 模块id
     * @return
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_API + " where module_id = #{moduleId}")
    @Options(useCache = true)
    List<Api> findByModuleId(@Param("moduleId") Long moduleId);

    /**
     * 查询所有接口信息
     *
     * @return
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_API+" where app_id=#{appId}")
    @Options(useCache = true)
    List<Api> findAll(@Param("appId") Integer appId);

    /**
     * 根据创建时间批量查询接口
     *
     * @param times 时间列表
     * @return 接口信息列表
     */
    @Select("<script> \n" +
            "select " + SELECT_ALL_FIELD + "\n" +
            "from " + TABLE_API + "\n" +
            "where create_time in \n" +
            "<foreach item=\"item\" index=\"index\" collection=\"times\" open=\"(\" separator=\",\" close=\")\">\n" +
            "\t#{item}\n" +
            "\t</foreach>\n" +
            "</script>")
    @Options(useCache = true)
    List<Api> batchFindByTime(@Param("times") List<Long> times);
}
