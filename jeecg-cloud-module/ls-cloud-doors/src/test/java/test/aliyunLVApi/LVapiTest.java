package test.aliyunLVApi;

import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LVapiTest {
    //web平台端
    static String appKey = "29177123";
    static String appSecret = "c1c0124e320d079b6925a19b546fd16d";
    static String token = "1e4ce6edb37547b6aa1341b4a9eb7bee";
    //Android APP端
    //    static String appKey = "31797845";
    //    static String appSecret = "f31b198f8e8efd78ad0ab1698b70fe59";
    public static void main(String[] args) {
//        getToken();
        queryIdentityByPage();
//        importAccount();
    }
    public static String getToken(){
        IoTApiClientBuilderParams ioTApiClientBuilderParams = new IoTApiClientBuilderParams();
        //web平台端
        ioTApiClientBuilderParams.setAppKey(appKey);
        ioTApiClientBuilderParams.setAppSecret(appSecret);
        //Android APP端
//        ioTApiClientBuilderParams.setAppKey("31797845");
//        ioTApiClientBuilderParams.setAppSecret("f31b198f8e8efd78ad0ab1698b70fe59");
        SyncApiClient syncClient = new SyncApiClient(ioTApiClientBuilderParams)    ;

        IoTApiRequest request = new IoTApiRequest();
        //设置API的版本
        //调用/cloud/token接口时，不需要传CloudToken，请参见获取云端资源Token接口的代码示例
        //调用其他接口（除获取云端资源Token接口外），都需要先调用获取云端资源Token接口，获取到token后，再传入ApiVer和CloudToken，才可以正常调用
        request.setApiVer("1.0.0");
//        request.setCloudToken("6285a9da389c40609afe1e22d7f46c0f");

        //设置接口的参数
        request.putParam("grantType", "project");
//        request.putParam("res", "a123BeJz2yycGU8y"); //授权的资源ID。grantType为project时，res的值为project的ID。
        request.putParam("res", "a123KB0m1So5VBvL"); //授权的资源ID。grantType为project时，res的值为project的ID。

        request.putParam("offset",0);
        request.putParam("count",10000);
        try {
            //请求参数域名、path、request
            //host地址  中国站：api.link.aliyun.com     新加坡：ap-southeast-1.api-iot.aliyuncs.com     美国（弗吉尼亚）：us-east-1.api-iot.aliyuncs.com     德国（法兰克福）：eu-central-1.api-iot.aliyuncs.com
            String apiUrl = "/cloud/token";
//            String apiUrl = "/cloud/account/queryIdentityByPage";
            ApiResponse response = syncClient.postBody("api.link.aliyun.com", apiUrl, request, true);
            System.out.println( "response code = " + response.getCode() + " response = " + new String(response.getBody(), "UTF-8"));
            return new String(response.getBody(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String queryIdentityByPage(){
        IoTApiClientBuilderParams ioTApiClientBuilderParams = new IoTApiClientBuilderParams();
//        ioTApiClientBuilderParams.setAppKey("29177123");
//        ioTApiClientBuilderParams.setAppSecret("c1c0124e320d079b6925a19b546fd16d");
        ioTApiClientBuilderParams.setAppKey(appKey);
        ioTApiClientBuilderParams.setAppSecret(appSecret);
        SyncApiClient syncClient = new SyncApiClient(ioTApiClientBuilderParams);

        IoTApiRequest request = new IoTApiRequest();
        //设置API的版本
        //调用/cloud/token接口时，不需要传CloudToken，请参见获取云端资源Token接口的代码示例
        //调用其他接口（除获取云端资源Token接口外），都需要先调用获取云端资源Token接口，获取到token后，再传入ApiVer和CloudToken，才可以正常调用
        request.setApiVer("1.0.0");
        request.setCloudToken(token);

        //设置接口的参数
        request.putParam("grantType", "project");
//        request.putParam("res", "a123BeJz2yycGU8y"); //授权的资源ID。grantType为project时，res的值为project的ID。
        request.putParam("res", "a123KB0m1So5VBvL"); //授权的资源ID。grantType为project时，res的值为project的ID。

        request.putParam("offset",1);
        request.putParam("count",10);
        try {
            //请求参数域名、path、request
            //host地址  中国站：api.link.aliyun.com     新加坡：ap-southeast-1.api-iot.aliyuncs.com     美国（弗吉尼亚）：us-east-1.api-iot.aliyuncs.com     德国（法兰克福）：eu-central-1.api-iot.aliyuncs.com
//            String apiUrl = "/cloud/token";
            String apiUrl = "/cloud/account/queryIdentityByPage";
            ApiResponse response = syncClient.postBody("api.link.aliyun.com", apiUrl, request, true);
            System.out.println( "response code = " + response.getCode() + " response = " + new String(response.getBody(), "UTF-8"));
            return new String(response.getBody(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void importAccount(){
        // https://github.com/aliyun/iotx-api-gateway-client
        IoTApiClientBuilderParams ioTApiClientBuilderParams = new IoTApiClientBuilderParams();
        ioTApiClientBuilderParams.setAppKey(appKey);
        ioTApiClientBuilderParams.setAppSecret(appSecret);
//        ioTApiClientBuilderParams.setAppKey("31797845");
//        ioTApiClientBuilderParams.setAppSecret("f31b198f8e8efd78ad0ab1698b70fe59");
        SyncApiClient syncApiClient = new SyncApiClient(ioTApiClientBuilderParams);
        IoTApiRequest request = new IoTApiRequest();
// 设置请求ID
        String uuid = UUID.randomUUID().toString();
        String id = uuid.replace("-", "");
        request.setId(id);
// 设置API版本号
        request.setApiVer("1.0.0");
// 设置参数
        request.putParam("openId","zhuxueshi001");
        request.putParam("nickName","zxs");
        request.putParam("phone","15013778598");
        request.putParam("email","122458203@qq.com");
        request.putParam("avatarUrl","http://182.106.236.230:8123/pic/20/student/47832.jpg");
        request.putParam("creater","zxs");
        request.putParam("loginName","zxs");
// 如果需要，设置headers
        Map<String, String> headers = new HashMap<String, String>(8);
// headers.put("你的<header", "你的<value>");

// 设置请求参数域名、path、request , isHttps, headers
        try {
            ApiResponse response = syncApiClient.postBody("api.link.aliyun.com", "/user/account/thirdparty/import", request, true, headers);
            System.out.println(
                    "response code = " + response.getCode()
                            + " response = " + new String(response.getBody(), "UTF-8")
                            + " headers = " + response.getHeaders().toString()
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
