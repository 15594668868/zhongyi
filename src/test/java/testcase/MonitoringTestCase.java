package testcase;

import com.alibaba.fastjson.JSON;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.DateUtil;
import utils.FilesUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 监控中心
 * @author: guozhixiong
 * @time: 2020/8/20 14:11
 */
public class MonitoringTestCase extends BaseTest {

    //excel测试用例
    private static String fileName = "中移监控中心.xls";

    //轨迹追踪
    private static final String TRACKING = "tracking";
    //运单监控
    private static final String WAYBILLMONITORING = "waybillMonitoring";
    //仓库温湿度实时监控
    private static final String AREAMONITORING = "areaMonitoring";
    //仓库温湿度历史
    private static final String AREAHISTORY = "areaHistory";
    /**
     * 每个用例之前拿excel文件
     * @param method
     */
    @BeforeMethod(alwaysRun = true)
    private void getFileRow(Method method) {
        HashMap<String,String> fileRow = new HashMap<String,String>();
        fileRow = FilesUtils.readExcelGetRow(fileName, method.getName());
        setFileEntity(platformUrl, fileRow);
        log.info("**************开始执行测试用例：" + fileEntity.getTestCaseName() + "**************");
    }

    /**
     * 登录拿到cookie
     */
    @BeforeClass(alwaysRun = true)
    private void loginGetCookie() {
        login(loginUrl, userCode, userPassword, shortsIv);
    }
    /**
     * 打印结束执行
     */
    @AfterMethod(alwaysRun = true)
    private void printLog() {
        log.info("**************结束执行测试用例：" + fileEntity.getTestCaseName() + "**************");
    }



    /*******************************轨迹追踪*******************/

    /**
     * 获取组织架构
     */
    @Test(description = "获取组织架构", groups = {TRACKING})
    private void test_001_findOrganizationTreeByOrgId() {
        startPerformTest();
    }

    /**
     * 获取统计数据
     */
    @Test(description = "获取统计数据", groups = {TRACKING})
    private void test_002_statisticalData() {
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 获取所有仓库
     */
    @Test(description = "获取所有仓库", groups = {TRACKING})
    private void test_003_getWarehouseListByLogin() {
        startPerformTest();
        List list = response.jsonPath().get();
        parameterMap.put("areaId", ((Map)list.get(0)).get("areaId"));

    }

    /**
     * 组合查询运单列表
     */
    @Test(description = "组合查询运单列表", groups = {TRACKING})
    private void test_004_findWaybillListByPage() {
        fileEntity.getPathParam().put("waybillCodes", "运单编号");
        fileEntity.getPathParam().put("areaIds", parameterMap.get("areaId"));
        //待运输
        fileEntity.getPathParam().put("waybillStatus", "1");
        fileEntity.getPathParam().put("materialCodes", "物料编码");
        fileEntity.getPathParam().put("materialDescribes", "物料描述");
        fileEntity.getPathParam().put("receiverName", "收货人");
        startPerformTest();
    }

    /**
     * 默认查询运单列表
     */
    @Test(description = "默认查询运单列表", groups = {TRACKING})
    private void test_005_findWaybillListByPage() {
        startPerformTest();
    }

    /**
     * 点击北京市获取统计数据
     */
    @Test(description = "点击北京市获取统计数据", groups = {TRACKING})
    private void test_006_statisticalData() {
        fileEntity.getBodyParam().put("waybillLevel", "2");
        fileEntity.getBodyParam().put("placeName", "北京市");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 查询北京市运单列表
     */
    @Test(description = "查询北京市运单列表", groups = {TRACKING})
    private void test_007_findWaybillListByPage() {
        fileEntity.getPathParam().put("waybillLevel", "2");
        fileEntity.getPathParam().put("placeName", "北京市");
        startPerformTest();
    }


    /*************************************运单监控*************************/
    /**
     * 获取运单监控列表格
     */
    @Test(description = "获取运单监控列表格", groups = {WAYBILLMONITORING})
    private void test_008_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "44");
        startPerformTest();
    }

