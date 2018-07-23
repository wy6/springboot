package com.example.common;

import com.example.enums.ResultCodeEnum;

/**
 * @Description: Specification for uniform return data.
 * @Author: WangY
 * @Date: Created in 2018/7/19 17:07
 * @Modified By：
 */

public class BaseResult<T> {

    public String returnCode;

    public String returnMsg;

    public T resData;

    public BaseResult(T data) {
        super();
        this.returnCode = "6666";
        this.returnMsg = "操作成功啦！";
        this.resData = data;
    }

    public BaseResult(String code, String msg, T data) {
        super();
        this.returnCode = code;
        this.returnMsg = msg;
        this.resData = data;
    }

    public static BaseResult ok() {
        return new BaseResult(null);
    }

    public static <T> BaseResult<T> ok(T data) {
        return new BaseResult<>(data);
    }

    public static BaseResult error(ResultCodeEnum codeEnum) {
        return new BaseResult(codeEnum.getCode(), codeEnum.getMsg(), null);
    }

    public static BaseResult error(String code, String msg) {
        return new BaseResult(code, msg, null);
    }

    public static BaseResult fail() {
        return error(ResultCodeEnum.ERROR);
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public T getResData() {
        return resData;
    }

    public void setResData(T resData) {
        this.resData = resData;
    }
}
