package testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.log4j.Logger;
import utils.DateUtil;
import utils.FileEntity;
import utils.StringUtil;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: 接口自动化公用Base类
 * @author: guozhixiong
 * @time: 2020/7/11 10:11
 */
public class BaseTestCase {
    protected Response response;
    protected ResponseSpecification responseSpecification;
    protected RequestSpecification requestSpecification;
    protected static final Logger log = Logger.getLogger(Logger.class);
    /**
     * cookie
     */
    protected String cookie;
    /**
     * 存储值
     */
    protected Map<String, Object> parameterMap = new HashMap<>();

    //excel文件
    protected FileEntity fileEntity = new FileEntity();


    /**
     * 请求头信息和请求参数设置
     */

    protected void createRequestSpec() {
        //拿到header
        Map<String, Object> header = fileEntity.getHeader();
        Map<String, Object> bodyParam = fileEntity.getBodyParam();
        Map<String, Object> pathParam = fileEntity.getPathParam();
        List<Map<String, Object>> bodyParamList = fileEntity.getBodyParamList();
        //因为存在数组格式数据，所以先判断数组
        if(StringUtil.isNotEmpty(bodyParamList)) {
            requestSpecification = given().log().all()
                    .headers(header)
                    .header("cookie", cookie != null ? cookie : "")
                    .body(bodyParamList);
        //如果bodyParam不为空
        } else if(StringUtil.isNotEmpty(bodyParam)) {
            requestSpecification = given().log().all()
                    .headers(header)
                    .header("cookie", cookie != null ? cookie : "")
                    .body(bodyParam);
        } else if (StringUtil.isNotEmpty(pathParam)) {
            requestSpecification = given().log().all()
                    .headers(header)
                    .header("cookie", cookie != null ? cookie : "")
                    .queryParams(pathParam);
        } else {
            requestSpecification = given().log().all()
                    .headers(header)
                    .header("cookie", cookie != null ? cookie : "");
        }
    }

    /**
     * 判断响应结果
     */

    protected void createResponseSpec() {
        Map<String, Object> result = fileEntity.getResult();
        responseSpecification = requestSpecification
                .expect().log().all()
                .statusCode(200);
        if(StringUtil.isNotEmpty(result)) {
            for(String s : result.keySet()) {
                responseSpecification.body(s, equalTo(result.get(s)));
            }
        }
    }
    /**
     * 发送请求
     */
    protected void sendRequest() {
        String method = fileEntity.getMethod();
        String url = fileEntity.getUrl();
        try {
            if(StringUtil.equals(method, "post")) {
                response = responseSpecification.when().post(url);
            } else if(StringUtil.equals(method, "get")) {
                response = responseSpecification.when().get(url);
            } else if(StringUtil.equals(method, "delete")) {
                response = responseSpecification.when().delete(url);
            } else if(StringUtil.equals(method, "put")) {
                response = responseSpecification.when().put(url);
            } else {
                log.info(fileEntity.getTestCaseName() + "没有请求方式");
            }
        } catch (Exception e) {
            log.error("执行用例" + fileEntity.getTestCaseName() + "失败，报错内容：" + e.toString());
            e.printStackTrace();
        }

    }

    /**
     * 封装fileEntity
     * @param platformUrl 平台地址
     * @param data excel文件数据
     */
    protected void setFileEntity(String platformUrl, Map<String, String> data) {
        //获取方法名称
        fileEntity.setMethodName(data.get("MethodName"));
        //获取请求方式
        fileEntity.setMethod(data.get("Method").toLowerCase());
        //获取url
        fileEntity.setUrl(platformUrl + data.get("Url"));
        //获取路径不拼接platformUrl
        fileEntity.setPathUrl(data.get("Url"));
        //获取header
        fileEntity.setHeader(JSON.parseObject(data.get("Header")));
        //获取BodyParam
        String bodyParam = data.get("BodyParam");
        //处理出现请求体为数组格式
        if(bodyParam.startsWith("[")) {
            JSONArray jsonObject = JSON.parseArray(bodyParam);
            Map<String, Object> map = JSON.parseObject(jsonObject.get(0).toString());
            List list = new ArrayList();
            list.add(map);
            fileEntity.setBodyParamList(list);
            //如果上一条用例执行存在param，当前用例不set null，会导致当前用例也存在param数据
            fileEntity.setBodyParam(null);
        } else {
            fileEntity.setBodyParam(JSON.parseObject(bodyParam));
            fileEntity.setBodyParamList(null);
        }
        //获取PathParam
        Map<String, Object> pathParam = JSON.parseObject(data.get("PathParam"));
        if(StringUtil.isNotEmpty(pathParam)) {
            fileEntity.setPathParam(pathParam);
            fileEntity.setBodyParam(null);
        }
        //获取Result
        fileEntity.setResult(JSON.parseObject(data.get("Result")));
        //获取用例名称
        fileEntity.setTestCaseName(data.get("TestCaseName"));
    }

