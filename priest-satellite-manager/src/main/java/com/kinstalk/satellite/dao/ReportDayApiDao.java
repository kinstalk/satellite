package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.ReportDayApi;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午2:52
 */
public interface ReportDayApiDao {

    String SELECT_ALL_FIELD = "th.api_id as apiId,th.module_id as moduleId,th.success_rate as successRate,th.total_count as totalCount," +
            "th.max_cost_time as maxCostTime,th.min_cost_time as minCostTime,th.ave_cost_time as aveCostTime,th.create_time as createTime,th.update_time as updateTime,ta.agent_area as agentArea";

    String INSERT_ALL_FIELD = "api_id,module_id,success_rate,total_count,max_cost_time,min_cost_time,ave_cost_time,create_time,update_time,agent_id,app_id";

    String TABLE_LAST_HOURS = " `report_day_api` ";

    String TABLE_AGENT=" `agent` ";

    /**
     * 新增一条结果信息
     *
     * @param reportDayApi 报表信息
     * @return 影响的行数
     */
    @Insert(
            "insert into " + TABLE_LAST_HOURS + " (" + INSERT_ALL_FIELD + ") " +
                    "values (#{apiId},#{moduleId},#{successRate},#{totalCount},#{maxCostTime},#{minCostTime},#{aveCostTime},#{createTime},#{updateTime})")
    int insert(ReportDayApi reportDayApi);


    /**
     * 根据接口id查询接口过去24小时信息,按接口排序
     *
     * @param startTime 开始时间
     * @return 接口列表
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_LAST_HOURS +" as th," + TABLE_AGENT + " as ta where th.agent_id=ta.agent_id and th.create_time >= #{startTime} and th.create_time <= #{endTime} and th.app_id=#{appId} order by th.module_id,th.api_id desc")
    @Options(useCache = true)
    List<ReportDayApi> findByTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime,@Param("appId") Integer appId);

    /**
     * 查询某个接口某一时间内接口调用情况
     *
     * @param startTime 开始时间
     * @param apiId     接口id
     * @return 接口列表
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_LAST_HOURS + " where create_time >= #{startTime} and api_id = #{apiId}")
    @Options(useCache = true)
    List<ReportDayApi> findByTimeAndId(@Param("startTime") Long startTime, @Param("apiId") Long apiId);

    /**
     * 批量插入记录
     *
     * @param reportDayApis 批量插入的列表
     * @return 影响的行数
     */
    @Options(useCache = true)
    @Insert("<script>" +
            "insert into " + TABLE_LAST_HOURS + " (" + INSERT_ALL_FIELD + ") values" +
            "<foreach item=\"item\" index=\"index\" collection=\"reportDayApis\" open=\"(\" separator=\"),(\" close=\")\">\n" +
            "\t#{item.apiId},#{item.moduleId},#{item.successRate},#{item.totalCount},#{item.maxCostTime},#{item.minCostTime},#{item.aveCostTime},#{item.createTime},#{item.updateTime},#{item.agentId},#{item.appId}\n" +
            "\t</foreach> </script>")
    int batchInsertReportDay(@Param("reportDayApis") List<ReportDayApi> reportDayApis);

    /**
     * 批量删除创建时间小于某时间的记录
     *
     * @param time 时间
     * @return 影响的行数
     */
    @Delete("delete from " + TABLE_LAST_HOURS + " where create_time < #{time}")
    int batchDeleteLogApi(@Param("time") Long time);
}
