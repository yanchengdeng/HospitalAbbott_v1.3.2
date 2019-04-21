package com.comvee.hospitalabbott.bean;

/**
 * Created by F011512088 on 2018/1/12.
 */

public class LoginModel {

    /**
     * doctorInfo : {"brief":"1111111111222222222222","departId":"1","departName":"心血管外科一病区","doctorId":"1","doctorName":"comvee_test","phoneNo":"15880050290","photoUrl":"","positionId":"1","positionName":"主任医师","sex":"男","workNo":"15880050290"}
     * machine : {"insertDt":1516155729000,"isValid":1,"machineCode":"864214018459042","machineId":"180117102100136","machineNo":"864214018459042","machineSn":"864214018459042","machineStatus":1,"machineType":1,"modifyDt":1516155729000}
     * sessionToken : dfb29cce-6ed2-40a5-8078-c75f97a042ca
     */

    private DoctorInfoBean doctorInfo;
    private MachineBean machine;
    private String sessionToken;

    public DoctorInfoBean getDoctorInfo() {
        return doctorInfo;
    }

    public void setDoctorInfo(DoctorInfoBean doctorInfo) {
        this.doctorInfo = doctorInfo;
    }

    public MachineBean getMachine() {
        return machine;
    }

    public void setMachine(MachineBean machine) {
        this.machine = machine;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public static class DoctorInfoBean {
        /**
         * brief : 1111111111222222222222
         * departId : 1
         * departName : 心血管外科一病区
         * doctorId : 1
         * doctorName : comvee_test
         * phoneNo : 15880050290
         * photoUrl :
         * positionId : 1
         * positionName : 主任医师
         * sex : 男
         * workNo : 15880050290
         */

        private String brief;
        private String departId;
        private String departName;
        private String doctorId;
        private String doctorName;
        private String phoneNo;
        private String photoUrl;
        private String positionId;
        private String positionName;
        private String sex;
        private String workNo;

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public String getDepartId() {
            return departId;
        }

        public void setDepartId(String departId) {
            this.departId = departId;
        }

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getPositionId() {
            return positionId;
        }

        public void setPositionId(String positionId) {
            this.positionId = positionId;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getWorkNo() {
            return workNo;
        }

        public void setWorkNo(String workNo) {
            this.workNo = workNo;
        }

        @Override
        public String toString() {
            return "DoctorInfoBean{" +
                    "brief='" + brief + '\'' +
                    ", departId='" + departId + '\'' +
                    ", departName='" + departName + '\'' +
                    ", doctorId='" + doctorId + '\'' +
                    ", doctorName='" + doctorName + '\'' +
                    ", phoneNo='" + phoneNo + '\'' +
                    ", photoUrl='" + photoUrl + '\'' +
                    ", positionId='" + positionId + '\'' +
                    ", positionName='" + positionName + '\'' +
                    ", sex='" + sex + '\'' +
                    ", workNo='" + workNo + '\'' +
                    '}';
        }
    }

    public static class MachineBean {
        /**
         * insertDt : 1516155729000
         * isValid : 1
         * machineCode : 864214018459042
         * machineId : 180117102100136
         * machineNo : 864214018459042
         * machineSn : 864214018459042
         * machineStatus : 1
         * machineType : 1
         * modifyDt : 1516155729000
         */

        private long insertDt;
        private int isValid;
        private String machineCode;
        private String machineId;
        private String machineNo;
        private String machineSn;
        private int machineStatus;
        private int machineType;
        private long modifyDt;

        public long getInsertDt() {
            return insertDt;
        }

        public void setInsertDt(long insertDt) {
            this.insertDt = insertDt;
        }

        public int getIsValid() {
            return isValid;
        }

        public void setIsValid(int isValid) {
            this.isValid = isValid;
        }

        public String getMachineCode() {
            return machineCode;
        }

        public void setMachineCode(String machineCode) {
            this.machineCode = machineCode;
        }

        public String getMachineId() {
            return machineId;
        }

        public void setMachineId(String machineId) {
            this.machineId = machineId;
        }

        public String getMachineNo() {
            return machineNo;
        }

        public void setMachineNo(String machineNo) {
            this.machineNo = machineNo;
        }

        public String getMachineSn() {
            return machineSn;
        }

        public void setMachineSn(String machineSn) {
            this.machineSn = machineSn;
        }

        public int getMachineStatus() {
            return machineStatus;
        }

        public void setMachineStatus(int machineStatus) {
            this.machineStatus = machineStatus;
        }

        public int getMachineType() {
            return machineType;
        }

        public void setMachineType(int machineType) {
            this.machineType = machineType;
        }

        public long getModifyDt() {
            return modifyDt;
        }

        public void setModifyDt(long modifyDt) {
            this.modifyDt = modifyDt;
        }

        @Override
        public String toString() {
            return "MachineBean{" +
                    "insertDt=" + insertDt +
                    ", isValid=" + isValid +
                    ", machineCode='" + machineCode + '\'' +
                    ", machineId='" + machineId + '\'' +
                    ", machineNo='" + machineNo + '\'' +
                    ", machineSn='" + machineSn + '\'' +
                    ", machineStatus=" + machineStatus +
                    ", machineType=" + machineType +
                    ", modifyDt=" + modifyDt +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "doctorInfo=" + doctorInfo +
                ", machine=" + machine +
                ", sessionToken='" + sessionToken + '\'' +
                '}';
    }
}
