package com.comvee.hospitalabbott.ui.search;

import com.comvee.greendao.gen.MemberModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.base.BaseRxActivity;
import com.comvee.hospitalabbott.bean.MemberModel;
import com.comvee.hospitalabbott.bean.SearchBean;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by F011512088 on 2018/1/29.
 */
public class SearchPresenter extends BasePresenter<SearchViewController>{
    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public SearchPresenter(SearchViewController viewModel) {
        super(viewModel);
    }

    @Override
    public void initData() {

    }

    /**
     * 向服务器请求
     */
    public void postSearch(BaseRxActivity activity, String searchStr) {
        ComveeLoader.getInstance().searchMemberInfo(searchStr, searchStr, searchStr, searchStr)
                .compose(activity.<List<SearchBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new HttpCall<List<SearchBean>>(null) {
                    @Override
                    public void onNext(List<SearchBean> searchBeen) {
                        if (viewModel!=null)
                            viewModel.onNetSearchList(searchBeen);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (viewModel!=null)
                            viewModel.onNetError(e.getMessage());
                    }
                });
    }

    public void localSearch(BaseRxActivity activity,final String searchStr) {
        Observable.create(new ObservableOnSubscribe<List<SearchBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchBean>> e) throws Exception {
                List<SearchBean> searchBeanList = new ArrayList<SearchBean>();
                MemberModelDao memberModelDao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
                List<MemberModel> memberModelList = memberModelDao.queryBuilder()
                        .whereOr(MemberModelDao.Properties.DepartmentName.like("%" + searchStr + "%"),
                                MemberModelDao.Properties.MemberName.like("%" + searchStr + "%"),
                                MemberModelDao.Properties.BedNo.like("%" + searchStr + "%"))
                        .orderAsc(MemberModelDao.Properties.BedNo)
                        .build()
                        .list();

                if (memberModelList.size() > 0) {
                    HashSet h = new HashSet(memberModelList);
                    memberModelList.clear();
                    memberModelList.addAll(h);

                    for (MemberModel memberModel : memberModelList) {
                        SearchBean searchBean = new SearchBean();
                        searchBean.setBedNo(memberModel.getBedNo());
                        searchBean.setBedId(memberModel.getBedId());
                        searchBean.setDepartmentId(memberModel.getDepartmentId());
                        searchBean.setDepartmentName(memberModel.getDepartmentName());
                        searchBean.setMemberId(memberModel.getMemberId());
                        searchBean.setMemberName(memberModel.getMemberName());
                        searchBeanList.add(searchBean);
                    }
                }
                e.onNext(searchBeanList);
                e.onComplete();
            }

        }).compose(activity.<List<SearchBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SearchBean>>() {
                    @Override
                    public void accept(@NonNull List<SearchBean> searchBeen) throws Exception {
                        if (viewModel!=null)
                            viewModel.onLocalSearch(searchBeen);
                    }
                });
    }

}
