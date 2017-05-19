package openapi;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import org.apache.commons.codec.binary.Base64;


public class HmacSHA1Main {
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    /*
     * 展示了一个生成指定算法密钥的过程 初始化HMAC密钥
     * @return
     * @throws Exception
     *
      public static String initMacKey() throws Exception {
      //得到一个 指定算法密钥的密钥生成器
      KeyGenerator KeyGenerator keyGenerator =KeyGenerator.getInstance(MAC_NAME);
      //生成一个密钥
      SecretKey secretKey =keyGenerator.generateKey();
      return null;
      }
     */

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception
    {
        byte[] data=encryptKey.getBytes(ENCODING);
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        //完成 Mac 操作
        return mac.doFinal(text);
    }



//    public static String hamcsha1(byte[] data, byte[] key)
//    {
//        try {
//            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
//            Mac mac = Mac.getInstance("HmacSHA1");
//            mac.init(signingKey);
//            return byte2hex(mac.doFinal(data));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static String byte2hex(byte[] b)
    {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

//    public static String base64(String text) throws UnsupportedEncodingException{
//        byte[] encodeBase64 = Base64.encodeBase64(text.getBytes("UTF-8"));
//        return new String(encodeBase64);
//    }


    public static void main(String[] args) throws Exception {
//	   Signature = Base64( HMAC-SHA1( UTF-8-Encoding-Of( appSecret, StringToSign ) ) );

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'",Locale.US);
        String date = sdf.format(new Date());

//        String date = "Tue, 25 Apr 2017 20:32:34 GMT";
        System.out.println("Date= " + date);
        String content_md5 = "8110534CCE5912264E75C6730E3D94F6";
        String appId = "fc4b5c07119984326925905a7b5009d1";
        String appKey = "tangfangwei";
        String appSecret = "0ba72694ca3c0a98e3338328a2f9c399";

        StringBuilder encryptText = new StringBuilder();
        encryptText
//	   .append("GET /example_rul HTTP/1.1").append("\\n")
                .append("appid: ").append(appId).append("\n")
                .append("date: ").append(date).append("\n")
                .append("content-md5: ").append(content_md5)
                ;

        System.out.println("encryptText=" + encryptText.toString());

        String encryptKey = appSecret;
        System.out.println("encryptKey="+encryptKey);

        byte[] encryptHmacSHA1 = HmacSHA1Encrypt(encryptText.toString(), encryptKey);

//        String encryptstr = byte2hex(encryptHmacSHA1);
//        System.out.println("HmacSHA1Encrypt=" + encryptstr);


//        byte[] encodeBase64 = Base64.encodeBase64(encryptHmacSHA1);
//
////        String base64Encrypt = base64(encryptstr);
//        String base64Encrypt = new String(encodeBase64);
//
//        System.out.println("base64Encrypt=" + base64Encrypt);

//	   jdk 1.8
        byte[] aaa = HmacSHA1Encrypt(encryptText.toString(), encryptKey);
	   String base64encoded_jdk = java.util.Base64.getEncoder().encodeToString(aaa);
       System.out.println("base64encoded_jdk=" + base64encoded_jdk);

//        String apiurl = "http://10.8.7.98:8000/vehicle.bus.aaa.hello";
        String apiurl = "http://10.8.7.98:8000/hello";
        String version = "1.0";
        String code = "8110534CCE5912264E75C6730E3D94F6";

        StringBuilder command = new StringBuilder();
        command.append("curl -i -X GET --url "+ apiurl +" \\").append("\n")
                .append("--header 'AppId: "+ appId +"' \\").append("\n")
                .append("--header 'Date: " + date + "' \\").append("\n")
                .append("--header 'Version: " + version + "' \\").append("\n")
                .append("--header 'Code: " + code + "' \\").append("\n")
                .append("--header 'Content-MD5: "+ content_md5 +"' \\").append("\n")
                .append("--header 'Authorization: hmac username=\""+ appKey + "\", algorithm=\"hmac-sha1\", headers=\"appid date content-md5\", signature=\""+base64encoded_jdk+"\"'");

        System.out.println("command = " + "\n" + command.toString());
    }

}