    /**
     * 组合查询运单监控
     */
    @Test(description = "组合查询运单监控", groups = {WAYBILLMONITORING})
    private void test_009_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("waybillSource", "1"); //运单来源：待运输
        fileEntity.getPathParam().put("waybillStatus", "1"); //待运输
        fileEntity.getPathParam().put("waybillCode", "运单编号");
        fileEntity.getPathParam().put("customerName", "客户名称");
        fileEntity.getPathParam().put("isTrackOrder", "1");//追单是
        fileEntity.getPathParam().put("moduleId", "44");
        startPerformTest();
    }

    /**
     * 默认查询运单监控
     */
    @Test(description = "默认查询运单监控", groups = {WAYBILLMONITORING})
    private void test_010_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", "44");
        startPerformTest();
    }

    /*********************仓库温湿度实时监控***********************/

    /**
     * 获取仓库实时监控列表格
     */
    @Test(description = "获取仓库实时监控列表格", groups = {AREAMONITORING})
    private void test_011_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "45");
        startPerformTest();
    }

    /**
     * 查询组织架构
     */
    @Test(description = "查询组织架构", groups = {AREAMONITORING})
    private void test_012_findOrganizationTreeByOrgId() {
        startPerformTest();
    }

    /**
     * 组合查询仓库实时监控
     */
    @Test(description = "组合查询仓库实时监控", groups = {AREAMONITORING})
    private void test_013_findByMonitorList() {
        Long[] dateTime = DateUtil.getTimeList(-10, 0);
        fileEntity.getPathParam().put("gpsTimeList[0]", dateTime[0]);
        fileEntity.getPathParam().put("gpsTimeList[1]", dateTime[1]);
        fileEntity.getPathParam().put("orgIds", orgId);
        fileEntity.getPathParam().put("areaName", "仓库名称");
        fileEntity.getPathParam().put("moduleId", "45");
        startPerformTest();
    }

    /**
     * 默认查询仓库实时监控
     */
    @Test(description = "默认查询仓库实时监控", groups = {AREAMONITORING})
    private void test_014_findByMonitorList() {
        Long[] dateTime = DateUtil.getTimeList(-10, 0);
        fileEntity.getPathParam().put("gpsTimeList[0]", dateTime[0]);
        fileEntity.getPathParam().put("gpsTimeList[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", "45");
        startPerformTest();
    }

    /*******************************仓库温湿度历史*************************/

    /**
     * 获取仓库温湿度历史列表格
     */
    @Test(description = "获取仓库温湿度历史列表格", groups = {AREAHISTORY})
    private void test_015_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "47");
        startPerformTest();
    }

    /**
     * 组合查询仓库温湿度历史
     */
    @Test(description = "组合查询仓库温湿度历史", groups = {AREAHISTORY})
    private void test_016_findByMonitorHistoryList() {
        Long[] dateTime = DateUtil.getTimeList(-10, 0);
        fileEntity.getPathParam().put("gpsTimeList[0]", dateTime[0]);
        fileEntity.getPathParam().put("gpsTimeList[1]", dateTime[1]);
        fileEntity.getPathParam().put("areaName", "仓库名称");
        fileEntity.getPathParam().put("lableCode", "标签编号");
        fileEntity.getPathParam().put("moduleId", "47");
        startPerformTest();
    }

    /**
     * 默认查询仓库温湿度历史
     */
    @Test(description = "默认查询仓库温湿度历史", groups = {AREAHISTORY})
    private void test_017_findByMonitorHistoryList() {
        Long[] dateTime = DateUtil.getTimeList(-10, 0);
        fileEntity.getPathParam().put("gpsTimeList[0]", dateTime[0]);
        fileEntity.getPathParam().put("gpsTimeList[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", "47");
        startPerformTest();
    }
}

