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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

/**
 * @description: 设备管理
 * @author: guozhixiong
 * @time: 2020/8/12 15:15
 */
public class DeviceTestCase extends BaseTest {

    //excel测试用例
    private static String fileName = "中移设备管理.xls";

    //设备基础信息
    private static final String TERMINAL = "terminal";
    //魔方管理
    private static final String GATEWAY = "gateway";
    //标签管理
    private static final String LABLE = "lable";
    //设备报警设置
    private static final String DEVICEALARMSET = "deviceAlarmSet";
    //温湿度报警设置
    private static final String HUMITUREALARMSET = "humitureAlarmSet";
    //查看电子锁密码
    private static final String ELECTRICLOCKPASSWORD = "electricLockPassword";
    //电子锁操作记录
    private static final String ELECTRICLOCKRECORD = "electricLockRecord";
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



    /*******************************设备基础信息*******************/

    /**
     * 获取设备基础信息模块列表
     */
    @Test(description = "获取设备基础信息模块列表", groups = {TERMINAL})
    private void test_001_getTableColumnSettings() {
        fileEntity.getPathParam().put("userId", userId);
        //设备基础信息模块id为9
        fileEntity.getPathParam().put("moduleId", "9");
        startPerformTest();
    }

    /**
     * 获取设备基础信息使用状态（使用中/空闲）
     */
    @Test(description = "获取设备基础信息使用状态（使用中/空闲）", groups = {TERMINAL})
    private void test_002_getDictByDicTypeKey() {
        //数据字典key
        fileEntity.getPathParam().put("dicTypeKey", "use_status");
        startPerformTest();
    }

    /**
     * 所有查询条件都增加后组合查询设备基础信息
     */
    @Test(description = "所有查询条件都增加后组合查询设备基础信息", groups = {TERMINAL})
    private void test_003_findByPage() {
        fileEntity.getPathParam().put("moduleId", 9);
        fileEntity.getPathParam().put("orgIds", orgId);
        startPerformTest();
    }

    /**
     * 默认查询设备基础信息
     */
    @Test(description = "默认查询设备基础信息", groups = {TERMINAL})
    private void test_004_findByPage() {
        fileEntity.getPathParam().put("moduleId", 9);
        startPerformTest();
    }
    /**
     * 查询设备名称是否存在接口
     */
    @Test(description = "查询设备名称是否存在接口", groups = {TERMINAL})
    private void test_005_findExistTerminalName() {
        startPerformTest();
    }
    /**
     * 查询设备编号是否存在接口
     */
    @Test(description = "查询设备编号是否存在接口", groups = {TERMINAL})
    private void test_006_findExistTerminalNo() {
        startPerformTest();
    }
    /**
     * 查询设备wid是否存在接口
     */
    @Test(description = "查询设备wid是否存在接口", groups = {TERMINAL})
    private void test_007_findExistCommids() {
        startPerformTest();
    }

    /**
     * 新增保存设备基础信息
     */
    @Test(description = "新增保存设备基础信息", groups = {TERMINAL})
    private void test_008_saveTerminal() {
        String date = DateUtil.getDate(new Date(), "yyyy-MM-dd");
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        //设备编号
        fileEntity.getPathParam().put("terminalNo", currentTime);
        //设备wid
        fileEntity.getPathParam().put("commids", currentTime);
        //设备名称
        fileEntity.getPathParam().put("terminalName", "自动化" + currentTime);
        //生产日期
        fileEntity.getPathParam().put("productionDate", date);
        //入库日期
        fileEntity.getPathParam().put("installDate", date);
        fileEntity.getPathParam().put("orgId", orgId);
        fileEntity.getPathParam().put("moduleId", 9);
        startPerformTest();
    }

    /**
     * 默认查询设备基础信息
     */
    @Test(description = "默认查询设备基础信息", groups = {TERMINAL})
    private void test_009_findByPage() {
        fileEntity.getPathParam().put("moduleId", 9);
        startPerformTest();
        //保存设备基础信息id
        saveResponseParameters("result.data[0].terminalId");
    }

    /**
     * 禁用设备基础信息
     */
    @Test(description = "禁用设备基础信息", groups = {TERMINAL})
    private void test_010_saveTerminalSetting() {
        //2是禁用
        fileEntity.getPathParam().put("isValid", 2);
        fileEntity.getPathParam().put("terminalId", parameterMap.get("terminalId"));
        startPerformTest();
    }

