/************************************************
 * createRecipeCard.module.js
 * Created at 2025. 4. 22. 오전 11:30:01.
 *
 * @author gyrud
 ************************************************/

const createRecipeCard=(resultJson, recipeContainer)=> {
	for (let i = 0; i < resultJson.length; i++) {
		const recipeCard = new udc.recipe.recipe_card();
		recipeCard.title = resultJson[i].recipeName;
		recipeCard.difficulty = resultJson[i].difficulty;
		recipeCard.cookingTime = resultJson[i].cookingTime;
		recipeCard.author = resultJson[i].userNickname;
		recipeCard.likesCount = resultJson[i].likeCount;
		recipeCard.recipeImg = resultJson[i].fileUrl;
		recipeCard.authorImg = resultJson[i].userProfileImageUrl;
		recipeCard.calories = resultJson[i].calories+"kcal";
		recipeCard.recipeId = resultJson[i].id;
		
		const categories = resultJson[i].categories;
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
		
		recipeContainer.addChild(recipeCard, {
		  width: "250px",
		  height: "400px",
		});	
	}
}

exports.createRecipeCard = createRecipeCard;