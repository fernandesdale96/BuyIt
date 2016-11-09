package com.example.dayle_fernandes.final_project;

import java.io.Serializable;

/**
 * Created by dayle_fernandes on 09-Nov-16.
 */

public class CCInfo implements Serializable{
    String cnum;
    String emonth;
    String eyear;
    String cvv;
    String mnum;

    public CCInfo(String cnum, String eyear, String cvv, String mnum, String emonth) {
        this.cnum = cnum;
        this.eyear = eyear;
        this.cvv = cvv;
        this.mnum = mnum;
        this.emonth = emonth;
    }

    public String getCnum() {
        return cnum;
    }

    public String getEmonth() {
        return emonth;
    }

    public String getCvv() {
        return cvv;
    }

    public String getEyear() {
        return eyear;
    }

    public String getMnum() {
        return mnum;
    }
}
