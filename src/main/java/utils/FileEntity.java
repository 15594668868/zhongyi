package utils;

import java.util.List;
import java.util.Map;

/**
 * @description: excel列内容
 * @author: guozhixiong
 * @time: 2020/8/11 8:53
 */
public class FileEntity {
    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 请求方式(post/get/delete/put)
     */
    private String method;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求路径
     */
    private String pathUrl;
    /**
     * 请求头
     */
    private Map<String, Object> header;
    /**
     * 请求参数map
     */
    private Map<String, Object> bodyParam;
    /**
     * 请求参数List
     */
    private List<Map<String, Object>> bodyParamList;

    /**
     * 请求参数map
     */
    private Map<String, Object> pathParam;

    /**
     * 判断内容
     */
    private Map<String, Object> result;

    /**
     * 用例名称
     * @return
     */
    private String testCaseName;
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Map<String, Object> getBodyParam() {
        return bodyParam;
    }

    public void setBodyParam(Map<String, Object> bodyParam) {
        this.bodyParam = bodyParam;
    }

    public Map<String, Object> getPathParam() {
        return pathParam;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public void setPathParam(Map<String, Object> pathParam) {
        this.pathParam = pathParam;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public List<Map<String, Object>> getBodyParamList() {
        return bodyParamList;
    }

    public void setBodyParamList(List<Map<String, Object>> bodyParamList) {
        this.bodyParamList = bodyParamList;
    }
}
