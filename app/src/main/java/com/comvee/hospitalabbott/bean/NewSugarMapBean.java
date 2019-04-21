package com.comvee.hospitalabbott.bean;

import java.io.Serializable;

/**
 * Created by F011512088 on 2017/4/12.
 */

public class NewSugarMapBean implements Serializable {

    /**
     * 久
     * afterBreakfast : {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""}
     * afterDinner : {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""}
     * afterLunch : {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""}
     * beforeBreakfast : {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""}
     * beforeDinner : {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""}
     * beforeLunch : {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""}
     * beforeSleep : {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""}
     * beforedawn : {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""}
     * randomtime : {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""}
     * 新
     * "ab": {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""},
     * "ad": {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""},
     * "al": {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""},
     * "bb": {"level":"","memberId":"","paramCode":"","paramLogId":"","recordOrigin":"","recordTime":"","remark":"","status":"","value":""},
     * "bd": {},
     * "beforedawn": {},
     * "bl": {},
     * "bs": {},
     * "randomtime": {}
     */

    private ABBean ab;
    private ADBean ad;
    private ALBean al;
    private BBBean bb;
    private BDBean bd;
    private BLBean bl;
    private BSBean bs;
    private BeforedawnBean beforedawn;
    private ThreeclockBean threeclock;
    private RandomtimeBean randomtime;

    public ABBean getAfterBreakfast() {
        return ab == null ? new ABBean() : ab;
    }

    public void setAfterBreakfast(ABBean afterBreakfast) {
        this.ab = afterBreakfast;
    }

    public ADBean getAfterDinner() {
        return ad == null ? new ADBean() : ad;
    }

    public void setAfterDinner(ADBean afterDinner) {
        this.ad = afterDinner;
    }

    public ALBean getAfterLunch() {
        return al == null ? new ALBean() : al;
    }

    public void setAfterLunch(ALBean afterLunch) {
        this.al = afterLunch;
    }

    public BBBean getBeforeBreakfast() {
        return bb == null ? new BBBean() : bb;
    }

    public void setBeforeBreakfast(BBBean beforeBreakfast) {
        this.bb = beforeBreakfast;
    }

    public BDBean getBeforeDinner() {
        return bd == null ? new BDBean() : bd;
    }

    public void setBeforeDinner(BDBean beforeDinner) {
        this.bd = beforeDinner;
    }

    public BLBean getBeforeLunch() {
        return bl == null ? new BLBean() : bl;
    }

    public void setBeforeLunch(BLBean beforeLunch) {
        this.bl = beforeLunch;
    }

    public BSBean getBeforeSleep() {
        return bs == null ? new BSBean() : bs;
    }

    public void setBeforeSleep(BSBean beforeSleep) {
        this.bs = beforeSleep;
    }

    public BeforedawnBean getBeforedawn() {
        return beforedawn == null ? new BeforedawnBean() : beforedawn;
    }

    public void setBeforedawn(BeforedawnBean beforedawn) {
        this.beforedawn = beforedawn;
    }

    public ThreeclockBean getThreeclock() {
        return threeclock == null ? new ThreeclockBean() : threeclock;
    }

    public void setThreeclock(ThreeclockBean threeclock) {
        this.threeclock = threeclock;
    }

    public RandomtimeBean getRandomtime() {
        return randomtime == null ? new RandomtimeBean() : randomtime;
    }

    public void setRandomtime(RandomtimeBean randomtime) {
        this.randomtime = randomtime;
    }

    public ABBean getAb() {
        return ab;
    }

    public void setAb(ABBean ab) {
        this.ab = ab;
    }

    public ADBean getAd() {
        return ad;
    }

    public void setAd(ADBean ad) {
        this.ad = ad;
    }

    public ALBean getAl() {
        return al;
    }

    public void setAl(ALBean al) {
        this.al = al;
    }

    public BBBean getBb() {
        return bb;
    }

    public void setBb(BBBean bb) {
        this.bb = bb;
    }

    public BDBean getBd() {
        return bd;
    }

    public void setBd(BDBean bd) {
        this.bd = bd;
    }

    public BLBean getBl() {
        return bl;
    }

    public void setBl(BLBean bl) {
        this.bl = bl;
    }

    public BSBean getBs() {
        return bs;
    }

    public void setBs(BSBean bs) {
        this.bs = bs;
    }

    @Override
    public String toString() {
        return "NewSugarMapBean{" +
                "afterBreakfast=" + ab.toString() +
                ", afterDinner=" + ad.toString() +
                ", afterLunch=" + al.toString() +
                ", beforeBreakfast=" + bb.toString() +
                ", beforeDinner=" + bd.toString() +
                ", beforeLunch=" + bl.toString() +
                ", beforeSleep=" + bs.toString() +
                ", beforedawn=" + beforedawn.toString() +
                ", randomtime=" + randomtime.toString() +
                '}';
    }
}
