package com.example.common.enums;

public enum CallEum {
    STATUS_NO("未叫号"),
    STATUS_OK("已叫号"),
    ;

    public String status;

    CallEum(String status) {
        this.status = status;
    }
}
