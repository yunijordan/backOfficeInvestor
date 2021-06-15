package com.investor.backofficeinvestor.services.dto;

public class PasswordDTO {
    private char[] currentPassword;
    private char[] newPassword;

    public char[] getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(char[] currentPassword) {
        this.currentPassword = currentPassword;
    }

    public char[] getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(char[] newPassword) {
        this.newPassword = newPassword;
    }
}
