package com.comvee.hospitalabbott.bean;

import java.io.Serializable;

public class BloodSugarChatInfo implements Serializable {

    public String avgNum;//";// "3",
    public String awiTime;//;// 60,
    public String eventCount;//


    public String avgAwiTimeOfHigh;// 510,
    public String avgAwiTimeOfLow;// "0.00",
    public String avgAwiTimeOfNormal;// 202,
    public float awiTimeRateOf13_9;// "--",
    public float awiTimeRateOf3_9;// "--",
    public float awiTimeRateOf4_0;// "--",
    public String awiTimeRateOfHigh;// "70.83",
    public String awiTimeRateOfLow;// "0.00",
    public float awiTimeRateOfNormal;

    public String eventCountOfHigh;// 2,
    public String eventCountOfLow;// 0,
    public String eventCountOfNormal;// 2,
    public String highLineVal;// 10,
    public String lowLineVal;// 3.9,
    public String meanAmplitudeOfGlycemicExcursion;// "4.37",
    public String standardVal;// "2.38"


    public int chartShow;//1：显示 0:不显示


    public ChartData chartData;//";// {
    public String coefficientOfVariation;


    public class ChartData implements Serializable {
        public float[][] dataSource;
//        public String[] xArea;
    }


}
