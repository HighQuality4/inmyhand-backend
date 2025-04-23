package com.inmyhand.refrigerator.member.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.healthinfo.domain.entity.HateFoodEntity;
import com.inmyhand.refrigerator.healthinfo.domain.entity.HealthInterestEntity;
import com.inmyhand.refrigerator.healthinfo.domain.entity.MemberAllergyEntity;
import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;
import com.inmyhand.refrigerator.payment.domain.entity.PaymentEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCommentEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeViewsEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"memeberAllergyList", "hateFoodList", "healthInterestList",
        "memberRoleList", "refreshTokenList", "paymentList",
        "fridgeMemberList", "recipeInfoList", "recipeCommentList", "recipeLikesList",
        "recipeViewsList", "recipeNutrientAnalysisList", "filesList"})
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name", nullable = false, length = 100)
    private String memberName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "nickname", nullable = false, unique = true, length = 100)
    private String nickname;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @Column(name = "provider_id", nullable = false, length = 100)
    private String providerId;

    @Column(name = "password", length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MemberStatus status = MemberStatus.active;
//    private MemberStatus status;

    @Column(name = "phone_num", length = 100)
    private String phoneNum;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) {
            this.regdate = new Date();
        }
    }

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("memberEntity")
    private List<MemberAllergyEntity> memberAllergyList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("memberEntity")
    private List<HateFoodEntity> hateFoodList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("memberEntity")
    private List<HealthInterestEntity> healthInterestList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("memberEntity")
    private List<MemberRoleEntity> memberRoleList = new ArrayList<>();

    @OneToOne(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("memberEntity")
    private RefreshTokenEntity refreshTokenEntity;

//    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnoreProperties("memberEntity")
//    private List<SubscriptionEntity> subscriptionList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("memberEntity")
    private List<PaymentEntity> paymentList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
//    @JsonIgnore
    //TODO N+1 발생
    @JsonIgnoreProperties("memberEntity")
    private List<FridgeMemberEntity> fridgeMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("memberEntity")
    private List<RecipeInfoEntity> recipeInfoList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JsonIgnore
    @JsonIgnoreProperties("memberEntity")
    private List<RecipeCommentEntity> recipeCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("memberEntity")
    private List<RecipeLikesEntity> recipeLikesList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("memberEntity")
    private List<RecipeViewsEntity> recipeViewsList = new ArrayList<>();


    @OneToOne(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("memberEntity")
    private FilesEntity filesEntity;


    /**
     * 냉장고 멤버 추가 메서드
     * @param fridgeMember 추가할 냉장고 멤버
     */
    public void addFridgeMember(FridgeMemberEntity fridgeMember) {
        if (fridgeMember == null) {
            return;
        }

        if (this.fridgeMemberList == null) {
            this.fridgeMemberList = new ArrayList<>();
        }

        // 중복 방지
        if (!this.fridgeMemberList.contains(fridgeMember)) {
            this.fridgeMemberList.add(fridgeMember);

            // 양방향 연관관계 설정
            if (fridgeMember.getMemberEntity() != this) {
                fridgeMember.setMemberEntity(this);
            }
        }
    }

    /**
     * 냉장고 멤버 제거 메서드
     * @param fridgeMember 제거할 냉장고 멤버
     * @return 제거 성공 여부
     */
    public boolean removeFridgeMember(FridgeMemberEntity fridgeMember) {
        if (fridgeMember == null || this.fridgeMemberList == null) {
            return false;
        }

        boolean removed = this.fridgeMemberList.remove(fridgeMember);

        if (removed && fridgeMember.getMemberEntity() == this) {
            fridgeMember.setMemberEntity(null);
        }

        return removed;
    }

    /**
     * 특정 ID의 냉장고 멤버 제거
     * @param fridgeMemberId 제거할 냉장고 멤버 ID
     * @return 제거 성공 여부
     */
    public boolean removeFridgeMemberById(Long fridgeMemberId) {
        if (this.fridgeMemberList == null || fridgeMemberId == null) {
            return false;
        }

        for (int i = 0; i < this.fridgeMemberList.size(); i++) {
            FridgeMemberEntity fridgeMember = this.fridgeMemberList.get(i);
            if (fridgeMemberId.equals(fridgeMember.getId())) {
                this.fridgeMemberList.remove(i);

                if (fridgeMember.getMemberEntity() == this) {
                    fridgeMember.setMemberEntity(null);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * 모든 냉장고 멤버 연관관계 제거
     */
    public void clearAllFridgeMembers() {
        if (this.fridgeMemberList == null) {
            return;
        }

        // ConcurrentModificationException 방지를 위해 새 리스트로 복사
        List<FridgeMemberEntity> membersToRemove = new ArrayList<>(this.fridgeMemberList);

        for (FridgeMemberEntity fridgeMember : membersToRemove) {
            removeFridgeMember(fridgeMember);
        }

        this.fridgeMemberList.clear();
    }

    /**
     * 특정 냉장고와 관련된 모든 멤버십 제거
     * @param fridgeId 냉장고 ID
     * @return 제거된 멤버십 수
     */
    public int removeFridgeMembersByFridgeId(Long fridgeId) {
        if (this.fridgeMemberList == null || fridgeId == null) {
            return 0;
        }

        List<FridgeMemberEntity> membersToRemove = new ArrayList<>();

        for (FridgeMemberEntity fridgeMember : this.fridgeMemberList) {
            if (fridgeMember.getFridgeEntity() != null &&
                    fridgeId.equals(fridgeMember.getFridgeEntity().getId())) {
                membersToRemove.add(fridgeMember);
            }
        }

        int count = membersToRemove.size();

        for (FridgeMemberEntity fridgeMember : membersToRemove) {
            removeFridgeMember(fridgeMember);
        }

        return count;
    }

    /**
     * 레시피 좋아요 추가
     */
    public void addRecipeLike(RecipeLikesEntity recipeLike) {
        this.recipeLikesList.add(recipeLike);
        recipeLike.setMemberEntity(this);
    }

    /**
     * 레시피 좋아요 제거
     */
    public void removeRecipeLike(RecipeLikesEntity recipeLike) {
        this.recipeLikesList.remove(recipeLike);
        recipeLike.setMemberEntity(null);
    }

    /**
     * 레시피 좋아요 모두 제거
     */
    public void clearRecipeLikes() {
        for (RecipeLikesEntity recipeLike : new ArrayList<>(this.recipeLikesList)) {
            removeRecipeLike(recipeLike);
        }
    }

    // RecipeViews 관련 편의 메서드

    /**
     * 레시피 조회 기록 추가
     */
    public void addRecipeView(RecipeViewsEntity recipeView) {
        this.recipeViewsList.add(recipeView);
        recipeView.setMemberEntity(this);
    }

    /**
     * 레시피 조회 기록 제거
     */
    public void removeRecipeView(RecipeViewsEntity recipeView) {
        this.recipeViewsList.remove(recipeView);
        recipeView.setMemberEntity(null);
    }

    /**
     * 레시피 조회 기록 모두 제거
     */
    public void clearRecipeViews() {
        for (RecipeViewsEntity recipeView : new ArrayList<>(this.recipeViewsList)) {
            removeRecipeView(recipeView);
        }
    }

    /**
     * 레시피 댓글 추가
     */
    public void addRecipeComment(RecipeCommentEntity comment) {
        this.recipeCommentList.add(comment);
        if (comment.getMemberEntity() != this) {
            comment.setMemberEntity(this);
        }
    }

    /**
     * 레시피 댓글 제거
     */
    public void removeRecipeComment(RecipeCommentEntity comment) {
        this.recipeCommentList.remove(comment);
        // 댓글의 작성자 참조는 유지할지 결정해야 함
        // 시스템에 따라 작성자 정보는 보존하고 싶을 수 있음
    }

    /**
     * 모든 레시피 댓글과의 연관관계 제거
     * 회원 탈퇴 시 사용 가능
     */
    public void clearRecipeComments() {
        // 방법 1: 댓글을 모두 삭제 (orphanRemoval=true 효과)
        this.recipeCommentList.clear();

        // 방법 2: 댓글은 유지하되 회원 연관관계만 수정
    /*
    for (RecipeCommentEntity comment : new ArrayList<>(this.recipeCommentList)) {
        this.recipeCommentList.remove(comment);
        // 댓글의 회원 정보를 null로 설정하거나
        // 특별한 "탈퇴한 회원" 엔티티로 변경
    }
    */
    }
}