package com.example.dayle_fernandes.final_project;

import java.util.ArrayList;



/**
 * Created by dayle_fernandes on 09-Nov-16.
 */

public class testCCList {

    private static testCCList instance = new testCCList();
    private  static ArrayList<CCInfo> ccinfo;

    private testCCList(){
        ccinfo = new ArrayList<CCInfo>();
    }

    public static void addCC(CCInfo cc){
        ccinfo.add(cc);
    }

    public static ArrayList<CCInfo> getCC(){
        return ccinfo;
    }



}
