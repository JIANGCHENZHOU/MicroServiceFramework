package com.cssweb.test;

import com.cssweb.network.RpcService;

/**
 * Created by chenh on 2016/12/27.
 */

@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String username) {
        String result = "Hello " + username;
        System.out.println(result);
        return result;
    }

    @Override
    public int add(int a, int b) {
        return a + b;
    }


}
