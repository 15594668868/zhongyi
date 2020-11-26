package testcase;

import com.alibaba.fastjson.JSON;
import org.testng.annotations.Test;
import testutils.AesEncryptUtil;
import utils.ConfigFile;
import utils.Md5Util;
import utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 中移自动化用例Base类
 * @author: guozhixiong
 * @time: 2020/8/11 9:42
 */
public class BaseTest extends BaseTestCase{
    //执行地址
    public static String platformUrl = ConfigFile.getValue("platform.url");
    //lis对接地址
    public static String lisUrl = ConfigFile.getValue("lisUrl.url");
    //登录请求
    public static String loginUrl = ConfigFile.getUrl("login.url");
    //用从文件获取到的需要登录的用户名到配置文件获取该用户登录账号密码
    public String userCode = ConfigFile.getValue( "login.userCode");
    public String userPassword = ConfigFile.getValue( "login.userPassword");
    //IV
    public String shortsIv = ConfigFile.getValue( "NdNpOKov1BWGb3qQzlYQR6SVR0HnwIKv");
    //加密key名称，发送请求时一起发送
    public String encryptRequestKey = ConfigFile.getValue("encryptRequestKey");
    //加密key和加密iv
    public String encryptKey = "RlCbkFwgD5ojYqQZ";
    public String encryptIv = "YnZCuXiv3VEOij5r";
    //是否加密
    public String isEncryption = ConfigFile.getValue("isEncryption");
    //请求是否带加密值
    public String isCarryValue = ConfigFile.getValue("isCarryValue");
    //中移物流组织架构id
    public String orgId = "1";
    //中移admin用户id
    public String userId = "1";

