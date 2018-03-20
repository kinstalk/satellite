package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.ReportLastHoursApi;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午2:44
 */
public interface ReportLastHoursApiDao {

    String SELECT_ALL_FIELD = "api_id as apiId,module_id as moduleId,success_rate as successRate,total_count as totalCount," +
            "max_cost_time as maxCostTime,min_cost_time as minCostTime,ave_cost_time as aveCostTime,create_time as createTime,update_time as updateTime";

    String INSERT_ALL_FIELD = "api_id,module_id,success_rate,total_count,max_cost_time,min_cost_time,ave_cost_time,create_time,update_time,agent_id,app_id";

    String TABLE_LAST_HOURS = " `report_last_hours_api` ";

    /**
     * 新增一条结果信息
     *
     * @param reportLastHoursApi 报表信息
     * @return
     */
    @Insert(
            "insert into " + TABLE_LAST_HOURS + " (" + INSERT_ALL_FIELD + ") " +
                    "values (#{apiId},#{moduleId},#{successRate},#{totalCount},#{maxCostTime},#{minCostTime},#{aveCostTime},#{createTime},#{updateTime})")
    int insert(ReportLastHoursApi reportLastHoursApi);


    /**
     * 根据接口id查询接口过去24小时信息
     *
     * @param apiId 接口id
     * @return 接口信息
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_LAST_HOURS + " where api_id = #{apiId}")
    @Options(useCache = true)
    List<ReportLastHoursApi> getByApiId(@Param("apiId") Long apiId);

    /**
     * 批量插入记录
     *
     * @param reportLastHoursApis 批量插入的列表
     * @return 影响的行数
     */
    @Options(useCache = true)
    @Insert("<script>" +
            "insert into " + TABLE_LAST_HOURS + " (" + INSERT_ALL_FIELD + ") values" +
            "<foreach item=\"item\" index=\"index\" collection=\"reportLastHoursApis\" open=\"(\" separator=\"),(\" close=\")\">\n" +
            "\t#{item.apiId},#{item.moduleId},#{item.successRate},#{item.totalCount},#{item.maxCostTime},#{item.minCostTime},#{item.aveCostTime},#{item.createTime},#{item.updateTime},#{item.agentId},#{item.appId}\n" +
            "\t</foreach> </script>")
    int batchInsertReportLastHour(@Param("reportLastHoursApis") List<ReportLastHoursApi> reportLastHoursApis);

    /**
     * 批量删除创建时间小于某时间的记录
     *
     * @param time 时间
     * @return 影响的行数
     */
    @Delete("delete from " + TABLE_LAST_HOURS + " where create_time < #{time}")
    int batchDeleteLastHours(@Param("time") Long time);

}
