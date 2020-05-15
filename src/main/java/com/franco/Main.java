package com.franco;

/**
 * Main
 *
 * @author franco
 */
public class Main {

    public static void main(String[] args) {
        MyBootStrap bootStrap = new MyBootStrap();
        try {
            bootStrap.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
