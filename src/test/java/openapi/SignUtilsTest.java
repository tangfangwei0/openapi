package openapi;

import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tangfw on 2017/4/25.
 */

public class SignUtilsTest {
    private boolean isGet = true;

    //test 10.8.7.98
    //didibus
//    private String appId= "fc4b5c07119984326925905a7b5009d1";
//    private String appKey="tangfangwei";
//    private String appSecret="0ba72694ca3c0a98e3338328a2f9c399";
//    private String apiurl = "http://10.8.7.98:8000/vehicle.bus.aaa.hello";
////    private String apiurl = "http://10.8.7.97:8000/vehicle.bus.aaa.hello";
////    private String apiurl = "http://10.8.7.98:8000/hello";
//
//    private String version = "1.0";
//    private String code = "8110534CCE5912264E75C6730E3D94F6";
//    private String requestBodyJason = "{\"username\":\"Jack\",\"age\":\"30\",\"weight\":\"60\"}";
//    private String content_type = "Content-Type: application/json;charset=UTF-8";


    //ofo
    private String appId= "e3a6073c4adb1ae087693973d2980725";
    private String appKey="tangfangwei_ofo";
    private String appSecret="a0abb00cdad149f5a0937429af175e1a";
    private String apiurl = "http://10.8.7.98:8000/vehicle.bus.aaa.hello";
//    private String apiurl = "http://10.8.7.98:8000/hello";

    private String version = "1.0";
    private String code = "8110534CCE5912264E75C6730E3D94F6";
    private String requestBodyJason = "{\"username\":\"Jack\",\"age\":\"30\",\"weight\":\"60\"}";
    private String content_type = "Content-Type: application/json;charset=UTF-8";




    //test 10.23.38.97
//    private String appId= "app_id_tangfw";
//    private String appKey="app_key_tangfw";
//    private String appSecret="app_secret_tangfw";
//    private String apiurl = "http://10.23.38.97:8000/hello";
//
//    private String version = "1.0";
//    private String code = "8110534CCE5912264E75C6730E3D94F6";
//    private String requestBodyJason = "{\"username\":\"Jack\",\"age\":\"30\",\"weight\":\"60\"}";
//    private String content_type = "Content-Type: application/json;charset=UTF-8";

    public String genGetMD5() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "Jack");
        params.put("age", "30");
        params.put("weight", "60");

        String content_MD5 = null;
        try {
            content_MD5 = SignUtils.signToGetMD5(params, appSecret);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("content_MD5=" + content_MD5);
        return content_MD5;
    }

    @Test
    public void testGenGetMD5() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "Jack");
        params.put("age", "30");
        params.put("weight", "60");

        String content_MD5 = null;
        try {
            content_MD5 = SignUtils.signToGetMD5(params, appSecret);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("content_MD5=" + content_MD5);
    }


    public String genPOSTMD5() {
        String content_MD5 = null;
        try {
            content_MD5 = SignUtils.signToPOSTMD5(requestBodyJason, appSecret);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("content_MD5=" + content_MD5);
        return content_MD5;
    }

    public String genGMTDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        String date = sdf.format(new Date());
//        System.out.println("Date= " + date);
        return date;
    }

    public String genSignature() {
        Map<String, String> headers = genHeaderMap();

        List signHeaders = new ArrayList();
        signHeaders.add("AppId");
        signHeaders.add("Date");
        signHeaders.add("Content-MD5");

        String signature = "";
        try {
            signature = SignUtils.signToHmacSHA1(headers, signHeaders, appSecret);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("signature =" + signature);
        return signature;
    }

    public Map<String, String> genHeaderMap(){
        Map<String, String> headers = new HashMap<String, String>();

        String date = genGMTDate();
        String content_MD5 = "";
        if(isGet){
            content_MD5 = genGetMD5();
        } else {
            content_MD5 = genPOSTMD5();
        }

        headers.put("AppId", appId);
        headers.put("Date", date);
        headers.put("Version", version);
        headers.put("Code", code);
        headers.put("Content-MD5", content_MD5);
//        System.out.println("head map =" + headers.toString());
        return headers;
    }

    public String genHeaders(){
        return "appid date content-md5";
    }

    public String genAuthorization(){
        StringBuilder authorization = new StringBuilder();
        String headers = genHeaders();
        String signature = genSignature();
        authorization
                .append("Authorization: ")
                .append("hmac ")
                .append("username=\"" + appKey+"\", ")
                .append("algorithm=\"hmac-sha1\", ")
                .append("headers=\"" + headers + "\", ")
                .append("signature=\"" + signature + "\"");

        System.out.println("authorization =" + authorization);

        return authorization.toString();
    }

    @Test
    public void genCURlRequestGET(){
        isGet = true;
        StringBuilder curl = new StringBuilder();
        Map<String, String> headers = genHeaderMap();

        curl.append("curl -i -X GET --url ").append(apiurl).append("?username=Jack\\&age=30\\&weight=60").append(" \\").append("\n");
        for(String headerName : headers.keySet()){
            curl.append("--header '").append(headerName).append(": ").append(headers.get(headerName)).append("' \\").append("\n");
        }
        curl.append("--header '").append(genAuthorization()).append("'");

        System.out.println("curl = \n" + curl.toString());
    }

    @Test
    public void genCURlRequestPOST(){
        isGet = false;
        StringBuilder curl = new StringBuilder();
        Map<String, String> headers = genHeaderMap();

        curl.append("curl -i -X POST --url ").append(apiurl).append(" \\").append("\n");
        curl.append("--header '").append(content_type).append("' \\").append("\n");
        for(String headerName : headers.keySet()){
            curl.append("--header '").append(headerName).append(": ").append(headers.get(headerName)).append("' \\").append("\n");
        }
        curl.append("--header '").append(genAuthorization()).append("' \\").append("\n");
        curl.append("--data '").append(requestBodyJason).append("'");

        System.out.println("curl = \n" + curl.toString());
    }

    @Test
    public void genABRequest(){
        isGet = true;
        StringBuilder curl = new StringBuilder();
        Map<String, String> headers = genHeaderMap();

        int c = 300;
        int n = 100000;

        curl.append("ab -c " + c + " -n " + n + " ").append(" \\").append("\n");
        for(String headerName : headers.keySet()){
            curl.append("-H '").append(headerName).append(": ").append(headers.get(headerName)).append("' \\").append("\n");
        }
        curl.append("-H  '").append(genAuthorization()).append("' \\").append("\n");
        curl.append("-p '").append("/home/install/src/test/ab/json").append("' ").append(apiurl);

        System.out.println("curl = \n" + curl.toString());
    }


}
