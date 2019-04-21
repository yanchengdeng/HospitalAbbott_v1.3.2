package com.comvee.hospitalabbott.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ParamLogListBean implements Parcelable{

    @Id
    private Long id;
    /**
     * adviseContent :
     * batchId :
     * bedNo :
     * departmentName :
     * highEmpty :
     * highFull :
     * innerCode :
     * innerId : 864214018251001
     * insertDt : 2017-04-07 11:00:08
     * isValid : 1
     * level : 3
     * lowEmpty :
     * lowFull :
     * memberId : 170330101300001
     * memberName :
     * modifyDt : 2017-04-07 11:00:08
     * paramCode : beforeLunch
     * paramLogId : 170407110000049
     * paramOption :
     * processed :
     * processedDt :
     * processedMsg :
     * recordDt : 2017-04-07
     * recordOrigin : XTY_ORIGIN
     * recordTime : 2017-04-07 11:00:09
     * remark :
     * status : 1
     * value : 5.0
     */

    private String adviseContent;
    private String batchId;
    private String bedNo;
    private String departmentName;
    private String highEmpty;
    private String highFull;
    private String innerCode;
    private String innerId;
    private String insertDt;
    private String isValid;
    private String level;
    private String lowEmpty;
    private String lowFull;
    private String memberId;
    private String memberName;
    private String modifyDt;
    private String paramCode;
    private String paramLogId;
    private String paramOption;
    private String processed;
    private String processedDt;
    private String processedMsg;
    @NotNull
    private String recordDt;
    private String recordOrigin;
    private String recordTime;
    private String remark;
    private String status;
    private String value;
//        private long ownerId;

    public ParamLogListBean(TestInfo testInfo, String recordDt, String level) {
        this.memberId = testInfo.getMemberId();
        this.value = testInfo.getValue();
        this.paramCode = testInfo.getParamCode();
        this.recordTime = testInfo.getRecordTime();
        this.level = level;
        this.status = testInfo.getStateInte() + "";
        this.recordDt = recordDt;
    }

    @Generated(hash = 1988010847)
    public ParamLogListBean(Long id, String adviseContent, String batchId,
                            String bedNo, String departmentName, String highEmpty, String highFull,
                            String innerCode, String innerId, String insertDt, String isValid,
                            String level, String lowEmpty, String lowFull, String memberId,
                            String memberName, String modifyDt, String paramCode, String paramLogId,
                            String paramOption, String processed, String processedDt,
                            String processedMsg, @NotNull String recordDt, String recordOrigin,
                            String recordTime, String remark, String status, String value) {
        this.id = id;
        this.adviseContent = adviseContent;
        this.batchId = batchId;
        this.bedNo = bedNo;
        this.departmentName = departmentName;
        this.highEmpty = highEmpty;
        this.highFull = highFull;
        this.innerCode = innerCode;
        this.innerId = innerId;
        this.insertDt = insertDt;
        this.isValid = isValid;
        this.level = level;
        this.lowEmpty = lowEmpty;
        this.lowFull = lowFull;
        this.memberId = memberId;
        this.memberName = memberName;
        this.modifyDt = modifyDt;
        this.paramCode = paramCode;
        this.paramLogId = paramLogId;
        this.paramOption = paramOption;
        this.processed = processed;
        this.processedDt = processedDt;
        this.processedMsg = processedMsg;
        this.recordDt = recordDt;
        this.recordOrigin = recordOrigin;
        this.recordTime = recordTime;
        this.remark = remark;
        this.status = status;
        this.value = value;
    }

    @Generated(hash = 1005693101)
    public ParamLogListBean() {
    }

    protected ParamLogListBean(Parcel in) {
        adviseContent = in.readString();
        batchId = in.readString();
        bedNo = in.readString();
        departmentName = in.readString();
        highEmpty = in.readString();
        highFull = in.readString();
        innerCode = in.readString();
        innerId = in.readString();
        insertDt = in.readString();
        isValid = in.readString();
        level = in.readString();
        lowEmpty = in.readString();
        lowFull = in.readString();
        memberId = in.readString();
        memberName = in.readString();
        modifyDt = in.readString();
        paramCode = in.readString();
        paramLogId = in.readString();
        paramOption = in.readString();
        processed = in.readString();
        processedDt = in.readString();
        processedMsg = in.readString();
        recordDt = in.readString();
        recordOrigin = in.readString();
        recordTime = in.readString();
        remark = in.readString();
        status = in.readString();
        value = in.readString();
    }

    public static final Creator<ParamLogListBean> CREATOR = new Creator<ParamLogListBean>() {
        @Override
        public ParamLogListBean createFromParcel(Parcel in) {
            return new ParamLogListBean(in);
        }

        @Override
        public ParamLogListBean[] newArray(int size) {
            return new ParamLogListBean[size];
        }
    };

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdviseContent() {
        return adviseContent;
    }

    public void setAdviseContent(String adviseContent) {
        this.adviseContent = adviseContent;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getHighEmpty() {
        return highEmpty;
    }

    public void setHighEmpty(String highEmpty) {
        this.highEmpty = highEmpty;
    }

    public String getHighFull() {
        return highFull;
    }

    public void setHighFull(String highFull) {
        this.highFull = highFull;
    }

    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }

    public String getInnerId() {
        return innerId;
    }

    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLowEmpty() {
        return lowEmpty;
    }

    public void setLowEmpty(String lowEmpty) {
        this.lowEmpty = lowEmpty;
    }

    public String getLowFull() {
        return lowFull;
    }

    public void setLowFull(String lowFull) {
        this.lowFull = lowFull;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(String modifyDt) {
        this.modifyDt = modifyDt;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamLogId() {
        return paramLogId;
    }

    public void setParamLogId(String paramLogId) {
        this.paramLogId = paramLogId;
    }

    public String getParamOption() {
        return paramOption;
    }

    public void setParamOption(String paramOption) {
        this.paramOption = paramOption;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String getProcessedDt() {
        return processedDt;
    }

    public void setProcessedDt(String processedDt) {
        this.processedDt = processedDt;
    }

    public String getProcessedMsg() {
        return processedMsg;
    }

    public void setProcessedMsg(String processedMsg) {
        this.processedMsg = processedMsg;
    }

    public String getRecordDt() {
        return recordDt;
    }

    public void setRecordDt(String recordDt) {
        this.recordDt = recordDt;
    }

    public String getRecordOrigin() {
        return recordOrigin;
    }

    public void setRecordOrigin(String recordOrigin) {
        this.recordOrigin = recordOrigin;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

//        public long getOwnerId() {
//            return this.ownerId;
//        }
//
//        public void setOwnerId(long ownerId) {
//            this.ownerId = ownerId;
//        }


    @Override
    public String toString() {
        return "ParamLogListBean{" +
                "id=" + id +
                ", adviseContent='" + adviseContent + '\'' +
                ", batchId='" + batchId + '\'' +
                ", bedNo='" + bedNo + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", highEmpty='" + highEmpty + '\'' +
                ", highFull='" + highFull + '\'' +
                ", innerCode='" + innerCode + '\'' +
                ", innerId='" + innerId + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", isValid='" + isValid + '\'' +
                ", level='" + level + '\'' +
                ", lowEmpty='" + lowEmpty + '\'' +
                ", lowFull='" + lowFull + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", modifyDt='" + modifyDt + '\'' +
                ", paramCode='" + paramCode + '\'' +
                ", paramLogId='" + paramLogId + '\'' +
                ", paramOption='" + paramOption + '\'' +
                ", processed='" + processed + '\'' +
                ", processedDt='" + processedDt + '\'' +
                ", processedMsg='" + processedMsg + '\'' +
                ", recordDt='" + recordDt + '\'' +
                ", recordOrigin='" + recordOrigin + '\'' +
                ", recordTime='" + recordTime + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adviseContent);
        dest.writeString(batchId);
        dest.writeString(bedNo);
        dest.writeString(departmentName);
        dest.writeString(highEmpty);
        dest.writeString(highFull);
        dest.writeString(innerCode);
        dest.writeString(innerId);
        dest.writeString(insertDt);
        dest.writeString(isValid);
        dest.writeString(level);
        dest.writeString(lowEmpty);
        dest.writeString(lowFull);
        dest.writeString(memberId);
        dest.writeString(memberName);
        dest.writeString(modifyDt);
        dest.writeString(paramCode);
        dest.writeString(paramLogId);
        dest.writeString(paramOption);
        dest.writeString(processed);
        dest.writeString(processedDt);
        dest.writeString(processedMsg);
        dest.writeString(recordDt);
        dest.writeString(recordOrigin);
        dest.writeString(recordTime);
        dest.writeString(remark);
        dest.writeString(status);
        dest.writeString(value);
    }
}