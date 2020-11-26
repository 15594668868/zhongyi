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

/**
 * @description: 运输管理
 * @author: guozhixiong
 * @time: 2020/8/17 14:31
 */
public class WaybillTestCase extends BaseTest {

    //excel测试用例
    private static String fileName = "中移运输管理.xls";

    //出库运单信息管理
    private static final String WAYBILL = "waybill";
    //派车单管理
    private static final String DISPATCH = "dispatch";
    //签收管理
    private static final String SIGNIN = "signin";
    //进出区域报表
    private static final String INOUT = "inOut";

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



    /*******************************出库运单信息管理*******************/

    /**
     * 获取出库运单信息管理列字段
     */
    @Test(description = "获取出库运单信息管理列字段", groups = {WAYBILL})
    private void test_001_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", 6);
        startPerformTest();
    }

    /**
     * 组合查询查询出库运单信息管理
     */
    @Test(description = "组合查询查询出库运单信息管理", groups = {WAYBILL})
    private void test_002_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", 6);
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {WAYBILL})
    private void test_003_exportWaybill() {
        Long[] dateTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        createRequestSpec();
        createResponseSpec();
        sendRequest();
    }

    /**
     * 获取发货仓库
     */
    @Test(description = "获取发货仓库", groups = {WAYBILL})
    private void test_004_getWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> WarehouseList = response.jsonPath().get();
        //保存发货仓库第一条
        parameterMap.put("WarehouseAreaId", WarehouseList.get(0).get("areaId"));
    }

    /**
     * 获取收货仓库
     */
    @Test(description = "获取收货仓库", groups = {WAYBILL})
    private void test_005_getReceiveWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> ReceiveWarehouseList = response.jsonPath().get();
        //保存发货收货仓库第一条
        parameterMap.put("ReceiveWarehouseAreaId", ReceiveWarehouseList.get(0).get("areaId"));
    }

    /**
     * 获取客户
     */
    @Test(description = "获取客户", groups = {WAYBILL})
    private void test_006_findAllMerchantByType() {
        startPerformTest();
    }

    /**
     * 根据发货仓库获取仓库数据
     */
    @Test(description = "根据发货仓库获取仓库数据", groups = {WAYBILL})
    private void test_007_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("WarehouseAreaId"));
        startPerformTest();
        parameterMap.put("WarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 根据收货仓库获取仓库数据
     */
    @Test(description = "根据收货仓库获取仓库数据", groups = {WAYBILL})
    private void test_008_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("ReceiveWarehouseAreaId"));
        startPerformTest();
        parameterMap.put("ReceiveWarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 判断仓库编号是否重复
     */
    @Test(description = "判断仓库编号是否重复", groups = {WAYBILL})
    private void test_009_findExistWaybillCode() {
        startPerformTest();
    }

    /**
     * 新增保存出库运单
     */
    @Test(description = "新增保存出库运单", groups = {WAYBILL})
    private void test_010_saveWaybill() {
        createWaybill("ZDHYD");
        startPerformTest();
    }

    /**
     * 创建新增出库单
     */
    private void createWaybill(String codeBegin) {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getBodyParam().put("waybillCode", codeBegin + currentTime);
        //发货仓库
        Map warehouseArea = (Map)parameterMap.get("WarehouseArea");
        fileEntity.getBodyParam().put("areaId", warehouseArea.get("areaId"));
        fileEntity.getBodyParam().put("areaCode", warehouseArea.get("areaCode"));
        fileEntity.getBodyParam().put("areaName", warehouseArea.get("areaName"));
        //收货仓库
        Map receiveWarehouseArea = (Map)parameterMap.get("ReceiveWarehouseArea");
        fileEntity.getBodyParam().put("receiveWarehouseId", receiveWarehouseArea.get("areaId"));
        fileEntity.getBodyParam().put("receiveWarehouseCode", receiveWarehouseArea.get("areaCode"));
        fileEntity.getBodyParam().put("receiveWarehouse", receiveWarehouseArea.get("areaName"));
        fileEntity.getBodyParam().put("receiverName", "收货人");
        fileEntity.getBodyParam().put("receiverCode", "622825199101010101");
        fileEntity.getBodyParam().put("receiverPhone", "15800000001");
        //收货地址
        fileEntity.getBodyParam().put("targetAddress", receiveWarehouseArea.get("positionAddress"));
        //发货地址
        fileEntity.getBodyParam().put("startAddress", warehouseArea.get("positionAddress"));
        fileEntity.getBodyParam().put("startCoord", warehouseArea.get("lonCenter") + "," + warehouseArea.get("latCenter"));
        //收货组织id
        fileEntity.getBodyParam().put("receiveOrgId", receiveWarehouseArea.get("orgId"));
        //发货组织id
        fileEntity.getBodyParam().put("orgId", warehouseArea.get("orgId"));
        fileEntity.getBodyParam().put("orgIds", warehouseArea.get("orgId") + "," + receiveWarehouseArea.get("orgId"));
        fileEntity.getBodyParam().put("targetCoord", receiveWarehouseArea.get("lonCenter") + "," + receiveWarehouseArea.get("latCenter"));
        fileEntity.getBodyParam().put("moduleId", 6);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
    }

    /**
     * 默认查询出库运单信息管理
     */
    @Test(description = "默认查询出库运单信息管理", groups = {WAYBILL})
    private void test_011_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", 6);
        fileEntity.getPathParam().put("isBindZhb", "0");
        startPerformTest();
        parameterMap.put("waybillId", response.jsonPath().get("result.data[0].waybillId"));
        parameterMap.put("waybillCode", response.jsonPath().get("result.data[0].waybillCode"));
    }

    /**
     * 获取追货宝wid
     */
    @Test(description = "获取追货宝wid", groups = {WAYBILL})
    private void test_012_findZhbWid() {
        startPerformTest();
        parameterMap.put("zhbWid", response.jsonPath().get("result[0].commids"));
    }

    /**
     * 根据追货宝wid获取详细信息
     */
    @Test(description = "根据追货宝wid获取详细信息", groups = {WAYBILL})
    private void test_013_getTerminalByWid() {
        fileEntity.getPathParam().put("wid", parameterMap.get("zhbWid"));
        startPerformTest();
        parameterMap.put("zhbName", response.jsonPath().get("result[0].terminalName"));
    }

    /**
     * 绑定追货宝
     */
    @Test(description = "绑定追货宝", groups = {WAYBILL})
    private void test_014_bindingObject() {
        fileEntity.getPathParam().put("zhbWid", parameterMap.get("zhbWid"));
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        fileEntity.getPathParam().put("zhbName", parameterMap.get("zhbName"));
        startPerformTest();
    }

    /**
     * 点击编辑获取数据
     */
    @Test(description = "点击编辑获取数据", groups = {WAYBILL})
    private void test_015_findByWaybillId() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑保存运单
     */
    @Test(description = "编辑保存运单", groups = {WAYBILL})
    private void test_016_saveWaybill() {
        fileEntity.setBodyParam((Map)parameterMap.get("result"));
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 获取派车单时电子锁wid
     */
    @Test(description = "获取派车单时电子锁wid", groups = {WAYBILL})
    private void test_017_findElecLockWid() {
        startPerformTest();
        parameterMap.put("elecLockWid", response.jsonPath().get("result[0].commids"));
    }

    /**
     * 根据电子锁wid获取详细信息
     */
    @Test(description = "根据电子锁wid获取详细信息", groups = {WAYBILL})
    private void test_018_getTerminalByWid() {
        fileEntity.getPathParam().put("wid", parameterMap.get("elecLockWid"));
        startPerformTest();
        parameterMap.put("elecLockName", response.jsonPath().get("result.terminalName"));
    }

    /**
     * 默认查询出库运单信息管理
     */
    @Test(description = "默认查询出库运单信息管理", groups = {WAYBILL})
    private void test_019_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", 6);
        //已绑定设备，因该运单在前面已经绑定了设备，所有查找已绑定设备
        fileEntity.getPathParam().put("isBindZhb", "1");
        startPerformTest();
        parameterMap.put("waybillId", response.jsonPath().get("result.data[0].waybillId"));
        parameterMap.put("waybillCode", response.jsonPath().get("result.data[0].waybillCode"));
        parameterMap.put("zhbWid", response.jsonPath().get("result.data[0].zhbWid"));
    }

    /**
     * 生成派车单保存提交
     */
    @Test(description = "生成派车单保存提交", groups = {WAYBILL})
    private void test_020_produceDispatchOrder() {
        //运单编号
        fileEntity.getPathParam().put("waybillCodes", parameterMap.get("waybillCode"));
        fileEntity.getPathParam().put("telephone", "15800000000");
        fileEntity.getPathParam().put("zhbWids", parameterMap.get("zhbWid"));
        fileEntity.getPathParam().put("vehicleNo", "陕A12345");
        fileEntity.getPathParam().put("waybillIds", parameterMap.get("waybillId"));
        fileEntity.getPathParam().put("eleclockName", parameterMap.get("elecLockName"));
        fileEntity.getPathParam().put("eleclockWid", parameterMap.get("elecLockWid"));
        fileEntity.getPathParam().put("driverName", "司机");
        fileEntity.getPathParam().put("isBindEleclock", "1");
        startPerformTest();
    }

    /**
     * 点击编辑获取数据
     */
    @Test(description = "点击编辑获取数据", groups = {WAYBILL})
    private void test_021_findByWaybillId() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑更改为运输中
     */
    @Test(description = "编辑更改为运输中", groups = {WAYBILL})
    private void test_022_saveWaybill() {
        Map map = (Map)parameterMap.get("result");
        Long[] dateTime = DateUtil.getTimeListDay(-1, 0);
        //运输中
        map.put("waybillStatus", "2");
        //起运时间
        map.put("sendTime", dateTime[0]);
        //删除结束时间
        map.remove("finishTime");
        fileEntity.setBodyParam(map);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 点击编辑获取数据
     */
    @Test(description = "点击编辑获取数据", groups = {WAYBILL})
    private void test_023_findByWaybillId() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑更改为已送达
     */
    @Test(description = "编辑更改为已送达", groups = {WAYBILL})
    private void test_024_saveWaybill() {
        Map map = (Map)parameterMap.get("result");
        Long[] dateTime = DateUtil.getTimeListDay(0, 0);
        //运输中
        map.put("waybillStatus", "3");
        //起运时间
        map.put("finishTime", dateTime[0]);
        fileEntity.setBodyParam(map);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /*******************派车单管理***********************/

    /**
     * 获取派车单管理列字段
     */
    @Test(description = "获取派车单管理列字段", groups = {DISPATCH})
    private void test_025_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", 18);
        startPerformTest();
    }

    /**
     * 获取电子锁wid
     */
    @Test(description = "获取电子锁wid", groups = {DISPATCH})
    private void test_026_findElecLockWid() {
        startPerformTest();
    }

    /**
     * 获取追货宝
     */
    @Test(description = "获取追货宝", groups = {DISPATCH})
    private void test_027_getTerminalByType() {
        startPerformTest();
        saveResponseParameters("result[0].commids");
    }

    /**
     * 组合查询派车单管理
     */
    @Test(description = "组合查询派车单管理", groups = {DISPATCH})
    private void test_028_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-10, 0);
        fileEntity.getPathParam().put("waybillCodes", "运单编号");
        fileEntity.getPathParam().put("vehicleNo", "车牌号");
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("eleclockWid", parameterMap.get("commids"));
        fileEntity.getPathParam().put("dispatchCode", "派车单号");
        fileEntity.getPathParam().put("moduleId", 18);
        startPerformTest();
    }

