package com.comvee.hospitalabbott.tool;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.bean.BloodRangeBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by comv098 on 2017/2/10.
 */

public class TestResultDataUtil {
    public static final float maxValues = 55.9f;
    public static final int lowValues = 4;
    public static final int highValues = 10;

    public static final String bloodStateHigh = "高";
    public static final String bloodStateNormal = "正常";
    public static final String bloodStateLow = "低";

    //public static final String TIME_BEFORE_DAWN = "凌晨";
    public static final String TIME_0AM = "0AM";
    public static final String TIME_3AM = "3AM";
    public static final String TIME_FAST = "空腹";
    public static final String TIME_AFTER_BREAKFAST = "早餐后";
    public static final String TIME_BEFORE_LUNCH = "午餐前";
    public static final String TIME_AFTER_LUNCH = "午餐后";
    public static final String TIME_BEFORE_DINNER = "晚餐前";
    public static final String TIME_AFTER_DINNER = "晚餐后";
    public static final String TIME_BEFORE_BED = "睡前";
    public static final String TIME_RANDOM = "随机";

    //public static String PARAM_CODE_BEFOREDAWN = "beforedawn";              //凌晨
    public static String PARAM_CODE_0AM = "beforedawn";              //0点
    public static String PARAM_CODE_3AM = "3am";              //3点
    public static String PARAM_CODE_BEFOREBREAKFAST = "beforeBreakfast";    //空腹
    public static String PARAM_CODE_AFTERBREAKFAST = "afterBreakfast";      //早餐后
    public static String PARAM_CODE_BEFORELUNCH = "beforeLunch";            //午餐前
    public static String PARAM_CODE_AFTERLUNCH = "afterLunch";              //午餐后
    public static String PARAM_CODE_BEFOREDINNER = "beforeDinner";          //晚餐前
    public static String PARAM_CODE_AFTERDINNER = "afterDinner";            //晚餐后
    public static String PARAM_CODE_BEFORESLEEP = "beforeSleep";            //睡觉前
    public static String PARAM_CODE_RANDOMTIME = "randomtime";              //随机

    private String timeStr;
    private String bloodStr;
    private List<String> strList;
    private List<Integer> imageList;

    public TestResultDataUtil(String timeStr, String bloodStr) {
        this.timeStr = timeStr;
        this.bloodStr = bloodStr;
        initData();
    }

