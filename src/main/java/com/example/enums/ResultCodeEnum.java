package com.example.enums;

public enum ResultCodeEnum {

    SUCCESS("666", "成功啦"),
    ERROR("9999", "服务异常"),
    SAVEFAIL("1001", "保存失败"),
    DELFAIL("1002", "删除失败"),
    UPDATEFAIL("1003", "更新失败"),
    NOTHISRE("1004", "查无此记录");

    public String code;

    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ResultCodeEnum getByCode(String code) {
        for (ResultCodeEnum element : values()) {
            if (element.code.equals(code)) {
                return element;
            }
        }
        return ERROR;
    }

    public static ResultCodeEnum getByMsg(String msg) {
        for (ResultCodeEnum element : values()) {
            if (element.msg.equals(msg)) {
                return element;
            }
        }
        return null;
    }
}