/***为了新增派车单用于解除运单**/
    /**
     * 获取发货仓库
     */
    @Test(description = "获取发货仓库", groups = {DISPATCH})
    private void test_029_getWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> WarehouseList = response.jsonPath().get();
        //保存发货仓库第一条
        parameterMap.put("WarehouseAreaId", WarehouseList.get(0).get("areaId"));
    }

    /**
     * 获取收货仓库
     */
    @Test(description = "获取收货仓库", groups = {DISPATCH})
    private void test_030_getReceiveWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> ReceiveWarehouseList = response.jsonPath().get();
        //保存发货收货仓库第一条
        parameterMap.put("ReceiveWarehouseAreaId", ReceiveWarehouseList.get(0).get("areaId"));
    }
    /**
     * 根据发货仓库获取仓库数据
     */
    @Test(description = "根据发货仓库获取仓库数据", groups = {DISPATCH})
    private void test_031_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("WarehouseAreaId"));
        startPerformTest();
        parameterMap.put("WarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 根据收货仓库获取仓库数据
     */
    @Test(description = "根据收货仓库获取仓库数据", groups = {DISPATCH})
    private void test_032_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("ReceiveWarehouseAreaId"));
        startPerformTest();
        parameterMap.put("ReceiveWarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 新增保存出库运单
     */
    @Test(description = "新增保存出库运单", groups = {DISPATCH})
    private void test_033_saveWaybill() {
        createWaybill("ZDHJCYD");
        startPerformTest();
    }

    /**
     * 默认查询出库运单信息管理
     */
    @Test(description = "默认查询出库运单信息管理", groups = {DISPATCH})
    private void test_034_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", 6);
        fileEntity.getPathParam().put("isBindZhb", "0");
        startPerformTest();
        parameterMap.put("waybillId", response.jsonPath().get("result.data[0].waybillId"));
        parameterMap.put("waybillCode", response.jsonPath().get("result.data[0].waybillCode"));
        parameterMap.put("zhbWid", response.jsonPath().get("result.data[0].zhbWid"));
    }

    /**
     * 获取派车单时电子锁wid
     */
    @Test(description = "获取派车单时电子锁wid", groups = {DISPATCH})
    private void test_035_findElecLockWid() {
        startPerformTest();
        parameterMap.put("elecLockWid", response.jsonPath().get("result[0].commids"));
    }

    /**
     * 根据电子锁wid获取详细信息
     */
    @Test(description = "根据电子锁wid获取详细信息", groups = {DISPATCH})
    private void test_036_getTerminalByWid() {
        fileEntity.getPathParam().put("wid", parameterMap.get("elecLockWid"));
        startPerformTest();
        parameterMap.put("elecLockName", response.jsonPath().get("result.terminalName"));
    }

    /**
     * 生成派车单保存提交
     */
    @Test(description = "生成派车单保存提交", groups = {DISPATCH})
    private void test_037_produceDispatchOrder() {
        //运单编号
        fileEntity.getPathParam().put("waybillCodes", parameterMap.get("waybillCode"));
        fileEntity.getPathParam().put("telephone", "15811111111");
        fileEntity.getPathParam().put("zhbWids", parameterMap.get("zhbWid"));
        fileEntity.getPathParam().put("vehicleNo", "陕A54321");
        fileEntity.getPathParam().put("waybillIds", parameterMap.get("waybillId"));
        fileEntity.getPathParam().put("eleclockName", parameterMap.get("elecLockName"));
        fileEntity.getPathParam().put("eleclockWid", parameterMap.get("elecLockWid"));
        fileEntity.getPathParam().put("driverName", "用于派车单司机");
        fileEntity.getPathParam().put("isBindEleclock", "1");
        startPerformTest();
    }