    /**
     * 当测量的血糖数值偏高时：
     * 空腹：1、晚餐进食太多     2、额外加点心   3、未按医嘱服药
     * 早餐后：1、早餐进食太多     2、额外加点心   3、未按医嘱服药
     * 午餐前：1、早餐进食太多     2、额外加点心   3、未按医嘱服药
     * 午餐后：1、午餐进食太多     2、额外加点心   3、未按医嘱服药
     * 晚餐前：1、午餐进食太多     2、额外加点心   3、未按医嘱服药
     * 晚餐后：1、晚餐进食太多     2、额外加点心   3、未按医嘱服药
     * 睡前：1、晚餐进食太多     2、额外加点心   3、未按医嘱服药
     * 凌晨：1、晚餐进食太多     2、额外加点心   3、未按医嘱服药
     * 当测量的血糖数值偏低时：
     * 空腹：：晚餐进食减少  2、超量饮酒    3、空腹运动    4、未按医嘱服药
     * 早餐后：1、早餐进食减少  2、工作强度增加   3、餐后运动过量   4、未按医嘱服药
     * 午餐前：1、早餐进食减少  2、工作强度增加   3、餐后运动过量   4、未按医嘱服药
     * 午餐后：1、午餐进食减少    2、超量饮酒 3、餐后运动过量   4、未按医嘱服药
     * 晚餐前：1、午餐进食减少    2、工作强度增加 3、餐后运动过量   4、未按医嘱服药
     * 晚餐后：1、晚餐进食减少    2、超量饮酒 3、餐后运动过量   4、未按医嘱服药
     * 睡前：1、晚餐进食减少  2、超量饮酒    3、餐后运动过量    4、未按医嘱服药
     * 凌晨：1、晚餐进食减少  2、超量饮酒    3、餐后运动过量    4、未按医嘱服药
     * 随机血糖：
     * 1、感觉不适；2、加餐；3、胰岛素释放延后；
     * c）其他原因备注
     */
    private void initData() {
        strList = new ArrayList<>();
        imageList = new ArrayList<>();
        if (!timeStr.equals(TIME_RANDOM)) { //判断是否是随机
            if (bloodStr.equals(bloodStateHigh)) { //偏高
                if (timeStr.equals(TIME_FAST)) {
                    strList.add("晚餐进食太多");
                } else if (timeStr.equals(TIME_AFTER_BREAKFAST)) {
                    strList.add("早餐进食太多");
                } else if (timeStr.equals(TIME_BEFORE_LUNCH)) {
                    strList.add("早餐进食太多");
                } else if (timeStr.equals(TIME_AFTER_LUNCH)) {
                    strList.add("午餐进食太多");
                } else if (timeStr.equals(TIME_BEFORE_DINNER)) {
                    strList.add("午餐进食太多");
                } else if (timeStr.equals(TIME_AFTER_DINNER)) {
                    strList.add("晚餐进食太多");
                } else if (timeStr.equals(TIME_BEFORE_BED)) {
                    strList.add("晚餐进食太多");
                } else if (timeStr.equals(TIME_0AM)) {
                    strList.add("晚餐进食太多");
                }else if (timeStr.equals(TIME_3AM)) {
                    strList.add("晚餐进食太多");
                }

                if (!TextUtils.isEmpty(timeStr)) {
                    imageList.add(R.drawable.remarks_13);
                }
                strList.add("额外加点心");
                imageList.add(R.drawable.remarks_17);
                strList.add("未按医嘱服药");
                imageList.add(R.drawable.remarks_19);
                strList.add("其他原因");
                imageList.add(R.drawable.remarks_21);


            } else if (bloodStr.equals(bloodStateLow)) { //偏低

                imageList.add(R.drawable.remarks_15);

                if (timeStr.equals(TIME_FAST)) {
                    strList.add("晚餐进食减少");
                    strList.add("超量饮酒");
                    strList.add("空腹运动");
                    imageList.add(R.drawable.remarks_28);
                    imageList.add(R.drawable.remarks_30);
                } else if (timeStr.equals(TIME_AFTER_BREAKFAST)) {
                    strList.add("早餐进食减少");
                    strList.add("工作强度增加");
                    strList.add("餐后运动过量");
                    imageList.add(R.drawable.remarks_29);
                    imageList.add(R.drawable.remarks_31);
                } else if (timeStr.equals(TIME_BEFORE_LUNCH)) {
                    strList.add("早餐进食减少");
                    strList.add("工作强度增加");
                    strList.add("餐后运动过量");
                    imageList.add(R.drawable.remarks_29);
                    imageList.add(R.drawable.remarks_31);
                } else if (timeStr.equals(TIME_AFTER_LUNCH)) {
                    strList.add("午餐进食减少");
                    strList.add("超量饮酒");
                    strList.add("餐后运动过量");
                    imageList.add(R.drawable.remarks_28);
                    imageList.add(R.drawable.remarks_31);
                } else if (timeStr.equals(TIME_BEFORE_DINNER)) {
                    strList.add("午餐进食太少");
                    strList.add("工作强度增加");
                    strList.add("餐后运动过量");
                    imageList.add(R.drawable.remarks_29);
                    imageList.add(R.drawable.remarks_31);
                } else if (timeStr.equals(TIME_AFTER_DINNER)) {
                    strList.add("晚餐进食减少");
                    strList.add("超量饮酒");
                    strList.add("餐后运动过量");
                    imageList.add(R.drawable.remarks_29);
                    imageList.add(R.drawable.remarks_31);
                } else if (timeStr.equals(TIME_BEFORE_BED)) {
                    strList.add("晚餐进食减少");
                    strList.add("超量饮酒");
                    strList.add("餐后运动过量");
                    imageList.add(R.drawable.remarks_29);
                    imageList.add(R.drawable.remarks_31);
                } else if (timeStr.equals(TIME_0AM)) {
                    strList.add("晚餐进食减少");
                    strList.add("超量饮酒");
                    strList.add("餐后运动过量");
                    imageList.add(R.drawable.remarks_29);
                    imageList.add(R.drawable.remarks_31);
                } else if (timeStr.equals(TIME_3AM)) {
                    strList.add("晚餐进食减少");
                    strList.add("超量饮酒");
                    strList.add("餐后运动过量");
                    imageList.add(R.drawable.remarks_29);
                    imageList.add(R.drawable.remarks_31);
                }

                strList.add("未按医嘱服药");
                imageList.add(R.drawable.remarks_19);
                strList.add("其他原因");
                imageList.add(R.drawable.remarks_21);
            }
        } else {
            strList.add("感觉不适");
            strList.add("加餐");
            strList.add("胰岛素释放延后");
            strList.add("其他原因");
            imageList.add(R.drawable.remarks_32);
            imageList.add(R.drawable.remarks_33);
            imageList.add(R.drawable.remarks_34);
            imageList.add(R.drawable.remarks_21);
        }


    }

