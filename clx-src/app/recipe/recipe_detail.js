/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const pathName = window.location.pathname;
	const recipeId = pathName.split("/").pop();
		
	const recipeDetailSms = app.lookup("recipeDetailSms");
	
	recipeDetailSms.setRequestActionUrl(recipeDetailSms.action+"/"+recipeId);
	recipeDetailSms.send();
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onRecipeDetailSmsSubmitSuccess(e){
	const recipeDetailSms = e.control;
	
	const result = recipeDetailSms.xhr.responseText;
	const resultJson = JSON.parse(result);
	
	// 기본 정보
	const recipeTitle = app.lookup("recipeTitle");
	const typeCategory = app.lookup("typeCategory");
	const situationCategory = app.lookup("situationCategory");
	const methodCategory = app.lookup("methodCategory");
	const recipeImg = app.lookup("recipeImg");
	const recipeSummary = app.lookup("recipeSummary");
	const difficultyValue = app.lookup("difficultyValue");
	const cookingTimeValue = app.lookup("cookingTimeValue");
	const caloriesValue = app.lookup("caloriesValue");
	const recipeAuthorImg = app.lookup("recipeAuthorImg");
	const recipeAuthorName = app.lookup("recipeAuthorName");
	const likeCountValue = app.lookup("likeCountValue");
	const viewCountValue = app.lookup("viewCountValue")
		
	recipeTitle.value = resultJson.recipeName;
	const categories = resultJson.categories;
	for (let j = 0; j < categories.length; j++) {
	  const category = categories[j];
	  const categoryType = category.recipeCategoryType;
	  switch (categoryType) {
	    case "종류별":
      	  typeCategory.value = "#" + category.recipeCategoryName;	
      	  typeCategory.visible = true;
	      break;
	    case "상황별":
	      situationCategory.value = "#" + category.recipeCategoryName;
	      situationCategory.visible = true;
	      break;
	    case "방법별":
	      methodCategory.value = "#" + category.recipeCategoryName;
	      methodCategory.visible = true;
	      break;
	    default:
	      break;
	  }
	}
	recipeImg.src = resultJson.fileUrl[0];
	recipeSummary.value = resultJson.fileUrl[0];
	if(resultJson.summary!=null) {
		recipeSummary.value = resultJson.summary;
	} else {
		recipeSummary.visible = false;
	}
	difficultyValue.value = resultJson.difficulty;
	cookingTimeValue.value = resultJson.cookingTime;
	caloriesValue.value = resultJson.calories;
	recipeAuthorImg.src = resultJson.userProfileImageUrl;
	recipeAuthorName.value = resultJson.userNickname;
	likeCountValue.value = "좋아요 "+resultJson.likeCount+"개";
	viewCountValue.value = "조회수 "+resultJson.viewCount+"번";
	
	// 재료
	const ingredientGroup = app.lookup("ingredientGroup");
	for (let i = 0; i < resultJson.ingredients.length; i++) {
		const ingredients = resultJson.ingredients[i];
		const ingredientUDC = new udc.recipe.recipe_ingredient();
		ingredientUDC.ingredientName = ingredients.ingredientName;
		ingredientUDC.ingredientCount = ingredients.ingredientQuantity + ingredients.ingredientUnit;
		
		ingredientGroup.addChild(ingredientUDC, {
		  width: "100%",
		  height: "30px",
		});	
	}
	
	// 요리 과정
	const cookingProcessGroup = app.lookup("cookingProcessGroup");
	for (let i = 0; i < resultJson.steps.length; i++) {
		const step = resultJson.steps[i];
		const cookingProcess = new udc.recipe.cooking_process();
		cookingProcess.sequence = step.stepNumber;
		cookingProcess.explanation = step.stepDescription;
		cookingProcess.img = step.fileUrl; 
				
		cookingProcessGroup.addChild(cookingProcess, {
		  width: "100%",
		  height: "100px",
		});	
	}
	
	// 등록일, 수정일
	const createAt = app.lookup("createdAt");
	createAt.value = resultJson.createdAt;
	
	if(resultJson.updateAt) {
		const updatedAt = app.lookup("updatedAt");
		const updatedAtLabel = app.lookup("updatedAtLabel");
		updatedAtLabel.visible = true;
		updatedAt.value = resultJson.updateAt;
		updatedAt.visible = true;
	}

	// 댓글
	const commentList = app.lookup("commentList");
	const commentsData = resultJson.comments;
	
	if (commentsData.length){
		for (let i = 0; i < commentsData.length; i++) {
			const comment = commentsData[i];
			const commentUDC = new udc.recipe.recipe_comment();
			commentUDC.userImg = comment.userProfileImageUrl;
			commentUDC.userNickName = comment.nickname;
			commentUDC.createAt = comment.createAt;
			commentUDC.comment = comment.commentContents;
				
			commentList.addChild(commentUDC, {
			  width: "100%",
			  height: "100px",
			});	
		}	
	} else {
		const commentNull = new cpr.controls.Output();
		commentNull.value = "아직 이 레시피에 대한 댓글이 없어요!";
		commentList.addChild(commentNull, {
		  width: "100%",
		  height: "100px",
		});	
	}
	
	
}