/*****用于解除运单而新增派车单*****/

    /**
     * 默认查询派车单管理
     */
    @Test(description = "默认查询派车单管理", groups = {DISPATCH})
    private void test_038_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-10, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", 18);
        startPerformTest();
        parameterMap.put("dispatchId", response.jsonPath().get("result.data[0].dispatchId"));
        parameterMap.put("vehicleNo", response.jsonPath().get("result.data[0].vehicleNo"));
        parameterMap.put("dispatchCode", response.jsonPath().get("result.data[0].dispatchCode"));
        parameterMap.put("dispatchStatus", response.jsonPath().get("result.data[0].dispatchStatus"));
        parameterMap.put("waybillCodes", response.jsonPath().get("result.data[0].waybillCodes"));
        parameterMap.put("waybillCode", response.jsonPath().get("result.data[0].orderWaybillPOList[0].waybillCode"));
        parameterMap.put("waybillIds", response.jsonPath().get("result.data[0].waybillIds"));
        parameterMap.put("waybillId", response.jsonPath().get("result.data[0].orderWaybillPOList[0].waybillId"));
        parameterMap.put("eleclockName", response.jsonPath().get("result.data[0].eleclockName"));
        parameterMap.put("eleclockWid", response.jsonPath().get("result.data[0].eleclockWid"));
    }

    /**
     * 点击编辑获取可绑定运单
     */
    @Test(description = "点击编辑获取可绑定运单", groups = {DISPATCH})
    private void test_039_findWayBillCodesByDispatchId() {
        fileEntity.getPathParam().put("dispatchId", parameterMap.get("dispatchId"));
        startPerformTest();
    }

    /**
     * 编辑保存数据
     */
    @Test(description = "编辑保存数据", groups = {DISPATCH})
    private void test_040_editOrderDispatch() {
        fileEntity.getPathParam().put("vehicleNo", parameterMap.get("vehicleNo"));
        fileEntity.getPathParam().put("dispatchCode", parameterMap.get("dispatchCode"));
        fileEntity.getPathParam().put("dispatchStatus", parameterMap.get("dispatchStatus"));
        fileEntity.getPathParam().put("dispatchId", parameterMap.get("dispatchId"));
        startPerformTest();
    }

    /**
     * 解除绑定电子锁
     */
    @Test(description = "解除绑定电子锁", groups = {DISPATCH})
    private void test_041_unboundOrderDispatch() {
        fileEntity.getPathParam().put("dispatchId", parameterMap.get("dispatchId"));
        fileEntity.getPathParam().put("eleclockWid", parameterMap.get("eleclockWid"));
        fileEntity.getPathParam().put("eleclockName", parameterMap.get("eleclockName"));
        startPerformTest();
    }

    /**
     * 获取派车单时电子锁wid
     */
    @Test(description = "获取派车单时电子锁wid", groups = {DISPATCH})
    private void test_042_findElecLockWid() {
        startPerformTest();
        parameterMap.put("elecLockWid", response.jsonPath().get("result[0].commids"));
    }

    /**
     * 根据电子锁wid获取详细信息
     */
    @Test(description = "根据电子锁wid获取详细信息", groups = {DISPATCH})
    private void test_043_getTerminalByWid() {
        fileEntity.getPathParam().put("wid", parameterMap.get("elecLockWid"));
        startPerformTest();
        parameterMap.put("elecLockName", response.jsonPath().get("result.terminalName"));
    }
    /**
     * 派车单绑定电子锁设备
     */
    @Test(description = "派车单绑定电子锁设备", groups = {DISPATCH})
    private void test_044_boundEleclockDispatch() {
        fileEntity.getPathParam().put("dispatchId", parameterMap.get("dispatchId"));
        fileEntity.getPathParam().put("eleclockWid", parameterMap.get("eleclockWid"));
        fileEntity.getPathParam().put("eleclockName", parameterMap.get("eleclockName"));
        startPerformTest();
    }

    /**
     * 解除运单
     */
    @Test(description = "解除运单", groups = {DISPATCH})
    private void test_045_unboundOrderWaybill() {
        fileEntity.getPathParam().put("waybillCodes", parameterMap.get("waybillCodes"));
        fileEntity.getPathParam().put("waybillIds", parameterMap.get("waybillIds"));
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        fileEntity.getPathParam().put("waybillCode", parameterMap.get("waybillCode"));
        fileEntity.getPathParam().put("dispatchId", parameterMap.get("dispatchId"));
        startPerformTest();
    }

