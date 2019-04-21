package com.comvee.hospitalabbott.ui.patient;

import com.comvee.greendao.gen.MemberModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.bean.MemberModel;
import com.comvee.hospitalabbott.bean.SearchBean;
import com.comvee.hospitalabbott.tool.ComparatorMember;
import com.comvee.hospitalabbott.tool.RxJavaUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by F011512088 on 2018/1/30.
 */
public class PatientPresenter extends BasePresenter<PatientViewController> {


    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public PatientPresenter(PatientViewController viewModel) {
        super(viewModel);
    }

    @Override
    public void initData() {

    }

    @Override
    public void unBind() {
        super.unBind();

    }

    //搜索患者
    public void searchStr(final String searchStr) {
        Observable.create(new ObservableOnSubscribe<List<SearchBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchBean>> e) throws Exception {
                MemberModelDao memberModelDao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
                //科室名称、患者名称、房间号、病床号(不存在房间号一说)
                List<MemberModel> modelList = memberModelDao.queryBuilder()
                        .whereOr(MemberModelDao.Properties.DepartmentName.like("%"+searchStr+"%"),
                                MemberModelDao.Properties.MemberName.like("%"+searchStr+"%"),
                                MemberModelDao.Properties.BedNo.like("%"+searchStr+"%"))
                        .orderAsc(MemberModelDao.Properties.BedNo)
                        .build()
                        .list();
                List<SearchBean> response = new ArrayList<>();
                if (modelList.size() > 0) {
                    /**
                     * 按照床位进行排序
                     */
                    ComparatorMember comparator = new ComparatorMember();
                    Collections.sort(modelList, comparator);
                    for (MemberModel memberModel : modelList) {
                        response.add(new SearchBean(memberModel));
                    }
                }
                e.onNext(response);
                e.onComplete();
            }
        }).compose(RxJavaUtil.<List<SearchBean>>toComputation())
                .subscribe(new Consumer<List<SearchBean>>() {
                    @Override
                    public void accept(@NonNull List<SearchBean> searchBeen) throws Exception {
                        viewModel.setSearchList(searchBeen);
                    }
                });
//             ComveeLoader.getInstance().searchMemberInfo(searchStr, searchStr, searchStr, searchStr)
//                     .subscribe(new HttpCall<List<SearchBean>>() {
//                @Override
//                public void onNext(List<SearchBean> searchBeen) {
//                    setSearchBeanList(searchBeen);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    super.onError(e);
//                    if (adapter != null) {
//                        adapter.setEmptyView(notDataView);
//                        endAnimation();
//                        recyclerView.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
    }

    public void getLocalMemberAll() {
        Observable.create(new ObservableOnSubscribe<List<SearchBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchBean>> e) throws Exception {
                List<SearchBean> searchBeanList = new ArrayList<SearchBean>();
                MemberModelDao memberModelDao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
                List<MemberModel> memberModelList = memberModelDao.queryBuilder()
                        .orderAsc(MemberModelDao.Properties.DepartmentId)
                        .build()
                        .list();
                if (memberModelList.size() > 0) {
                    /**
                     * 按照床位进行排序
                     */
//                    ComparatorMember comparator = new ComparatorMember();
//                    Collections.sort(memberModelList, comparator);

                    for (MemberModel memberModel : memberModelList) {
                        SearchBean searchBean = new SearchBean();
                        searchBean.setBedNo(memberModel.getBedNo());
                        searchBean.setBedId(memberModel.getBedId());
                        searchBean.setDepartmentId(memberModel.getDepartmentId());
                        searchBean.setDepartmentName(memberModel.getDepartmentName());
                        searchBean.setMemberId(memberModel.getMemberId());
                        searchBean.setMemberName(memberModel.getMemberName());

//                        String tempName = SortUtil.changeIntToSpecifyLength(searchBean.getBedNo());
//                        searchBean.setTempUserName(tempName);
                        searchBeanList.add(searchBean);
                    }
//                    SearchComparator comparator = new SearchComparator();
//                    Collections.sort(searchBeanList, comparator);
                }
                e.onNext(searchBeanList);
                e.onComplete();
            }
        }).compose(RxJavaUtil.<List<SearchBean>>applySchedulers())
                .subscribe(new Consumer<List<SearchBean>>() {
                    @Override
                    public void accept(@NonNull List<SearchBean> searchBeen) throws Exception {
                        viewModel.setSearchList(searchBeen);
                    }
                });
    }
}
