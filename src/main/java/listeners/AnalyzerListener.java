package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.ConfigFile;

/**
 * @description: 重试监听器
 * @author: guozhixiong
 * @time: 2020/8/12 9:42
 */
public class AnalyzerListener implements IRetryAnalyzer {
    //统计测试用例失败的次数
    private int retryCount = 0;
    //测试用例最大失败次数
    private final int MAX_RETRY_COUNT = Integer.parseInt(ConfigFile.getValue("max.retry.count"));


    /**
     * 设置testng用例失败重试次数
     * @param result
     * @return
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true;
        }
        return false;
    }
}