/****新增派车单为了强制解除****/
    /**
     * 获取发货仓库
     */
    @Test(description = "获取发货仓库", groups = {DISPATCH})
    private void test_046_getWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> WarehouseList = response.jsonPath().get();
        //保存发货仓库第一条
        parameterMap.put("WarehouseAreaId", WarehouseList.get(0).get("areaId"));
    }

    /**
     * 获取收货仓库
     */
    @Test(description = "获取收货仓库", groups = {DISPATCH})
    private void test_047_getReceiveWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> ReceiveWarehouseList = response.jsonPath().get();
        //保存发货收货仓库第一条
        parameterMap.put("ReceiveWarehouseAreaId", ReceiveWarehouseList.get(0).get("areaId"));
    }

    /**
     * 根据发货仓库获取仓库数据
     */
    @Test(description = "根据发货仓库获取仓库数据", groups = {DISPATCH})
    private void test_048_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("WarehouseAreaId"));
        startPerformTest();
        parameterMap.put("WarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 根据收货仓库获取仓库数据
     */
    @Test(description = "根据收货仓库获取仓库数据", groups = {DISPATCH})
    private void test_049_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("ReceiveWarehouseAreaId"));
        startPerformTest();
        parameterMap.put("ReceiveWarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 新增保存出库运单
     */
    @Test(description = "新增保存出库运单", groups = {DISPATCH})
    private void test_050_saveWaybill() {
        createWaybill("ZDHQZJC");
        startPerformTest();
    }

    /**
     * 默认查询出库运单信息管理
     */
    @Test(description = "默认查询出库运单信息管理", groups = {DISPATCH})
    private void test_051_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", 6);
        fileEntity.getPathParam().put("isBindZhb", "0");
        startPerformTest();
        parameterMap.put("waybillId", response.jsonPath().get("result.data[0].waybillId"));
        parameterMap.put("waybillCode", response.jsonPath().get("result.data[0].waybillCode"));
        parameterMap.put("zhbWid", response.jsonPath().get("result.data[0].zhbWid"));
    }

    /**
     * 获取派车单时电子锁wid
     */
    @Test(description = "获取派车单时电子锁wid", groups = {DISPATCH})
    private void test_052_findElecLockWid() {
        startPerformTest();
        parameterMap.put("elecLockWid", response.jsonPath().get("result[0].commids"));
    }

    /**
     * 根据电子锁wid获取详细信息
     */
    @Test(description = "根据电子锁wid获取详细信息", groups = {DISPATCH})
    private void test_053_getTerminalByWid() {
        fileEntity.getPathParam().put("wid", parameterMap.get("elecLockWid"));
        startPerformTest();
        parameterMap.put("elecLockName", response.jsonPath().get("result.terminalName"));
    }

    /**
     * 生成派车单保存提交
     */
    @Test(description = "生成派车单保存提交", groups = {DISPATCH})
    private void test_054_produceDispatchOrder() {
        //运单编号
        fileEntity.getPathParam().put("waybillCodes", parameterMap.get("waybillCode"));
        fileEntity.getPathParam().put("telephone", "15811111111");
        fileEntity.getPathParam().put("zhbWids", parameterMap.get("zhbWid"));
        fileEntity.getPathParam().put("vehicleNo", "陕A54321");
        fileEntity.getPathParam().put("waybillIds", parameterMap.get("waybillId"));
        fileEntity.getPathParam().put("eleclockName", parameterMap.get("elecLockName"));
        fileEntity.getPathParam().put("eleclockWid", parameterMap.get("elecLockWid"));
        fileEntity.getPathParam().put("driverName", "用于派车单司机");
        fileEntity.getPathParam().put("isBindEleclock", "1");
        startPerformTest();
    }
