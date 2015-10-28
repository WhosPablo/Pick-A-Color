package com.parango.pick_a_color.util;



public class HSVColor {
    private float hueStart;
    private float hueEnd;
    private float saturation;
    private float value;

    public HSVColor(float start, float end, float saturation, float value){

        this.hueStart = start;
        this.hueEnd = end;
        this.saturation = saturation;
        this.value = value;
    }

//    private float toCorrectRange( float num){
//        float tempnum = num;
//        if(tempnum < 0){
//            tempnum = -1*tempnum;
//            if(tempnum > 360){
//                tempnum = tempnum%360;
//            }
//            tempnum = 360 - tempnum;
//
//        }
//        if(tempnum > 360){
//            tempnum = tempnum%360;
//        }
//        return  tempnum;
//    }

    public float getHueStart(){
        return hueStart;
    }

    public float getHueEnd(){
        return hueEnd;
    }

    public float getSaturation(){
        return saturation;
    }

    public float getValue(){
        return value;
    }

}
