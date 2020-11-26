package testcase;

import com.alibaba.fastjson.JSON;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.DateUtil;
import utils.FilesUtils;
import utils.StringUtil;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @description: 访客管理
 * @author: guozhixiong
 * @time: 2020/8/24 11:00
 */
public class VisitorTestCase extends BaseTest {
    //excel测试用例
    private static String fileName = "中移访客管理.xls";
    //访客预约管理和访客预约审核
    private static final String VISITORAPPOINTMENT = "visitorAppointment";
    //临时访客管理和临时访客审核
    private static final String TEMPORARYVISITOR = "temporaryVisitor";
    //访客记录管理
    private static final String VISITORRECORD = "visitorRecord";
    //员工出勤管理
    private static final String ATTENDANCE = "attendance";
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



    /*******************************访客预约管理*******************/

    /**
     * 获取访客预约管理表格列
     */
    @Test(description = "获取访客预约管理表格列", groups = {VISITORAPPOINTMENT})
    private void test_001_getTableColumnSettings() {
        startPerformTest();
    }

    /**
     * 获取仓库
     */
    @Test(description = "获取仓库", groups = {VISITORAPPOINTMENT})
    private void test_002_getWarehouseList() {
        startPerformTest();
        List list = response.jsonPath().get();
        parameterMap.put("areaId", ((Map)list.get(0)).get("areaId"));
    }

