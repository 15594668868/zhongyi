package testcase;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.DateUtil;
import utils.FilesUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 接口中心
 * @author: guozhixiong
 * @time: 2020/8/27 11:04
 */
public class InterfaceTestCase extends BaseTest {
    //excel测试用例
    private static String fileName = "中移接口中心.xls";
    //接口管理
    private static final String INTERFACEMANAGE = "interfaceManage";
    //账号管理
    private static final String ACCOUNTMANAGE = "accountManage";
    //接口调用日志
    private static final String INTERFACELOG = "interfaceLog";
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



    /*******************************接口管理*******************/

    /**
     * 查询接口管理表格列
     */
    @Test(description = "查询接口管理表格列", groups = {INTERFACEMANAGE})
    private void test_001_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "10");
        startPerformTest();
    }

    /**
     * 组合查询接口管理
     */
    @Test(description = "组合查询接口管理", groups = {INTERFACEMANAGE})
    private void test_002_findByPage() {
        fileEntity.getPathParam().put("interfaceDesc", "接口描述");
        fileEntity.getPathParam().put("interfaceName", "接口名称");
        fileEntity.getPathParam().put("moduleId", "10");
        startPerformTest();
    }

    /**
     * 验证接口名称是否存在
     */
    @Test(description = "验证接口名称是否存在", groups = {INTERFACEMANAGE})
    private void test_003_findExistInterfaceName() {
        fileEntity.getPathParam().put("interfaceName", "接口名称");
        startPerformTest();
    }

    /**
     * 新增接口
     */
    @Test(description = "新增接口", groups = {INTERFACEMANAGE})
    private void test_004_saveInterfaceInfo() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("interfaceDesc", "接口描述" + currentTime);
        fileEntity.getPathParam().put("callSpace", "10"); //每毫秒最大访问次数
        fileEntity.getPathParam().put("interfaceUrl", "接口地址" + currentTime);
        fileEntity.getPathParam().put("dayCallNum", "20"); //每日最大访问次数
        fileEntity.getPathParam().put("interfaceName", "自动化接口名称" + currentTime);
        parameterMap.put("interfaceName", "自动化接口名称" + currentTime);
        startPerformTest();
    }

    /**
     * 默认查询接口管理
     */
    @Test(description = "默认查询接口管理", groups = {INTERFACEMANAGE})
    private void test_005_findByPage() {
        fileEntity.getPathParam().put("interfaceName", parameterMap.get("interfaceName"));
        fileEntity.getPathParam().put("moduleId", "10");
        startPerformTest();
        saveResponseParameters("result.data[0].interfaceId");
    }

    /**
     * 点击编辑根据id获取接口内容
     */
    @Test(description = "点击编辑根据id获取接口内容", groups = {INTERFACEMANAGE})
    private void test_006_findByInterfaceId() {
        fileEntity.getPathParam().put("interfaceId", parameterMap.get("interfaceId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑保存接口
     */
    @Test(description = "编辑保存接口", groups = {INTERFACEMANAGE})
    private void test_007_saveInterfaceInfo() {
        Map interfaceData = (Map) parameterMap.get("result");
        interfaceData.put("id", parameterMap.get("interfaceId"));
        startPerformTest();
    }


    /******************************账号管理*****************/
    /**
     * 查询账号管理表格列
     */
    @Test(description = "查询账号管理表格列", groups = {ACCOUNTMANAGE})
    private void test_008_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "12");
        startPerformTest();
    }

    /**
     * 默认查询账号管理
     */
    @Test(description = "默认查询账号管理", groups = {ACCOUNTMANAGE})
    private void test_009_findInterfaceInfoUserByPage() {
        fileEntity.getPathParam().put("moduleId", "12");
        startPerformTest();
    }

    /**
     * 获取所有接口
     */
    @Test(description = "获取所有接口", groups = {ACCOUNTMANAGE})
    private void test_010_findInterfaceInfo() {
        startPerformTest();
    }

    /**
     * 新增账号管理
     */
    @Test(description = "新增账号管理", groups = {ACCOUNTMANAGE})
    private void test_011_saveApiInterfaceUser() {
        RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        StringBuffer str = new StringBuffer();
        str.append("zdh" + RandomStringUtils.randomAlphanumeric(5) + "-" +
                RandomStringUtils.randomAlphanumeric(4) + "-" +
                RandomStringUtils.randomAlphanumeric(4) + "-" +
                RandomStringUtils.randomAlphanumeric(4) + "-");
        String appKey = str.append(RandomStringUtils.randomAlphanumeric(12)).toString().toLowerCase();
        parameterMap.put("appKey", appKey);
        String appSecret = str.append(RandomStringUtils.randomAlphanumeric(12)).toString().toLowerCase();
        fileEntity.getPathParam().put("ip", "01.02.03.04");
        fileEntity.getPathParam().put("appKey", appKey);
        fileEntity.getPathParam().put("appSecret", appSecret);
        fileEntity.getPathParam().put("moduleId", "12");
        startPerformTest();
    }

    /**
     * 组合查询账号管理
     */
    @Test(description = "组合查询账号管理", groups = {ACCOUNTMANAGE})
    private void test_012_findInterfaceInfoUserByPage() {
        fileEntity.getPathParam().put("moduleId", "12");
        fileEntity.getPathParam().put("appKey", parameterMap.get("appKey"));
        startPerformTest();
        saveResponseParameters("result.data[0].appId");
    }

    /**
     * 点击编辑根据id获取详情
     */
    @Test(description = "点击编辑根据id获取详情", groups = {ACCOUNTMANAGE})
    private void test_013_findUserByAppId() {
        fileEntity.getPathParam().put("appId", parameterMap.get("appId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 修改保存账号管理
     */
    @Test(description = "修改保存账号管理", groups = {ACCOUNTMANAGE})
    private void test_014_saveApiInterfaceUser() {
        Map map = (Map)parameterMap.get("result");
        map.put("id", parameterMap.get("appId"));
        map.put("moduleId", "12");
        fileEntity.setPathParam(map);
        startPerformTest();
    }

    /**
     * 查看账号管理详情
     */
    @Test(description = "查看账号管理详情", groups = {ACCOUNTMANAGE})
    private void test_015_findApiUserInfoDetailByAppId() {
        fileEntity.getPathParam().put("appId", parameterMap.get("appId"));
        startPerformTest();
    }


    /******************接口调用日志********************/

    /**
     * 查询接口调用日志表格列
     */
    @Test(description = "查询接口调用日志表格列", groups = {INTERFACELOG})
    private void test_016_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "13");
        startPerformTest();
    }

    /**
     * 默认查询接口调用日志
     */
    @Test(description = "默认查询接口调用日志", groups = {INTERFACELOG})
    private void test_017_findByPage() {
        fileEntity.getPathParam().put("moduleId", "13");
        startPerformTest();
    }

    /**
     * 组合查询接口调用日志
     */
    @Test(description = "组合查询接口调用日志", groups = {INTERFACELOG})
    private void test_018_findByPage() {
        fileEntity.getPathParam().put("interfaceName", "interfaceName");
        fileEntity.getPathParam().put("appId", "1");
        fileEntity.getPathParam().put("appKey", "appKey");
        fileEntity.getPathParam().put("moduleId", "13");
        startPerformTest();
    }
}
