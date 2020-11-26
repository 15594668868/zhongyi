package testcase;

import com.alibaba.fastjson.JSON;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.DateUtil;
import utils.FilesUtils;
import utils.Md5Util;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @description: 仓库管理
 * @author: guozhixiong
 * @time: 2020/8/11 9:45
 */
public class AreaTestCase extends BaseTest {

    //excel测试用例
    private static String fileName = "中移仓库管理.xls";

    //库房信息管理
    private static final String AREA = "area";
    //库房时间设定管理
    private static final String AREATIMESETTING = "areaTimeSetting";
    //客户自提预约
    private static final String CUSTOMERSBOOKING = "customersBooking";
    //库房预约看板
    private static final String AREABOOKING = "areaBooking";
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



    /*******************************库房信息管理*******************/

    /**
     * 查找库房管理默认
     */
    @Test(description = "查找库房管理默认", groups = {AREA})
    private void test_001_findByPage() {
        fileEntity.getPathParam().put("moduleId", 15);
        startPerformTest();
    }

    /**
     * 查找库房管理组合查询
     */
    @Test(description = "查找库房管理组合查询", groups = {AREA})
    private void test_002_findByPage() {
        fileEntity.getPathParam().put("moduleId", 15);
        fileEntity.getPathParam().put("orgIds", orgId);
        startPerformTest();
    }

    /**
     * 获取部门
     */
    @Test(description = "获取部门", groups = {AREA})
    private void test_003_findOrganizationTreeByOrgId() {
        startPerformTest();
    }

    /**
     * 获取库房状态数据字典（启用/禁用）
     */
    @Test(description = "获取库房状态数据字典（启用/禁用）", groups = {AREA})
    private void test_004_getDictByDicTypeKey() {
        startPerformTest();
    }

    /**
     * 查询该仓库名称是否重复
     */
    @Test(description = "查询该仓库名称是否重复", groups = {AREA})
    private void test_005_findExistAreaName() {
        startPerformTest();
    }

    /**
     * 查询该仓库编号是否重复
     */
    @Test(description = "查询该仓库编号是否重复", groups = {AREA})
    private void test_006_findExistAreaCode() {
        startPerformTest();
    }

    /**
     * 新增仓库
     */
    @Test(description = "新增仓库", groups = {AREA})
    private void test_007_saveCustomPoint() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        String areaName = "自动化仓库" + currentTime;
        String areaCode =  currentTime.substring(currentTime.length()-10, currentTime.length());
        parameterMap.put("areaName", areaName);
        parameterMap.put("areaCode", areaCode);
        //仓库名称
        fileEntity.getBodyParam().put("areaName", areaName);
        //仓库code
        fileEntity.getBodyParam().put("areaCode", areaCode);
        fileEntity.getBodyParam().put("orgId", orgId);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 查询仓库获取新增的仓库id
     */
    @Test(description = "查询仓库获取新增的仓库id", groups = {AREA})
    private void test_008_findByPage() {
        fileEntity.getPathParam().put("moduleId", 15);
        startPerformTest();
        saveResponseParameters("result.data[0].areaId");
    }

    /**
     * 获取编辑数据/仓库名称获取仓库详情
     */
    @Test(description = "获取编辑数据/仓库名称获取仓库详情", groups = {AREA})
    private void test_009_findWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑仓库
     */
    @Test(description = "编辑仓库", groups = {AREA})
    private void test_010_saveCustomPoint() {
        Map resultMap = (Map)parameterMap.get("result");
        fileEntity.setBodyParam(resultMap);
        createEncrypt(JSON.toJSONString(resultMap), "bodyParam");
        startPerformTest();
    }


    /**
     * 新增标签（用于新增监测点）
     */
    @Test(description = "新增标签（用于新增监测点）", groups = {AREA})
    private void test_011_saveLable() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("lableName", "标签仓库" + currentTime);
        //仓库id
        fileEntity.getPathParam().put("warehouseId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("lableCode", "标签仓库" + currentTime);
        //中移物流的id
        fileEntity.getPathParam().put("orgId", orgId);
        startPerformTest();
    }

