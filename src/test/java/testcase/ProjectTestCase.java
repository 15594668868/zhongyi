package testcase;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.RandomStringUtils;
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
 * @description: 签收宝项目管理
 * @author: guozhixiong
 * @time: 2020/9/15 9:17
 */
public class ProjectTestCase extends BaseTest {
    //excel测试用例
    private static String fileName = "中移签收宝项目管理.xls";

    //使用公司管理
    private static final String EMPLOYCOMPANY = "employCompany";
    //项目人员管理
    private static final String PROJECTPERSONNEL = "projectPersonnel";
    //项目签章明细
    private static final String SIGNATUREDETAIL = "signatureDetail";
    //项目签章汇总
    private static final String SIGNATURECOLLECT = "signatureCollect";
    //签收宝套餐设置
    private static final String SETMEAL = "setMeal";

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



    /*******************************使用公司管理*******************/

    /**
     * 获取检查规则
     */
    @Test(description = "获取检查规则", groups = {EMPLOYCOMPANY})
    private void test_001_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "check_rule");
        startPerformTest();
    }

    /**
     * 获取组织架构
     */
    @Test(description = "获取组织架构", groups = {EMPLOYCOMPANY})
    private void test_002_findOrganizationTreeByOrgId() {
        fileEntity.getPathParam().put("orgType", "0");
        startPerformTest();
    }

    /**
     * 获取使用公司组织架构
     */
    @Test(description = "获取使用公司组织架构", groups = {EMPLOYCOMPANY})
    private void test_003_findOrganizationTreeByOrgId() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        //获取最高公司组织架构id
        parameterMap.put("orgId", response.jsonPath().get("result[0].id"));
    }

    /**
     * 获取组织属性
     */
    @Test(description = "获取组织属性", groups = {EMPLOYCOMPANY})
    private void test_004_findOrganizationTreeByOrgId() {
        fileEntity.getPathParam().put("dicTypeKey", "org_property");
        startPerformTest();
        parameterMap.put("orgPropertyId", response.jsonPath().get("result[1].dicId"));
    }

    /**
     * 新增使用公司
     */
    @Test(description = "新增使用公司", groups = {EMPLOYCOMPANY})
    private void test_005_saveShbOrganization() {
        String appKey = ("zdh" + RandomStringUtils.randomAlphanumeric(5) + "-" +
                RandomStringUtils.randomAlphanumeric(4) + "-" +
                RandomStringUtils.randomAlphanumeric(4) + "-" +
                RandomStringUtils.randomAlphanumeric(4) + "-" +
                RandomStringUtils.randomAlphanumeric(12)).toLowerCase();
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("pOrgId", parameterMap.get("orgId")); //上级部门
        fileEntity.getPathParam().put("orgName", "自动化名称" + currentTime.substring(8, currentTime.length()));
        parameterMap.put("orgName", "自动化名称" + currentTime.substring(8, currentTime.length()));
        fileEntity.getPathParam().put("apId", "apid" + currentTime.substring(8, currentTime.length()));
        fileEntity.getPathParam().put("telephone", "15800000001");
        fileEntity.getPathParam().put("remark", "备注");
        fileEntity.getPathParam().put("simPublicKey", appKey);
        fileEntity.getPathParam().put("orgCode", "自动化代码" + currentTime.substring(8, currentTime.length()));
        fileEntity.getPathParam().put("orgProperty", parameterMap.get("orgPropertyId")); //部门性质
        fileEntity.getPathParam().put("contact", "负责人" + currentTime.substring(8, currentTime.length()));
        fileEntity.getPathParam().put("isCheckSame", "0");
        fileEntity.getPathParam().put("isGrant", "1");
        startPerformTest();
    }

    /**
     * 获取使用公司组织架构
     */
    @Test(description = "获取使用公司组织架构", groups = {EMPLOYCOMPANY})
    private void test_006_findOrganizationTreeByOrgId() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        List<Map<String, Object>> children = response.jsonPath().getList("result[0].children");
        for (Map map : children){
            if(StringUtil.equals(parameterMap.get("orgName").toString(), map.get("label").toString())) {
                parameterMap.put("orgLabelId", map.get("id"));
                break;
            }
        }
    }

    /**
     * 根据orgid获取部门详细信息
     */
    @Test(description = "根据orgid获取部门详细信息", groups = {EMPLOYCOMPANY})
    private void test_007_findOrganizationByOrgId() {
        fileEntity.getPathParam().put("orgId", parameterMap.get("orgLabelId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 修改保存使用公司
     */
    @Test(description = "修改保存使用公司", groups = {EMPLOYCOMPANY})
    private void test_008_saveShbOrganization() {
        fileEntity.setPathParam((Map)parameterMap.get("result"));
        startPerformTest();
    }


/***********************************项目人员管理*************************************/

    /**
     * 获取项目人员列
     */
    @Test(description = "获取项目人员列", groups = {PROJECTPERSONNEL})
    private void test_009_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "52");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
    }

    /**
     * 获取使用公司列表
     */
    @Test(description = "获取使用公司列表", groups = {PROJECTPERSONNEL})
    private void test_010_findOrganizationTreeByOrgId() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        List<Map<String, Object>> children = response.jsonPath().getList("result[0].children");
        parameterMap.put("orgLabelId", children.get(children.size()-1).get("id"));
    }

    /**
     * 获取是否启用数据字典
     */
    @Test(description = "获取是否启用数据字典", groups = {PROJECTPERSONNEL})
    private void test_011_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "whether");
        startPerformTest();
        saveResponseParameters("result[0].dicId");
    }

    /**
     * 组合查询项目人员
     */
    @Test(description = "组合查询项目人员", groups = {PROJECTPERSONNEL})
    private void test_012_findByPage() {
        fileEntity.getPathParam().put("orgIds", parameterMap.get("orgLabelId"));
        fileEntity.getPathParam().put("telephone", "手机号");
        fileEntity.getPathParam().put("userName", "姓名");
        fileEntity.getPathParam().put("moduleId", "52");
        fileEntity.getPathParam().put("isAbleUse", parameterMap.get("dicId"));
        startPerformTest();
    }

    /**
     * 新增项目人员
     */
    @Test(description = "新增项目人员", groups = {PROJECTPERSONNEL})
    private void test_013_saveShbUser() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("address", "地址");
        fileEntity.getPathParam().put("orgIds", parameterMap.get("orgLabelId").toString());
        fileEntity.getPathParam().put("idCard", "6228251991" + currentTime.substring(9, currentTime.length()));
        fileEntity.getPathParam().put("fixedPhone", "022-1233211");
        fileEntity.getPathParam().put("telephone", "158" + currentTime.substring(9, currentTime.length()));
        fileEntity.getPathParam().put("userName", "自动化" + currentTime);
        parameterMap.put("userName", "自动化" + currentTime);
        fileEntity.getPathParam().put("moduleId", "52");
        fileEntity.getPathParam().put("email", "5@qq.com");
        startPerformTest();
    }

    /**
     * 验证手机号是否重复
     */
    @Test(description = "验证手机号是否重复", groups = {PROJECTPERSONNEL})
    private void test_014_findExistUserTelephone() {
        fileEntity.getPathParam().put("telephone", "15800000000");
        startPerformTest();
    }

    /**
     * 验证身份证号是否重复
     */
    @Test(description = "验证身份证号是否重复", groups = {PROJECTPERSONNEL})
    private void test_015_findExistUserTelephone() {
        fileEntity.getPathParam().put("idCard", "622825199101010103");
        startPerformTest();
    }


    /**
     * 根据姓名查询项目人员
     */
    @Test(description = "根据姓名查询项目人员", groups = {PROJECTPERSONNEL})
    private void test_016_findByPage() {
        fileEntity.getPathParam().put("userName", parameterMap.get("userName"));
        fileEntity.getPathParam().put("moduleId", "52");
        startPerformTest();
        saveResponseParameters("result.data[0].userId");
    }

    /**
     * 点击编辑根据用户id获取详细信息
     */
    @Test(description = "点击编辑根据用户id获取详细信息", groups = {PROJECTPERSONNEL})
    private void test_017_getShbUserById() {
        fileEntity.getPathParam().put("userId", parameterMap.get("userId"));
        startPerformTest();
    }

    /**
     * 禁用
     */
    @Test(description = "禁用", groups = {PROJECTPERSONNEL})
    private void test_018_operateByUserId() {
        fileEntity.getPathParam().put("userId", parameterMap.get("userId"));
        fileEntity.getPathParam().put("isAbleUse", "0");
        startPerformTest();
    }

    /**
     * 启用
     */
    @Test(description = "启用", groups = {PROJECTPERSONNEL})
    private void test_019_operateByUserId() {
        fileEntity.getPathParam().put("userId", parameterMap.get("userId"));
        fileEntity.getPathParam().put("isAbleUse", "1");
        startPerformTest();
    }

    /**
     * 重置密码
     */
    @Test(description = "重置密码", groups = {PROJECTPERSONNEL})
    private void test_020_resetPwd() {
        fileEntity.getPathParam().put("userId", parameterMap.get("userId"));
        startPerformTest();
    }

    /**
     * 生成平台账号信息
     */
    @Test(description = "生成平台账号信息", groups = {PROJECTPERSONNEL})
    private void test_021_generateUserCode() {
        fileEntity.getPathParam().put("userId", parameterMap.get("userId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 生成平台账号确认
     */
    @Test(description = "生成平台账号确认", groups = {PROJECTPERSONNEL})
    private void test_022_generateAccount() {
        fileEntity.getPathParam().put("userId", parameterMap.get("userId"));
        fileEntity.getPathParam().put("userCode", parameterMap.get("result"));
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {PROJECTPERSONNEL})
    private void test_023_exportShbUser() {
        startPerformTest();
    }


    /******************************项目签章明细******************/

    /**
     * 获取签章明细列
     */
    @Test(description = "获取签章明细列", groups = {SIGNATUREDETAIL})
    private void test_024_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "53");
        startPerformTest();
    }

    /**
     * 获取使用公司组织架构
     */
    @Test(description = "获取使用公司组织架构", groups = {SIGNATUREDETAIL})
    private void test_025_findOrganizationTreeByOrgId() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        List<Map<String, Object>> children = response.jsonPath().getList("result[0].children");
        parameterMap.put("orgLabelId", children.get(children.size()-1).get("id"));
    }

    /**
     * 组合查询
     */
    @Test(description = "组合查询", groups = {SIGNATUREDETAIL})
    private void test_026_findByPage() {
        fileEntity.getPathParam().put("code", "编号");
        fileEntity.getPathParam().put("moduleId", "53");
        fileEntity.getPathParam().put("orgIds", parameterMap.get("orgLabelId"));
        Long[] createdTimeRange = DateUtil.getTimeListDay(-1, 0);
        fileEntity.getPathParam().put("createdTimeRange[0]", createdTimeRange[0]);
        fileEntity.getPathParam().put("createdTimeRange[1]", createdTimeRange[1]);
        startPerformTest();
    }

    /**
     * 默认查询
     */
    @Test(description = "默认查询", groups = {SIGNATUREDETAIL})
    private void test_027_findByPage() {
        fileEntity.getPathParam().put("moduleId", "53");
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {SIGNATUREDETAIL})
    private void test_028_exportShbSignature() {
        startPerformTest();
    }


    /*******************************项目签章汇总******************/
    /**
     * 获取签章汇总列
     */
    @Test(description = "获取签章汇总列", groups = {SIGNATURECOLLECT})
    private void test_029_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "54");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
    }

    /**
     * 获取使用公司组织架构
     */
    @Test(description = "获取使用公司组织架构", groups = {SIGNATURECOLLECT})
    private void test_030_findOrganizationTreeByOrgId() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        List<Map<String, Object>> children = response.jsonPath().getList("result[0].children");
        parameterMap.put("orgLabelId", children.get(children.size()-1).get("id"));
    }

    /**
     * 组合查询
     */
    @Test(description = "组合查询", groups = {SIGNATURECOLLECT})
    private void test_031_findSummaryByPage() {
        Long[] createdTimeRange = DateUtil.getTimeListDay(-1, 0);
        fileEntity.getPathParam().put("moduleId", "54");
        fileEntity.getPathParam().put("orgIds", parameterMap.get("orgLabelId"));
        fileEntity.getPathParam().put("userCode", "账号");
        fileEntity.getPathParam().put("createdTimeRange[0]", createdTimeRange[0]);
        fileEntity.getPathParam().put("createdTimeRange[1]", createdTimeRange[1]);
        startPerformTest();
    }

    /**
     * 默认查询
     */
    @Test(description = "默认查询", groups = {SIGNATURECOLLECT})
    private void test_032_findSummaryByPage() {
        Long[] createdTimeRange = DateUtil.getTimeListDay(-1, 0);
        fileEntity.getPathParam().put("moduleId", "54");
        fileEntity.getPathParam().put("createdTimeRange[0]", createdTimeRange[0]);
        fileEntity.getPathParam().put("createdTimeRange[1]", createdTimeRange[1]);
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {SIGNATURECOLLECT})
    private void test_033_exportShbSummary() {
        Long[] createdTimeRange = DateUtil.getTimeListDay(-1, 0);
        fileEntity.getPathParam().put("createdTimeRange[0]", createdTimeRange[0]);
        fileEntity.getPathParam().put("createdTimeRange[1]", createdTimeRange[1]);
        startPerformTest();
    }


    /**************************签收宝套餐设置******************************/
    /***
     * 获取签收宝套餐列
     */
    @Test(description = "获取签收宝套餐列", groups = {SETMEAL})
    private void test_034_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "71");
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
    }

    /***
     * 获取套餐状态
     */
    @Test(description = "获取套餐状态", groups = {SETMEAL})
    private void test_035_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "meal_status");
        startPerformTest();
        saveResponseParameters("result[0].dicId");
    }

    /***
     * 组合查询
     */
    @Test(description = "组合查询", groups = {SETMEAL})
    private void test_036_findByPage() {
        Long[] createdTimeRange = DateUtil.getTimeListDay(-1, 0);
        fileEntity.getPathParam().put("name", "公司/姓名");
        fileEntity.getPathParam().put("moduleId", "71");
        fileEntity.getPathParam().put("mealStatus", parameterMap.get("dicId"));
        fileEntity.getPathParam().put("type", "1"); //套餐类型：固定套餐
        fileEntity.getPathParam().put("createdDateRange[0]", createdTimeRange[0]);
        fileEntity.getPathParam().put("createdDateRange[1]", createdTimeRange[1]);
        startPerformTest();
    }

    /***
     * 默认查询
     */
    @Test(description = "默认查询", groups = {SETMEAL})
    private void test_037_findByPage() {
        fileEntity.getPathParam().put("moduleId", "71");
        startPerformTest();
    }

    /***
     * 获取使用公司列表
     */
    @Test(description = "获取使用公司列表", groups = {SETMEAL})
    private void test_038_findOrganizationTreeByOrgId() {
        fileEntity.getPathParam().put("userId", userId);
        startPerformTest();
        List<Map<String, Object>> children = response.jsonPath().getList("result[0].children");
        parameterMap.put("orgLabelId", children.get(children.size()-1).get("id"));
    }

    /***
     * 新增签收宝套餐设置
     */
    @Test(description = "新增签收宝套餐设置", groups = {SETMEAL})
    private void test_039_saveShbSetMeal() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        Long[] date = DateUtil.getTimeListDay(0, 32);
        fileEntity.getBodyParam().put("shbOrgId", parameterMap.get("orgLabelId"));
        fileEntity.getBodyParam().put("shbOrgName", "自动化" + currentTime);
        fileEntity.getBodyParam().put("startDate", date[0]);
        fileEntity.getBodyParam().put("endDate", date[1]);
        fileEntity.getBodyParam().put("monthMealQuota", "200"); //套餐价额包额度
        fileEntity.getBodyParam().put("isRoll", "0"); //是否滚动计费：否
        fileEntity.getBodyParam().put("isFlowPool", "0"); //是否流量池计算：否
        fileEntity.getBodyParam().put("mealType", "2"); //套餐类型：个人套餐
        fileEntity.getBodyParam().put("setType", "1"); //公司
        createEncrypt(JSON.toJSONString(fileEntity.getBodyParam()), "bodyParam");
        startPerformTest();
    }


    /***
     * 获取该公司下的用户
     */
    @Test(description = "获取该公司下的用户", groups = {SETMEAL})
    private void test_040_initShbUserByOrgId() {
        fileEntity.getPathParam().put("orgId", parameterMap.get("orgLabelId"));
        startPerformTest();
        saveResponseParameters("result[0].userId");
    }

    /***
     * 新增加油包设置
     */
    @Test(description = "新增加油包设置", groups = {SETMEAL})
    private void test_041_saveShbRefuelBag() {
        fileEntity.getPathParam().put("shbUserId", parameterMap.get("userId"));
        fileEntity.getPathParam().put("shbOrgId", parameterMap.get("orgLabelId"));
        fileEntity.getPathParam().put("refuelBagQuota", "1");
        startPerformTest();
    }
}
