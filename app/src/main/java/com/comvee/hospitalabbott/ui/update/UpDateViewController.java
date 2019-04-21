package com.comvee.hospitalabbott.ui.update;

import com.comvee.hospitalabbott.base.BaseViewController;
import com.comvee.hospitalabbott.bean.VersionBean;

/**
 * Created by F011512088 on 2018/1/25.
 */
public interface UpDateViewController extends BaseViewController{

    void toDownload(VersionBean bean);//前往更新界面

    void latest();//已是最新版本

    void onError();
}
