package com.inmyhand.refrigerator.member.controller;

import com.inmyhand.refrigerator.member.domain.dto.MyFoodInfoDTO;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.member.service.MemberService;
import com.inmyhand.refrigerator.member.service.MemberServiceImpl;
import com.inmyhand.refrigerator.recipe.service.RecipeCommentService;
import com.inmyhand.refrigerator.recipe.service.RecipeLikeService;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final MemberServiceImpl memberService;
    private final RecipeQueryService recipeQueryService;


    @PostMapping("/profile")
    public ResponseEntity<?> getProfileInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", userDetails.getNickname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        profile.put("createdAt", sdf.format(userDetails.getRegdate()));

        Map<String, Object> result = new HashMap<>();
        result.put("dmProfile", profile);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/myrefre")
    public ResponseEntity<?> getMyRefreInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUserId();

        List<MyFoodInfoDTO> flist = memberService.MyRefreInfo(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("dsMyRef", flist);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/myrecipe/like")
    public ResponseEntity<?> getMyLikeRecipe(@AuthenticationPrincipal CustomUserDetails userDetails
                                            ,@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(memberService.getMyLikeRecipeInfo(userId, page, size));
    }

    @GetMapping("/myrecipe/write")
    public ResponseEntity<?> getMyWriteRecipe(@AuthenticationPrincipal CustomUserDetails userDetails
                                            ,@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(memberService.getMyOwnRecipeInfo(userId, page, size));
    }

    @GetMapping("/mycomment")
    public ResponseEntity<?> getMyCommentRecipe(@AuthenticationPrincipal CustomUserDetails userDetails
                                                ,@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(memberService.getMyCommentedRecipeInfo(userId, page, size));
    }
}
