package com.example.les17.dto;

public class UserDto {
    private String username;

    private String password;

    private String[] roles;

    // uuid for latest cv file
    private String cvUniqueId;
    private String cvFilename;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getCvUniqueId() {
        return cvUniqueId;
    }

    public void setCvUniqueId(String cvUniqueId) {
        this.cvUniqueId = cvUniqueId;
    }

    public String getCvFilename() {
        return cvFilename;
    }

    public void setCvFilename(String cvFilename) {
        this.cvFilename = cvFilename;
    }
}