    /**
     * 获取预约事由数据字典
     */
    @Test(description = "获取预约事由数据字典", groups = {VISITORAPPOINTMENT})
    private void test_003_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "appointment_reason");
        startPerformTest();
    }

    /**
     * 组合查询访客预约管理
     */
    @Test(description = "组合查询访客预约管理", groups = {VISITORAPPOINTMENT})
    private void test_004_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-5, 0);
        fileEntity.getPathParam().put("signDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("signDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("visitorName", "访客姓名");
        fileEntity.getPathParam().put("visitorIdCard", "身份证号");
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("visitorCompany", "单位");
        fileEntity.getPathParam().put("moduleId", "20");
        //预约事由：开会
        fileEntity.getPathParam().put("appointmentReason", "1");
        startPerformTest();
    }

    /**
     * 获取仓库
     */
    @Test(description = "获取仓库", groups = {VISITORAPPOINTMENT})
    private void test_005_getWarehouseList() {
        startPerformTest();
        List list = response.jsonPath().get();
        parameterMap.put("areaId", ((Map)list.get(0)).get("areaId"));
    }

    /**
     * 新增访客预约管理
     */
    @Test(description = "新增访客预约管理", groups = {VISITORAPPOINTMENT})
    private void test_006_addVisitorAppointment() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        Calendar call = DateUtil.getCurrentTime();
        Long startTime = call.getTimeInMillis();
        Long endTime = new Date(call.getTimeInMillis() + 1799 * 1000).getTime();
        Long[] times = new Long[]{startTime, endTime};
        fileEntity.getBodyParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getBodyParam().put("visitorCompany", "访客单位" + currentTime);
        fileEntity.getBodyParam().put("visitorName", "访客姓名" + currentTime);
        fileEntity.getBodyParam().put("visitorIdCard", "6" + currentTime);
        //事由：开会
        fileEntity.getBodyParam().put("appointmentReason", "1");
        fileEntity.getBodyParam().put("receptionist", "接待人");
        fileEntity.getBodyParam().put("times", times);
        fileEntity.getBodyParam().put("remark", "备注");
        fileEntity.getBodyParam().put("moduleId", "20");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 默认查询访客预约管理
     */
    @Test(description = "默认查询访客预约管理", groups = {VISITORAPPOINTMENT})
    private void test_007_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-5, 0);
        fileEntity.getPathParam().put("signDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("signDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", "20");
        startPerformTest();
    }

    /**
     * 导出访客预约管理
     */
    @Test(description = "导出访客预约管理", groups = {VISITORAPPOINTMENT})
    private void test_008_exportAppointment() { ;
        startPerformTest();
    }





    /**********************************访客预约审核**************************/

    /**
     * 获取访客预约管理表格列
     */
    @Test(description = "获取访客预约管理表格列", groups = {VISITORAPPOINTMENT})
    private void test_009_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "29");
        startPerformTest();
    }

    /**
     * 获取仓库
     */
    @Test(description = "获取仓库", groups = {VISITORAPPOINTMENT})
    private void test_010_getWarehouseList() {
        startPerformTest();
        List list = response.jsonPath().get();
        parameterMap.put("areaId", ((Map)list.get(0)).get("areaId"));
    }

    /**
     * 获取预约状态
     */
    @Test(description = "获取预约状态", groups = {VISITORAPPOINTMENT})
    private void test_011_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "visitor_appointment_status");
        startPerformTest();
    }

    /**
     * 组合查询访客预约审核
     */
    @Test(description = "组合查询访客预约审核", groups = {VISITORAPPOINTMENT})
    private void test_012_findByPage() {
        Long[] dataTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("visitorName", "访客姓名");
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("visitorCompany", "单位");
        //预约状态：待审核
        fileEntity.getPathParam().put("appointmentStatus", "1");
        fileEntity.getPathParam().put("moduleId", "29");
        fileEntity.getPathParam().put("signDateRange[0]", dataTime[0]);
        fileEntity.getPathParam().put("signDateRange[1]", dataTime[1]);
        startPerformTest();
    }

    /**
     * 默认查询访客预约审核
     */
    @Test(description = "默认查询访客预约审核", groups = {VISITORAPPOINTMENT})
    private void test_013_findByPage() {
        Long[] dataTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("moduleId", "29");
        fileEntity.getPathParam().put("signDateRange[0]", dataTime[0]);
        fileEntity.getPathParam().put("signDateRange[1]", dataTime[1]);
        //预约状态：待审核
        fileEntity.getPathParam().put("appointmentStatus", "1");
        startPerformTest();
        saveResponseParameters("result.data[0].visitorRegisterId");
    }

    /**
     * 点击审核获取数据
     */
    @Test(description = "点击审核获取数据", groups = {VISITORAPPOINTMENT})
    private void test_014_getVisitorAppointmentById() {
        fileEntity.getPathParam().put("visitorRegisterId", parameterMap.get("visitorRegisterId"));
        startPerformTest();
        saveResponseParameters("result.visitorName",
                "result.auditDeclare");
    }

    /**
     * 审核驳回
     */
    @Test(description = "审核驳回", groups = {VISITORAPPOINTMENT})
    private void test_015_verifyAppointment() {
        fileEntity.getPathParam().put("visitorName", parameterMap.get("visitorName"));
        //驳回
        fileEntity.getPathParam().put("appointmentStatus", 3);
        fileEntity.getPathParam().put("auditDeclare", parameterMap.get("auditDeclare"));
        fileEntity.getPathParam().put("ids", parameterMap.get("visitorRegisterId"));
        startPerformTest();
    }

    /**
     * 默认查询访客预约管理
     */
    @Test(description = "默认查询访客预约管理", groups = {VISITORAPPOINTMENT})
    private void test_016_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-5, 0);
        fileEntity.getPathParam().put("signDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("signDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", "20");
        startPerformTest();
        List<Map<String, Object>> list = response.jsonPath().get("result.data");
        for(Map map : list) {
            //已驳回状态
            if(StringUtil.equals(map.get("appointmentStatus").toString(), "3")) {
                parameterMap.put("visitorRegisterId", map.get("visitorRegisterId"));
            }
        }
    }

    /**
     * 访客预约管理点击编辑获取数据
     */
    @Test(description = "访客预约管理点击编辑获取数据", groups = {VISITORAPPOINTMENT})
    private void test_017_getVisitorAppointmentById() {
        fileEntity.getPathParam().put("visitorRegisterId", parameterMap.get("visitorRegisterId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 访客预约管理编辑点击保存
     */
    @Test(description = "访客预约管理编辑点击保存", groups = {VISITORAPPOINTMENT})
    private void test_018_addVisitorAppointment() {
        Map map = (Map)parameterMap.get("result");
        map.put("roleId", "");
        map.put("waybillId", "");
        map.put("crenelId", "");
        map.put("moduleId", "20");
        String[] times = new String[]{map.get("appointmentStartTime").toString(), map.get("appointmentEndTime").toString()};
        map.put("times", times);
        fileEntity.setBodyParam(map);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 默认查询访客预约审核
     */
    @Test(description = "默认查询访客预约审核", groups = {VISITORAPPOINTMENT})
    private void test_019_findByPage() {
        Long[] dataTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("moduleId", "29");
        fileEntity.getPathParam().put("signDateRange[0]", dataTime[0]);
        fileEntity.getPathParam().put("signDateRange[1]", dataTime[1]);
        //预约状态：待审核
        fileEntity.getPathParam().put("appointmentStatus", "1");
        startPerformTest();
        saveResponseParameters("result.data[0].visitorRegisterId");
    }

    /**
     * 审核通过
     */
    @Test(description = "审核通过", groups = {VISITORAPPOINTMENT})
    private void test_020_verifyAppointment() {
        fileEntity.getPathParam().put("visitorName", parameterMap.get("visitorName"));
        //通过
        fileEntity.getPathParam().put("appointmentStatus", 2);
        fileEntity.getPathParam().put("auditDeclare", parameterMap.get("auditDeclare"));
        fileEntity.getPathParam().put("ids", parameterMap.get("visitorRegisterId"));
        startPerformTest();
    }


    /*******************临时访客管理**************************/
    /**
     * 获取访客预约管理表格列
     */
    @Test(description = "获取访客预约管理表格列", groups = {TEMPORARYVISITOR})
    private void test_021_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "21");
        startPerformTest();
    }

    /**
     * 获取仓库
     */
    @Test(description = "获取仓库", groups = {TEMPORARYVISITOR})
    private void test_022_getWarehouseList() {
        startPerformTest();
        List list = response.jsonPath().get();
        parameterMap.put("areaId", ((Map)list.get(0)).get("areaId"));
    }

    /**
     * 组合查询临时访客管理
     */
    @Test(description = "组合查询临时访客管理", groups = {TEMPORARYVISITOR})
    private void test_023_findByPage() {
        fileEntity.getPathParam().put("visitorName", "访客姓名");
        fileEntity.getPathParam().put("visitorIdCard", "身份证号");
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("visitorCompany", "单位");
        fileEntity.getPathParam().put("appointmentReason", "1");
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
    }

    /**
     * 新增临时访客预约
     */
    @Test(description = "新增临时访客预约", groups = {TEMPORARYVISITOR})
    private void test_024_addTempVisitorAppointment() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        Calendar call = DateUtil.getCurrentTime();
        Long startTime = call.getTimeInMillis();
        Long endTime = new Date(call.getTimeInMillis() + 1799 * 1000).getTime();
        Long[] times = new Long[]{startTime, endTime};
        fileEntity.getBodyParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getBodyParam().put("visitorCompany", "访客单位" + currentTime);
        fileEntity.getBodyParam().put("visitorName", "访客姓名" + currentTime);
        fileEntity.getBodyParam().put("visitorIdCard", "6" + currentTime);
        //事由：开会
        fileEntity.getBodyParam().put("appointmentReason", "1");
        fileEntity.getBodyParam().put("receptionist", "接待人");
        fileEntity.getBodyParam().put("times", times);
        fileEntity.getBodyParam().put("remark", "备注");
        fileEntity.getBodyParam().put("moduleId", "21");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {TEMPORARYVISITOR})
    private void test_025_exportTempAppointment() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
    }

    /**
     * 默认查询临时访客管理
     */
    @Test(description = "默认查询临时访客管理", groups = {TEMPORARYVISITOR})
    private void test_026_findByPage() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
    }


    /*****************************临时访客审核**************************/

    /**
     * 获取访客预约管理表格列
     */
    @Test(description = "获取访客预约管理表格列", groups = {TEMPORARYVISITOR})
    private void test_027_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "30");
        startPerformTest();
    }

    /**
     * 获取仓库
     */
    @Test(description = "获取仓库", groups = {TEMPORARYVISITOR})
    private void test_028_getWarehouseList() {
        startPerformTest();
        List list = response.jsonPath().get();
        parameterMap.put("areaId", ((Map)list.get(0)).get("areaId"));
    }

    /**
     * 组合查询临时访客审核
     */
    @Test(description = "组合查询临时访客审核", groups = {TEMPORARYVISITOR})
    private void test_029_findByPage() {
        fileEntity.getPathParam().put("visitorName", "访客姓名");
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("visitorCompany", "单位");
        fileEntity.getPathParam().put("appointmentReason", "1");
        fileEntity.getPathParam().put("moduleId", "30");
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
    }

    /**
     * 默认查询临时访客审核
     */
    @Test(description = "默认查询临时访客审核", groups = {TEMPORARYVISITOR})
    private void test_030_findByPage() {
        //待审核
        fileEntity.getPathParam().put("appointmentReason", "1");
        fileEntity.getPathParam().put("moduleId", "30");
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
        saveResponseParameters("result.data[0].visitorRegisterId");
    }

    /**
     * 点击审核获取数据
     */
    @Test(description = "点击审核获取数据", groups = {TEMPORARYVISITOR})
    private void test_031_getVisitorAppointmentById() {
        fileEntity.getPathParam().put("visitorRegisterId", parameterMap.get("visitorRegisterId"));
        startPerformTest();
        saveResponseParameters("result.auditDeclare");
    }

    /**
     * 点击驳回提交
     */
    @Test(description = "点击驳回提交", groups = {TEMPORARYVISITOR})
    private void test_032_verifyTempAppointment() {
        fileEntity.getPathParam().put("appointmentStatus", "3");
        fileEntity.getPathParam().put("auditDeclare", parameterMap.get("auditDeclare"));
        fileEntity.getPathParam().put("ids", parameterMap.get("visitorRegisterId"));
        startPerformTest();
    }

    /**
     * 默认查询临时访客管理
     */
    @Test(description = "默认查询临时访客管理", groups = {TEMPORARYVISITOR})
    private void test_033_findByPage() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
        List<Map<String, Object>> list = response.jsonPath().get("result.data");
        for(Map map : list) {
            //已驳回状态
            if(StringUtil.equals(map.get("appointmentStatus").toString(), "3")) {
                parameterMap.put("visitorRegisterId", map.get("visitorRegisterId"));
            }
        }
    }


    /**
     * 点击临时访客管理编辑
     */
    @Test(description = "点击临时访客管理编辑", groups = {VISITORAPPOINTMENT})
    private void test_034_getVisitorAppointmentById() {
        fileEntity.getPathParam().put("visitorRegisterId", parameterMap.get("visitorRegisterId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 临时访客管理编辑点击保存
     */
    @Test(description = "临时访客管理编辑点击保存", groups = {VISITORAPPOINTMENT})
    private void test_035_addTempVisitorAppointment() {
        Map map = (Map)parameterMap.get("result");
        map.put("roleId", "");
        map.put("waybillId", "");
        map.put("crenelId", "");
        map.put("moduleId", "20");
        String[] times = new String[]{map.get("appointmentStartTime").toString(), map.get("appointmentEndTime").toString()};
        map.put("times", times);
        fileEntity.setBodyParam(map);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 默认查询临时访客审核
     */
    @Test(description = "默认查询临时访客审核", groups = {TEMPORARYVISITOR})
    private void test_036_findByPage() {
        //待审核
        fileEntity.getPathParam().put("appointmentReason", "1");
        fileEntity.getPathParam().put("moduleId", "30");
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
        saveResponseParameters("result.data[0].visitorRegisterId");
    }

    /**
     * 临时访客审核通过
     */
    @Test(description = "临时访客审核通过", groups = {TEMPORARYVISITOR})
    private void test_037_verifyTempAppointment() {
        fileEntity.getPathParam().put("ids", parameterMap.get("visitorRegisterId"));
        //审核通过
        fileEntity.getPathParam().put("appointmentStatus", "2");
        startPerformTest();
    }

    /***新增用于删除***/

    /**
     * 获取仓库
     */
    @Test(description = "获取仓库", groups = {TEMPORARYVISITOR})
    private void test_038_getWarehouseList() {
        startPerformTest();
        List list = response.jsonPath().get();
        parameterMap.put("areaId", ((Map)list.get(0)).get("areaId"));
    }

    /**
     * 新增临时访客预约
     */
    @Test(description = "新增临时访客预约", groups = {TEMPORARYVISITOR})
    private void test_039_addTempVisitorAppointment() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        Calendar call = DateUtil.getCurrentTime();
        Long startTime = call.getTimeInMillis();
        Long endTime = new Date(call.getTimeInMillis() + 1799 * 1000).getTime();
        Long[] times = new Long[]{startTime, endTime};
        fileEntity.getBodyParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getBodyParam().put("visitorCompany", "访客单位" + currentTime);
        fileEntity.getBodyParam().put("visitorName", "访客姓名" + currentTime);
        fileEntity.getBodyParam().put("visitorIdCard", "6" + currentTime);
        //事由：开会
        fileEntity.getBodyParam().put("appointmentReason", "1");
        fileEntity.getBodyParam().put("receptionist", "接待人");
        fileEntity.getBodyParam().put("times", times);
        fileEntity.getBodyParam().put("remark", "备注");
        fileEntity.getBodyParam().put("moduleId", "21");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 默认查询临时访客审核
     */
    @Test(description = "默认查询临时访客审核", groups = {TEMPORARYVISITOR})
    private void test_040_findByPage() {
        //待审核
        fileEntity.getPathParam().put("appointmentReason", "1");
        fileEntity.getPathParam().put("moduleId", "30");
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
        saveResponseParameters("result.data[0].visitorRegisterId");
    }

    /**
     * 点击审核获取数据
     */
    @Test(description = "点击审核获取数据", groups = {TEMPORARYVISITOR})
    private void test_041_getVisitorAppointmentById() {
        fileEntity.getPathParam().put("visitorRegisterId", parameterMap.get("visitorRegisterId"));
        startPerformTest();
        saveResponseParameters("result.auditDeclare");
    }

    /**
     * 点击驳回提交
     */
    @Test(description = "点击驳回提交", groups = {TEMPORARYVISITOR})
    private void test_042_verifyTempAppointment() {
        fileEntity.getPathParam().put("appointmentStatus", "3");
        fileEntity.getPathParam().put("auditDeclare", parameterMap.get("auditDeclare"));
        fileEntity.getPathParam().put("ids", parameterMap.get("visitorRegisterId"));
        startPerformTest();
    }

    /**
     * 默认查询临时访客管理
     */
    @Test(description = "默认查询临时访客管理", groups = {TEMPORARYVISITOR})
    private void test_043_findByPage() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
        List<Map<String, Object>> list = response.jsonPath().get("result.data");
        for(Map map : list) {
            //已驳回状态
            if(StringUtil.equals(map.get("appointmentStatus").toString(), "3")) {
                parameterMap.put("visitorRegisterId", map.get("visitorRegisterId"));
            }
        }
    }

    /**
     * 删除/批量删除
     */
    @Test(description = "删除/批量删除", groups = {TEMPORARYVISITOR})
    private void test_044_deleteAppointment() {
        fileEntity.getPathParam().put("ids", parameterMap.get("visitorRegisterId"));
        startPerformTest();
    }

    /***删除结束***/


    /***********************访客记录管理********************/

    /**
     * 获取访客记录管理表格列
     */
    @Test(description = "获取访客记录管理表格列", groups = {VISITORRECORD})
    private void test_045_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "22");
        startPerformTest();
    }

    /**
     * 默认查询访客记录管理
     */
    @Test(description = "默认查询访客记录管理", groups = {VISITORRECORD})
    private void test_046_findByPage() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("moduleId", "22");
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
    }

    /**
     * 获取仓库
     */
    @Test(description = "获取仓库", groups = {VISITORRECORD})
    private void test_047_getWarehouseList() {
        startPerformTest();
        List list = response.jsonPath().get();
        parameterMap.put("areaId", ((Map)list.get(0)).get("areaId"));
    }

    /**
     * 获取访客记录管理状态
     */
    @Test(description = "获取访客记录管理状态", groups = {VISITORRECORD})
    private void test_048_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "record_status");
        startPerformTest();
    }


    /**
     * 组合查询访客记录管理
     */
    @Test(description = "组合查询访客记录管理", groups = {VISITORRECORD})
    private void test_049_findByPage() {
        //状态：成功
        fileEntity.getPathParam().put("recordStatus", "1");
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("moduleId", "22");
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {VISITORRECORD})
    private void test_050_exportVisitorRecord() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("moduleId", "22");
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
    }

    /************************员工出勤管理**********************/

    /**
     * 获取员工出勤管理表格列
     */
    @Test(description = "获取员工出勤管理表格列", groups = {ATTENDANCE})
    private void test_051_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "23");
        startPerformTest();
    }

    /**
     * 默认查询员工出勤管理
     */
    @Test(description = "默认查询员工出勤管理", groups = {ATTENDANCE})
    private void test_052_findByPage() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        fileEntity.getPathParam().put("moduleId", "23");
        startPerformTest();
    }

    /**
     * 组合查询员工出勤管理
     */
    @Test(description = "组合查询员工出勤管理", groups = {ATTENDANCE})
    private void test_053_findByPage() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        fileEntity.getPathParam().put("moduleId", "23");
        fileEntity.getPathParam().put("orgIds", orgId);
        startPerformTest();
    }

    /**
     * 获取管理机构
     */
    @Test(description = "获取管理机构", groups = {ATTENDANCE})
    private void test_054_findOrganizationTreeByOrgId() {
        createEncrypt("1", "bodyParam");
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {ATTENDANCE})
    private void test_055_exportUserAttendanceRecord() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        fileEntity.getPathParam().put("moduleId", "23");
        startPerformTest();
    }


}
