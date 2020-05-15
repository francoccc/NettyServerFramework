package com.franco;

import com.alibaba.fastjson.JSONObject;
import com.franco.action.BaseAction;
import com.franco.spring.annotation.Command;
import com.franco.spring.annotation.RequestParam;
import com.franco.spring.core.Request;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAction extends BaseAction {

    @Autowired
    private ITestService testService;

    @Command("test@add")
    public ByteResult add(@RequestParam("a") int a, @RequestParam("b") int b, Request request) {
        return getResult(testService.add(a, b), request);
    }
}
