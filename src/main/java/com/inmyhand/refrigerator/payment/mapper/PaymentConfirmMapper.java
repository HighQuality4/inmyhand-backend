package com.inmyhand.refrigerator.payment.mapper;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.payment.domain.dto.PaymentConfirmResponse;
import com.inmyhand.refrigerator.payment.domain.entity.PaymentEntity;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentConfirmMapper {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    /**
     * PaymentConfirmResponse를 PaymentEntity로 변환
     */
    public static PaymentEntity toEntity(PaymentConfirmResponse response, 
                                        MemberEntity memberEntity
                                       ) {
        if (response == null) {
            return null;
        }

        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(response.getOrderId());
        entity.setPaymentKey(response.getPaymentKey());
        entity.setOrderName(response.getOrderName());
        
        // 금액 변환 (문자열 -> Integer)
        if (response.getTotalAmount() != null) {
            try {
                entity.setAmount(Integer.parseInt(response.getTotalAmount()));
            } catch (NumberFormatException e) {
                entity.setAmount(0);
            }
        }
        
        entity.setMethod(response.getMethod());
        entity.setStatus(response.getStatus());
        
        // 날짜 변환 (문자열 -> Timestamp)
        if (response.getRequestedAt() != null) {
            try {
                Date requestDate = dateFormat.parse(response.getRequestedAt());
                entity.setRequestAt(new Timestamp(requestDate.getTime()));
            } catch (ParseException e) {
                // 날짜 파싱 오류 처리
            }
        }
        
        if (response.getApprovedAt() != null && !response.getApprovedAt().isEmpty()) {
            try {
                Date approvedDate = dateFormat.parse(response.getApprovedAt());
                entity.setApprovedAt(new Timestamp(approvedDate.getTime()));
            } catch (ParseException e) {
                // 날짜 파싱 오류 처리
            }
        }
        
        // 관계 설정
        entity.setMemberEntity(memberEntity);

        // 현재 시간으로 생성/수정일 설정
        Date now = new Date();
        entity.setRegdate(now);
        entity.setUpdatedAt(now);
        
        return entity;
    }

    /**
     * PaymentEntity를 PaymentConfirmResponse로 변환
     */
    public static PaymentConfirmResponse toDto(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        PaymentConfirmResponse response = new PaymentConfirmResponse();
        response.setPaymentKey(entity.getPaymentKey());
        response.setOrderId(entity.getOrderId());
        response.setOrderName(entity.getOrderName());
        
        // 금액 변환 (Integer -> 문자열)
        if (entity.getAmount() != null) {
            response.setTotalAmount(entity.getAmount().toString());
        }
        
        response.setMethod(entity.getMethod());
        response.setStatus(entity.getStatus());
        
        // 날짜 변환 (Timestamp -> 문자열)
        if (entity.getRequestAt() != null) {
            response.setRequestedAt(dateFormat.format(entity.getRequestAt()));
        }
        
        if (entity.getApprovedAt() != null) {
            response.setApprovedAt(dateFormat.format(entity.getApprovedAt()));
        }
        
        return response;
    }

    /**
     * 기존 엔티티 업데이트
     */
    public static void updateEntityFromResponse(PaymentConfirmResponse response, PaymentEntity entity) {
        if (response == null || entity == null) {
            return;
        }
        
        if (response.getOrderId() != null) entity.setOrderId(response.getOrderId());
        if (response.getPaymentKey() != null) entity.setPaymentKey(response.getPaymentKey());
        if (response.getOrderName() != null) entity.setOrderName(response.getOrderName());
        
        // 금액 변환
        if (response.getTotalAmount() != null) {
            try {
                entity.setAmount(Integer.parseInt(response.getTotalAmount()));
            } catch (NumberFormatException e) {
                // 숫자 변환 오류 처리
            }
        }
        
        if (response.getMethod() != null) entity.setMethod(response.getMethod());
        if (response.getStatus() != null) entity.setStatus(response.getStatus());
        
        // 날짜 변환
        if (response.getRequestedAt() != null) {
            try {
                Date requestDate = dateFormat.parse(response.getRequestedAt());
                entity.setRequestAt(new Timestamp(requestDate.getTime()));
            } catch (ParseException e) {
                // 날짜 파싱 오류 처리
            }
        }
        
        if (response.getApprovedAt() != null && !response.getApprovedAt().isEmpty()) {
            try {
                Date approvedDate = dateFormat.parse(response.getApprovedAt());
                entity.setApprovedAt(new Timestamp(approvedDate.getTime()));
            } catch (ParseException e) {
                // 날짜 파싱 오류 처리
            }
        }
        
        // 업데이트 시간 갱신
        entity.setUpdatedAt(new Date());
    }
}
