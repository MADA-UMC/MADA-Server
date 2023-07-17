package com.umc.mada.login.controller;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpSession;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.message.BasicNameValuePair;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;

public class KakaoRestApi {
//    private final static String K_CLIENT_ID = "";
//    private final static String K_REDIRECT_URI="";
//
//    public String getAuthorizationUrl(HttpSession session) {
//
//        String kakoUrl = "https://kauth.kakao.com/oauth/authorize?"
//                +"client_id=" + K_CLIENT_ID + "&redirect_uri="
//                + K_REDIRECT_URI + "&response_type=code";
//        return kakoUrl;
//    }
//
//    public JsonNode getAccessToken(String autorize_code) throws ClientProtocolException, IOException {
//
//        final String RequestUrl = "https://kauth.kakao.com/oauth/token";
//        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
//        postParams.add(new BasicNameValuePair("client_id", K_CLIENT_ID)); //REST API KEY
//        postParams.add(new BasicNameValuePair("redirect_uri", K_REDIRECT_URI)); // redrect uri
//        postParams.add(new BasicNameValuePair("code", autorize_code));
//
//        final HttpClient client = HttpClientBuilder.create().build();
//        final HttpPost post = new HttpPost(RequestUrl);
//        JsonNode returnNode=null;
//
//        try {
//            post.setEntity(new UrlEncodedFormEntity(postParams));
//            final HttpResponse response = client.execute(post);
//            final int reposeCode =response.getStatusLine().getStatusCode();
//
//
//            ObjectMapper mapper = new ObjectMapper();
//            returnNode = mapper.readTree(response.getEntity().getContent());
//
//        }catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        System.out.println(returnNode);
//
//        return returnNode;
//    }
//
//    public JsonNode getKakaoUserInfo(String accessToken) throws ClientProtocolException, IOException {
//
//        final String RequestUrl = "";
//
//        final HttpClient client = HttpClientBuilder.create().build();
//        final HttpPost post = new HttpPost(RequestUrl);
//
//        post.addHeader("Authorization", "Bearer " + accessToken);
//
//        JsonNode returnNode =null;
//
//        try {
//
//            final HttpResponse response = client.execute(post);
//            final int responseCode = response.getStatusLine().getStatusCode();
//
//
//            ObjectMapper mapper = new ObjectMapper();
//            returnNode = mapper.readTree(response.getEntity().getContent());
//
//        }catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        }catch (ClientProtocolException e) {
//            e.printStackTrace();
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        System.out.println(returnNode);
//        return returnNode;
//    }
//
//    public JsonNode Logout(String autorize_code) {
//
//        final String RequestUrl = "https://kapi.kakao.com/v1/user/logout";
//        final HttpClient client = HttpClientBuilder.create().build();
//        final HttpPost post = new HttpPost(RequestUrl);
//        post.addHeader("Authorization", "Bearer " + autorize_code);
//        JsonNode returnNode = null;
//
//        try {
//            final HttpResponse response = client.execute(post);
//            ObjectMapper mapper = new ObjectMapper();
//            returnNode = mapper.readTree(response.getEntity().getContent());
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return returnNode;
//    }

}