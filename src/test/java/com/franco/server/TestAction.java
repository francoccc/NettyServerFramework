package com.franco.server;

import com.alibaba.fastjson.JSONObject;
import com.franco.ByteResult;
import com.franco.action.BaseAction;
import com.franco.spring.annotation.Action;
import com.franco.spring.annotation.Command;
import com.franco.spring.annotation.RequestParam;
import com.franco.spring.core.Request;
import com.franco.spring.core.Session;
import org.springframework.beans.factory.annotation.Autowired;

@Action
public class TestAction extends BaseAction {

    @Autowired
    private ITestService testService;

    @Command("test@add")
    public ByteResult add(@RequestParam("a") int a, @RequestParam("b") int b, Request request) {
        final Session session = request.getSession();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // push Task
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("test", 1);
                System.out.println(jsonObject.toJSONString());
                session.push("push@test", jsonObject.toString().getBytes());
            }
        });
        thread.start();
        return getResult(testService.add(a, b), request);
    }
}
