package com.skin.minicraft.pe.skinmeloforminecraft;

public class Utils {

    public  static  String handleErrorMessage(String message){
        if (BuildConfig.DEBUG){
            return  message;
        }
        return  "Error";
    }
}
