package com.livetv.normal.model;

public class User {
    private String code;
    private String device;
    private String deviceId;
    private String expiration_date;
    private String name;
    private String password;
    private String user_agent;
    private String user_name;
    private String version;

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version2) {
        this.version = version2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getExpiration_date() {
        return this.expiration_date;
    }

    public void setExpiration_date(String expiration_date2) {
        this.expiration_date = expiration_date2;
    }

    public String getDevice() {
        return this.device;
    }

    public void setDevice(String device2) {
        this.device = device2;
    }

    public String getUser_agent() {
        return this.user_agent;
    }

    public void setUser_agent(String user_agent2) {
        this.user_agent = user_agent2;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name2) {
        this.user_name = user_name2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId2) {
        this.deviceId = deviceId2;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getCode() {
        return this.code;
    }
}