    /**
     * 新增监测点时调用获取当前仓库所有标签
     */
    @Test(description = "新增监测点时调用获取当前仓库所有标签", groups = {AREA})
    private void test_012_findAllLableByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        startPerformTest();
        //将标签id存起来
        saveResponseParameters("result[0].lableId");
    }

    /**
     * 保存监测点时判断监测点名称是否存在
     */
    @Test(description = "保存监测点时判断监测点名称是否存在", groups = {AREA})
    private void test_013_checkMonitorPointName() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("monitorPointName", "监测点名称");
        startPerformTest();
    }

    /**
     * 新增监测点
     */
    @Test(description = "新增监测点", groups = {AREA})
    private void test_014_saveMonitorPoint() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        //一个仓库下才判重，所以直接给成监测点名称
        fileEntity.getPathParam().put("monitorPointName", "监测点名称");
        fileEntity.getPathParam().put("lableId", parameterMap.get("lableId"));
        startPerformTest();
    }

    /**
     * 查询获取新增监测点id
     */
    @Test(description = "查询获取新增监测点id", groups = {AREA})
    private void test_015_findByPage() {
        fileEntity.getPathParam().put("moduleId", 15);
        startPerformTest();
        //拿到监测点id和仓库id
        saveResponseParameters("result.data[0].monitorPointList[0].monitorPointId", "result.data[0].areaId");

    }

    /**
     * 获取历史绑定设备
     */
    @Test(description = "获取历史绑定设备", groups = {AREA})
    private void test_016_findAllBindHistory() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getPathParam().put("monitorPointId", parameterMap.get("monitorPointId"));
        startPerformTest();
    }

    /**
     * 点击改绑设备时根据监测点id获取监测点数据
     */
    @Test(description = "点击改绑设备时根据监测点id获取监测点数据", groups = {AREA})
    private void test_017_getMonitorPointById() {
        fileEntity.getPathParam().put("monitorPointId", parameterMap.get("monitorPointId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 修改监测点保存
     */
    @Test(description = "修改监测点保存", groups = {AREA})
    private void test_018_updateLable() {
        Map map = (Map)parameterMap.get("result");
        fileEntity.setPathParam(map);
        startPerformTest();
    }

    /**
     * 删除监测点
     */
    @Test(description = "删除监测点", groups = {AREA})
    private void test_019_deleteMonitorPoint() {
        fileEntity.getPathParam().put("monitorPointId", parameterMap.get("monitorPointId"));
        startPerformTest();
    }

    /**
     * 仓库获取表格列
     */
    @Test(description = "仓库获取表格列", groups = {AREA})
    private void test_020_01_deleteMonitorPoint() {
        fileEntity.getPathParam().put("userId", userId);
        //库房信息管理id是15
        fileEntity.getPathParam().put("moduleId", 15);
        startPerformTest();
    }

    /**
     * lis系统仓库信息同步接口新增
     */
    @Test(description = "lis系统仓库信息同步接口新增", groups = {AREA})
    private void test_020_02_pushWarehouses() {
        String currentTime = DateUtil.getCurrentTimeMillis();
        String areaCode =  currentTime.substring(currentTime.length()-10, currentTime.length());
        //存储用于下方再次更新该数据
        parameterMap.put("areaCode", areaCode);
        createLisData(areaCode, currentTime);
    }

    /**
     * lis系统仓库信息同步接口修改（传入areaCode相同即可）
     */
    @Test(description = "lis系统仓库信息同步接口修改", groups = {AREA})
    private void test_020_03_pushWarehouses() {
        String currentTime = DateUtil.getCurrentTimeMillis();
        //获取新增时的code
        String areaCode =  parameterMap.get("areaCode").toString();
        createLisData(areaCode, currentTime);
    }

    /**
     * 创建lis同步数据
     * @param areaCode
     */
    private void createLisData(String areaCode, String timestamp) {
        String dateTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        String areaName = "自动化仓库" + dateTime;
        //参数需要的值
        String appKey = "0153f56c-bc94-4225-92a7-dd1e7fa32249";
        String appSecret = "3b79e4c9-305e-4bc4-ab50-9cd8b727b19f";
        String interfaceName = "pushWarehouses";
        //创建data
        List data = new ArrayList();
        Map map = new HashMap();
        map.put("areaName", areaName);
        map.put("areaCode", areaCode);
        map.put("orgName", "自动化" + dateTime);
        map.put("typeId", "3");
        map.put("manager", "1");
        map.put("telephone", "15800000000");
        map.put("enableStatus", "1");
        map.put("categoryId", "1");
        map.put("lonCenter", "116.334704");
        map.put("latCenter", "40.010718");
        map.put("rangeLength", "3");
        map.put("province", "北京市");
        map.put("city", "市辖区");
        map.put("county", "海淀区");
        map.put("positionAddress", "北京市海淀区至善路八街");
        map.put("remark", "备注");
        data.add(0, map);
        String sign = Md5Util.md5(appKey + appSecret + interfaceName + timestamp + appKey + JSON.toJSONString(data));
        //创建请求参数
        Map pathParam = new HashMap();
        pathParam.put("appKey", appKey);
        pathParam.put("interfaceName", interfaceName);
        pathParam.put("timestamp", timestamp);
        pathParam.put("data", JSON.toJSONString(data));
        pathParam.put("sign", sign);
        fileEntity.setPathParam(pathParam);
        //单独去调用，不需要加密
        createRequestSpec();
        createResponseSpec();
        //lis对接地址与平台地址不一致，重写地址
        response = responseSpecification.when().post(lisUrl + fileEntity.getPathUrl());
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {AREA})
    private void test_020_04_exportWarehouse() {
        startPerformTest();
    }

    /*****************库房时间设定管理*************************/

    /**
     * 获取库房时间设定管理表格列
     */
    @Test(description = "获取库房时间设定管理表格列", groups = {AREATIMESETTING})
    private void test_021_getTableColumnSettings() {
        fileEntity.getPathParam().put("userId", userId);
        //库房时间设定管理id是17
        fileEntity.getPathParam().put("moduleId", 17);
        startPerformTest();
    }

    /**
     * 根据orgid获取所有仓库
     */
    @Test(description = "根据orgid获取所有仓库", groups = {AREATIMESETTING})
    private void test_022_getWarehouseListByOrgIdList() {
        createEncrypt("[" + userId + "]", "bodyParam");
        startPerformTest();
        //获取到所有仓库，取最后一个仓库，也就是最新的仓库
        List list = response.jsonPath().get("result");
        saveResponseParameters("result["+ (list.size()-1) +"].areaId");
    }


    /**
     * 输入部门和最新获取的仓库组合查询
     */
    @Test(description = "输入部门和仓库组合查询", groups = {AREATIMESETTING})
    private void test_023_findByPage() {
        fileEntity.getBodyParam().put("settingDate",  DateUtil.getCurrentTime().getTimeInMillis());
        String[] orgIdList = new String[] {userId};
        //管理机构
        fileEntity.getBodyParam().put("orgIdList", orgIdList);
        //仓库
        fileEntity.getBodyParam().put("areaId", parameterMap.get("areaId"));
        //加密
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 库房时间设定管理默认查询
     */
    @Test(description = "库房时间设定管理默认查询", groups = {AREATIMESETTING})
    private void test_024_findByPage() {
        fileEntity.getBodyParam().put("settingDate", DateUtil.getCurrentTime().getTimeInMillis());
        //加密
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
        //存仓库id，仓库名称，垛口id，垛口名称
        saveResponseParameters("result.data[0].areaId", "result.data[0].crenelId",
                "result.data[0].areaName", "result.data[0].crenelName");
    }

    /**
     * 库房时间设定选择
     */
    @Test(description = "库房时间设定选择", groups = {AREATIMESETTING})
    private void test_025_judgeTimeCanSet() {
        Calendar call = DateUtil.getCurrentTime();
        Long selectStartTime = call.getTimeInMillis();
        Long selectEndTime = new Date(call.getTimeInMillis() + 1799 * 1000).getTime();
        fileEntity.getBodyParam().put("selectStartTime", selectStartTime);
        fileEntity.getBodyParam().put("selectEndTime", selectEndTime);
        fileEntity.getBodyParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getBodyParam().put("crenelId", parameterMap.get("crenelId"));
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 保存库房时间设定星期（全局设置）
     */
    @Test(description = "保存库房时间设定星期（全局设置）", groups = {AREATIMESETTING})
    private void test_026_saveSetting() {

        //设置为周三，1为周日，2为周一，3为周二，以此类推
        fileEntity.getBodyParam().put("weekList", new int[]{1});

        //创建warehouseNonIdleTimeSettingPOList
        List warehouseNonIdleTimeSettingPOList = new ArrayList();
        Map map = new HashMap();
        //仓库id和名称
        map.put("areaId", parameterMap.get("areaId"));
        map.put("areaName", parameterMap.get("areaName"));
        //垛口id和名称
        map.put("crenelId", parameterMap.get("crenelId"));
        map.put("crenelName", parameterMap.get("crenelName"));
        //选择的时间段
        Calendar call = DateUtil.getCurrentTime();
        Long selectStartTime = call.getTimeInMillis();
        Long selectEndTime = new Date(call.getTimeInMillis() + 1799 * 1000).getTime();
        map.put("selectStartTime", selectStartTime);
        map.put("selectEndTime", selectEndTime);
        warehouseNonIdleTimeSettingPOList.add(0, map);
        fileEntity.getBodyParam().put("warehouseNonIdleTimeSettingPOList", warehouseNonIdleTimeSettingPOList);

        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 保存库房时间设定（自定义设置）
     */
    //todo 设置未成功，原因为时间格式有问题，后期解决
    @Test(description = "保存库房时间设定（自定义设置）", groups = {AREATIMESETTING})
    private void test_027_saveSetting() {
        Calendar call = DateUtil.getCurrentTime();
        Long selectStartTime = call.getTimeInMillis();
        Long selectEndTime = new Date(call.getTimeInMillis() + 1799 * 1000).getTime();
        //创建settingDateList
        Long[] settingDateList = new Long[]{selectStartTime, selectStartTime};
        fileEntity.getBodyParam().put("settingDateList", settingDateList);
        //创建warehouseNonIdleTimeSettingPOList
        List warehouseNonIdleTimeSettingPOList = new ArrayList();
        Map map = new HashMap();
        //仓库id和名称
        map.put("areaId", parameterMap.get("areaId"));
        map.put("areaName", parameterMap.get("areaName"));
        //垛口id和名称
        map.put("crenelId", parameterMap.get("crenelId"));
        map.put("crenelName", parameterMap.get("crenelName"));
        //选择的时间段
        map.put("selectStartTime", selectStartTime);
        map.put("selectEndTime", selectEndTime);
        warehouseNonIdleTimeSettingPOList.add(0, map);
        fileEntity.getBodyParam().put("warehouseNonIdleTimeSettingPOList", warehouseNonIdleTimeSettingPOList);

        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }


    /***********************客户自提预约********************/

    /**
     * 获取客户自提预约列表格
     */
    @Test(description = "获取客户自提预约列表格", groups = {CUSTOMERSBOOKING})
    private void test_028_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "24");
        startPerformTest();
    }

    /**
     * 获取运单来源数据字典
     */
    @Test(description = "获取运单来源数据字典", groups = {CUSTOMERSBOOKING})
    private void test_029_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "waybill_source");
        startPerformTest();
    }

    /**
     * 组合查询客户自提预约
     */
    @Test(description = "组合查询客户自提预约", groups = {CUSTOMERSBOOKING})
    private void test_030_findByPage() {
        //运单来源
        fileEntity.getPathParam().put("waybillSource", "1");
        //预约单状态
        fileEntity.getPathParam().put("appointmentStatus", "1");
        fileEntity.getPathParam().put("waybillCode", "运单编号");
        fileEntity.getPathParam().put("moduleId", "24");
        fileEntity.getPathParam().put("appointmentOrderCode", "预约单号");
        fileEntity.getPathParam().put("customerName", "客户名称");
        startPerformTest();
    }


/***新增运单预约信息开始****/
    /**
     * 获取发货仓库
     */
    @Test(description = "获取发货仓库", groups = {CUSTOMERSBOOKING})
    private void test_031_getWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> WarehouseList = response.jsonPath().get();
        //保存发货仓库第一条
        parameterMap.put("WarehouseAreaId", WarehouseList.get(0).get("areaId"));
    }

    /**
     * 获取收货仓库
     */
    @Test(description = "获取收货仓库", groups = {CUSTOMERSBOOKING})
    private void test_032_getReceiveWarehouseList() {
        startPerformTest();
        List<Map<String, Object>> ReceiveWarehouseList = response.jsonPath().get();
        //保存发货收货仓库第一条
        parameterMap.put("ReceiveWarehouseAreaId", ReceiveWarehouseList.get(0).get("areaId"));
    }
    /**
     * 根据发货仓库获取仓库数据
     */
    @Test(description = "根据发货仓库获取仓库数据", groups = {CUSTOMERSBOOKING})
    private void test_033_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("WarehouseAreaId"));
        startPerformTest();
        parameterMap.put("WarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 根据收货仓库获取仓库数据
     */
    @Test(description = "根据收货仓库获取仓库数据", groups = {CUSTOMERSBOOKING})
    private void test_034_getWarehouseByAreaId() {
        fileEntity.getPathParam().put("areaId", parameterMap.get("ReceiveWarehouseAreaId"));
        startPerformTest();
        parameterMap.put("ReceiveWarehouseArea", response.jsonPath().get("result"));
    }

    /**
     * 新增保存出库运单
     */
    @Test(description = "新增保存出库运单", groups = {CUSTOMERSBOOKING})
    private void test_035_saveWaybill() {
        createWaybill();
        startPerformTest();
    }

    /**
     * 创建新增出库单
     */
    private void createWaybill() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getBodyParam().put("waybillCode", "ZDHKHYY" + currentTime);
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
/***新增运单预约信息完成****/

    /**
     * 默认查询客户自提预约
     */
    @Test(description = "默认查询客户自提预约", groups = {CUSTOMERSBOOKING})
    private void test_036_findByPage() {
        fileEntity.getPathParam().put("moduleId", "24");
        startPerformTest();
        //存仓库id，垛口id，运单id,预约单号
        saveResponseParameters("result.data[0].areaId",
                "result.data[0].waybillId",
                "result.data[0].appointmentOrderCode");
    }

    /**
     * 点击预约获取可选择垛口
     */
    @Test(description = "点击预约获取可选择垛口", groups = {CUSTOMERSBOOKING})
    private void test_037_findCrenelListByWarehouse() {
        fileEntity.getPathParam().put("warehouseId", parameterMap.get("areaId"));
        startPerformTest();
    }

    /**
     * 点击预约获取仓库和垛口进行显示
     */
    @Test(description = "点击预约获取仓库和垛口进行显示", groups = {CUSTOMERSBOOKING})
    private void test_038_findFreeByAreaIdAndSettingTime() {
        Long[] dateTime = DateUtil.getTimeListDay(0, 0);
        fileEntity.getBodyParam().put("areaId", parameterMap.get("areaId"));
        fileEntity.getBodyParam().put("settingDate", dateTime[0]);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
        saveResponseParameters("result[0].crenelId");
    }

    /**
     * 提交预约
     */
    @Test(description = "提交预约", groups = {CUSTOMERSBOOKING})
    private void test_039_appointmentSubmit() {
        Calendar call = DateUtil.getCurrentTime();
        Long startTime = call.getTimeInMillis();
        Long endTime = new Date(call.getTimeInMillis() + 1799 * 1000).getTime();
        //创建appointmentDateRange
        Long[] appointmentDateRange = new Long[]{startTime, endTime};
        fileEntity.getBodyParam().put("waybillIds", parameterMap.get("waybillId"));
        //选择的垛口id
        fileEntity.getBodyParam().put("crenelId", parameterMap.get("crenelId"));
        fileEntity.getBodyParam().put("appointmentDateRange", appointmentDateRange);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 审核驳回
     */
    @Test(description = "审核驳回", groups = {CUSTOMERSBOOKING})
    private void test_040_reviewRejectSubmit() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        fileEntity.getPathParam().put("appointmentReviewRemark", "预约驳回");
        startPerformTest();
    }

    /**
     * 再次提交预约
     */
    @Test(description = "再次提交预约", groups = {CUSTOMERSBOOKING})
    private void test_041_appointmentSubmit() {
        Calendar call = DateUtil.getCurrentTime();
        Long startTime = call.getTimeInMillis();
        Long endTime = new Date(call.getTimeInMillis() + 1799 * 1000).getTime();
        //创建appointmentDateRange
        Long[] appointmentDateRange = new Long[]{startTime, endTime};
        fileEntity.getBodyParam().put("waybillIds", parameterMap.get("waybillId"));
        fileEntity.getBodyParam().put("crenelId", parameterMap.get("crenelId"));
        fileEntity.getBodyParam().put("appointmentDateRange", appointmentDateRange);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 预约审核通过
     */
    @Test(description = "预约审核通过", groups = {CUSTOMERSBOOKING})
    private void test_042_reviewPassSubmit() {
        fileEntity.getPathParam().put("waybillId", parameterMap.get("waybillId"));
        startPerformTest();
    }

    /**
     * 查看自提预约操作记录详情
     */
    @Test(description = "查看自提预约操作记录详情", groups = {CUSTOMERSBOOKING})
    private void test_043_findActionRecordByAppointmentOrderCode() {
        fileEntity.getPathParam().put("appointmentOrderCode", parameterMap.get("appointmentOrderCode"));
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {CUSTOMERSBOOKING})
    private void test_044_exportAppointment() {
        startPerformTest();
    }


    /************************************库房预约看板************************/

    /**
     * 获取库房预约看板列表格
     */
    @Test(description = "获取库房预约看板列表格", groups = {AREABOOKING})
    private void test_045_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "42");
        startPerformTest();
    }

    /**
     * 获取管理机构
     */
    @Test(description = "获取管理机构", groups = {AREABOOKING})
    private void test_046_findOrganizationTreeByOrgId() {
        startPerformTest();
    }

    /**
     * 根据管理机构获取所有仓库
     */
    @Test(description = "根据管理机构获取所有仓库", groups = {AREABOOKING})
    private void test_047_getWarehouseListByOrgIdList() {
        createEncrypt("[" + userId + "]", "bodyParam");
        startPerformTest();
        //获取到所有仓库，取最后一个仓库，也就是最新的仓库
        List list = response.jsonPath().get("result");
        saveResponseParameters("result["+ (list.size()-1) +"].areaId");
    }

    /**
     * 组合查询库房预约看板
     */
    @Test(description = "组合查询库房预约看板", groups = {AREABOOKING})
    private void test_048_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(0, 0);
        fileEntity.getBodyParam().put("settingDate", dateTime[0]);
        fileEntity.getBodyParam().put("orgIdList", new String[]{orgId});
        fileEntity.getBodyParam().put("areaId", parameterMap.get("areaId"));
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }
    /**
     * 默认查询库房预约看板
     */
    @Test(description = "默认查询库房预约看板", groups = {AREABOOKING})
    private void test_049_findByPage() {
        Long[] dateTime = DateUtil.getTimeListDay(0, 0);
        fileEntity.getBodyParam().put("settingDate", dateTime[0]);
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }
}