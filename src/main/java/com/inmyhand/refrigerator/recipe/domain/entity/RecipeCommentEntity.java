package com.inmyhand.refrigerator.recipe.domain.entity;

import com.fasterxml.jackson.annotation.*;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "recipe_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"memberEntity", "recipeInfoEntity"})
public class RecipeCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_contents", nullable = false)
    private String commentContents;

    @Column(name = "created_at", columnDefinition = "timestamp default now()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nickname")
    @JsonIdentityReference(alwaysAsId = true)
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private RecipeInfoEntity recipeInfoEntity;

    // RecipeCommentEntity 클래스에 추가
    /**
     * 댓글 작성자 설정
     */
    public void setMemberEntity(MemberEntity memberEntity) {
        // 기존 관계 제거
        if (this.memberEntity != null && this.memberEntity != memberEntity) {
            this.memberEntity.getRecipeCommentList().remove(this);
        }

        this.memberEntity = memberEntity;

        // 새 관계 추가
        if (memberEntity != null && !memberEntity.getRecipeCommentList().contains(this)) {
            memberEntity.getRecipeCommentList().add(this);
        }
    }
}