    /**
     * 时间段集合
     *
     * @return
     */
    public static List<String> getTimeStr() {
        List<String> dayList = new ArrayList<>();
        dayList.add(TIME_0AM);
        dayList.add(TIME_3AM);
        dayList.add(TIME_FAST);
        dayList.add(TIME_AFTER_BREAKFAST);
        dayList.add(TIME_BEFORE_LUNCH);
        dayList.add(TIME_AFTER_LUNCH);
        dayList.add(TIME_BEFORE_DINNER);
        dayList.add(TIME_AFTER_DINNER);
        dayList.add(TIME_BEFORE_BED);
        dayList.add(TIME_RANDOM);
        return dayList;
    }

    /**
     * 服务器时间
     *
     * @return
     */
    public static List<String> getPeriod() {
        List<String> dayList = new ArrayList<>();
        dayList.add(PARAM_CODE_0AM);
        dayList.add(PARAM_CODE_3AM);
        dayList.add(PARAM_CODE_BEFOREBREAKFAST);
        dayList.add(PARAM_CODE_AFTERBREAKFAST);
        dayList.add(PARAM_CODE_BEFORELUNCH);
        dayList.add(PARAM_CODE_AFTERLUNCH);
        dayList.add(PARAM_CODE_BEFOREDINNER);
        dayList.add(PARAM_CODE_AFTERDINNER);
        dayList.add(PARAM_CODE_BEFORESLEEP);
        dayList.add(PARAM_CODE_RANDOMTIME);
        return dayList;
    }

