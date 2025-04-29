package com.inmyhand.refrigerator.payment.service;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.payment.domain.dto.PaymentConfirmResponse;
import com.inmyhand.refrigerator.payment.domain.dto.SubscriptionDTO;
import com.inmyhand.refrigerator.payment.domain.entity.PaymentEntity;
import com.inmyhand.refrigerator.payment.mapper.PaymentConfirmMapper;
import com.inmyhand.refrigerator.payment.repository.PaymentRepository;
import com.inmyhand.refrigerator.payment.domain.entity.SubscriptionEntity;
import com.inmyhand.refrigerator.payment.domain.entity.SubscriptionPlansEntity;
import com.inmyhand.refrigerator.payment.repository.SubscriptionPlansEntityRepository;
import com.inmyhand.refrigerator.payment.repository.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlansEntityRepository subscriptionPlansEntityRepository;

    /**
     * 결제 승인 응답을 처리하여 결제 정보 저장
     */
    public void savePaymentFromConfirmation(PaymentConfirmResponse response, Long memberId) {
        // 관련 엔티티 조회
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다: " + memberId));

        // DTO -> Entity 변환
        PaymentEntity paymentEntity = PaymentConfirmMapper.toEntity(response, memberEntity);

        // 저장 및 반환
        PaymentEntity saved = paymentRepository.save(paymentEntity);

        List<PaymentEntity> byMemberEntityId = paymentRepository.findByMemberEntityId(2L);

        SubscriptionEntity sub = subscriptionRepository.save(SubscriptionEntity.builder()
                .status("paid")
                .subscriptionPlansEntity(subscriptionPlansEntityRepository
                        .findById(1L)
                        .orElse(new SubscriptionPlansEntity()))
                .paymentList(byMemberEntityId)
                .build()
        );

        saved.setSubscriptionEntity(sub);

    }

    /**
     * 결제 키로 결제 정보 조회 후 업데이트
     */
    public PaymentEntity updatePaymentByKey(PaymentConfirmResponse response) {
        PaymentEntity existingEntity = (PaymentEntity) paymentRepository.findByPaymentKey(response.getPaymentKey())
                .orElseThrow(() -> new EntityNotFoundException("결제 정보를 찾을 수 없습니다: " + response.getPaymentKey()));

        PaymentConfirmMapper.updateEntityFromResponse(response, existingEntity);
        return paymentRepository.save(existingEntity);
    }

    /**
     * 결제 정보를 DTO로 변환
     */
    public PaymentConfirmResponse getPaymentResponseByKey(String paymentKey) {
        PaymentEntity entity = (PaymentEntity) paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new EntityNotFoundException("결제 정보를 찾을 수 없습니다: " + paymentKey));

        return PaymentConfirmMapper.toDto(entity);
    }

    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionRepository.findAllSubscriptionDTOs();
    }

    public List<SubscriptionDTO> getSubscriptionsByMemberId(Long memberId) {
        return subscriptionRepository.findSubscriptionDTOsByMemberId(memberId);
    }


}
