/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

const showToastModule = cpr.core.Module.require("module/common/showToast");

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
	
	const similarSms = app.lookup("similarSms");
	similarSms.setRequestActionUrl(similarSms.action+"/"+recipeId);
	similarSms.send();
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
	recipeImg.src = resultJson.fileUrl;
	if(resultJson.summary!=null) {
		recipeSummary.value = resultJson.summary;
	} else {
		recipeSummary.visible = false;
	}
	difficultyValue.value = resultJson.difficulty;
	cookingTimeValue.value = resultJson.cookingTime;
	caloriesValue.value = `${resultJson.calories}kcal`;
	recipeAuthorImg.src = resultJson.userProfileImageUrl;
	recipeAuthorName.value = resultJson.userNickname;
	likeCountValue.value = "좋아요 "+resultJson.likeCount+"개";
	viewCountValue.value = "조회수 "+resultJson.viewCount+"번";
	
	// 재료
	const ingredientGroup = app.lookup("ingredientGroup");
	
	const ingredients = resultJson.ingredients;
	const allGroupsAreNull = ingredients.every(item => item.ingredientGroup == null);
	
	if (allGroupsAreNull) {
		for (let i = 0; i < ingredients.length; i++) {
			const ingredients = resultJson.ingredients[i];
			const ingredientUDC = new udc.recipe.recipe_ingredient();
			ingredientUDC.ingredientName = ingredients.ingredientName;
			ingredientUDC.ingredientCount = ingredients.ingredientQuantity + ingredients.ingredientUnit;
			
			ingredientGroup.addChild(ingredientUDC, {
			  width: "100%",
			  height: "30px",
			});	
		}
	} else {
		const groupMap = ingredients.reduce((acc, item) => {
		    const group = item.ingredientGroup || '기타';
		    if (!acc[group]) acc[group] = [];
		    acc[group].push(item);
		    return acc;
	    }, {});
	    Object.entries(groupMap).map(([group, items]) => ({
		    ingredientGroup: group,
		    ingredients: items
	    }));

	  const groupKeys = Object.keys(groupMap);
	  
	  for (let i = 0; i < groupKeys.length; i++) {
		  const group = groupKeys[i];
		  const ingredientsArray = groupMap[group];
		
		  const groupName = new cpr.controls.Output("groupName");
		  groupName.value = `[${group}]`;
		  groupName.style.css({
		    "font-weight": "bold"
		  });
		
		  ingredientGroup.addChild(groupName, {
		    width: "100%",
		    height: "30px",
		  });
		
		  for (let j = 0; j < ingredientsArray.length; j++) {
		    const ingredient = ingredientsArray[j];
		    const ingredientUDC = new udc.recipe.recipe_ingredient();
		    ingredientUDC.ingredientName = ingredient.ingredientName;
		    ingredientUDC.ingredientCount = ingredient.ingredientQuantity + ingredient.ingredientUnit;
		
		    ingredientGroup.addChild(ingredientUDC, {
		      width: "100%",
		      height: "30px",
		    });
		  }
		
		  if (i < groupKeys.length - 1) {
		    const gap = new cpr.controls.Output("gap");
		    ingredientGroup.addChild(gap, {
		      width: "100%",
		      height: "30px",
		    });
		  }
		}
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

function similar() {
    var ds = app.lookup("similar");
    var rowCount = ds.getRowCount();
    var outputPrefix = "out";
    var outextPrefix = "outext";
    
    // 데이터셋의 각 행을 순회하며 해당 컨트롤에 값 할당
    for(var i = 0; i < rowCount; i++) {
        
        var recipeName = ds.getValue(i, "recipeName");
        var recipeId = ds.getValue(i, "id");
        var fileUrl = ds.getValue(i, "fileUrl");
        
        var outputId = outputPrefix + (i + 1);
        var outextId = outextPrefix + (i + 1);
        let outputControl = app.lookup(outputId);
        let outextControl = app.lookup(outextId);


        if (outputControl) {
            outputControl.value = ""; 
            outputControl.userData("id", recipeId);
            outputControl.style.css({
                "cursor": "pointer",
                "background-image": "url(" + fileUrl + ")",
                "background-position": "center",
                "background-repeat": "no-repeat",
                "background-size": "cover",
                "width": "100%",
                "height": "100%"
            });
            outputControl.removeEventListeners("click");
            outputControl.addEventListener("click", function(e) {
                var control = e.control;
                var recipeId = control.userData("id");
                if (recipeId) {
                    window.location.href = "/recipe/" + recipeId;
                    console.log("이동: /recipe/" + recipeId);
                }
            });
        }


        if (outextControl) {
            outextControl.value = recipeName;
            outextControl.userData("id", recipeId);
            outextControl.style.css({
                "cursor": "pointer",
                "text-decoration": "underline",
                "background-image": "none",
                
            });
            outextControl.removeEventListeners("click");
            outextControl.addEventListener("click", function(e) {
                var control = e.control;
                var recipeId = control.userData("id");
                if (recipeId) {
                    window.location.href = "/recipe/" + recipeId;
                    console.log("이동: /recipe/" + recipeId);
                }
            });
        }
    }
}


/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onSimilarSmsSubmitSuccess(e){
	var similarSms = e.control;
	similar();
	app.lookup("grb9").redraw();
}

/*
 * 공유하기 버튼 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onShareGroupClick(e){
	const shareBtn = e.control;
	const url = window.location.href;
  	navigator.clipboard.writeText(url).then(() => {
      showToastModule.showToast("레시피 URL이 복사되었습니다!");
    });
}