    /**
     * 掌控时间段判断标准：
     * 0:00—7:59，凌晨血糖；
     * 0:00—8:00，空腹血糖
     * 8:00—9:59，早餐后血糖；
     * 10:00—11:59，午餐前血糖；
     * 12:00—13:59,午餐后血糖；
     * 14:00—17:59，晚餐前血糖；
     * 18:00—19:59，晚餐后血糖；
     * 20:00—23:59，睡前血糖
     */
    public static String toTimeSlot(Date date) {
        if (DateUtil.isInDate(date, "23.00.00", "23.59.59")) {
            return TIME_0AM;
        } else if (DateUtil.isInDate(date, "00.00.00", "01.59.59")) {
            return TIME_0AM;
        } else if (DateUtil.isInDate(date, "02.00.00", "03.59.59")) {
            return TIME_3AM;
        } else if (DateUtil.isInDate(date, "04.00.00", "08.29.59")) {
            return TIME_FAST;
        } else if (DateUtil.isInDate(date, "08.30.00", "10.29.59")) {
            return TIME_AFTER_BREAKFAST;
        } else if (DateUtil.isInDate(date, "10.30.00", "12.59.59")) {
            return TIME_BEFORE_LUNCH;
        } else if (DateUtil.isInDate(date, "13.00.00", "15.59.59")) {
            return TIME_AFTER_LUNCH;
        } else if (DateUtil.isInDate(date, "16.00.00", "18.29.59")) {
            return TIME_BEFORE_DINNER;
        } else if (DateUtil.isInDate(date, "18.30.00", "20.59.59")) {
            return TIME_AFTER_DINNER;
        } else if (DateUtil.isInDate(date, "21.00.00", "22.59.59")) {
            return TIME_BEFORE_BED;
        }
        return TIME_RANDOM;
    }


    public static String getParamCode(Date date) {
        if (DateUtil.isInDate(date, "23.00.00", "23.59.59")) {
            return PARAM_CODE_0AM;
        } else if (DateUtil.isInDate(date, "00.00.00", "01.59.59")) {
            return PARAM_CODE_0AM;
        } else if (DateUtil.isInDate(date, "02.00.00", "03.59.59")) {
            return PARAM_CODE_3AM;
        } else if (DateUtil.isInDate(date, "04.00.00", "08.29.59")) {
            return PARAM_CODE_BEFOREBREAKFAST;
        } else if (DateUtil.isInDate(date, "08.30.00", "10.29.59")) {
            return PARAM_CODE_AFTERBREAKFAST;
        } else if (DateUtil.isInDate(date, "10.30.00", "12.59.59")) {
            return PARAM_CODE_BEFORELUNCH;
        } else if (DateUtil.isInDate(date, "13.00.00", "15.59.59")) {
            return PARAM_CODE_AFTERLUNCH;
        } else if (DateUtil.isInDate(date, "16.00.00", "18.29.59")) {
            return PARAM_CODE_BEFOREDINNER;
        } else if (DateUtil.isInDate(date, "18.30.00", "20.59.59")) {
            return PARAM_CODE_AFTERDINNER;
        } else if (DateUtil.isInDate(date, "21.00.00", "22.59.59")) {
            return PARAM_CODE_BEFORESLEEP;
        }
        return PARAM_CODE_RANDOMTIME;
    }

    public static String getParamCode(String timeStr) {
        if (timeStr.equals(TIME_0AM)) {
            return PARAM_CODE_0AM;
        } else if (timeStr.equals(TIME_3AM)) {
            return PARAM_CODE_3AM;
        } else if (timeStr.equals(TIME_FAST)) {
            return PARAM_CODE_BEFOREBREAKFAST;
        } else if (timeStr.equals(TIME_AFTER_BREAKFAST)) {
            return PARAM_CODE_AFTERBREAKFAST;
        } else if (timeStr.equals(TIME_BEFORE_LUNCH)) {
            return PARAM_CODE_BEFORELUNCH;
        } else if (timeStr.equals(TIME_AFTER_LUNCH)) {
            return PARAM_CODE_AFTERLUNCH;
        } else if (timeStr.equals(TIME_BEFORE_DINNER)) {
            return PARAM_CODE_BEFOREDINNER;
        } else if (timeStr.equals(TIME_AFTER_DINNER)) {
            return PARAM_CODE_AFTERDINNER;
        } else if (timeStr.equals(TIME_BEFORE_BED)) {
            return PARAM_CODE_BEFORESLEEP;
        } else if (timeStr.equals(TIME_RANDOM)) {
            return PARAM_CODE_RANDOMTIME;
        }
        return timeStr;
    }