    /**
     * 启用设备基础信息
     */
    @Test(description = "启用设备基础信息", groups = {TERMINAL})
    private void test_011_saveTerminalSetting() {
        //1是启用
        fileEntity.getPathParam().put("isValid", 1);
        fileEntity.getPathParam().put("terminalId", parameterMap.get("terminalId"));
        startPerformTest();
    }


    /**
     * 点击编辑根据id查找设备基础信息
     */
    @Test(description = "点击编辑根据id查找设备基础信息", groups = {TERMINAL})
    private void test_012_findByTerminalId() {
        fileEntity.getPathParam().put("terminalId", parameterMap.get("terminalId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑保存设备基础信息
     */
    @Test(description = "编辑保存设备基础信息", groups = {TERMINAL})
    private void test_013_saveTerminal() {
        fileEntity.setPathParam((Map)parameterMap.get("result"));
        startPerformTest();
    }


    /**
     * 批量设置
     */
    @Test(description = "批量设置", groups = {TERMINAL})
    private void test_014_setZhbModelAndInterval() {
        //运单模式上传间隔(分钟)
        fileEntity.getPathParam().put("model", 2);
        //非运单模式上传间隔(分钟)
        fileEntity.getPathParam().put("uploadInterval", 3);
        fileEntity.getPathParam().put("terminalIds", parameterMap.get("terminalId"));
        startPerformTest();
    }

    /*************************魔方管理******************/
    /**
     * 查询可用仓库
     */
    @Test(description = "查询可用仓库", groups = {GATEWAY})
    private void test_015_getWarehouseList() {
        startPerformTest();
        //获取到所有仓库，取最后一个仓库，也就是最新的仓库
        List<Map> list = response.jsonPath().get();
        parameterMap.put("areaId", list.get((list.size()-1)).get("areaId"));
    }

    /**
     * 新增标签（用于魔方管理）
     */
    @Test(description = "新增标签（用于魔方管理）", groups = {GATEWAY})
    private void test_016_saveLable() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("lableName", "标签魔方" + currentTime);
        //仓库id
        fileEntity.getPathParam().put("warehouseId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("lableCode", "标签魔方" + currentTime);
        //中移物流的id
        fileEntity.getPathParam().put("orgId", orgId);
        startPerformTest();
    }

    /**
     * 查询标签列表获取可用标签和仓库
     */
    @Test(description = "查询标签列表获取可用标签和仓库", groups = {GATEWAY})
    private void test_017_findByPage() {
        fileEntity.getPathParam().put("moduleId", 16);
        startPerformTest();
        //保存仓库id，标签id，标签编号
        saveResponseParameters("result.data[0].warehouseId", "result.data[0].lableId",
                "result.data[0].lableCode");
    }

    /**
     * 验证魔方名称是否重复
     */
    @Test(description = "验证魔方名称是否重复", groups = {GATEWAY})
    private void test_018_findExistGatewayName() {
        startPerformTest();
    }
    /**
     * 验证魔方编号是否重复
     */
    @Test(description = "验证魔方编号是否重复", groups = {GATEWAY})
    private void test_019_findExistGatewayCode() {
        startPerformTest();
    }
    /**
     * 验证魔方中心识别码是否重复
     */
    @Test(description = "验证魔方中心识别码是否重复", groups = {GATEWAY})
    private void test_020_findExistGatewayCode() {
        startPerformTest();
    }

    /**
     * 新增魔方
     */
    @Test(description = "新增魔方", groups = {GATEWAY})
    private void test_021_saveGateway() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("gatewayCode", "魔方编号" + currentTime);
        parameterMap.put("centerCode", "魔方编号" + currentTime);
        fileEntity.getPathParam().put("gatewayName", "魔方名称" + currentTime);
        fileEntity.getPathParam().put("warehouseId", parameterMap.get("warehouseId"));
        fileEntity.getPathParam().put("centerCode", currentTime);
        fileEntity.getPathParam().put("moduleId", 8);
        fileEntity.getPathParam().put("orgId", orgId);
        startPerformTest();
    }

    /**
     * 新增魔方时下发指令
     */
    @Test(description = "新增魔方时下发指令", groups = {GATEWAY})
    private void test_022_sendGatewayCmd() {
        fileEntity.getPathParam().put("centerCode", parameterMap.get("centerCode"));
        startPerformTest();
    }

    /**
     * 获取魔方页面列字段
     */
    @Test(description = "获取魔方页面列字段", groups = {GATEWAY})
    private void test_023_getTableColumnSettings() {
        fileEntity.getPathParam().put("userId", userId);
        fileEntity.getPathParam().put("moduleId", 8);
        startPerformTest();
    }

    /**
     * 查询魔方页面组合查询
     */
    @Test(description = "查询魔方页面组合查询", groups = {GATEWAY})
    private void test_024_getTableColumnSettings() {
        fileEntity.getPathParam().put("gatewayCode", "魔方编码");
        fileEntity.getPathParam().put("centerCode", "中心识别码");
        fileEntity.getPathParam().put("orgId", orgId);
        fileEntity.getPathParam().put("moduleId", 8);
        startPerformTest();
    }

    /**
     * 默认查询魔方页面
     */
    @Test(description = "默认查询魔方页面", groups = {GATEWAY})
    private void test_025_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", 8);
        startPerformTest();
        //保存魔方id和仓库id，魔方名称，中心识别码
        saveResponseParameters("result.data[0].gatewayId", "result.data[0].warehouseId",
                "result.data[0].gatewayName", "result.data[0].centerCode");
    }

    /**
     * 点击编辑获取魔方信息
     */
    @Test(description = "点击编辑获取魔方信息", groups = {GATEWAY})
    private void test_026_findByGatewayId() {
        fileEntity.getPathParam().put("gatewayId", parameterMap.get("gatewayId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑保存魔方信息
     */
    @Test(description = "编辑保存魔方信息", groups = {GATEWAY})
    private void test_027_saveGateway() {
        Map map = (Map) parameterMap.get("result");
        map.put("moduleId", 8);
        map.remove("baseLableVOList");
        fileEntity.setPathParam(map);
        startPerformTest();
    }

    /**
     * 点击绑定标签信息获取可绑定标签
     */
    @Test(description = "点击绑定标签信息获取可绑定标签", groups = {GATEWAY})
    private void test_028_findByNoBindPage() {
        fileEntity.getPathParam().put("gatewayId", parameterMap.get("gatewayId"));
        fileEntity.getPathParam().put("warehouseId", parameterMap.get("warehouseId"));
        startPerformTest();
    }

    /**
     * 绑定标签确定
     */
    @Test(description = "绑定标签确定", groups = {GATEWAY})
    private void test_029_bindGateway() {
        fileEntity.getPathParam().put("labelIds", parameterMap.get("lableId"));
        fileEntity.getPathParam().put("gatewayName", parameterMap.get("gatewayName"));
        fileEntity.getPathParam().put("centerCode", parameterMap.get("centerCode"));
        fileEntity.getPathParam().put("gatewayId", parameterMap.get("gatewayId"));
        startPerformTest();
    }

    /**
     * 绑定标签指令下发
     */
    @Test(description = "绑定标签指令下发", groups = {GATEWAY})
    private void test_030_sendGatewayCmd() {
        fileEntity.getPathParam().put("centerCode", parameterMap.get("centerCode"));
        fileEntity.getPathParam().put("content", "0|" + parameterMap.get("lableCode"));
        startPerformTest();
    }


    /**
     * 查看绑定历史
     */
    @Test(description = "查看绑定历史", groups = {GATEWAY})
    private void test_031_bindGatewayHistory() {
        fileEntity.getPathParam().put("gatewayId", parameterMap.get("gatewayId"));
        startPerformTest();
    }

    /**
     * 删除
     */
    @Test(description = "删除", groups = {GATEWAY})
    private void test_032_bindGatewayHistory() {
        fileEntity.getPathParam().put("ids", parameterMap.get("gatewayId"));
        startPerformTest();
    }



    /**********************标签管理******************/

    /**
     * 验证标签编号是否存在
     */
    @Test(description = "验证标签编号是否存在", groups = {LABLE})
    private void test_033_getWarehouseList() {
        startPerformTest();
    }


    /**
     * 查询可用仓库
     */
    @Test(description = "查询可用仓库", groups = {LABLE})
    private void test_034_getWarehouseList() {
        startPerformTest();
        //获取到所有仓库，取最后一个仓库，也就是最新的仓库
        List<Map> list = response.jsonPath().get();
        parameterMap.put("areaId", list.get((list.size()-1)).get("areaId"));
    }

    /**
     * 新增标签保存
     */
    @Test(description = "新增标签保存", groups = {LABLE})
    private void test_035_saveLable() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("lableName", "标签" + currentTime);
        //仓库id
        fileEntity.getPathParam().put("warehouseId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("lableCode", "标签" + currentTime);
        //中移物流的id
        fileEntity.getPathParam().put("orgId", orgId);
        startPerformTest();
    }

    /**
     * 标签管理组合查询
     */
    @Test(description = "标签管理组合查询", groups = {LABLE})
    private void test_036_findByPage() {
        fileEntity.getPathParam().put("moduleId", 16);
        fileEntity.getPathParam().put("lableCode", "标签编号");
        fileEntity.getPathParam().put("orgId", orgId);
        startPerformTest();
    }

    /**
     * 标签管理默认查询
     */
    @Test(description = "标签管理默认查询", groups = {LABLE})
    private void test_037_findByPage() {
        fileEntity.getPathParam().put("moduleId", 16);
        startPerformTest();
        saveResponseParameters("result.data[0].lableId");
    }


    /**
     * 点击编辑获取数据
     */
    @Test(description = "点击编辑获取数据", groups = {LABLE})
    private void test_038_findByLableId() {
        fileEntity.getPathParam().put("lableId", parameterMap.get("lableId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 标签编辑保存
     */
    @Test(description = "标签编辑保存", groups = {LABLE})
    private void test_039_saveLable() {
        Map map = (Map)parameterMap.get("result");
        fileEntity.setPathParam(map);
        startPerformTest();
    }

    /**
     * 删除标签
     */
    @Test(description = "删除标签", groups = {LABLE})
    private void test_040_deleteLable() {
        fileEntity.getPathParam().put("ids", parameterMap.get("lableId"));
        startPerformTest();
    }



    /******************************设备报警设置******************/

    /**
     * 获取设备报警设置表格列字段
     */
    @Test(description = "获取设备报警设置表格列字段", groups = {DEVICEALARMSET})
    private void test_041_getTableColumnSettings() {
        fileEntity.getPathParam().put("userId", userId);
        fileEntity.getPathParam().put("moduleId", 28);
        startPerformTest();
    }

    /**
     * 设备报警设置组合查询
     */
    @Test(description = "设备报警设置组合查询", groups = {DEVICEALARMSET})
    private void test_042_findByPage() {
        //报警类型
        fileEntity.getBodyParam().put("alarmTypeId", "2");
        //设备类型
        fileEntity.getBodyParam().put("terminalType", "1");
        fileEntity.getBodyParam().put("terminalName", "设备名称");
        fileEntity.getBodyParam().put("moduleId", 28);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 设备报警设置获取自定义设备（低电报警）
     */
    @Test(description = "设备报警设置获取自定义设备（低电报警）", groups = {DEVICEALARMSET})
    private void test_043_getTerminalTreeByAlarmTypeId() {
        startPerformTest();
    }
    /**
     * 设备报警设置获取自定义设备（关机报警）
     */
    @Test(description = "设备报警设置获取自定义设备（关机报警）", groups = {DEVICEALARMSET})
    private void test_044_getTerminalTreeByAlarmTypeId() {
        startPerformTest();
    }
    /**
     * 设备报警设置获取自定义设备（掉线报警）
     */
    @Test(description = "设备报警设置获取自定义设备（掉线报警）", groups = {DEVICEALARMSET})
    private void test_045_getTerminalTreeByAlarmTypeId() {
        startPerformTest();
        List list = response.jsonPath().get("result[0].children");
        list.forEach( listMap -> {
            Map map = (Map)listMap;
            if(StringUtil.equals(map.get("disabled").toString(), "false")) {
                parameterMap.put("terminalIdList", map.get("id")); //不break，找到最后一个可用设备
                System.out.println(map.toString());
            }
        });
    }

    /**
     * 新增掉线报警设置
     */
    @Test(description = "新增掉线报警设置", groups = {DEVICEALARMSET})
    private void test_046_saveTerminalCommonAlarmSet() {
        //掉线报警设置类型为8
        fileEntity.getBodyParam().put("alarmTypeId", 8);
        //获取自定义设置中的设备
        fileEntity.getBodyParam().put("terminalIdList",
                new String[]{parameterMap.get("terminalIdList").toString()});
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }


    /**
     * 设备报警设置默认查询
     */
    @Test(description = "设备报警设置默认查询", groups = {DEVICEALARMSET})
    private void test_047_findByPage() {
        //掉线报警设置类型为8
        fileEntity.getBodyParam().put("moduleId", 28);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
        //存储设备报警id
        saveResponseParameters("result.data[0].alarmSetId");
    }

    /**
     * 获取报警联系人
     */
    @Test(description = "获取报警联系人", groups = {DEVICEALARMSET})
    private void test_048_getAllLinkManTree() {
        startPerformTest();
    }

    /**
     * 点击编辑获取数据
     */
    @Test(description = "点击编辑获取数据", groups = {DEVICEALARMSET})
    private void test_049_getTerminalCommonAlarmSetById() {
        fileEntity.getPathParam().put("alarmSetId", parameterMap.get("alarmSetId"));
        startPerformTest();
        //存储设备报警id
        saveResponseParameters("result");
    }

    /**
     * 编辑保存报警设置
     */
    @Test(description = "编辑保存报警设置", groups = {DEVICEALARMSET})
    private void test_050_saveTerminalCommonAlarmSet() {
        Map map = (Map)parameterMap.get("result");
        fileEntity.setBodyParam(map);
        fileEntity.getBodyParam().put("linkmanIdListOne", new String[]{});
        fileEntity.getBodyParam().put("linkmanIdListTwo", new String[]{});
        fileEntity.getBodyParam().put("linkmanIdListThree", new String[]{});
        //绑定的设备id
        fileEntity.getBodyParam().put("terminalIdList", new String[]{map.get("terminalId").toString()});
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 删除单个掉线报警设置
     */
    @Test(description = "删除单个掉线报警设置", groups = {DEVICEALARMSET})
    private void test_051_deleteTerminalCommonAlarmSetBatch() {
        fileEntity.getPathParam().put("ids", parameterMap.get("alarmSetId"));
        startPerformTest();
    }


    /**********************温湿度报警设置*****************/

    /**
     * 获取温湿度报警设置表格列字段
     */
    @Test(description = "获取温湿度报警设置表格列字段", groups = {HUMITUREALARMSET})
    private void test_052_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", 48);
        startPerformTest();
    }

    /**
     * 组合查询温湿度报警设置
     */
    @Test(description = "组合查询温湿度报警设置", groups = {HUMITUREALARMSET})
    private void test_053_findByPage() {
        fileEntity.getBodyParam().put("moduleId", 48);
        fileEntity.getBodyParam().put("terminalName", "设备名称");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 获取温湿度报警设置设备
     */
    @Test(description = "获取温湿度报警设置设备", groups = {HUMITUREALARMSET})
    private void test_054_getTerminalTree() {
        startPerformTest();
        List list = response.jsonPath().get("result[0].children");
        list.forEach( listMap -> {
            Map map = (Map)listMap;
            if(StringUtil.equals(map.get("disabled").toString(), "false")) {
                parameterMap.put("terminalIdList", map.get("id")); //不break，找到最后一个可用设备
                System.out.println(map.toString());
            }
        });
    }

    /**
     * 点击确定新增温湿度报警设置
     */
    @Test(description = "点击确定新增温湿度报警设置", groups = {HUMITUREALARMSET})
    private void test_055_saveHumitureCommonAlarmSet() {
        //掉线报警设置类型为8
        fileEntity.getBodyParam().put("terminalType", 4);
        //获取自定义设置中的设备
        fileEntity.getBodyParam().put("terminalIdList",
                new String[]{parameterMap.get("terminalIdList").toString()});
        //温度要求下限
        fileEntity.getBodyParam().put("lt", "1");
        //温度要求上限
        fileEntity.getBodyParam().put("ht", "10");
        //湿度要求下限
        fileEntity.getBodyParam().put("lh", "2");
        //湿度要求上限
        fileEntity.getBodyParam().put("hh", "8");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 默认查询温湿度报警设置
     */
    @Test(description = "默认查询温湿度报警设置", groups = {HUMITUREALARMSET})
    private void test_056_findByPage() {
        fileEntity.getBodyParam().put("moduleId", 48);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
        saveResponseParameters("result.data[0].alarmSetId");
    }

    /**
     * 点击编辑获取编辑信息
     */
    @Test(description = "点击编辑获取编辑信息", groups = {HUMITUREALARMSET})
    private void test_057_getHumitureCommonAlarmSetById() {
        fileEntity.getPathParam().put("alarmSetId", parameterMap.get("alarmSetId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 点击确定修改温湿度报警设置
     */
    @Test(description = "点击确定修改温湿度报警设置", groups = {HUMITUREALARMSET})
    private void test_058_saveHumitureCommonAlarmSet() {
        fileEntity.setBodyParam((Map)parameterMap.get("result"));
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 删除温湿度报警设置
     */
    @Test(description = "删除温湿度报警设置", groups = {HUMITUREALARMSET})
    private void test_059_deleteHumitureCommonAlarmSetBatch() {
        fileEntity.getPathParam().put("ids", parameterMap.get("alarmSetId"));
        startPerformTest();
    }


    /**********************查看电子锁密码******************************/
    /**
     *获取查看电子锁密码列字段
     */
    @Test(description = "获取查看电子锁密码列字段", groups = {ELECTRICLOCKPASSWORD})
    private void test_060_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "14");
        startPerformTest();
    }

    /**
     *组合查询查看电子锁密码
     */
    @Test(description = "组合查询查看电子锁密码", groups = {ELECTRICLOCKPASSWORD})
    private void test_061_findByPage() {
        fileEntity.getPathParam().put("wid", "设备wid");
        fileEntity.getPathParam().put("terminalName", "设备名称");
        fileEntity.getPathParam().put("moduleId", "14");
        startPerformTest();
    }

    /**
     *默认查询查看电子锁密码
     */
    @Test(description = "默认查询查看电子锁密码", groups = {ELECTRICLOCKPASSWORD})
    private void test_062_findByPage() {
        fileEntity.getPathParam().put("moduleId", "14");
        startPerformTest();
        saveResponseParameters("result.data[0].wid");
    }

    /**
     *查看密码
     */
    @Test(description = "查看密码", groups = {ELECTRICLOCKPASSWORD})
    private void test_063_checkTerminal() {
        fileEntity.getPathParam().put("wid", parameterMap.get("wid"));
        if(StringUtil.isNotEmpty(parameterMap.get("wid"))) {
            encryption();
            createRequestSpec();
            Map<String, Object> result = fileEntity.getResult();
            //以为message不能全匹配，所以重写
            responseSpecification = requestSpecification
                    .expect().log().all()
                    .statusCode(200)
                    .body("code", equalTo(result.get("code")))
                    .body("message", containsString(result.get("message").toString()));
            sendRequest();
        } else {
            log.info("没有可以查看密码的电子锁");
        }
    }


    /**********************电子锁操作记录******************************/
    /**
     *获取查看电子锁操作记录列字段
     */
    @Test(description = "获取查看电子锁操作记录列字段", groups = {ELECTRICLOCKRECORD})
    private void test_064_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "31");
        startPerformTest();
    }

    /**
     *组合查询电子锁操作记录
     */
    @Test(description = "组合查询电子锁操作记录", groups = {ELECTRICLOCKRECORD})
    private void test_065_findByPage() {
        fileEntity.getPathParam().put("wid", "设备wid");
        fileEntity.getPathParam().put("terminalName", "设备名称");
        fileEntity.getPathParam().put("moduleId", "31");
        startPerformTest();
    }

    /**
     * 默认查询电子锁操作记录
     */
    @Test(description = "默认查询电子锁操作记录", groups = {ELECTRICLOCKRECORD})
    private void test_066_findByPage() {
        fileEntity.getPathParam().put("moduleId", "31");
        startPerformTest();
        saveResponseParameters("result.data[0].wid");
    }

    /**
     *电子锁操作历史详情
     */
    @Test(description = "电子锁操作历史详情", groups = {ELECTRICLOCKRECORD})
    private void test_067_findHistoryDetailByWid() {
        fileEntity.getPathParam().put("wid", parameterMap.get("wid"));
        if(StringUtil.isNotEmpty(parameterMap.get("wid"))) {
            startPerformTest();
        } else {
            log.info("没有可以查看操作历史详情的电子锁");
        }
    }
}
