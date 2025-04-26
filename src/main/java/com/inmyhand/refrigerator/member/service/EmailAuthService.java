package com.inmyhand.refrigerator.member.service;

public interface EmailAuthService {
    void sendEmailVerification(String email);
    boolean verifyEmailCode(int code, String email);
}
