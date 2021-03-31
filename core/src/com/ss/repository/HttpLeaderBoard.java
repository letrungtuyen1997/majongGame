package com.ss.repository;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.GMain;
import com.ss.gameLogic.config.Config;
import com.ss.utils.Utils;

public class HttpLeaderBoard {
    private int                             status;
    private HttpLeaderBoard.GetLeaderBoard  getData;
    private Utils                           utils;
    private String                          UriLeaderboard       = "http://f901a710-default-defaultin-56c2-2091124869.ap-southeast-1.elb.amazonaws.com/mahj-ldb/";
//    private String                          UriLeaderboard       = "https://event.vongxoaytulinh.tk/mahj-ldb/";


    public interface GetLeaderBoard {
        void getLeaderBoard(JsonValue data);
        void Fail(String s);

    }

    public void setIGetdata(HttpLeaderBoard.GetLeaderBoard getData){
        this.getData = getData;
    }

    public HttpLeaderBoard(){

    }

    public void GetLeaderBoard(long id, String name, long star ){
        String reqUrl = UriLeaderboard + "record?userId=" + id + "&name=" + name + "&score=" + star;
        System.out.println("log uri: "+reqUrl);
        Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
        httpGet.setUrl(reqUrl);
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.setTimeOut(15000);
        Gdx.net.sendHttpRequest(httpGet, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                status = httpResponse.getStatus().getStatusCode();
                GMain.platform.log("check get token: "+status);
                System.out.println("here: "+status);
               // if(status==200){
                    String data = httpResponse.getResultAsString();
                    GMain.platform.log("check get token: "+data);
                    System.out.println("check data: "+data);
                    JsonValue jv = utils.GetJsV(data);
                    getData.getLeaderBoard(jv);
                //}else {
                    //getData.Fail("loi mang");

                //}
            }

            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
                getData.Fail("load that bai!!");
//                postData.Fail("load that bai!!");

            }

            @Override
            public void cancelled() {

            }
        });
    }



}
