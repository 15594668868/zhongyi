package testcase;

import com.alibaba.fastjson.JSON;
import org.testng.Assert;
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
 * @description: 智能柜业务管理
 * @author: guozhixiong
 * @time: 2020/9/17 10:09
 */
public class SmartCabinetTestCase extends BaseTest {
    //excel测试用例
    private static String fileName = "中移智能柜业务管理.xls";

    //货柜管理
    private static final String CABINET = "cabinet";
    //渠道商管理
    private static final String CHANNEL = "channel";
    //商品信息管理
    private static final String COMMODITY = "commodity";
    //入库单管理
    private static final String INBOUNDORDER = "inboundOrder";
    //出库单管理
    private static final String OUTBOUNDORDER = "outboundOrder";

    /**
     * 每个用例之前拿excel文件
     *
     * @param method
     */
    @BeforeMethod(alwaysRun = true)
    private void getFileRow(Method method) {
        HashMap<String, String> fileRow = new HashMap<String, String>();
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


    /*******************************货柜管理*******************/

    /**
     * 获取货柜管理列表
     */
    @Test(description = "获取货柜管理列表", groups = {CABINET})
    private void test_001_getTableColumnSettings() {
        fileEntity.getPathParam().put("userId", userId);
        fileEntity.getPathParam().put("moduleId", "59");
        startPerformTest();
    }

    /**
     * 查询状态数据字典
     */
    @Test(description = "查询状态数据字典", groups = {CABINET})
    private void test_002_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "is_able_use");
        startPerformTest();
        parameterMap.put("ableUseId", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 查询盘库状态数据字典
     */
    @Test(description = "查询盘库状态数据字典", groups = {CABINET})
    private void test_003_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "state");
        startPerformTest();
        parameterMap.put("stateId", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 查询组织架构
     */
    @Test(description = "查询组织架构", groups = {CABINET})
    private void test_004_findOrganizationTreeByOrgId() {
        fileEntity.getPathParam().put("orgType", "0");
        startPerformTest();
    }

    /**
     * 组合查询
     */
    @Test(description = "组合查询", groups = {CABINET})
    private void test_005_findByPage() {
        fileEntity.getPathParam().put("cabCode", "货柜编号");
        fileEntity.getPathParam().put("cabName", "货柜名称");
        fileEntity.getPathParam().put("isAbleUse", parameterMap.get("ableUseId")); //状态
        fileEntity.getPathParam().put("state", parameterMap.get("stateId")); //盘库状态
        fileEntity.getPathParam().put("moduleId", "59");
        fileEntity.getPathParam().put("userId", userId);
        fileEntity.getPathParam().put("orgId", orgId);
        startPerformTest();
    }

    /**
     * 获取货柜管理员
     */
    @Test(description = "获取货柜管理员", groups = {CABINET})
    private void test_006_initZnhgManagerUserList() {
        startPerformTest();
        saveResponseParameters("result[0].userId");
    }

    /**
     * 获取货柜编号
     */
    @Test(description = "获取货柜编号", groups = {CABINET})
    private void test_007_initSynCabList() {
        fileEntity.getPathParam().put("cabCode", "");
        startPerformTest();
        saveResponseParameters("result[0].cabCode");
    }

    /**
     * 查询单个货柜详细信息
     */
    @Test(description = "查询单个货柜详细信息", groups = {CABINET})
    private void test_008_getSynCabByCabCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))) {
            fileEntity.getPathParam().put("cabCode", parameterMap.get("cabCode"));
            startPerformTest();
            //货柜地址
            parameterMap.put("address", response.jsonPath().get("result.address"));
            //总库位数
            parameterMap.put("useableCellCount", response.jsonPath().get("result.useableCellCount"));
        } else {
            log.info("没有查找到货柜信息，未执行查询单个货柜详细信息方法test_008_getSynCabByCabCode");
            Assert.assertTrue(false);
        }
    }

    /**
     * 更新
     */
    @Test(description = "更新", groups = {CABINET})
    private void test_009_findCabinetsByCabCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))) {
            fileEntity.getPathParam().put("cabCode", parameterMap.get("cabCode"));
            startPerformTest();
        } else {
            log.info("没有查找到货柜信息，未执行更新方法test_009_findCabinetsByCabCode");
            Assert.assertTrue(false);
        }
    }

    /**
     * 新增保存货柜
     */
    @Test(description = "新增保存货柜", groups = {CABINET})
    private void test_010_saveZnhgCab() {
        //如果没有找到货柜编号或者货柜管理员用户，不新增保存
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode")) && StringUtil.isNotEmpty(parameterMap.get("userId"))) {
            String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
            //货柜编号
            fileEntity.getPathParam().put("cabCode", parameterMap.get("cabCode"));
            //货柜管理员id
            fileEntity.getPathParam().put("managerIds", parameterMap.get("userId"));
            //货柜地址
            fileEntity.getPathParam().put("address", parameterMap.get("address"));
            //货柜总库位数
            fileEntity.getPathParam().put("useableCellCount", parameterMap.get("useableCellCount"));
            //货柜名称
            fileEntity.getPathParam().put("cabName", "自动化" + currentTime);
            //状态：启用
            fileEntity.getPathParam().put("isAbleUse", "1");
            fileEntity.getPathParam().put("remark", "备注");
            fileEntity.getPathParam().put("moduleId", "59");
            fileEntity.getPathParam().put("orgId", orgId);
            startPerformTest();
        } else {
            log.info("没有查找到货柜信息和管理员信息，未执行新增保存货柜方法test_010_saveZnhgCab");
            Assert.assertTrue(false);
        }
    }

    /**
     * 判断货柜编号是否存在
     */
    @Test(description = "判断货柜编号是否存在", groups = {CABINET})
    private void test_011_findExistCabCode() {
        fileEntity.getPathParam().put("cabCode", "F2020042000053");
        startPerformTest();
    }

    /**
     * 判断货柜名称是否存在
     */
    @Test(description = "判断货柜名称是否存在", groups = {CABINET})
    private void test_012_findExistCabName() {
        fileEntity.getPathParam().put("cabName", "货柜名称");
        startPerformTest();
    }


    /**
     * 默认查询
     */
    @Test(description = "默认查询", groups = {CABINET})
    private void test_013_findByPage() {
        fileEntity.getPathParam().put("moduleId", "59");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        saveResponseParameters("result.data[0].cabCode", "result.data[0].cabName");
    }


    /**
     * 获取货柜详情
     */
    @Test(description = "获取货柜详情", groups = {CABINET})
    private void test_014_findSynCabDetail() {
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))
                && StringUtil.isNotEmpty(parameterMap.get("cabName"))) {
            fileEntity.getPathParam().put("cabCode", parameterMap.get("cabCode"));
            fileEntity.getPathParam().put("cabName", parameterMap.get("cabName"));
            startPerformTest();
        } else {
            log.info("没有货柜数据，未执行获取货柜详情方法test_010_findSynCabDetail");
            Assert.assertTrue(false);
        }
    }

    /**
     * 盘库
     */
    @Test(description = "盘库", groups = {CABINET})
    private void test_015_createProcessManualStocktaking() {
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))) {
            String[] cabCodeList = new String[]{parameterMap.get("cabCode").toString()};
            fileEntity.getBodyParam().put("cabCodeList", cabCodeList);
            createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
            startPerformTest();
        } else {
            log.info("没有货柜数据，未执行获取货柜详情方法test_010_findSynCabDetail");
            Assert.assertTrue(false);
        }
    }


    /************************渠道商管理************************/

    /**
     * 获取渠道商管理列
     */
    @Test(description = "获取渠道商管理列", groups = {CHANNEL})
    private void test_016_getTableColumnSettings() {
        fileEntity.getPathParam().put("userId", userId);
        fileEntity.getPathParam().put("moduleId", "64");
        startPerformTest();
    }

    /**
     * 获取是否有效数据字典
     */
    @Test(description = "获取是否有效数据字典", groups = {CHANNEL})
    private void test_017_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "whether");
        startPerformTest();
        saveResponseParameters("result[0].dicId");
    }

    /**
     * 组合查询渠道商管理
     */
    @Test(description = "组合查询渠道商管理", groups = {CHANNEL})
    private void test_018_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("idCard", "身份证号");
        fileEntity.getPathParam().put("channelName", "客户名称");
        fileEntity.getPathParam().put("telephone", "手机号");
        fileEntity.getPathParam().put("moduleId", "64");
        fileEntity.getPathParam().put("channelCode", "用户名");
        fileEntity.getPathParam().put("isAbleUse", parameterMap.get("dicId"));
        startPerformTest();
    }

    /**
     * 获取所属分公司
     */
    @Test(description = "获取所属分公司", groups = {CHANNEL})
    private void test_019_findDisabledOrganizations() {
        startPerformTest();
        List<Map<String, Object>> children = response.jsonPath().get("result[0].children");
        String labelId = getLabelId(children);
        //没有找到西安科捷，则取第一个
        if (StringUtil.isEmpty(labelId)) {
            labelId = response.jsonPath().get("result[0].children[0].id");
        }
        parameterMap.put("labelId", labelId);
    }

    /**
     * 根据children拿到西安科捷的id
     *
     * @param children
     * @return
     */
    private String getLabelId(List<Map<String, Object>> children) {
        String labelId = null;
        for (Map map : children) {
            //只要lavelId为null就继续查找，不为null再查找重新进入方法未找到会导致labelId重新置空
            if (StringUtil.isEmpty(labelId)) {
                //获取所有子部门是否存在西安科捷
                if (StringUtil.equals("西安科捷", map.get("label").toString())) {
                    labelId = map.get("id").toString();
                    break;
                    //不存在西安科捷，则判断该子部门是否还有子部门，如果有子部门，再判断其子部门是否有西安科捷
                } else {
                    if (StringUtil.isNotEmpty(map.get("children"))) {
                        List<Map<String, Object>> list = (List) map.get("children");
                        labelId = getLabelId(list);
                    }
                }
            }
        }
        return labelId;
    }

    /**
     * 根据orgid获取可操作的货柜
     */
    @Test(description = "根据orgid获取可操作的货柜", groups = {CHANNEL})
    private void test_020_findAbleUseCabListByOrgId() {
        fileEntity.getPathParam().put("orgId", parameterMap.get("labelId"));
        startPerformTest();
        parameterMap.put("cabId", response.jsonPath().get("result[0].cabId"));
    }

    /**
     * 根据orgid获取选择负责渠道经理
     */
    @Test(description = "根据orgid获取选择负责渠道经理", groups = {CHANNEL})
    private void test_021_initChannelUserList() {
        fileEntity.getPathParam().put("orgId", parameterMap.get("labelId"));
        startPerformTest();
        parameterMap.put("userId", response.jsonPath().get("result[0].userId"));
    }


    /**
     * 获取所有角色名
     */
    @Test(description = "获取所有角色名", groups = {CHANNEL})
    private void test_022_getUserAllRole() {
        fileEntity.getPathParam().put("roleProperty", "1");
        startPerformTest();
        List<Map<String, Object>> result = response.jsonPath().getList("result");
        for (Map map : result) {
            //获取渠道商角色id
            if (StringUtil.equals("渠道商", map.get("roleName").toString())) {
                parameterMap.put("roleId", map.get("roleId"));
                break;
            }
        }
    }

    /**
     * 根据客户名称获取用户名
     */
    @Test(description = "根据客户名称获取用户名", groups = {CHANNEL})
    private void test_023_getCodeByChannelName() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        String channelName =  "自动化" + currentTime;
        parameterMap.put("channelName", channelName);
        fileEntity.getPathParam().put("channelName", channelName);
        startPerformTest();
        //存储获取到的用户名
        parameterMap.put("channelCode", response.jsonPath().get("result"));
    }

    /**
     * 新增渠道商
     */
    @Test(description = "新增渠道商", groups = {CHANNEL})
    private void test_024_saveZnhgChannel() {
        //可操作货柜和渠道经理存在再新增渠道商
        if (StringUtil.isNotEmpty(parameterMap.get("cabId"))
                && StringUtil.isNotEmpty(parameterMap.get("userId"))) {
            String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
            fileEntity.getPathParam().put("address", "地址");
            fileEntity.getPathParam().put("isHaveAccount", "0");
            //可操作货柜id
            fileEntity.getPathParam().put("operateCabIds", parameterMap.get("cabId"));
            fileEntity.getPathParam().put("bossId", "BOSS工号");
            //角色名
            fileEntity.getPathParam().put("roleId", parameterMap.get("roleId"));
            //折扣
            fileEntity.getPathParam().put("discount", "10");
            //手机号
            fileEntity.getPathParam().put("telephone", "158" + currentTime.substring(9, currentTime.length()));
            //是否有效：是
            fileEntity.getPathParam().put("isAbleUse", "1");
            fileEntity.getPathParam().put("remark", "备注");
            //负责渠道经理id
            fileEntity.getPathParam().put("managerId", parameterMap.get("userId"));
            fileEntity.getPathParam().put("orgId", parameterMap.get("labelId"));
            fileEntity.getPathParam().put("linkman", "联系人");
            fileEntity.getPathParam().put("isForbidden", "1");
            //客户名称
            fileEntity.getPathParam().put("channelName", parameterMap.get("channelName"));
            //总额度
            fileEntity.getPathParam().put("totalQuota", "123");
            fileEntity.getPathParam().put("moduleId", "64");
            //获取到的用户名
            fileEntity.getPathParam().put("channelCode", parameterMap.get("channelCode"));
            startPerformTest();
        } else {
            log.info("没有找到货柜和渠道经理，未执行新增渠道商方法test_024_saveZnhgChannel");
            Assert.assertTrue(false);
        }

    }


    /**
     * 判断用户名是否重复
     */
    @Test(description = "判断用户名是否重复", groups = {CHANNEL})
    private void test_025_findExistChannelCode() {
        fileEntity.getPathParam().put("channelCode", "用户名");
        startPerformTest();
    }


    /**
     * 判断手机号是否重复
     */
    @Test(description = "判断手机号是否重复", groups = {CHANNEL})
    private void test_026_findExistChannelTelephone() {
        fileEntity.getPathParam().put("telephone", "15800000000");
        startPerformTest();
    }

    /**
     * 默认查询渠道商管理
     */
    @Test(description = "默认查询渠道商管理", groups = {CHANNEL})
    private void test_027_findByPage() {
        startPerformTest();
        saveResponseParameters("result.data[0].channelId");
    }

    /**
     * 根据id获取渠道商信息
     */
    @Test(description = "根据id获取渠道商信息", groups = {CHANNEL})
    private void test_028_getZnhgChannelById() {
        if (StringUtil.isNotEmpty(parameterMap.get("channelId"))) {
            fileEntity.getPathParam().put("channelId", parameterMap.get("channelId"));
            startPerformTest();
        } else {
            log.info("没有找到渠道商数据，未执行根据id获取渠道商信息方法test_028_getZnhgChannelById");
            Assert.assertTrue(false);
        }
    }

    /**
     * 获取平台账号
     */
    @Test(description = "获取平台账号", groups = {CHANNEL})
    private void test_029_generateUserCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("channelId"))) {
            fileEntity.getPathParam().put("channelId", parameterMap.get("channelId"));
            startPerformTest();
            parameterMap.put("userCode", response.jsonPath().get("result"));
        } else {
            log.info("没有找到渠道商数据，未执行获取平台账号方法test_029_generateUserCode");
            Assert.assertTrue(false);
        }
    }

    /**
     * 生成平台账号
     */
    @Test(description = "生成平台账号", groups = {CHANNEL})
    private void test_030_generateAccount() {
        if (StringUtil.isNotEmpty(parameterMap.get("userCode"))) {
            fileEntity.getPathParam().put("channelId", parameterMap.get("channelId"));
            fileEntity.getPathParam().put("userCode", parameterMap.get("userCode"));
            startPerformTest();
        } else {
            log.info("没有找到渠道商数据，未获取到usercode，未执行生成平台账号方法test_030_generateAccount");
            Assert.assertTrue(false);
        }
    }

    /**
     * 额度维护保存
     */
    @Test(description = "额度维护保存", groups = {CHANNEL})
    private void test_031_quotaMaintain() {
        if (StringUtil.isNotEmpty(parameterMap.get("channelId"))) {
            fileEntity.getPathParam().put("channelId", parameterMap.get("channelId"));
            //额度
            fileEntity.getPathParam().put("availableQuota", "100");
            startPerformTest();
        } else {
            log.info("没有找到渠道商数据，未执行额度维护保存方法test_031_quotaMaintain");
            Assert.assertTrue(false);
        }
    }

    /**
     * 禁用
     */
    @Test(description = "禁用", groups = {CHANNEL})
    private void test_032_forbiddenByChannelId() {
        if (StringUtil.isNotEmpty(parameterMap.get("channelId"))) {
            fileEntity.getPathParam().put("channelId", parameterMap.get("channelId"));
            startPerformTest();
        } else {
            log.info("没有找到渠道商数据，未执行禁用方法test_032_forbiddenByChannelId");
            Assert.assertTrue(false);
        }
    }

    /**
     * 启用
     */
    @Test(description = "启用", groups = {CHANNEL})
    private void test_033_startUpUseByChannelId() {
        if (StringUtil.isNotEmpty(parameterMap.get("channelId"))) {
            fileEntity.getPathParam().put("channelId", parameterMap.get("channelId"));
            startPerformTest();
        } else {
            log.info("没有找到渠道商数据，未执行启用方法test_033_startUpUseByChannelId");
            Assert.assertTrue(false);
        }
    }

    /**
     * 重置密码
     */
    @Test(description = "重置密码", groups = {CHANNEL})
    private void test_034_resetPwd() {
        if (StringUtil.isNotEmpty(parameterMap.get("channelId"))) {
            fileEntity.getPathParam().put("channelId", parameterMap.get("channelId"));
            startPerformTest();
        } else {
            log.info("没有找到渠道商数据，未执行重置密码方法test_034_resetPwd");
            Assert.assertTrue(false);
        }
    }

    /*******************************商品信息管理************************/

    /**
     * 获取商品信息管理列
     */
    @Test(description = "获取商品信息管理列", groups = {COMMODITY})
    private void test_035_getTableColumnSettings() {
        fileEntity.getPathParam().put("userId", userId);
        fileEntity.getPathParam().put("moduleId", "60");
        startPerformTest();
    }

    /**
     * 组合查询
     */
    @Test(description = "组合查询", groups = {COMMODITY})
    private void test_036_findByPage() {
        fileEntity.getPathParam().put("code", "商品编号");
        fileEntity.getPathParam().put("cabCode", "货柜编号");
        fileEntity.getPathParam().put("name", "商品名称");
        fileEntity.getPathParam().put("commoditySn", "串码");
        fileEntity.getPathParam().put("moduleId", "60");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
    }

    /**
     * 获取商品类目数据字典
     */
    @Test(description = "获取商品类目数据字典", groups = {COMMODITY})
    private void test_037_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "commodity_category");
        startPerformTest();
    }

    /**
     * 从lis系统根据商品编号获取商品信息
     */
    @Test(description = "从lis系统根据商品编号获取商品信息", groups = {COMMODITY})
    private void test_038_getSynCommodityByCode() {
        fileEntity.getPathParam().put("code", "商品编号");
        startPerformTest();
    }

    /**
     * 默认查询
     */
    @Test(description = "默认查询", groups = {COMMODITY})
    private void test_039_findByPage() {
        fileEntity.getPathParam().put("moduleId", "60");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        parameterMap.put("commodityId", response.jsonPath().get("result.data[0].commodityId"));
        parameterMap.put("code", response.jsonPath().get("result.data[0].code"));
    }

    /**
     * 查看商品价格
     */
    @Test(description = "查看商品价格", groups = {COMMODITY})
    private void test_040_findAllByCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("code"))) {
            fileEntity.getPathParam().put("code", parameterMap.get("code"));
            startPerformTest();
        } else {
            log.info("没有找到商品数据，未执行查看商品价格方法test_040_findAllByCode");
            Assert.assertTrue(false);
        }
    }

    /**
     * 获取商品详情
     */
    @Test(description = "获取商品详情", groups = {COMMODITY})
    private void test_041_getByCommodityId() {
        if (StringUtil.isNotEmpty(parameterMap.get("commodityId"))) {
            fileEntity.getPathParam().put("commodityId", parameterMap.get("commodityId"));
            startPerformTest();
        } else {
            log.info("没有找到商品数据，未执行获取商品详情方法test_041_getByCommodityId");
            Assert.assertTrue(false);
        }
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {COMMODITY})
    private void test_042_exportZnhgCommoditySn() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
    }

    /**********************************入库单管理********************/
    /**
     * 查询入库单管理列
     */
    @Test(description = "查询入库单管理列", groups = {INBOUNDORDER})
    private void test_043_getTableColumnSettings() {
        fileEntity.getPathParam().put("userId", userId);
        fileEntity.getPathParam().put("moduleId", "61");
        startPerformTest();
    }

    /**
     * 获取入库单状态
     */
    @Test(description = "获取入库单状态", groups = {INBOUNDORDER})
    private void test_044_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "in_bill_status");
        startPerformTest();
        parameterMap.put("billStatus", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 获取是否在草稿箱状态
     */
    @Test(description = "获取是否在草稿箱状态", groups = {INBOUNDORDER})
    private void test_045_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "whether");
        startPerformTest();
        parameterMap.put("saveType", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 组合查询
     */
    @Test(description = "组合查询", groups = {INBOUNDORDER})
    private void test_046_findByPage() {
        fileEntity.getPathParam().put("cabCode", "货柜编号");
        fileEntity.getPathParam().put("inboundOrderCode", "入库单号");
        fileEntity.getPathParam().put("moduleId", "61");
        fileEntity.getPathParam().put("userId", userId);
        //入库单状态
        fileEntity.getPathParam().put("billStatus", parameterMap.get("billStatus"));
        //是否在草稿箱
        fileEntity.getPathParam().put("saveType", parameterMap.get("saveType"));
        startPerformTest();
    }

    /**
     * 获取货柜
     */
    @Test(description = "获取货柜", groups = {INBOUNDORDER})
    private void test_047_initCabList() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        parameterMap.put("cabCode", response.jsonPath().get("result[0].cabCode"));
    }

    /**
     * 获取入库码接收人
     */
    @Test(description = "获取入库码接收人", groups = {INBOUNDORDER})
    private void test_048_getReceiverListByCabCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))) {
            fileEntity.getPathParam().put("cabCode", parameterMap.get("cabCode"));
            startPerformTest();
            parameterMap.put("userId", response.jsonPath().get("result[0].userId"));
        } else {
            log.info("没有找到货柜，未执行获取入库码接收人方法test_047_getReceiverListByCabCode");
            Assert.assertTrue(false);
        }
    }

    /**
     * 获取商品
     */
    @Test(description = "获取商品", groups = {INBOUNDORDER})
    private void test_049_initCommodityList() {
        startPerformTest();
        parameterMap.put("code", response.jsonPath().get("result[0].code"));
    }

    /**
     * 获取商品详情
     */
    @Test(description = "获取商品详情", groups = {INBOUNDORDER})
    private void test_050_getByCommodityCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("code"))) {
            fileEntity.getPathParam().put("code", parameterMap.get("code"));
            startPerformTest();
            parameterMap.put("commodityCode", response.jsonPath().get("result.code"));
            parameterMap.put("commodityName", response.jsonPath().get("result.name"));
        } else {
            log.info("没有找到商品，未执行获取商品详情方法test_049_getByCommodityCode");
            Assert.assertTrue(false);
        }
    }

    /**
     * 获取入库类型
     */
    @Test(description = "获取入库类型", groups = {INBOUNDORDER})
    private void test_051_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "inbound_type");
        startPerformTest();
        parameterMap.put("dicId", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 保存到草稿箱
     */
    @Test(description = "保存到草稿箱", groups = {INBOUNDORDER})
    private void test_052_saveInboundOrder() {
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))
                && StringUtil.isNotEmpty(parameterMap.get("userId"))) {
            String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
            //保存类型:保存到草稿箱
            fileEntity.getBodyParam().put("saveType", "1");
            //货柜code
            fileEntity.getBodyParam().put("cabCode", parameterMap.get("cabCode"));
            //入库类型
            fileEntity.getBodyParam().put("inboundType", parameterMap.get("dicId"));
            //入库码接收人id
            fileEntity.getBodyParam().put("operateCodeReceiverIds", parameterMap.get("userId"));
            //经办人(当前登录用户)
            fileEntity.getBodyParam().put("operateUserId", userId);
            fileEntity.getBodyParam().put("operateUserName", "管理员");
            //经办时间
            fileEntity.getBodyParam().put("operateTime", new Date());
            fileEntity.getBodyParam().put("remark", "备注");
            List<Map<String, Object>> commoditySnList = new ArrayList<>();
            //如果不存在商品编码和名称则不进行输入商品内容
            if (StringUtil.isNotEmpty(parameterMap.get("commodityCode"))
                    && StringUtil.isNotEmpty(parameterMap.get("commodityName"))) {
                Map commoditySnMap = new HashMap();
                //商品编码
                commoditySnMap.put("commodityCode", parameterMap.get("commodityCode"));
                //商品名称
                commoditySnMap.put("commodityName", parameterMap.get("commodityName"));
                //商品串码(15位纯数字)
                commoditySnMap.put("commoditySn", currentTime.substring(2, currentTime.length()));
                commoditySnList.add(commoditySnMap);
            }
            fileEntity.getBodyParam().put("commoditySnList", commoditySnList);
            createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
            startPerformTest();
        } else {
            log.info("没有找到货柜、入库码接收人，未执行保存到草稿箱方法test_051_saveInboundOrder");
            Assert.assertTrue(false);
        }
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {INBOUNDORDER})
    private void test_053_exportZnhgInboundOrder() {
        startPerformTest();
    }

    /**
     * 默认查询
     */
    @Test(description = "默认查询", groups = {INBOUNDORDER})
    private void test_054_findByPage() {
        fileEntity.getPathParam().put("moduleId", "61");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        parameterMap.put("inboundOrderId", response.jsonPath().get("result.data[0].inboundOrderId"));
    }

    /**
     * 根据id获取入库单详情
     */
    @Test(description = "根据id获取入库单详情", groups = {INBOUNDORDER})
    private void test_055_getVOByInboundOrderId() {
        fileEntity.getPathParam().put("inboundOrderId", parameterMap.get("inboundOrderId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑保存
     */
    @Test(description = "编辑保存", groups = {INBOUNDORDER})
    private void test_056_01_saveInboundOrder() {
        fileEntity.setBodyParam((Map) parameterMap.get("result"));
        //保存类型:保存替吉奥
        fileEntity.getBodyParam().put("saveType", "0");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 默认查询
     */
    @Test(description = "默认查询", groups = {INBOUNDORDER})
    private void test_056_02_findByPage() {
        fileEntity.getPathParam().put("moduleId", "61");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        parameterMap.put("inboundOrderId", response.jsonPath().get("result.data[0].inboundOrderId"));
        //入库码
        parameterMap.put("operateCode", response.jsonPath().get("result.data[0].operateCode"));
        parameterMap.put("inboundOrderCode", response.jsonPath().get("result.data[0].inboundOrderCode"));
        parameterMap.put("cabCode", response.jsonPath().get("result.data[0].cabCode"));
    }

    /**
     * 查询入库商品详情
     */
    @Test(description = "查询入库商品详情", groups = {INBOUNDORDER})
    private void test_057_findDetailByInboundOrderId() {
        fileEntity.getPathParam().put("inboundOrderId", parameterMap.get("inboundOrderId"));
        startPerformTest();
    }

    /**
     * 重新发送入库码
     */
    @Test(description = "重新发送入库码", groups = {INBOUNDORDER})
    private void test_058_sendOperateCode() {
        fileEntity.getPathParam().put("operateCode", parameterMap.get("operateCode"));
        fileEntity.getPathParam().put("receiverIds", userId);
        startPerformTest();
    }

    /**
     * 关单
     */
    @Test(description = "关单", groups = {INBOUNDORDER})
    private void test_059_inCloseOrderToFengLink() {
        fileEntity.getBodyParam().put("inbound_order_code", parameterMap.get("inboundOrderCode"));
        fileEntity.getBodyParam().put("cab_code", parameterMap.get("cabCode"));
        fileEntity.getBodyParam().put("method", "close");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }


    /******************出库单管理************************/

    /**
     * 获取出库单管理列
     */
    @Test(description = "获取出库单管理列", groups = {OUTBOUNDORDER})
    private void test_060_getTableColumnSettings() {
        fileEntity.getPathParam().put("userId", userId);
        fileEntity.getPathParam().put("moduleId", "62");
        startPerformTest();
    }

    /**
     * 获取出库单状态
     */
    @Test(description = "获取出库单状态", groups = {OUTBOUNDORDER})
    private void test_061_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "out_bill_status");
        startPerformTest();
        parameterMap.put("billStatus", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 获取是否在草稿箱状态
     */
    @Test(description = "获取是否在草稿箱状态", groups = {OUTBOUNDORDER})
    private void test_062_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "whether");
        startPerformTest();
        parameterMap.put("saveType", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 组合查询
     */
    @Test(description = "组合查询", groups = {OUTBOUNDORDER})
    private void test_063_findByPage() {
        fileEntity.getPathParam().put("cabCode", "货柜编号");
        fileEntity.getPathParam().put("inboundOrderCode", "出库单号");
        fileEntity.getPathParam().put("moduleId", "62");
        fileEntity.getPathParam().put("userId", userId);
        //入库单状态
        fileEntity.getPathParam().put("billStatus", parameterMap.get("billStatus"));
        //是否在草稿箱
        fileEntity.getPathParam().put("saveType", parameterMap.get("saveType"));
        startPerformTest();
    }

    /**
     * 点击新增获取角色名和id
     */
    @Test(description = "检查角色名和id", groups = {OUTBOUNDORDER})
    private void test_064_findByRoleNameAndUserId() {
        fileEntity.getPathParam().put("roleName", "渠道商");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
    }

    /**
     * 点击新增检查Qds顺序
     */
    @Test(description = "点击新增检查Qds顺序", groups = {OUTBOUNDORDER})
    private void test_065_checkIsQdsjlOrder() {
        startPerformTest();
    }

    /**
     * 点击新增检查Ptyh顺序
     */
    @Test(description = "点击新增检查Ptyh顺序", groups = {OUTBOUNDORDER})
    private void test_066_checkIsPtyhOrder() {
        startPerformTest();
    }


    /**
     * 获取出库类型
     */
    @Test(description = "获取出库类型", groups = {OUTBOUNDORDER})
    private void test_067_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "outbound_type");
        startPerformTest();
        parameterMap.put("dicType", response.jsonPath().get("result[0].dicId"));
    }

    /**
     * 获取货柜
     */
    @Test(description = "获取货柜", groups = {OUTBOUNDORDER})
    private void test_068_initCabList() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        parameterMap.put("cabCode", response.jsonPath().get("result[0].cabCode"));
    }

    /**
     * 根据货柜编码获取出库码接收人
     */
    @Test(description = "根据货柜编码获取出库码接收人", groups = {OUTBOUNDORDER})
    private void test_069_getReceiverListByCabCode() {
        //cabCode 货柜编码
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))) {
            fileEntity.getPathParam().put("cabCode", parameterMap.get("cabCode"));
            startPerformTest();
            parameterMap.put("userName", response.jsonPath().get("result[0].userName"));
            parameterMap.put("userId", response.jsonPath().get("result[0].userId"));
        } else {
            log.info("没有找到货柜，未执行根据货柜编码获取出库码接收人方法test_069_getReceiverListByCabCode");
            Assert.assertTrue(false);
        }

    }

    /**
     * 根据货柜编码获取商品编码
     */
    @Test(description = "根据货柜编码获取商品编码", groups = {OUTBOUNDORDER})
    private void test_070_initCommodityListByCabCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))) {
            fileEntity.getPathParam().put("cabCode", parameterMap.get("cabCode"));
            startPerformTest();
            parameterMap.put("code", response.jsonPath().get("result[0].code"));
        } else {
            log.info("没有找到货柜，未执行根据货柜编码获取商品编码方法test_070_initCommodityListByCabCode");
            Assert.assertTrue(false);
        }
    }

    /**
     * 根据货柜编码获取所有商品串码
     */
    @Test(description = "根据货柜编码获取所有商品串码", groups = {OUTBOUNDORDER})
    private void test_071_initCommoditySnListByCabCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))) {
            fileEntity.getPathParam().put("cabCode", parameterMap.get("cabCode"));
            startPerformTest();
            parameterMap.put("commodityCode", response.jsonPath().get("result[0].commodityCode"));
        } else {
            log.info("没有找到货柜，未执行根据货柜编码获取所有商品串码方法test_071_initCommoditySnListByCabCode");
            Assert.assertTrue(false);
        }
    }

    /**
     * 根据商品编码获取商品详情
     */
    @Test(description = "根据商品编码获取商品详情", groups = {OUTBOUNDORDER})
    private void test_072_getByCommodityCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("code"))) {
            fileEntity.getPathParam().put("code", parameterMap.get("code"));
            startPerformTest();
        } else {
            log.info("没有找到货柜，未执行根据商品编码获取商品详情方法test_072_getByCommodityCode");
            Assert.assertTrue(false);
        }
    }

    /**
     * 根据商品编码和货柜编码获取该货柜下该商品的商品串码
     */
    @Test(description = "根据商品编码和货柜编码获取该货柜下该商品的商品串码", groups = {OUTBOUNDORDER})
    private void test_073_getByCommodityCodeAndCabCode() {
        if (StringUtil.isNotEmpty(parameterMap.get("commodityCode"))
                && StringUtil.isNotEmpty(parameterMap.get("cabCode"))) {
            fileEntity.getPathParam().put("commodityCode", parameterMap.get("commodityCode"));
            fileEntity.getPathParam().put("cabCode", parameterMap.get("cabCode"));
            startPerformTest();
            parameterMap.put("commoditySn", response.jsonPath().get("result[0].commoditySn"));
        } else {
            log.info("没有找到货柜，" +
                    "未执行根据商品编码和货柜编码获取该货柜下该商品的商品串码方法test_073_getByCommodityCodeAndCabCode");
            Assert.assertTrue(false);
        }
    }


    /**
     * 根据commoditySn获取商品串码详情
     */
    @Test(description = "根据commoditySn获取商品串码详情", groups = {OUTBOUNDORDER})
    private void test_074_getByCommoditySn() {
        if (StringUtil.isNotEmpty(parameterMap.get("commoditySn"))) {
            fileEntity.getPathParam().put("commoditySn", parameterMap.get("commoditySn"));
            startPerformTest();
            parameterMap.put("commoditySnId", response.jsonPath().get("result.commoditySnId"));
            parameterMap.put("commodityCode", response.jsonPath().get("result.commodityCode"));
            parameterMap.put("commodityName", response.jsonPath().get("result.commodityName"));
            parameterMap.put("commoditySn", response.jsonPath().get("result.commoditySn"));
            parameterMap.put("cabCode", response.jsonPath().get("result.cabCode"));
            parameterMap.put("cellCode", response.jsonPath().get("result.cellCode"));
            parameterMap.put("inboundDatetime", response.jsonPath().get("result.inboundDatetime"));
        } else {
            log.info("没有找到商品串码详情，" +
                    "未执行根据commoditySn获取商品串码详情方法test_074_getByCommoditySn");
            Assert.assertTrue(false);
        }
    }


    /**
     * 出库单保存到草稿箱
     */
    @Test(description = "出库单保存到草稿箱", groups = {OUTBOUNDORDER})
    private void test_075_saveOutboundOrder() {
        if (StringUtil.isNotEmpty(parameterMap.get("cabCode"))
                && StringUtil.isNotEmpty(parameterMap.get("userId"))) {
            //保存类型：保存到草稿箱
            fileEntity.getBodyParam().put("saveType", "1");
            //货柜编码
            fileEntity.getBodyParam().put("cabCode", parameterMap.get("cabCode"));
            fileEntity.getBodyParam().put("outboundType", "1");
            //出库码接收人id
            fileEntity.getBodyParam().put("operateCodeReceiverId", parameterMap.get("userId"));
            //经办人(当前登录用户)
            fileEntity.getBodyParam().put("operateUserId", userId);
            fileEntity.getBodyParam().put("operateUserName", "管理员");
            //经办时间
            fileEntity.getBodyParam().put("operateTime", new Date());
            fileEntity.getBodyParam().put("remark", "备注");
            fileEntity.getBodyParam().put("customerName", "客户名称");
            List<Map<String, Object>> commoditySnList = new ArrayList<>();
            if (StringUtil.isNotEmpty(parameterMap.get("commoditySn"))) {
                Map commoditySnMap = new HashMap();
                commoditySnMap.put("commoditySnId", parameterMap.get("commoditySnId"));
                commoditySnMap.put("commodityCode", parameterMap.get("commodityCode"));
                commoditySnMap.put("commodityName", parameterMap.get("commodityName"));
                commoditySnMap.put("commoditySn", parameterMap.get("commoditySn"));
                //出库单价
                commoditySnMap.put("outboundPrice", "100");
                commoditySnMap.put("cabCode", parameterMap.get("cabCode"));
                commoditySnMap.put("cellCode", parameterMap.get("cellCode"));
                commoditySnMap.put("inboundDatetime", parameterMap.get("inboundDatetime"));
                commoditySnList.add(commoditySnMap);
            }
            fileEntity.getBodyParam().put("commoditySnList", commoditySnList);
            createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
            startPerformTest();
        } else {
            log.info("没有找到货柜、出库码接收人，" +
                    "未执行出库单保存到草稿箱方法test_075_saveOutboundOrder");
            Assert.assertTrue(false);
        }
    }


    /**
     * 导出
     */
    @Test(description = "导出", groups = {OUTBOUNDORDER})
    private void test_076_exportZnhgOutboundOrder() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
    }

    /**
     * 默认查询
     */
    @Test(description = "默认查询", groups = {OUTBOUNDORDER})
    private void test_077_findByPage() {
        fileEntity.getPathParam().put("moduleId", "62");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        parameterMap.put("outboundOrderId", response.jsonPath().get("result.data[0].outboundOrderId"));
    }

    /**
     * 根据id查询出库单详情
     */
    @Test(description = "根据id查询出库单详情", groups = {OUTBOUNDORDER})
    private void test_078_getVOByOutboundOrderId() {
        fileEntity.getPathParam().put("outboundOrderId", parameterMap.get("outboundOrderId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑保存
     */
    @Test(description = "编辑保存", groups = {OUTBOUNDORDER})
    private void test_079_saveOutboundOrder() {
        fileEntity.setBodyParam((Map) parameterMap.get("result"));
        fileEntity.getBodyParam().put("saveType", "0");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }

    /**
     * 根据id查询出库单详情
     */
    @Test(description = "根据id查询出库单详情", groups = {OUTBOUNDORDER})
    private void test_080_getVOByOutboundOrderId() {
        fileEntity.getPathParam().put("outboundOrderId", parameterMap.get("outboundOrderId"));
        startPerformTest();
        saveResponseParameters("result");
        parameterMap.put("operateCode", response.jsonPath().get("result.operateCode"));
        parameterMap.put("outboundOrderCode", response.jsonPath().get("result.outboundOrderCode"));
        parameterMap.put("cabCode", response.jsonPath().get("result.cabCode"));
    }

    /**
     * 查询出库商品详情
     */
    @Test(description = "查询出库商品详情", groups = {OUTBOUNDORDER})
    private void test_081_findDetailByOutboundOrderId() {
        fileEntity.getPathParam().put("outboundOrderId", parameterMap.get("outboundOrderId"));
        startPerformTest();
    }

    /**
     * 重发出库码
     */
    @Test(description = "重发出库码", groups = {OUTBOUNDORDER})
    private void test_082_sendOperateCode() {
        fileEntity.getPathParam().put("operateCode", parameterMap.get("operateCode"));
        fileEntity.getPathParam().put("receiverId", userId);
        startPerformTest();
    }

    /**
     * 关单
     */
    @Test(description = "关单", groups = {OUTBOUNDORDER})
    private void test_083_outCloseOrderToFengLink() {
        fileEntity.getBodyParam().put("outbound_order_code", parameterMap.get("outboundOrderCode"));
        fileEntity.getBodyParam().put("receiverId", parameterMap.get("cabCode"));
        fileEntity.getBodyParam().put("method", "close");
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }
}
