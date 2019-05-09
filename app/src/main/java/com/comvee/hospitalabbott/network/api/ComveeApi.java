package com.comvee.hospitalabbott.network.api;


import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.BloodSugarChatDynmicInfo;
import com.comvee.hospitalabbott.bean.BloodSugarChatInfo;
import com.comvee.hospitalabbott.bean.HistoryModel;
import com.comvee.hospitalabbott.bean.LoginModel;
import com.comvee.hospitalabbott.bean.MemberCountBean;
import com.comvee.hospitalabbott.bean.RefreshBedModel;
import com.comvee.hospitalabbott.bean.SearchBean;
import com.comvee.hospitalabbott.bean.TestPaperBean;
import com.comvee.hospitalabbott.bean.UncheckinBean;
import com.comvee.hospitalabbott.bean.VersionBean;
import com.comvee.hospitalabbott.network.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by F011512088 on 2017/12/27.
 */

public interface ComveeApi {

    /**
     * 登录
     * userNo       账号（手机号）
     * password     密码（md5码加密字符串）
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/user/login.do")
    Observable<BaseResponse<LoginModel>> login(@FieldMap Map<String, String> map);

    /**
     * 获取床位列表(分页)
     * machineId        机器id
     * userId           用户ID
     * page             页数
     * ROWS             行数
     * concernStatus    关注状态 0-未关注 1-有关注 不传全部
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/member/getBedListByPage.do")
    Observable<BaseResponse<Object>> getBedListByPage(@FieldMap Map<String, String> map);

    /**
     * 刷新接口
     * userId           用户ID
     * refreshTime      刷新时间
     * page             页数
     * ROWS             行数
     * concernStatus    关注状态 0-未关注 1-有关注 不传全部
     * machineId        机器id
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/member/getBedListByPageForRefresh.do")
    Observable<BaseResponse<RefreshBedModel>> refreshBedList(@FieldMap Map<String, String> map);

    /**
     * 添加用户血糖记录(自动记录)
     * value        血糖值
     * paramCode    血糖时间段code
     * recordTime   记录时间
     * memberId     成员id
     * remark       备注
     * status       1是正常 2是拒绝
     * recordOrigin     XTY_ORIGIN  自动上传     XTY_ORIGIN_MANUAL 手动录入
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/bloodSugar/addMemberParamLog.do")
    Observable<BaseResponse<String>> addMemberParamLog(@FieldMap Map<String, String> map);

    /**
     * 获取用户血糖记录列表
     * memberId         成员id
     * startDt          开始时间
     * endDt            结束时间
     * paramCodeStr     需要查询的血糖时间段code  传空字符串获取全部
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/bloodSugar/getMemberParamLog.do")
    Observable<BaseResponse<HistoryModel>> getMemberParamLog(@FieldMap Map<String, String> map);


    /**
     *批量上传血糖数据
     * memberId
     *
     * paramLogArrayJson
     */

    @FormUrlEncoded
    @POST("glucometer/bloodSugar/batchUploadMemberParamLog.do")
    Observable<BaseResponse<String>> batchUploadMemberData(@FieldMap Map<String, String> map );


    /**
     * 获取统计图信息
     */

    @FormUrlEncoded
    @POST("glucometer/bloodSugar/loadMemberBloodSugarChart.do")
    Observable<BaseResponse<BloodSugarChatInfo>> loadMemberBloodSugarChar(@FieldMap Map<String,String> maps);


    /**
     * 获取统计图信息
     */

    @FormUrlEncoded
    @POST("glucometer/bloodSugar/loadMemberBloodSugarChart.do")
    Observable<BaseResponse<BloodSugarChatDynmicInfo>> loadMemberBloodSugarCharDyanmic(@FieldMap Map<String,String> maps);





    /**
     * 刷新用户血糖记录列表
     * refreshTime  刷新时间
     * memberId     用户id
     * paramCodeStr 记录时间code串, 传空刷新所有时段
     * status       记录状态  传空刷新全部 1是正常 2是拒绝
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/bloodSugar/refreshParamLogList.do")
    Observable<BaseResponse<HistoryModel>> refreshParamLogList(@FieldMap Map<String, String> map);

    /**
     * 查询患者信息
     * machineId        服务器下发设备ID
     * departmentName   科室名称
     * memberName       患者名称
     * roomNo           房间号
     * bedNo            病床号
     * userId           用户ID
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/member/listMemberBySearch.do")
    Observable<BaseResponse<List<SearchBean>>> searchMemberInfo(@FieldMap Map<String, String> map);

    /**
     * 获取最新版本号
     *
     * @param map 上传默认参数即可(版本号、设备号emei码)
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/application/getMachineVersionModel.do")
    Observable<BaseResponse<VersionBean>> getMachineVersionModel(@FieldMap Map<String, String> map);

    /**
     * 添加质控模式
     * qcPagerNo     质控试纸编号
     * qcLiquidNo    质控液编号
     * qcLiquidLevel 质控液等级
     * value         测试数据
     * passStatus    通过状态 0-不通过 1-通过
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/quality/addQualityControlLog.do")
    Observable<BaseResponse<String>> addQualityControlLog(@FieldMap Map<String, String> map);

    /**
     * 获取患者血糖控制目标
     * machineId     服务器下发设备id
     * memberId    患者id
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/member/getMemberRange.do")
    Observable<BaseResponse<BloodRangeBean>> getMemberRange(@FieldMap Map<String, String> map);

    /**
     * 获取质控等级
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/quality/getQualityControlLevel.do")
    Observable<BaseResponse<String>> getQualityControlLevel(@FieldMap Map<String, String> map);

    /**
     * 添加非住院患者血糖记录
     * userId     登录用户id
     * recordTime 记录时间
     * value      血糖记录值
     * status     记录状态
     * remark     备注 1是正常 2是拒绝
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/member/addUncheckinMemberParamLog.do")
    Observable<BaseResponse<String>> addUncheckinMemberParamLog(@FieldMap Map<String, String> map);

    /**
     * 获取非住院患者血糖记录
     * userId 登录用户id
     * page   页码
     * ROWS   每页条数
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/member/loadUncheckinMemberParamLogList.do")
    Observable<BaseResponse<UncheckinBean>> loadUncheckinMemberParamLogList(@FieldMap Map<String, String> map);

    /**
     * 系统反馈
     * userId     用户id
     * adviseType 建议类型
     * adviseText 建议内容
     * contactWay 联系方式
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/application/addSystemAdviseLog.do")
    Observable<BaseResponse<String>> addSystemAdviseLog(@FieldMap Map<String, String> map);

    /**
     * 今日任务未测人数
     * userId     用户id
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/member/getMemberSmbgCount.do")
    Observable<BaseResponse<MemberCountBean>> getMemberSmbgCount(@FieldMap Map<String, String> map);

    /**
     * 获取试纸列表
     * userId   用户ID
     * page      页数
     * "*ROWS     每页个数
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("glucometer/testPaper/getTestpaperListByPage.do")
    Observable<BaseResponse<TestPaperBean>> getTestpaperList(@FieldMap Map<String, String> map);


}