/****新增派车单强制解除完成****/

    /**
     * 查询新增的用于强制解除的派车单
     */
    @Test(description = "查询新增的用于强制解除的派车单", groups = {DISPATCH})
    private void test_055_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-10, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", 18);
        startPerformTest();
        parameterMap.put("dispatchId", response.jsonPath().get("result.data[0].dispatchId"));
        parameterMap.put("waybillCodes", response.jsonPath().get("result.data[0].waybillCodes"));
        parameterMap.put("waybillCode", response.jsonPath().get("result.data[0].orderWaybillPOList[0].waybillCode"));
        parameterMap.put("waybillIds", response.jsonPath().get("result.data[0].waybillIds"));
        parameterMap.put("waybillId", response.jsonPath().get("result.data[0].orderWaybillPOList[0].waybillId"));
    }

    /**
     * 强制解除运单
     */
    @Test(description = "强制解除运单", groups = {DISPATCH})
    private void test_056_unboundOrderWaybill2() {
        fileEntity.getPathParam().put("waybillCodes", parameterMap.get("waybillCodes"));
        fileEntity.getPathParam().put("waybillIds", parameterMap.get("waybillIds"));
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        fileEntity.getPathParam().put("dispatchId", parameterMap.get("dispatchId"));
        fileEntity.getPathParam().put("waybillCode", parameterMap.get("waybillCode"));
        startPerformTest();
    }




    /********************************************签收管理*************************/
    /**
     * 获取签收管理列字段
     */
    @Test(description = "获取签收管理列字段", groups = {SIGNIN})
    private void test_057_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", 19);
        startPerformTest();
    }

    /**
     * 组合查询签收管理
     */
    @Test(description = "组合查询签收管理", groups = {SIGNIN})
    private void test_058_findByPage() {
        Long[] signDateRange = DateUtil.getTimeListDay(-10, 0);
        fileEntity.getPathParam().put("receiverPhone", "联系电话");
        fileEntity.getPathParam().put("moduleId", 19);
        startPerformTest();
    }


