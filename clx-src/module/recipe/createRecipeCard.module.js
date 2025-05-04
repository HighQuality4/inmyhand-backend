/************************************************
 * createRecipeCard.module.js
 * Created at 2025. 4. 22. 오전 11:30:01.
 *
 * @author gyrud
 ************************************************/

const createRecipeCard=(resultJson, recipeContainer, recommendRecipe = false)=> {
	for (let i = 0; i < resultJson.length; i++) {
		const data = recommendRecipe ? resultJson[i].recipeSummary : resultJson[i];
		
		const recipeCard = new udc.recipe.recipe_card();
		recipeCard.title = data.recipeName;
		recipeCard.difficulty = data.difficulty;
		recipeCard.cookingTime = data.cookingTime;
		recipeCard.author = data.userNickname;
		recipeCard.likesCount = data.likeCount;
		recipeCard.recipeImg = data.fileUrl;
		recipeCard.authorImg = data.userProfileImageUrl;
		recipeCard.calories = data.calories+"kcal";
		recipeCard.recipeId = data.id;
		
		const categories = data.categories;
		for (let j = 0; j < categories.length; j++) {
		  const category = categories[j];
		  switch (category.recipeCategoryType) {
		    case "종류별":
		      recipeCard.typeCategory = "#" + category.recipeCategoryName;
		      break;
		    case "상황별":
		      recipeCard.situationCategory = "#" + category.recipeCategoryName;
		      break;
		    case "방법별":
		      recipeCard.methodCategory = "#" + category.recipeCategoryName;
		      break;
		    default:
		      break;
		  }
		}
		
		if(recommendRecipe) {
			recipeContainer[i].addChild(recipeCard, {
			  width: "250px",
			  height: "400px",
			});		
		} else {
			recipeContainer.addChild(recipeCard, {
			  width: "250px",
			  height: "400px",
			});		
		}	
	}
}

const createRecommendRecipeCard=(resultJson, recipeContainer)=> {
	const recipeGroupList = [];
	
	for (let i = 0; i < resultJson.length; i++) {
		// 버티컬 레이아웃 생성
		const recipeGroup = new cpr.controls.Container("recipeGroup");
		const recipeLayout = new cpr.controls.layouts.VerticalLayout();
		recipeLayout.spacing = "10px";
		recipeGroup.setLayout(recipeLayout);
		
		// 아웃풋 설정
		const matchedIngredients = resultJson[i].matchedIngredients;
		const similarityOutput = new udc.recipe.recipe_similarity_output();

		similarityOutput.value = `냉장고 재료와 ${matchedIngredients}개 일치!`;
		
		recipeGroup.addChild(similarityOutput, {
		  width: "250px",
		  height: "30px",
		});	
				
		// 버티컬 레이아웃 배열에 넣기
		recipeGroupList.push(recipeGroup);	
	}
	
	// 레시피 카드 넣기
	createRecipeCard(resultJson, recipeGroupList, true);
	
	// 레시피 그룹 Container에 넣기
	for (let i = 0; i < recipeGroupList.length; i++) {
		recipeContainer.addChild(recipeGroupList[i], {
			  width: "250px",
			  height: "440px",
		});		
	}
}

exports.createRecipeCard = createRecipeCard;
exports.createRecommendRecipeCard = createRecommendRecipeCard;