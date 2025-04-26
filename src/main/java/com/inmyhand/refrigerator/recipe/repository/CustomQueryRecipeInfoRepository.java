package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.admin.dto.AdminRecipeInfoDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomQueryRecipeInfoRepository {

    Page<AdminRecipeInfoDto> customQueryRecipe(Pageable pageable, String name, Long id);
}