    public static String getPeriod(String timeStr) {
        if (timeStr.equals(PARAM_CODE_0AM)) {
            return TIME_0AM;
        } else if (timeStr.equals(PARAM_CODE_3AM)) {
            return TIME_3AM;
        } else if (timeStr.equals(PARAM_CODE_BEFOREBREAKFAST)) {
            return TIME_FAST;
        } else if (timeStr.equals(PARAM_CODE_AFTERBREAKFAST)) {
            return TIME_AFTER_BREAKFAST;
        } else if (timeStr.equals(PARAM_CODE_BEFORELUNCH)) {
            return TIME_BEFORE_LUNCH;
        } else if (timeStr.equals(PARAM_CODE_AFTERLUNCH)) {
            return TIME_AFTER_LUNCH;
        } else if (timeStr.equals(PARAM_CODE_BEFOREDINNER)) {
            return TIME_BEFORE_DINNER;
        } else if (timeStr.equals(PARAM_CODE_AFTERDINNER)) {
            return TIME_AFTER_DINNER;
        } else if (timeStr.equals(PARAM_CODE_BEFORESLEEP)) {
            return TIME_BEFORE_BED;
        } else if (timeStr.equals(PARAM_CODE_RANDOMTIME)) {
            return TIME_RANDOM;
        }
        return "";
    }

    public static int getParamCodePosition(String paramCode) {
        int position = -1;
        if (paramCode.equals(TIME_0AM) || paramCode.equals(PARAM_CODE_0AM)) {
            position = 0;
        } else if (paramCode.equals(TIME_3AM) || paramCode.equals(PARAM_CODE_3AM)) {
            position = 1;
        } else if (paramCode.equals(TIME_FAST) || paramCode.equals(PARAM_CODE_BEFOREBREAKFAST)) {
            position = 2;
        } else if (paramCode.equals(TIME_AFTER_BREAKFAST) || paramCode.equals(PARAM_CODE_AFTERBREAKFAST)) {
            position = 3;
        } else if (paramCode.equals(TIME_BEFORE_LUNCH) || paramCode.equals(PARAM_CODE_BEFORELUNCH)) {
            position = 4;
        } else if (paramCode.equals(TIME_AFTER_LUNCH) || paramCode.equals(PARAM_CODE_AFTERLUNCH)) {
            position = 5;
        } else if (paramCode.equals(TIME_BEFORE_DINNER) || paramCode.equals(PARAM_CODE_BEFOREDINNER)) {
            position = 6;
        } else if (paramCode.equals(TIME_AFTER_DINNER) || paramCode.equals(PARAM_CODE_AFTERDINNER)) {
            position = 7;
        } else if (paramCode.equals(TIME_BEFORE_BED) || paramCode.equals(PARAM_CODE_BEFORESLEEP)) {
            position = 8;
        } else if (paramCode.equals(TIME_RANDOM) || paramCode.equals(PARAM_CODE_RANDOMTIME)) {
            position = 9;
        }
        return position;
    }

    /**
     * 根据血糖值返回颜色值 (过去方法)
     *
     * @return
     */
//    public static int bloodGlucoseToColor(Context context, int values) {
//        int color = 0;
//        if (values <= lowValues) {
//            color = context.getResources().getColor(R.color.blue);
//        } else if (values >= highValues) {
//            color = context.getResources().getColor(R.color.red);
//        } else {
//            color = context.getResources().getColor(R.color.green);
//        }
//        return color;
//    }