    /**
     * 参数加密解密
     * @param data  数据
     * @param b true是解密，false是加密
     */
    public Map dataEncryption(String data, Boolean b){
        Map<String, Object> map = null;
        Map<String, Object> newMap = new HashMap<>();
        if(data.startsWith("[")){
            List list = JSON.parseArray(data);
            map = (Map)list.get(0);
        } else {
            map = JSON.parseObject(data);
        }
        map.forEach((key, value)->{
            if(StringUtil.equals(encryptRequestKey, key)){
                return;
            }
            try {
                if(b){//解密
                    newMap.put(key, AesEncryptUtil.desEncryptDataByHexStr(value.toString(), encryptKey, encryptIv));
                } else {//加密
                    newMap.put(key, AesEncryptUtil.encryptData2Hex(value.toString(), encryptKey, encryptIv));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return newMap;
    }

    /**
     * json解密
     */
    @Test
    public void decode() {
        String string = "{\"appId\":\"df88828b205be310bbe9de5698427a32\",\"interfaceName\":\"df88828b205be310bbe9de5698427a32\",\"appKey\":\"df88828b205be310bbe9de5698427a32\",\"moduleId\":\"70c72d2ea7b4717a7c252f80eb098035\",\"curPage\":\"df88828b205be310bbe9de5698427a32\",\"pageSize\":\"37e822f08d70cf94a42c662812641d04\"}";
        Map map = dataEncryption(string, true);
        outputMap(map, string);
    }

    /**
     * 单字符串解密
     */
    @Test
    public void decodeString() {
        String string = "62b8ec96b8254a6ef26f5c9b338f0052";
        try {
            System.out.println(AesEncryptUtil.desEncryptDataByHexStr(string, encryptKey, encryptIv));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * json加密
     */
    @Test
    public void encrypt() {
        String string = "{\"areaId\":1000,\"areaName\":\"自动化仓库1597117578409\",\"areaCode\":\"7117578409\",\"orgId\":1,\"categoryId\":2,\"areaColor\":\"#409eff\",\"titleColor\":\"#000\",\"areaAcreage\":\"3.142\",\"lonCenter\":109.15836,\"latCenter\":34.295967,\"imgUrl\":\"/e6Images/m_1.png\",\"rangeLength\":1,\"positionAddress\":\"陕西省西安市灞桥区洪庆街道灞临路\",\"shareMode\":1,\"remark\":\"备注\",\"manager\":\"郭志雄\",\"telephone\":\"15800000000\",\"enableStatus\":1,\"typeId\":\"\",\"province\":\"陕西省\",\"warehouseAndCrenelPOList\":[{\"areaId\":1212,\"createdTime\":\"2020-08-11 11:48:07\",\"createdUserId\":1,\"crenelId\":1699,\"crenelName\":\"垛口1\",\"isValid\":1,\"modifiedTime\":\"2020-08-11 14:15:03\",\"modifiedUserId\":1},{\"areaId\":1212,\"createdTime\":\"2020-08-11 11:48:07\",\"createdUserId\":1,\"crenelId\":1700,\"crenelName\":\"垛口2\",\"isValid\":1,\"modifiedTime\":\"2020-08-11 14:15:03\",\"modifiedUserId\":1}]}";
        Map map = dataEncryption(string, false);
        outputMap(map, string);
    }

    /**
     * 根据字符串打印
     * @param map
     * @param string
     */
    private void outputMap(Map map, String string) {
        if(string.startsWith("[")){
            System.out.println("[");
        }
        System.out.println("{");
        StringBuffer mapString = new StringBuffer();
        map.forEach((key, value)->{
            mapString.append("\t\"" + key + "\" : " + "\"" + value + "\",\n");
        });
        //去括号
        System.out.println(mapString.substring(0, mapString.length()-2));
        System.out.println("}");
        if(string.startsWith("[")){
            System.out.println("]");
        }
    }

    /**
     * 因为要配置加密，所以重写该方法
     */
    @Override
    protected void startPerformTest() {
        encryption();
        createRequestSpec();
        createResponseSpec();
        sendRequest();
    }

    /**
     * 加密参数
     */
    public void encryption() {
        //用于判断是否需要给请求加上encryptRequestKey也就是加密参数
        Boolean isRequestIv = false;
        //加密
        if(StringUtil.equals("true", isEncryption)) {
            String string = "";
            if(StringUtil.isNotEmpty(fileEntity.getBodyParam())){
                string = JSON.toJSONString(fileEntity.getBodyParam());
                //已经存在该值说明已经进行过加密，无需再加密
                if(!string.contains(encryptRequestKey)) {
                    //如果没有加密过，则给true，说明进行单参数加密，需要带上encryptRequestKey加密参数
                    isRequestIv = true;
                    System.out.println("加密前： " + string);
                    Map map = dataEncryption(string, false);
                    System.out.println("加密后： " + map.toString());
                    fileEntity.setBodyParam(map);
                }
            }
            if(StringUtil.isNotEmpty(fileEntity.getPathParam())) {
                string = JSON.toJSONString(fileEntity.getPathParam());
                if(!string.contains(encryptRequestKey)) {
                    isRequestIv = true;
                    System.out.println("加密前： " + string);
                    Map map = dataEncryption(string, false);
                    System.out.println("加密后： " + map.toString());
                    fileEntity.setPathParam(map);
                }
            }
            if(StringUtil.isNotEmpty(fileEntity.getBodyParamList())) {
                string = JSON.toJSONString(fileEntity.getBodyParamList());
                if(!string.contains(encryptRequestKey)) {
                    isRequestIv = true;
                    System.out.println("加密前： " + string);
                    Map map = dataEncryption(string, false);
                    System.out.println("加密后： " + map.toString());
                    List list = new ArrayList();
                    list.add(0, map);
                    fileEntity.setBodyParamList(list);
                }
            }
        }
        //请求带默认值
        if(StringUtil.equals("true", isCarryValue)) {
            if(isRequestIv) {
                if(StringUtil.isNotEmpty(fileEntity.getBodyParam())){
                    fileEntity.getBodyParam().put(encryptRequestKey, shortsIv);
                }
                if(StringUtil.isNotEmpty(fileEntity.getPathParam())) {
                    fileEntity.getPathParam().put(encryptRequestKey, shortsIv);
                }
                if(StringUtil.isNotEmpty(fileEntity.getBodyParamList())) {
                    fileEntity.getBodyParamList().get(0).put(encryptRequestKey, shortsIv);
                }
            }
        }
    }

    /**
     * 传入json串和需要set进对象的参数
     * @param jsonString json串
     * @param parame bodyParam pathParam bodyParamList
     */
    public void createEncrypt(String jsonString, String parame) {
        System.out.println("加密前  " + jsonString);
        //加密
        if(StringUtil.equals("true", isEncryption)) {
            Map map = new HashMap();
            try {
                map.put(encryptRequestKey, AesEncryptUtil.encryptData2Hex(jsonString, null, null));
                System.out.println("加密后   " + map.get(encryptRequestKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(StringUtil.equals("bodyParam", parame)) {
                fileEntity.setBodyParam(map);
            } else if(StringUtil.equals("pathParam", parame)) {
                fileEntity.setPathParam(map);
            } else if(StringUtil.equals("bodyParamList", parame)) {
                List list = new ArrayList();
                list.add(0, map);
                fileEntity.setBodyParamList(list);
            }
        }
    }


    @Test
    public void test() {
        //参数需要的值
        String appKey = "0153f56c-bc94-4225-92a7-dd1e7fa32249";
        String appSecret = "3b79e4c9-305e-4bc4-ab50-9cd8b727b19f";
        String interfaceName = "createOutboundOrderOfSales";
        String timestamp = "1603432269579";
        //创建data
        String data = "{\"outboundType\":2,\"operateTime\":\"1603432053578\",\"remark\":\"备注\",\"customerId\":\"客户名称\",\"cabCode\":\"F2020042000046\",\"receiverId\":\"\",\"operateUserId\":\"\",\"commodityList\":[{\"code\":\"1B99000107000211\",\"count\":1}]}";
        String str = appKey + appSecret + interfaceName + timestamp + appKey + JSON.toJSONString(data);
        System.out.println(str);
        String sign = Md5Util.md5(appKey + appSecret + interfaceName + timestamp + appKey + JSON.toJSONString(data));
        System.out.println(sign);
    }
}
