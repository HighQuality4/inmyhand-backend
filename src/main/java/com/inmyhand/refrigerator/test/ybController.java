package com.inmyhand.refrigerator.test;

import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.admin.service.AdminService;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.fridge.repository.FridgeMemberRepository;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCommentEntity;
import com.inmyhand.refrigerator.recipe.mapper.RecipeDetailMapper;
import com.inmyhand.refrigerator.recipe.mapper.RecipeSummaryMapper;
import com.inmyhand.refrigerator.recipe.repository.RecipeCommentRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import com.inmyhand.refrigerator.recipe.service.engine.SimilarRecipeLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/yb")
public class ybController {

    private final RecipeInfoRepository recipeInfoRepository;
    private final MemberRepository memberRepository;
    private final RecipeCommentRepository recipeCommentRepository;
    private final FridgeMemberRepository fridgeMemberRepository;
    private final AdminService adminService;
    private final RecipeDetailMapper recipeDetailMapper;
    private final RecipeSummaryMapper recipeSummaryMapper;
    private final SimilarRecipeLogic similarRecipeLogic;

    @GetMapping("/rec")
    @ResponseBody
    public ResponseEntity<?> rec() {
        return ResponseEntity.ok(recipeInfoRepository.findAll());
    }


    @GetMapping("/rec2")
    @ResponseBody
    public ResponseEntity<?> rec2() {
        return ResponseEntity.ok(recipeDetailMapper.toDtoList(recipeInfoRepository.findAll()));
    }

    @GetMapping("/rec3")
    @ResponseBody
    public ResponseEntity<?> rec3() {
        return ResponseEntity.ok(recipeSummaryMapper.toDtoList(recipeInfoRepository.findAll()));
    }

    @GetMapping("/rec4/{id}")
    @ResponseBody
    public ResponseEntity<?> rec4(@PathVariable("id") Long id) {

        return ResponseEntity.ok(similarRecipeLogic.getSimilarRecipes(id));
    }

    @GetMapping("/mem")
    @ResponseBody
    public ResponseEntity<?> mem() {

        return ResponseEntity.ok(memberRepository.findAll());
    }

    @GetMapping("/mem2")
    @ResponseBody
    public ResponseEntity<?> mem2() {

        return ResponseEntity.ok(memberRepository.findById(1L).get());
    }


    @GetMapping("/mem4")
    @ResponseBody
    public ResponseEntity<?> mem4() {
        MemberEntityDto memberDTOTest = adminService.findByMemberOne(1L);
        return ResponseEntity.ok(memberDTOTest);
    }

    @GetMapping("/mem5")
    @ResponseBody
    public ResponseEntity<?> mem5() {
        List<MemberEntityDto> byMemberAll = adminService.findByMemberAll();
        return ResponseEntity.ok(byMemberAll);
    }

    @GetMapping("/comm")
    @ResponseBody
    public ResponseEntity<?> comm() {

//        Optional<RecipeCommentEntity> byIdCustom = recipeCommentRepository.findByIdCustom(2L);
        List<RecipeCommentEntity> all = recipeCommentRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/fri")
    @ResponseBody
    public ResponseEntity<?> fri() {

        List<FridgeMemberEntity> all = fridgeMemberRepository.findAll();
        return ResponseEntity.ok(all);
    }

}