    /**
     * 根据血糖值和测试时间段返回颜色值
     *
     * @param context
     * @param values    血糖值
     * @param highEmpty 高空腹前
     * @param highFull
     * @param lowEmpty  低空腹前
     * @param lowFull
     * @param state     测试时间段
     * @return
     */
    public static int bloodGlucoseToColor(Context context, float values, String highEmpty,
                                          String highFull, String lowEmpty, String lowFull,
                                          String state) {
        float high;
        float low;
        if (!state.equals(PARAM_CODE_BEFOREBREAKFAST)) { //空腹
            if (TextUtils.isEmpty(highFull)) {
                high = highValues;
            } else {
                high = Float.valueOf(highFull);
            }
            if (TextUtils.isEmpty(lowFull)) {
                low = lowValues;
            } else {
                low = Float.valueOf(lowFull);
            }

        } else {
            if (TextUtils.isEmpty(highEmpty)) {
                high = highValues;
            } else {
                high = Float.valueOf(highEmpty);
            }
            if (TextUtils.isEmpty(lowEmpty)) {
                low = lowValues;
            } else {
                low = Float.valueOf(highEmpty);
            }
        }
        return bloodGlucoseToColor(context, values, high, low);
    }

    public static int bloodGlucoseToColor(Context context, float values, float high, float low) {
        int color = 0;
        if (values < low) {
            color = context.getResources().getColor(R.color.blue);
        } else if (values > high) {
            color = context.getResources().getColor(R.color.red);
        } else {
            color = context.getResources().getColor(R.color.green);
        }
        return color;
    }

    /**
     * 根据血糖值返回图片 (过期方法)
     *
     * @return
     */
//    public static Drawable bloodGlucoseToDrawable(Context context, int values) {
//        Drawable imageId;
//        if (values <= lowValues) {
//            imageId = context.getResources().getDrawable(R.drawable.hospital_50);
//        } else if (values >= highValues) {
//            imageId = context.getResources().getDrawable(R.drawable.hospital_46);
//        } else {
//            imageId = context.getResources().getDrawable(R.drawable.hospital_48);
//        }
//        return imageId;
//    }

    /**
     * 根据血糖值和测试时间段返回图片
     *
     * @param context
     * @param values    血糖值
     * @param highEmpty 高空腹前
     * @param highFull
     * @param lowEmpty  低空腹前
     * @param lowFull
     * @param state     测试时间段
     * @return
     */
    public static Drawable bloodGlucoseToDrawable(Context context, float values, String highEmpty,
                                                  String highFull, String lowEmpty, String lowFull,
                                                  String state) {
        float high;
        float low;
        if (!state.equals(PARAM_CODE_BEFOREBREAKFAST)) { //空腹
            if (TextUtils.isEmpty(highFull)) {
                high = highValues;
            } else {
                high = Float.valueOf(highFull);
            }
            if (TextUtils.isEmpty(lowFull)) {
                low = lowValues;
            } else {
                low = Float.valueOf(lowFull);
            }
        } else {
            if (TextUtils.isEmpty(highEmpty)) {
                high = highValues;
            } else {
                high = Float.valueOf(highEmpty);
            }
            if (TextUtils.isEmpty(lowEmpty)) {
                low = lowValues;
            } else {
                low = Float.valueOf(highEmpty);
            }
        }
        return bloodGlucoseToDrawable(context, values, high, low);
    }

    public static Drawable bloodGlucoseToDrawable(Context context, float values, float high, float low) {
        if (high == 0) {
            high = highValues;
        }
        if (low == 0) {
            low = lowValues;
        }
        Drawable imageId;
        if (values < low) {
            imageId = context.getResources().getDrawable(R.drawable.hospital_50);
        } else if (values > high) {
            imageId = context.getResources().getDrawable(R.drawable.hospital_46);
        } else {
            imageId = context.getResources().getDrawable(R.drawable.hospital_48);
        }
        return imageId;
    }


