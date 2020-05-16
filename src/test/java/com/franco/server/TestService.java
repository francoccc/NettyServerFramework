package com.franco.server;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Component("testService")
public class TestService implements ITestService {

    @Override
    public byte[] add(int a, int b) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sum", a + b);
        System.out.println(jsonObject.toString());
        return jsonObject.toString().getBytes();
    }
}
