package com.comvee.hospitalabbott.ui.quality;

import com.comvee.greendao.gen.QualityResultBeanDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.bean.QualityModel;
import com.comvee.hospitalabbott.bean.QualityResultBean;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;

import io.reactivex.Observable;

/**
 * Created by F011512088 on 2018/2/27.
 */
public class QualityResultPresenter extends BasePresenter<QualityResultViewController> {

    private QualityModel qualityModel;
    private QualityResultBean resultBean;
    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public QualityResultPresenter(QualityResultViewController viewModel) {
        super(viewModel);
    }

    @Override
    public void initData() {

    }

    public void setQualityModel(QualityModel qualityModel) {
        this.qualityModel = qualityModel;
        resultBean = new QualityResultBean(qualityModel);
        resultBean.setMachineId(UserHelper.getMachineId());
        resultBean.setExplainText("");
    }

    public float getQualityLow() {
        if (qualityModel == null)
            return 5.4f;
        return qualityModel.getLow();
    }

    public float getQualityHigh() {
        if (qualityModel == null)
            return 7.2f;
        return qualityModel.getHigh();
    }

    public int getType(){
        int type = -1;
        if ((resultBean.getBloodValues() > resultBean.getLow())
                && (resultBean.getBloodValues() < resultBean.getHigh())) {
            type = 0;
            resultBean.setPassStatus("1");// 0-不通过 1-通过
        } else if (resultBean.getBloodValues() < resultBean.getLow()) {
            type = -1;
            resultBean.setPassStatus("0");
        } else if (resultBean.getBloodValues() > resultBean.getHigh()) {
            type = 1;
            resultBean.setPassStatus("0");
        }
        return type;
    }

    public float getValue() {
        if (qualityModel == null)
           return  0;
        return qualityModel.getValue();
    }

    public QualityResultBean getResultBean() {
        return resultBean;
    }

    public Observable<String> submit() {
        /**
         * qcPagerNo	    String      质控试纸编号
         * qcLiquidNo	    String      质控液编号
         * qcLiquidLevel	String      质控液等级
         * value	        String      测试数据
         * passStatus	    String      通过状态 0-不通过 1-通过
         * machineId	    String      机器id
         */
        return ComveeLoader.getInstance().addQualityControlLog(resultBean.getQcPagerNo(), resultBean.getQcLiquidNo(),
                resultBean.getQcLiquidLevel(), resultBean.getBloodValues() + "", resultBean.getPassStatus(),
                resultBean.getLow(),resultBean.getHigh(),resultBean.getTime(),resultBean.getMachineId());
    }

    public void setLocalQuality() {
        QualityResultBeanDao dao = XTYApplication.getInstance().getDaoSession().getQualityResultBeanDao();
        dao.insert(resultBean);
    }
}
