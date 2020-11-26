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

/**
 * @description: 设备异常中心,该模块只增加了基本查询，未增加处理和忽略功能，因为不一定存在数据，且数据较少无法调试
 * @author: guozhixiong
 * @time: 2020/8/27 15:04
 */
public class DeviceExceptionTestCase extends BaseTest {
    //excel测试用例
    private static String fileName = "中移设备异常中心.xls";
    //设备报警信息
    private static final String DEVICEALARM = "deviceAlarm";
    //电子锁异常管理
    private static final String LOCKEXCEPTION = "lockException";
    //位置偏移异常管理
    private static final String LOCATIONEXCEPTION = "locationException";
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



    /*******************************设备报警信息*******************/
    /**
     * 查询接口管理表格列
     */
    @Test(description = "查询接口管理表格列", groups = {DEVICEALARM})
    private void test_001_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "43");
        startPerformTest();
    }

    /**
     * 获取设备类型数据字典
     */
    @Test(description = "获取设备类型数据字典", groups = {DEVICEALARM})
    private void test_002_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "terminal_type");
        startPerformTest();
        parameterMap.put("dataTerminalType", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 获取报警名称数据字典
     */
    @Test(description = "获取报警名称数据字典", groups = {DEVICEALARM})
    private void test_003_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "terminal_alarm_type");
        startPerformTest();
        parameterMap.put("alarmTypeId", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 获取处理状态数据字典
     */
    @Test(description = "获取处理状态数据字典", groups = {DEVICEALARM})
    private void test_004_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "handle_status");
        startPerformTest();
        parameterMap.put("handleStatus", response.jsonPath().get("result[0].dicId"));
    }


    /**
     * 组合查询设备报警信息
     */
    @Test(description = "组合查询设备报警信息", groups = {DEVICEALARM})
    private void test_005_findByPage() {
        Long[] alarmDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("terminalName", "设备名称");
        fileEntity.getPathParam().put("terminalNo", "设备编号");
        fileEntity.getPathParam().put("alarmTypeId", parameterMap.get("alarmTypeId"));
        fileEntity.getPathParam().put("dataTerminalType", parameterMap.get("dataTerminalType"));
        fileEntity.getPathParam().put("handleStatus", parameterMap.get("handleStatus"));
        fileEntity.getPathParam().put("moduleId", "43");
        fileEntity.getPathParam().put("alarmDateRange[0]", alarmDateRange[0]);
        fileEntity.getPathParam().put("alarmDateRange[1]", alarmDateRange[1]);
        startPerformTest();
    }

    /**
     * 默认查询设备报警信息
     */
    @Test(description = "默认查询设备报警信息", groups = {DEVICEALARM})
    private void test_006_findByPage() {
        Long[] alarmDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("moduleId", "43");
        fileEntity.getPathParam().put("alarmDateRange[0]", alarmDateRange[0]);
        fileEntity.getPathParam().put("alarmDateRange[1]", alarmDateRange[1]);
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {DEVICEALARM})
    private void test_007_exportTerminalAlarmInfo() {
        Long[] alarmDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("alarmDateRange[0]", alarmDateRange[0]);
        fileEntity.getPathParam().put("alarmDateRange[1]", alarmDateRange[1]);
        startPerformTest();
    }


    /************************电子锁异常管理***********************/

    /**
     * 查询电子锁异常管理表格列
     */
    @Test(description = "查询电子锁异常管理表格列", groups = {LOCKEXCEPTION})
    private void test_008_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "25");
        startPerformTest();
    }

    /**
     * 默认查询电子锁异常管理
     */
    @Test(description = "默认查询电子锁异常管理", groups = {LOCKEXCEPTION})
    private void test_009_findByPage() {
        Long[] alarmTimeList = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getBodyParam().put("orgIds", orgId);
        fileEntity.getBodyParam().put("alarmTimeList", alarmTimeList);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 组合查询电子锁异常管理
     */
    @Test(description = "组合查询电子锁异常管理", groups = {LOCKEXCEPTION})
    private void test_010_findByPage() {
        Long[] alarmTimeList = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getBodyParam().put("orgIds", orgId);
        fileEntity.getBodyParam().put("alarmTimeList", alarmTimeList);
        fileEntity.getBodyParam().put("terminalWid", "设备wid");
        fileEntity.getBodyParam().put("terminalName", "设备名称");
        //处理状态：未处理
        fileEntity.getBodyParam().put("handleStatus", "1");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

/********************************位置偏移异常管理**********************************/
    /**
     * 查询位置偏移异常管理表格列
     */
    @Test(description = "查询位置偏移异常管理表格列", groups = {LOCATIONEXCEPTION})
    private void test_011_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "26");
        startPerformTest();
    }
    /**
     * 默认查询电子锁异常管理
     */
    @Test(description = "默认查询电子锁异常管理", groups = {LOCATIONEXCEPTION})
    private void test_012_findByPage() {
        Long[] alarmDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("moduleId", "26");
        fileEntity.getPathParam().put("alarmDateRange[0]", alarmDateRange[0]);
        fileEntity.getPathParam().put("alarmDateRange[1]", alarmDateRange[1]);
        startPerformTest();
    }
    /**
     * 组合查询电子锁异常管理
     */
    @Test(description = "组合查询电子锁异常管理", groups = {LOCATIONEXCEPTION})
    private void test_013_findByPage() {
        Long[] alarmDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("moduleId", "26");
        fileEntity.getPathParam().put("alarmDateRange[0]", alarmDateRange[0]);
        fileEntity.getPathParam().put("alarmDateRange[1]", alarmDateRange[1]);
        fileEntity.getPathParam().put("handleStatus", "1"); //处理状态：未处理
        startPerformTest();
    }

}