    /**
     * 执行
     */
    protected void startPerformTest() {
        createRequestSpec();
        createResponseSpec();
        sendRequest();
    }


    /**
     * 登录
     * @param url 登录地址
     * @param username 用户名
     * @param password 密码
     * @return 所有cookie
     */
    protected void login(String url, String username, String password, String NdNpOKov1BWGb3qQzlYQR6SVR0HnwIKv) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userCode", username);
        requestBody.put("userPassword", password);
        requestBody.put("NdNpOKov1BWGb3qQzlYQR6SVR0HnwIKv",NdNpOKov1BWGb3qQzlYQR6SVR0HnwIKv);
        Response loginRequest = given().contentType("application/x-www-form-urlencoded")
                .queryParams(requestBody)
                .expect().statusCode(200)
                .when().post(url);
        cookie = "JSESSIONID=" + loginRequest.getCookie("JSESSIONID");
    }

    /**
     * 传入多个值存储response，存储key为传入的值
     * @param parameterUri
     */
    protected void saveResponseParameters(String... parameterUri) {
        for(String uri : parameterUri) {
            String key = uri.substring(uri.lastIndexOf(".") + 1, uri.length());
            parameterMap.put(key, response.jsonPath().get(uri));
        }
    }

    /**
     * 替换bodyParam中的参数，取存储起来的参数
     * @param parameter
     */
    protected void replaceBodyParam(String... parameter){
        for(String par : parameter) {
            fileEntity.getBodyParam().put(par, parameterMap.get(par));
        }
    }

    /**
     * 替换bodyParam中的参数，取随机数
     * @param parameter
     */
    protected void randomCurrentTimeBodyParam(String... parameter){
        for(String par : parameter) {
            String currentTime = DateUtil.getDate(new Date(), "yyyyMMddHHmmssSSS");
            fileEntity.getBodyParam().put(par, "自动化" + currentTime);
            parameterMap.put(par, "自动化" + currentTime);
        }
    }


    /**
     * 上传文件
     * @param contentType 文件类型，例如"multipart/form-data;"
     * @param fileName 文件上传name
     * @param queryParams 上传时的参数
     * @param filePath 上传文件的路径
     * @param url 上传url
     *
     *
     *         String contentType = "multipart/form-data; boundary=----WebKitFormBoundaryM95bASaKslF5vOAO";
     *         String fileName = "files";
     *         Map<String, Object> queryParams = new HashMap<>();
     *         queryParams.put("customerId", "baoli");
     *         queryParams.put("businessTypeKey", "driver");
     *         String filePath = "C:\\Users\\admin\\Pictures\\1.jpg";
     *         String url = "http://58.214.0.26:60006/api/upload/fildUploadPublicByInputStream";
     *         uploadFile(contentType, fileName, queryParams, filePath, url);
     */
    protected void uploadFile(String contentType, String fileName, Map<String, Object> queryParams,
                              String filePath, String url) {
        if(StringUtil.isNotEmpty(queryParams)) {
            given().log().all()
                    .contentType(contentType)
                    .multiPart(fileName, new File(filePath))
                    .queryParams(queryParams)
            .expect().log().all()
                    .statusCode(200)
            .when()
                    .post(url);
        } else {
            given().log().all()
                    .contentType(contentType)
                    .multiPart(fileName, new File(filePath))
            .expect().log().all()
                    .statusCode(200)
            .when()
                    .post(url);
        }

    }


    /**
     * 用于下载文件后保存到本地
     * @param fileName
     */
    public void downloadSaveFile(String fileName) {
        InputStream inputStream = response.asInputStream();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("src/test/resources/downloadfile/" + DateUtil.getCurrentTimeMillis() + fileName);
            byte [] buffer = new byte[1024];
            int len=0;
            while ((len=inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
