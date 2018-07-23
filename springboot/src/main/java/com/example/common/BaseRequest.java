package com.example.common;

/**
 * @Description: Data specification for unified request Information.
 * @Author: WangY
 * @Date: Created in 2018/7/19 16:51
 * @Modified By：
 */

public class BaseRequest<T> {

    private T reqData;

    public T getReqData() {
        return reqData;
    }

    public void setReqData(T reqData) {
        this.reqData = reqData;
    }
}
