package openapi;


import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Created by tangfw on 2017/4/25.
 */
public class SignUtils {
    public static final String SIGN_METHOD_MD5 = "MD5";
    public static final String SIGN_METHOD_HMAC_SHA1 = "HmacSHA1";
    public static final String CHARSET_UTF8 = "UTF-8";

    public static String signToGetMD5(Map<String, String> params, String secret) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        query.append(secret);
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtils.areNotEmpty(key, value)) {
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5加密
        query.append(secret);
        byte[] bytes = encryptMD5(query.toString());

        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }

    public static String signToPOSTMD5(String requestBodyJason, String secret) throws IOException {
        // 拼装字符串
        StringBuilder query = new StringBuilder();
        query.append(secret).append(requestBodyJason).append(secret);

        // 使用MD5加密
        byte[] bytes = encryptMD5(query.toString());

        // 把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }

    public static String signToHmacSHA1(Map<String, String> headers, List<String> signHeaders, String secret) throws IOException {
        // 第一步：把参与鉴名header的参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        for (String key : signHeaders) {
            String value = headers.get(key);
            if (StringUtils.areNotEmpty(key, value)) {
                query.append(key.toLowerCase()).append(": ").append(value).append("\n");
            }
        }

        // 第二步：去掉最后的"\n"
        String headerStr = query.toString();
        if(headerStr.endsWith("\n")){
            headerStr = headerStr.substring(0, headerStr.length()-1);
        }
        System.out.println("signature =" + headerStr);

        // 第三步：使用HmacSHA1加密
        byte[] bytes = encryptHMACSHA1(headerStr, secret);

        // 第四步：BASE 64 加密
        String base64encoded = Base64.getEncoder().encodeToString(bytes);

        return base64encoded;
    }

    public static byte[] encryptHMACSHA1(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(CHARSET_UTF8),SIGN_METHOD_HMAC_SHA1);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    public static byte[] encryptMD5(String data) throws IOException {
        return encryptMD5(data.getBytes(CHARSET_UTF8));
    }

    private static byte[] encryptMD5(byte[] bytes) {
        MessageDigest md5= null;
        try {
            md5 = MessageDigest.getInstance(SIGN_METHOD_MD5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5.digest(bytes);
    }



    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }




}