/**为了新增签收数据**/
    /**
     * 获取发货仓库
     */
    @Test(description = "获取发货仓库", groups = {SIGNIN})
    private void test_059_getWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> WarehouseList = response.jsonPath().get();
        //保存发货仓库第一条
        parameterMap.put("WarehouseAreaId", WarehouseList.get(0).get("areaId"));
    }

    /**
     * 获取收货仓库
     */
    @Test(description = "获取收货仓库", groups = {SIGNIN})
    private void test_060_getReceiveWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> ReceiveWarehouseList = response.jsonPath().get();
        //保存发货收货仓库第一条
        parameterMap.put("ReceiveWarehouseAreaId", ReceiveWarehouseList.get(0).get("areaId"));
    }
    /**
     * 根据发货仓库获取仓库数据
     */
    @Test(description = "根据发货仓库获取仓库数据", groups = {SIGNIN})
    private void test_061_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("WarehouseAreaId"));
        startPerformTest();
        parameterMap.put("WarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 根据收货仓库获取仓库数据
     */
    @Test(description = "根据收货仓库获取仓库数据", groups = {SIGNIN})
    private void test_062_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("ReceiveWarehouseAreaId"));
        startPerformTest();
        parameterMap.put("ReceiveWarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 新增保存出库运单
     */
    @Test(description = "新增保存出库运单", groups = {SIGNIN})
    private void test_063_saveWaybill() {
        createWaybill("ZDHQS");
        startPerformTest();
    }

    /**
     * 默认查询出库运单信息管理
     */
    @Test(description = "默认查询出库运单信息管理", groups = {SIGNIN})
    private void test_064_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("selectDateRange[0]", dateTime[0]);
        fileEntity.getPathParam().put("selectDateRange[1]", dateTime[1]);
        fileEntity.getPathParam().put("moduleId", 6);
        fileEntity.getPathParam().put("isBindZhb", "0");
        startPerformTest();
        parameterMap.put("waybillId", response.jsonPath().get("result.data[0].waybillId"));
        parameterMap.put("waybillCode", response.jsonPath().get("result.data[0].waybillCode"));
    }

    /**
     * 点击编辑获取数据
     */
    @Test(description = "点击编辑获取数据", groups = {SIGNIN})
    private void test_065_findByWaybillId() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑更改为运输中
     */
    @Test(description = "编辑更改为运输中", groups = {SIGNIN})
    private void test_066_saveWaybill() {
        Map map = (Map)parameterMap.get("result");
        Long[] dateTime = DateUtil.getTimeListDay(-1, 0);
        //运输中
        map.put("waybillStatus", "2");
        //起运时间
        map.put("sendTime", dateTime[0]);
        //删除结束时间
        map.remove("finishTime");
        fileEntity.setBodyParam(map);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 点击编辑获取数据
     */
    @Test(description = "点击编辑获取数据", groups = {SIGNIN})
    private void test_067_findByWaybillId() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑更改为已送达
     */
    @Test(description = "编辑更改为已送达", groups = {SIGNIN})
    private void test_068_saveWaybill() {
        Map map = (Map)parameterMap.get("result");
        Long[] dateTime = DateUtil.getTimeListDay(0, 0);
        //运输中
        map.put("waybillStatus", "3");
        //起运时间
        map.put("finishTime", dateTime[0]);
        fileEntity.setBodyParam(map);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }
/**签收数据新增完成**/

    /**
     * 默认查询签收管理
     */
    @Test(description = "默认查询签收管理", groups = {SIGNIN})
    private void test_069_findByPage() {
        fileEntity.getPathParam().put("moduleId", "19");
        startPerformTest();
        List<Map<String, Object>> list = response.jsonPath().get("result.data");
        //查找到第一条已到达的待签收数据
        for(Map map : list) {
            if(StringUtil.equals("3", map.get("waybillStatus").toString())
                    && StringUtil.equals("已到达", map.get("waybillStatusStr").toString())) {
                parameterMap.put("waybillId", map.get("waybillId"));
                Map material = (Map) ((List)map.get("materialList")).get(0);
                parameterMap.put("deliveryAmount", material.get("deliveryAmount"));
                parameterMap.put("materialDescribe", material.get("materialDescribe"));
                parameterMap.put("materialAmount", material.get("materialAmount"));
                parameterMap.put("materialId", material.get("materialId"));
                break;
            }
        }
    }

    /**
     * 签收
     */
    @Test(description = "签收", groups = {SIGNIN})
    private void test_070_signWaybill() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
    }

    /**
     * 取消签收
     */
    @Test(description = "取消签收", groups = {SIGNIN})
    private void test_071_cancelSignWaybill() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
    }

    /**
     * 批量签收
     */
    @Test(description = "批量签收", groups = {SIGNIN})
    private void test_072_01_signWaybillBatch() {
        fileEntity.getPathParam().put("waybillIds", parameterMap.get("waybillId"));
        startPerformTest();
    }

    /**
     * 取消签收
     */
    @Test(description = "取消签收", groups = {SIGNIN})
    private void test_072_02_cancelSignWaybill() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
    }

    /**
     * 通过物料签收
     */
    @Test(description = "通过物料签收", groups = {SIGNIN})
    private void test_072_03_signMaterial() {
        //签收6个
        fileEntity.getPathParam().put("signAmount", "6");
        fileEntity.getPathParam().put("deliveryAmount", parameterMap.get("deliveryAmount"));
        fileEntity.getPathParam().put("materialDescribe", parameterMap.get("materialDescribe"));
        fileEntity.getPathParam().put("materialAmount", parameterMap.get("materialAmount"));
        fileEntity.getPathParam().put("remark", "签收6个物料");
        fileEntity.getPathParam().put("materialId", parameterMap.get("materialId"));
        startPerformTest();
    }


    /****************进出区域报表***************/

    /**
     * 获取进出区域报表列字段
     */
    @Test(description = "获取进出区域报表列字段", groups = {INOUT})
    private void test_073_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "63");
        startPerformTest();
    }

    /**
     * 进出区域报表默认查询
     */
    @Test(description = "进出区域报表默认查询", groups = {INOUT})
    private void test_074_findByPage() {
        fileEntity.getPathParam().put("moduleId", "63");
        startPerformTest();
    }

    /**
     * 进出区域报表组合查询
     */
    @Test(description = "进出区域报表组合查询", groups = {INOUT})
    private void test_075_findByPage() {
        fileEntity.getPathParam().put("moduleId", "63");
        fileEntity.getPathParam().put("vehicleNo", "车牌号");
        //进区域
        fileEntity.getPathParam().put("inOutType", "1");
        fileEntity.getPathParam().put("areaName", "场站");
        startPerformTest();
    }
    /**
     * 导出
     */
    @Test(description = "导出", groups = {INOUT})
    private void test_076_exportInOutArea() {
        startPerformTest();
    }
}
