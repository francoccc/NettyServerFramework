package com.franco.common;

/**
 * Tuple
 *
 * @author franco
 */
public class Tuple<L, R> {

    /** 左值  */
    public L left;
    public R right;

    public Tuple(L left, R right) {
        this.left = left;
        this.right = right;
    }
}
