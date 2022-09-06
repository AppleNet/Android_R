package com.example.startup.store;

/**
 * com.example.startup.store.Result
 *
 * @author liulongchao
 * @since 2022/9/2
 */
public class Result<T> {

    private T data;

    public Result(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
