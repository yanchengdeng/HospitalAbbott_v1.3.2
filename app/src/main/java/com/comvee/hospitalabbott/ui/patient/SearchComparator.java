package com.comvee.hospitalabbott.ui.patient;

import com.comvee.hospitalabbott.bean.SearchBean;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by F011512088 on 2018/1/30.
 */
public class SearchComparator implements Comparator<SearchBean>{

    private Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

    @Override
    public int compare(SearchBean o1, SearchBean o2) {
        String bedNo1 = o1.getBedNo();
        String bedNo2 = o2.getBedNo();
        if (null == bedNo1) {
            if (null == bedNo2) {
                return 0;
            } else {
                return 1;
            }
        } else if (null == bedNo2) {
            return -1;
        } else {
            int result = cmp.compare(bedNo1, bedNo2);
            return result;
        }
    }
}
