package com.wzhy.viewpagerserial.nav;

import java.io.Serializable;

public class FuncEntity implements Serializable {


    /**
     * funName : 关注
     * id : 2
     */

    private String funName;
    private int id;


    public FuncEntity() {
    }

    public FuncEntity(String funName, int id) {
        this.funName = funName;
        this.id = id;
    }


    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}