package com.comvee.hospitalabbott.helper;


import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.helper.HistoryHelper;
import com.comvee.hospitalabbott.helper.BedRefreshHelper;
import com.comvee.hospitalabbott.tool.LogUtils;
import com.comvee.hospitalabbott.tool.RxJavaUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by F011512088 on 2018/1/17.
 */

public class RecordHelper {

    public static void recordBloodSugar(final TestInfo testInfo,
                                        final String dateStr, final int level, final boolean isNetwork) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observable) throws Exception {
                LogUtils.e("-保存最新数值到患者数据-");
                //设置本地历史数据
                //HistoryHelper.upDataHistoryMember(testInfo, dateStr, level + "");
                float value = 0;
                try {
                    value = Float.valueOf(testInfo.getValue());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                //保存最新数值到患者数据
                BedRefreshHelper.upDataFilterParamBean(testInfo, level, isNetwork);
                observable.onNext("");
                observable.onComplete();
            }
        }).compose(RxJavaUtil.<String>toComputation())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        //拿到病床界面的当前信息修改为最新内容
//                        MainBasFragment mainBasFragment = MainActivity.activity.getMainBasFragment();
//                        if (mainBasFragment != null)
//                            mainBasFragment.onRefreshData();
                    }
                });
    }
}
