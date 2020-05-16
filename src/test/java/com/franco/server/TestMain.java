package com.franco.server;

import com.franco.MyBootStrap;

/**
 * TestMain
 */
public class TestMain {

    public static void main(String[] args) {
        MyBootStrap mBootStrap = new MyBootStrap();
        try {
            mBootStrap.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