    /**
     * 根据血糖值和测试时间段返回是否正常
     *
     * @param values    血糖值
     * @param highEmpty 高空腹前
     * @param highFull
     * @param lowEmpty  低空腹前
     * @param lowFull
     * @param state     测试时间段
     * @return
     */
    public static String bloodGlucoseToString(float values, String highEmpty,
                                              String highFull, String lowEmpty, String lowFull,
                                              String state) {
        float high;
        float low;
        if (!state.equals(PARAM_CODE_BEFOREBREAKFAST)) { //空腹
            if (TextUtils.isEmpty(highFull)) {
                high = highValues;
            } else {
                high = Float.valueOf(highFull);
            }
            if (TextUtils.isEmpty(lowFull)) {
                low = lowValues;
            } else {
                low = Float.valueOf(lowFull);
            }

        } else {
            if (TextUtils.isEmpty(highEmpty)) {
                high = highValues;
            } else {
                high = Float.valueOf(highEmpty);
            }
            if (TextUtils.isEmpty(lowEmpty)) {
                low = lowValues;
            } else {
                low = Float.valueOf(highEmpty);
            }
        }
        return bloodGlucoseToString(values, high, low);
    }

    public static String bloodGlucoseToString(float values, float high, float low) {
        if (high == 0) {
            high = highValues;
        }
        if (low == 0) {
            low = lowValues;
        }
        String str;
        if (values < low) {
            str = bloodStateLow;
        } else if (values > high) {
            str = bloodStateHigh;
        } else {
            str = bloodStateNormal;
        }
        return str;
    }


    /**
     * 根据血糖等级返回图片
     *
     * @return
     */
    public static Drawable bloodLevelToDrawable(Context context, int level) {
        Drawable imageId;
        if (level < 3) {
            imageId = context.getResources().getDrawable(R.drawable.hospital_50);
        } else if (level > 3) {
            imageId = context.getResources().getDrawable(R.drawable.hospital_46);
        } else {
            imageId = context.getResources().getDrawable(R.drawable.hospital_48);
        }
        return imageId;
    }


    /**
     * 根据血糖值返回颜色值
     *
     * @return
     */
    public static int bloodLevelToColor(Context context, int level) {
        int color = 0;
        if (level < 3) {
            color = context.getResources().getColor(R.color.blue);
        } else if (level > 3) {
            color = context.getResources().getColor(R.color.red);
        } else {
            color = context.getResources().getColor(R.color.green);
        }
        return color;
    }

    /**
     * 获取血糖值类型状态
     *
     * @param data
     * @param highFull
     * @param lowFull
     * @return 0: 正常 0: 高 1: 低 -1
     */
    public static int getBloodType(float data, String highFull, String lowFull) {
        float high;
        float low;
        if (TextUtils.isEmpty(highFull)) {
            high = TestResultDataUtil.highValues;
        } else {
            high = Float.valueOf(highFull);
        }
        if (TextUtils.isEmpty(lowFull)) {
            low = TestResultDataUtil.lowValues;
        } else {
            low = Float.valueOf(lowFull);
        }
        int type;
        if (data < low) {
            type = -1;
        } else if (data > high) {
            type = 1;
        } else {
            type = 0;
        }
        return type;
    }

