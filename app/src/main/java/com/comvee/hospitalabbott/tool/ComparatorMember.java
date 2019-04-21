package com.comvee.hospitalabbott.tool;

import com.comvee.hospitalabbott.bean.MemberModel;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by F011512088 on 2018/1/29.
 */

/**
 * 正序
 * 按照床位进行排序
 */
public class ComparatorMember implements Comparator<MemberModel> {
    /**
     * 返回正数，零，负数各代表大于，等于，小于。
     *
     * @param obj0
     * @param obj1
     * @return
     */
//    @Override
//    public int compare(MemberModel obj0, MemberModel obj1) {
//        int flag = 0;
//        try {
//            Integer o1 = Integer.valueOf(obj0.getBedNo());
//            Integer o2 = Integer.valueOf(obj1.getBedNo());
//            flag = o1.compareTo(o2);// 正确的方式
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//        return flag;
//    }
    private Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

    @Override
    public int compare(MemberModel obj0, MemberModel obj1) {
        int flag = 0;
        try {
            Integer o1 = Integer.valueOf(obj0.getBedNo());
            Integer o2 = Integer.valueOf(obj1.getBedNo());
            flag = o1.compareTo(o2);// 正确的方式
        } catch (NumberFormatException e) {
            e.printStackTrace();
            /**
             * 按汉字首字母排序
             */
            if (null == obj0.getBedNo()) {
                if (null == obj1.getBedNo()) {
                    return 0;
                } else {
                    return 1;
                }
            } else if (null == obj1.getBedNo()) {
                return -1;
            } else {
                flag = cmp.compare(obj0.getBedNo(), obj1.getBedNo());
            }
        }
        return flag;
    }


}
