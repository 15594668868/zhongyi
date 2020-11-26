package testcase;

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
 * @description: 系统管理
 * @author: guozhixiong
 * @time: 2020/8/25 15:03
 */
public class SystemTestCase extends BaseTest {
    //excel测试用例
    private static String fileName = "中移系统管理.xls";
    //部门管理
    private static final String ORGMANAGE = "orgManage";
    //角色管理
    private static final String ROLEMANAGE = "roleManage";
    //用户管理
    private static final String USERMANAGE = "userManage";
    //操作日志
    private static final String LOGMANAGE = "logManage";
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



    /*******************************部门管理*******************/



    /**
     * 新增部门管理
     */
    @Test(description = "新增部门管理", groups = {ORGMANAGE})
    private void test_001_saveOrganization() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("pOrgId", orgId);
        fileEntity.getPathParam().put("linkPhone", "158" + currentTime.substring(9, currentTime.length()));
        fileEntity.getPathParam().put("orgName", "部门名称" + currentTime);
        fileEntity.getPathParam().put("address", "地址" + currentTime);
        fileEntity.getPathParam().put("orgCode", "部门代码" + currentTime);
        parameterMap.put("orgName", "部门名称" + currentTime);
        fileEntity.getPathParam().put("managerName", "部门负责人" + currentTime);
        fileEntity.getPathParam().put("linkMan", "部门联系人" + currentTime);
        fileEntity.getPathParam().put("managerPhoneNo", "139" + currentTime.substring(9, currentTime.length()));
        startPerformTest();
    }

    /**
     * 获取所有部门信息
     */
    @Test(description = "获取所有部门信息", groups = {ORGMANAGE})
    private void test_002_findOrganizationTreeByOrgId() {
        startPerformTest();
        List<Map<String, Object>> children = response.jsonPath().getList("result[0].children");
        for (Map map : children) {
            if(StringUtil.equals(map.get("label").toString(), parameterMap.get("orgName").toString())) {
                parameterMap.put("orgId", map.get("id"));
                break;
            }
        }
    }

    /**
     * 根据id获取部门信息
     */
    @Test(description = "根据id获取部门信息", groups = {ORGMANAGE})
    private void test_003_findOrganizationDetail() {
        fileEntity.getPathParam().put("orgId", parameterMap.get("orgId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 编辑修改部门信息
     */
    @Test(description = "编辑修改部门信息", groups = {ORGMANAGE})
    private void test_004_saveOrganization() {
        fileEntity.setPathParam((Map)parameterMap.get("result"));
        startPerformTest();
    }

    /**
     * 删除部门
     */
    @Test(description = "删除部门", groups = {ORGMANAGE})
    private void test_005_deleteOrganizationByOrgId() {
        fileEntity.getPathParam().put("orgId", parameterMap.get("orgId"));
        startPerformTest();
    }


    /*********************角色管理********************/

    /**
     * 获取角色表格列
     */
    @Test(description = "获取角色表格列", groups = {ROLEMANAGE})
    private void test_006_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "2");
        startPerformTest();
    }

    /**
     * 获取所有权限
     */
    @Test(description = "获取所有权限", groups = {ROLEMANAGE})
    private void test_007_getAllModule() {
        startPerformTest();
        saveResponseParameters("result[0].moduleId");
    }

    /**
     * 获取角色属性
     */
    @Test(description = "获取角色属性", groups = {ROLEMANAGE})
    private void test_008_getDictByDicTypeKey() {
        fileEntity.getPathParam().put("dicTypeKey", "user_property");
        startPerformTest();
        saveResponseParameters("result[0].dicId");
    }

    /**
     * 组合查询角色管理
     */
    @Test(description = "组合查询角色管理", groups = {ROLEMANAGE})
    private void test_009_findByPage() {
        fileEntity.getPathParam().put("moduleIds", parameterMap.get("moduleId"));
        fileEntity.getPathParam().put("roleProperty", parameterMap.get("dicId"));
        fileEntity.getPathParam().put("roleName", "角色名称");
        startPerformTest();
    }

    /**
     * 获取所有权限菜单
     */
    @Test(description = "获取所有权限菜单", groups = {ROLEMANAGE})
    private void test_010_findAllMenuTree() {
        startPerformTest();
        List<Map<String, Object>> list = response.jsonPath().getList("result");
        StringBuffer privilegeIds = new StringBuffer();
        for (Map map : list) {
            List<Map<String, Object>> children = (List)map.get("children");
            for (Map childrenMap : children) {
                List<Map<String, Object>> privilegeList = (List)childrenMap.get("privilegeList");
                for (Map privilegeMap : privilegeList) {
                    privilegeIds.append(privilegeMap.get("privilegeId") + ",");
                }
            }
        }
        parameterMap.put("privilegeIds", privilegeIds.substring(0, privilegeIds.length()-1));
        System.out.println(parameterMap.get("privilegeIds").toString());
    }

    /**
     * 检查角色名是否存在
     */
    @Test(description = "检查角色名是否存在", groups = {ROLEMANAGE})
    private void test_011_checkRoleNameIsExist() {
        startPerformTest();
    }

    /**
     * 新增保存角色
     */
    @Test(description = "新增保存角色", groups = {ROLEMANAGE})
    private void test_012_saveRole() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("roleProperty", parameterMap.get("dicId"));
        fileEntity.getPathParam().put("roleName", "角色名" + currentTime);
        fileEntity.getPathParam().put("remark", "备注");
        fileEntity.getPathParam().put("privilegeIds", parameterMap.get("privilegeIds"));
        startPerformTest();
    }

    /**
     * 默认查询角色管理
     */
    @Test(description = "默认查询角色管理", groups = {ROLEMANAGE})
    private void test_013_findByPage() {
        startPerformTest();
        saveResponseParameters("result.data[0].roleId");
    }

    /**
     * 点击修改查询角色详情
     */
    @Test(description = "点击修改查询角色详情", groups = {ROLEMANAGE})
    private void test_014_findRoleDetailByRoleId() {
        fileEntity.getPathParam().put("roleId", parameterMap.get("roleId"));
        startPerformTest();
        saveResponseParameters("result.roleName",
                "result.roleProperty",
                "result.remark",
                "result.menuIds",
                "result.roleId");
    }

    /**
     * 获取所有权限菜单
     */
    @Test(description = "获取所有权限菜单", groups = {ROLEMANAGE})
    private void test_015_findAllMenuTree() {
        startPerformTest();
        List<Map<String, Object>> list = response.jsonPath().getList("result");
        StringBuffer privilegeIds = new StringBuffer();
        for (Map map : list) {
            List<Map<String, Object>> children = (List)map.get("children");
            for (Map childrenMap : children) {
                List<Map<String, Object>> privilegeList = (List)childrenMap.get("privilegeList");
                for (Map privilegeMap : privilegeList) {
                    privilegeIds.append(privilegeMap.get("privilegeId") + ",");
                }
            }
        }
        parameterMap.put("privilegeIds", privilegeIds.substring(0, privilegeIds.length()-1));
        System.out.println(parameterMap.get("privilegeIds").toString());
    }

    /**
     * 修改保存角色
     */
    @Test(description = "修改保存角色", groups = {ROLEMANAGE})
    private void test_016_saveRole() {
        fileEntity.getPathParam().put("roleName", parameterMap.get("roleName"));
        fileEntity.getPathParam().put("roleProperty", parameterMap.get("roleProperty"));
        fileEntity.getPathParam().put("remark", parameterMap.get("remark"));
        fileEntity.getPathParam().put("privilegeIds", parameterMap.get("privilegeIds"));
        fileEntity.getPathParam().put("roleId", parameterMap.get("roleId"));
        startPerformTest();
    }

    /**
     * 删除角色
     */
    @Test(description = "删除角色", groups = {ROLEMANAGE})
    private void test_017_deleteRole() {
        fileEntity.getPathParam().put("roleIds", parameterMap.get("roleId"));
        startPerformTest();
    }


    /*********************************用户管理***********************/

    /**
     * 获取用户管理表格列
     */
    @Test(description = "获取用户管理表格列", groups = {USERMANAGE})
    private void test_018_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "1");
        startPerformTest();
    }

    /**
     * 获取所有角色
     */
    @Test(description = "获取所有角色", groups = {USERMANAGE})
    private void test_019_getUserAllRole() {
        startPerformTest();
        saveResponseParameters("result[0].roleId");
    }

    /**
     * 获取组织架构
     */
    @Test(description = "获取组织架构", groups = {USERMANAGE})
    private void test_020_findOrganizationTreeByOrgId() {
        startPerformTest();
    }

    /**
     * 组合查询用户管理
     */
    @Test(description = "组合查询用户管理", groups = {USERMANAGE})
    private void test_021_findByPage() {
        fileEntity.getPathParam().put("roleIds", parameterMap.get("roleId"));
        fileEntity.getPathParam().put("orgIds", orgId);
        fileEntity.getPathParam().put("idCard", "身份证号");
        fileEntity.getPathParam().put("userProperty", "1"); //用户属性：中移
        fileEntity.getPathParam().put("isAbleUse", "1");  //是否有效
        fileEntity.getPathParam().put("userName", "姓名");
        fileEntity.getPathParam().put("moduleId", "1");
        fileEntity.getPathParam().put("userCode", "用户名");
        startPerformTest();
    }

    /**
     * 获取货主信息
     */
    @Test(description = "获取货主信息", groups = {USERMANAGE})
    private void test_022_findAllMerchantByType() {
        startPerformTest();
    }

    /**
     * 验证用户名是否存在
     */
    @Test(description = "验证用户名是否存在", groups = {USERMANAGE})
    private void test_023_checkUserCodeIsExist() {
        startPerformTest();
    }

    /**
     * 验证手机号是否存在
     */
    @Test(description = "验证手机号是否存在", groups = {USERMANAGE})
    private void test_024_checkTelephoneIsExist() {
        startPerformTest();
    }

    /**
     * 验证身份证号是否存在
     */
    @Test(description = "验证身份证号是否存在", groups = {USERMANAGE})
    private void test_025_checkIdCardIsExist() {
        startPerformTest();
    }

    /**
     * 新增用户
     */
    @Test(description = "新增用户", groups = {USERMANAGE})
    private void test_026_saveUser() {
        String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
        fileEntity.getPathParam().put("address", "地址");
        fileEntity.getPathParam().put("idCard", "6" + currentTime);
        fileEntity.getPathParam().put("userProperty", "1");
        fileEntity.getPathParam().put("telephone", "158" + currentTime.substring(9, currentTime.length()));
        fileEntity.getPathParam().put("remark", "备注");
        fileEntity.getPathParam().put("isAbleUse", "1"); //是否有效
        fileEntity.getPathParam().put("userName", "自动化姓名" + currentTime);
        fileEntity.getPathParam().put("userCode", "自动化用户名" + currentTime);
        parameterMap.put("userCode", "自动化用户名" + currentTime);
        fileEntity.getPathParam().put("roleIds", parameterMap.get("roleId"));
        fileEntity.getPathParam().put("orgIds", orgId);
        fileEntity.getPathParam().put("moduleId", "1");
        fileEntity.getPathParam().put("fixedTelephone", "022-1234561");
        fileEntity.getPathParam().put("email", "528282828@qq.com");
        startPerformTest();
    }


    /**
     * 默认查询用户管理
     */
    @Test(description = "默认查询用户管理", groups = {USERMANAGE})
    private void test_027_findByPage() {
        //todo
        parameterMap.put("userCode", "自动化用户名20200826161819143");
        fileEntity.getPathParam().put("moduleId", "1");
        fileEntity.getPathParam().put("userCode", parameterMap.get("userCode"));
        startPerformTest();
        saveResponseParameters("result.data[0].userId");
    }

    /**
     * 点击编辑根据用户id查询详情
     */
    @Test(description = "点击编辑根据用户id查询详情", groups = {USERMANAGE})
    private void test_028_findUserByUserId() {
        fileEntity.getPathParam().put("userId", parameterMap.get("userId"));
        startPerformTest();
        saveResponseParameters("result");
    }

    /**
     * 点击保存保存数据
     */
    @Test(description = "点击保存保存数据", groups = {USERMANAGE})
    private void test_029_saveUser() {
        fileEntity.setPathParam((Map)parameterMap.get("result"));
        startPerformTest();
    }
    /**
     * 重置密码
     */
    @Test(description = "重置密码", groups = {USERMANAGE})
    private void test_030_resetPwd() {
        fileEntity.getPathParam().put("userId", parameterMap.get("userId"));
        startPerformTest();
    }

    /**
     * 导出
     */
    @Test(description = "导出", groups = {USERMANAGE})
    private void test_031_exportBaseUser() {
        startPerformTest();
    }



    /*******************操作日志*****************/

    /**
     * 获取操作日志表格列
     */
    @Test(description = "获取操作日志表格列", groups = {LOGMANAGE})
    private void test_032_getTableColumnSettings() {
        fileEntity.getPathParam().put("moduleId", "4");
        startPerformTest();
    }

    /**
     * 获取操作类型
     */
    @Test(description = "获取操作类型", groups = {LOGMANAGE})
    private void test_033_findAllOperateType() {
        startPerformTest();
        saveResponseParameters("result[0].operationId");
    }

    /**
     * 获取操作模块
     */
    @Test(description = "获取操作模块", groups = {LOGMANAGE})
    private void test_034_findAllOperateModule() {
        startPerformTest();
        saveResponseParameters("result[0].moduleId");
    }

    /**
     * 组合查询操作日志
     */
    @Test(description = "组合查询操作日志", groups = {LOGMANAGE})
    private void test_035_findByPage() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        //操作模块
        fileEntity.getPathParam().put("operateSecondModule", parameterMap.get("moduleId"));
        //操作类型
        fileEntity.getPathParam().put("operateTypeId", parameterMap.get("operationId"));
        fileEntity.getPathParam().put("operateUserName", "操作人");
        fileEntity.getPathParam().put("orgIds", orgId);
        fileEntity.getPathParam().put("moduleId", "4");
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
    }

    /**
     * 默认查询操作日志
     */
    @Test(description = "默认查询操作日志", groups = {LOGMANAGE})
    private void test_036_findByPage() {
        Long[] signDateRange = DateUtil.getTimeListDay(-7, 0);
        fileEntity.getPathParam().put("moduleId", "4");
        fileEntity.getPathParam().put("signDateRange[0]", signDateRange[0]);
        fileEntity.getPathParam().put("signDateRange[1]", signDateRange[1]);
        startPerformTest();
    }
}
