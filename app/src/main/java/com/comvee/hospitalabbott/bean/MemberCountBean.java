package com.comvee.hospitalabbott.bean;


import com.comvee.hospitalabbott.tool.TestResultDataUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by F011512088 on 2017/6/28.
 */

public class MemberCountBean implements Serializable {

    /**
     * countList : {"breakfast":2,"beforedinner":3}
     * total : 0
     */

    private CountListBean countList;
    private int total;

    public CountListBean getCountList() {
        return countList;
    }

    public void setCountList(CountListBean countList) {
        this.countList = countList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class CountListBean {

        /**
         * beforedawn  : 2
         * beforeBreakfast : 3
         * afterBreakfast : 2
         * beforeLunch : 3
         * afterLunch  : 2
         * beforeDinner : 3
         * afterDinner  : 2
         * beforeSleep  : 2
         * randomtime  : 2
         */
        private int threeclock;
        private int beforedawn;
        private int beforeBreakfast;
        private int afterBreakfast;
        private int beforeLunch;
        private int afterLunch;
        private int beforeDinner;
        private int afterDinner;
        private int beforeSleep;
        private int randomtime;

        public CountListBean(){}

        public CountListBean(Map<String,Integer> map){
            setCount(map);
        }

        public int getBeforedawn() {
            return beforedawn;
        }

        public void setBeforedawn(int beforedawn) {
            this.beforedawn = beforedawn;
        }

        public int getBeforeBreakfast() {
            return beforeBreakfast;
        }

        public void setBeforeBreakfast(int beforeBreakfast) {
            this.beforeBreakfast = beforeBreakfast;
        }

        public int getAfterBreakfast() {
            return afterBreakfast;
        }

        public void setAfterBreakfast(int afterBreakfast) {
            this.afterBreakfast = afterBreakfast;
        }

        public int getBeforeLunch() {
            return beforeLunch;
        }

        public void setBeforeLunch(int beforeLunch) {
            this.beforeLunch = beforeLunch;
        }

        public int getAfterLunch() {
            return afterLunch;
        }

        public void setAfterLunch(int afterLunch) {
            this.afterLunch = afterLunch;
        }

        public int getBeforeDinner() {
            return beforeDinner;
        }

        public void setBeforeDinner(int beforeDinner) {
            this.beforeDinner = beforeDinner;
        }

        public int getAfterDinner() {
            return afterDinner;
        }

        public void setAfterDinner(int afterDinner) {
            this.afterDinner = afterDinner;
        }

        public int getBeforeSleep() {
            return beforeSleep;
        }

        public void setBeforeSleep(int beforeSleep) {
            this.beforeSleep = beforeSleep;
        }

        public int getRandomtime() {
            return randomtime;
        }

        public void setRandomtime(int randomtime) {
            this.randomtime = randomtime;
        }

        public void setCount(Map<String, Integer> countMap) {
            beforedawn = countMap.get(TestResultDataUtil.PARAM_CODE_0AM);
            threeclock = countMap.get(TestResultDataUtil.PARAM_CODE_3AM);
            beforeBreakfast = countMap.get(TestResultDataUtil.PARAM_CODE_BEFOREBREAKFAST);
            afterBreakfast = countMap.get(TestResultDataUtil.PARAM_CODE_AFTERBREAKFAST);
            beforeLunch = countMap.get(TestResultDataUtil.PARAM_CODE_BEFORELUNCH);
            afterLunch = countMap.get(TestResultDataUtil.PARAM_CODE_AFTERLUNCH);
            beforeDinner = countMap.get(TestResultDataUtil.PARAM_CODE_BEFOREDINNER);
            afterDinner = countMap.get(TestResultDataUtil.PARAM_CODE_AFTERDINNER);
            beforeSleep = countMap.get(TestResultDataUtil.PARAM_CODE_BEFORESLEEP);
            randomtime = countMap.get(TestResultDataUtil.PARAM_CODE_RANDOMTIME);
        }
    }
}