    /**
     * 血糖整数区间值
     *
     * @return
     */
    public static List<String> getBloodIntegerValues() {
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i < TestResultDataUtil.maxValues; i++) {
            dayList.add(i + "");
        }
        return dayList;
    }

    /**
     * 血糖小数区间值
     *
     * @return
     */
    public static List<String> getBloodDecimalValues() {
        List<String> dayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dayList.add(i + "");
        }
        return dayList;
    }

    /**
     * 根据血糖等级返回趋势图片
     *
     * @return
     */
    public static Drawable bloodLevelToTrendDrawable(Context context, int level) {
        if (level < 3) {
            return context.getResources().getDrawable(R.drawable.blood_33);
        } else if (level > 3) {
            return context.getResources().getDrawable(R.drawable.blood_31);
        } else {
            return null;
        }
    }

    public static String[] getLowAndHigh(String paramCode, BloodRangeBean rangeBean) {
        String[] lowAndHigh = new String[2];
        if (paramCode.equals(TestResultDataUtil.TIME_0AM)) { //凌晨
            lowAndHigh[0] = rangeBean.getLowBeforedawn();
            lowAndHigh[1] = rangeBean.getHighBeforedawn();
        } else if (paramCode.equals(TestResultDataUtil.TIME_3AM)) { //凌晨
            lowAndHigh[0] = rangeBean.getLowBeforedawn();
            lowAndHigh[1] = rangeBean.getHighBeforedawn();
        } else if (paramCode.equals(TestResultDataUtil.TIME_FAST)) { //空腹
            lowAndHigh[0] = rangeBean.getLowBeforeBreakfast();
            lowAndHigh[1] = rangeBean.getHighBeforeBreakfast();
        } else if (paramCode.equals(TestResultDataUtil.TIME_AFTER_BREAKFAST)) { //早餐后
            lowAndHigh[0] = rangeBean.getLowAfterMeal();
            lowAndHigh[1] = rangeBean.getHighAfterMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_BEFORE_LUNCH)) { //午餐前
            lowAndHigh[0] = rangeBean.getLowBeforeMeal();
            lowAndHigh[1] = rangeBean.getHighBeforeMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_AFTER_LUNCH)) { //午餐后
            lowAndHigh[0] = rangeBean.getLowAfterMeal();
            lowAndHigh[1] = rangeBean.getHighAfterMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_BEFORE_DINNER)) { //晚餐前
            lowAndHigh[0] = rangeBean.getLowBeforeMeal();
            lowAndHigh[1] = rangeBean.getHighBeforeMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_AFTER_DINNER)) {//晚餐后
            lowAndHigh[0] = rangeBean.getLowAfterMeal();
            lowAndHigh[1] = rangeBean.getHighAfterMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_BEFORE_BED)) { //睡觉前
            lowAndHigh[0] = rangeBean.getLowBeforeSleep();
            lowAndHigh[1] = rangeBean.getHighBeforeSleep();
        }
        if (TextUtils.isEmpty(lowAndHigh[0])) {
            lowAndHigh[0] = TestResultDataUtil.lowValues + "";
        }
        if (TextUtils.isEmpty(lowAndHigh[1])) {
            lowAndHigh[1] = TestResultDataUtil.highValues + "";
        }
        return lowAndHigh;
    }


    public List<String> getStrList() {
        return strList;
    }

    public List<Integer> getImageList() {
        return imageList;
    }


    public static int getPosition(String paramCode) {
        int position = -1;
        if (paramCode.equals(TIME_0AM) || paramCode.equals(PARAM_CODE_0AM)) {
            position = 0;
        } else if (paramCode.equals(TIME_3AM) || paramCode.equals(PARAM_CODE_3AM)) {
            position = 1;
        } else if (paramCode.equals(TIME_FAST) || paramCode.equals(PARAM_CODE_BEFOREBREAKFAST)) {
            position = 2;
        } else if (paramCode.equals(TIME_AFTER_BREAKFAST) || paramCode.equals(PARAM_CODE_AFTERBREAKFAST)) {
            position = 3;
        } else if (paramCode.equals(TIME_BEFORE_LUNCH) || paramCode.equals(PARAM_CODE_BEFORELUNCH)) {
            position = 4;
        } else if (paramCode.equals(TIME_AFTER_LUNCH) || paramCode.equals(PARAM_CODE_AFTERLUNCH)) {
            position = 5;
        } else if (paramCode.equals(TIME_BEFORE_DINNER) || paramCode.equals(PARAM_CODE_BEFOREDINNER)) {
            position = 6;
        } else if (paramCode.equals(TIME_AFTER_DINNER) || paramCode.equals(PARAM_CODE_AFTERDINNER)) {
            position = 7;
        } else if (paramCode.equals(TIME_BEFORE_BED) || paramCode.equals(PARAM_CODE_BEFORESLEEP)) {
            position = 8;
        } else if (paramCode.equals(TIME_RANDOM) || paramCode.equals(PARAM_CODE_RANDOMTIME)) {
            position = 9;
        }
        return position;
    }
}
