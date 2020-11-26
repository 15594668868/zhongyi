package testutils;

/**
 * AES 128bit 加密解密工具类
 *
 * @author wangshuai@e6yun.com
 * @date 2020-5-29 10:25:07
 * 参考https://www.cnblogs.com/yadongliang/p/11933891.html
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Set;


public class AesEncryptUtil {
    private static final Logger log = LoggerFactory.getLogger(AesEncryptUtil.class);
    private static final String DEFUALT_SECURITY_KEY = "RlCbkFwgD5ojYqQZ";

    private static final String DEFUALT_SECURITY_IV = "YnZCuXiv3VEOij5r";
    private static final String TOKEN_CONSTANT = "34iCiyBb4xC76d5WmEJ6Mg==";


    /**
     * 加密方法
     *
     * @param data 要加密的数据
     * @param key  加密key
     * @param iv   加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt2Base64(String data, String key, String iv) throws Exception {
        try {
            //"算法/模式/补码方式"NoPadding PkcsPadding
            byte[] encrypted = getEncryptBytes(data, key, iv);

            return new Base64().encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加密后转成hex
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String encrypt2Hex(String data, String key, String iv) throws Exception {
        try {
            //"算法/模式/补码方式"NoPadding PkcsPadding
            byte[] encrypted = getEncryptBytes(data, key, iv);

            return  byteToHex(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  获取加密后字节数组
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static byte[] getEncryptBytes(String data, String key, String iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        int blockSize = cipher.getBlockSize();

        byte[] dataBytes = data.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }

        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        return cipher.doFinal(plaintext);
    }

    /**
     * 解密方法
     *
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv   解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static synchronized String desEncryptByBase64(String data, String key, String iv) throws Exception {
        byte[] encrypted1 = new Base64().decode(data);
        return getDesString(key, iv, encrypted1);

    }

    /**
     * 解密方法
     *
     * @param hexString 要解密的数据
     * @param key       解密key
     * @param iv        解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static synchronized String desEncryptByHexStr(String hexString, String key, String iv) throws Exception {
        byte[] encrypted1 = hex2byte(hexString);
        return getDesString(key, iv, encrypted1);

    }

    /**
     * 获取明文串
     *
     * @param key
     * @param iv
     * @param encrypted1
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     */
    private static String getDesString(String key, String iv, byte[] encrypted1) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

        byte[] original = cipher.doFinal(encrypted1);
        byte[] original2 = new Base64().encode(original);
        byte[] original3 = new Base64().decode(original2);
        return URLDecoder.decode(new String(original3, "UTF-8").trim(), "UTF-8");
    }

    /**
     * 使用默认的key和iv加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData2Base64(String data, String key, String iv) throws Exception {
        if (StringUtils.isEmpty(data.trim())) {
            return data;
        }
        key = Optional.ofNullable(key).orElse(DEFUALT_SECURITY_KEY);
        iv = Optional.ofNullable(iv).orElse(DEFUALT_SECURITY_IV);
        return encrypt2Base64(data, key, iv);
    }
    /**
     * 使用默认的key和iv加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData2Hex(String data, String key, String iv) throws Exception {
        if (StringUtils.isEmpty(data.trim())) {
            return data;
        }
        key = Optional.ofNullable(key).orElse(DEFUALT_SECURITY_KEY);
        iv = Optional.ofNullable(iv).orElse(DEFUALT_SECURITY_IV);
        return encrypt2Hex(data, key, iv);
    }

    /**
     * 使用默认的key和iv解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncryptDataByBase64(String data, String key, String iv) throws Exception {
        if (StringUtils.isEmpty(data.trim())) {
            return data;
        }
        key = Optional.ofNullable(key).orElse(DEFUALT_SECURITY_KEY);
        iv = Optional.ofNullable(iv).orElse(DEFUALT_SECURITY_IV);
        return desEncryptByBase64(data, key, iv);
    }

    /**
     * 使用默认的key和iv解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncryptDataByHexStr(String data, String key, String iv) throws Exception {
        if (StringUtils.isEmpty(data.trim())) {
            return data;
        }
        key = Optional.ofNullable(key).orElse(DEFUALT_SECURITY_KEY);
        iv = Optional.ofNullable(iv).orElse(DEFUALT_SECURITY_IV);
        return desEncryptByHexStr(data, key, iv);
    }

    /**
     * 递归读取所有的key，并解密value
     *
     * @param jsonStr
     */
    public static String getAllKeyByBase64(String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Set<String> keysSet = jsonObject.keySet();
        keysSet.forEach(key -> {
            Object value = jsonObject.get(key);
            String str = "";
            try {
                if (value instanceof String) {
                    str = AesEncryptUtil.desEncryptDataByBase64((String) value, null, null);
                }
                try {
                    JSONObject.parseObject((String) value);
                } catch (Exception e) {

                }
                getAllKeyByBase64((String) value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonObject.put(key, str);
        });
        return jsonObject.toJSONString();
    }

    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 将hex字符串转换成字节数组
     *
     * @param str hexStr
     * @return
     */

    private static byte[] hex2byte(String str) {
        if (str == null || str.length() < 2||str.equals(TOKEN_CONSTANT)) {
            return new byte[0];
        }
        str = str.toLowerCase();
        int l = str.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = str.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }

    /**
     * byte数组转hex
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes){
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            // 每个字节由两个字符表示，位数不够，高位补0
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
        }
        return sb.toString().trim();
    }


    public static void main(String[] args) throws Exception {
        String toEncryptStr="admin中国34543123admin中国34543123admin中国34543123admin中国34543123admin中国34543123admin中国34543123";
        System.out.println("加密前：" + toEncryptStr);
        String hexString = encryptData2Hex(toEncryptStr, null, null);
        System.out.println("加密后：" + hexString);
        // 调用解密方法
        String desEncryptStr = desEncryptDataByHexStr(hexString,null,null);
        System.out.println("解密后：" + desEncryptStr);
    }

}
