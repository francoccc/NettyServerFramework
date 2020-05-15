package com.franco;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

@Component("testService")
public class TestService implements ITestService {

    @Override
    public byte[] add(int a, int b) {
        int sum = a + b;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sum", a+b);
        System.out.println(jsonObject.toString());
        return jsonObject.toString().getBytes();
    }
}
