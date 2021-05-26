package com.naury.boramjul;

public class UserInfo {
    private static Integer login_type=0;
    private static String name="";
    private static String birthday="";
    private static String sex="";
    private static String email="";
    private static String ph_num="";
    private static String address="";
    private static String rank = "";
    private static String joindate = "";
    private static String point = "";

    public  UserInfo(){

    }

    public UserInfo(Integer login_type, String name, String birthday, String sex, String email, String ph_num, String address, String rank, String joindate, String point) {
        this.login_type = login_type;
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.email = email;
        this.ph_num = ph_num;
        this.address = address;
        this.rank = rank;
        this.joindate = joindate;
        this.point = point;
    }

    public static String getPoint() {
        return point;
    }

    public static void setPoint(String point) {
        UserInfo.point = point;
    }

    public static String getRank() {
        return rank;
    }

    public static void setRank(String rank) {
        UserInfo.rank = rank;
    }

    public static String getJoindate() {
        return joindate;
    }

    public static void setJoindate(String joindate) {
        UserInfo.joindate = joindate;
    }

    public Integer getLogin_type() {
        return login_type;
    }

    public void setLogin_type(Integer login_type) {
        this.login_type = login_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPh_num() {
        return ph_num;
    }

    public void setPh_num(String ph_num) {
        this.ph_num = ph_num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
