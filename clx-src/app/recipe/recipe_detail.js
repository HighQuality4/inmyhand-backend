/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

const showToastModule = cpr.core.Module.require("module/common/showToast");

const getRecipeId=()=> {
	const pathName = window.location.pathname;
	return  pathName.split("/").pop();
}

const addCommentUDC=(comment, commentList)=>{
	const commentUDC = new udc.recipe.recipe_comment();
			
	if (!comment.userProfileImageUrl) {
		commentUDC.userImg = "theme/images/user.svg";
	} else {
		commentUDC.userImg = comment.userProfileImageUrl;
	}
	commentUDC.userNickName = comment.nickname;
	commentUDC.createdAt = comment.createdAt;
	commentUDC.comment = comment.commentContents;
		
	commentList.addChild(commentUDC, {
	  width: "100%",
	  height: "100px",
	});	
}

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const recipeId = getRecipeId();
		
	const recipeDetailSms = app.lookup("recipeDetailSms");
	
	recipeDetailSms.setRequestActionUrl(recipeDetailSms.action+"/"+recipeId);
	recipeDetailSms.send();
	
	const similarSms = app.lookup("similarSms");
	similarSms.setRequestActionUrl(similarSms.action+"/"+recipeId);
	similarSms.send();
	
	const recipeViewSms = app.lookup("recipeViewCreateSms");
	recipeViewSms.setRequestActionUrl(recipeViewSms.action+recipeId);
	recipeViewSms.send();	
	
	const recipeNutrientSms = app.lookup("recipeNutrientSms");
	recipeNutrientSms.setRequestActionUrl(recipeNutrientSms.action+recipeId);
	recipeNutrientSms.send();
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
	if(!resultJson.userProfileImageUrl){
		recipeAuthorImg.src = "theme/images/user.svg";
	} else {
		recipeAuthorImg.src = resultJson.userProfileImageUrl;	
	}
	
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
		cookingProcess.sequence = `${step.stepNumber}.`;
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
			addCommentUDC(comment, commentList);
		}
	} else {
		const commentNull = new cpr.controls.Output();
		commentNull.value = "아직 이 레시피에 대한 댓글이 없어요!";
		commentNull.style.css({
		  "text-align": "center",
		  "line-height": "100px"
		});
		commentList.addChild(commentNull, {
		  width: "100%",
		  height: "100px",
		});	
	}
}


// 유사한 레시피 3개 추출 및 표시 함수
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
                    // SPA 방식으로 URL 변경
                    history.pushState({}, '', `/recipe/${recipeId}`);
                    // 세션 스토리지에 저장
                    sessionStorage.setItem('currentRecipeId', recipeId);
//    window.location.href = `/recipe/${recipeId}`;
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
                    // SPA 방식으로 URL 변경
                    history.pushState({}, '', `/recipe/${recipeId}`);
//                   	history.replaceState({}, '', `/recipe/${recipeId}`);
                    // 세션 스토리지에 저장
                    sessionStorage.setItem('currentRecipeId', recipeId);
                    // 이동
                    window.location.href = `/recipe/${recipeId}`;
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
	app.getContainer().redraw();
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

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onCheckLikeStatusSmsSubmitSuccess(e){
	const checkLikeStatusSms = e.control;

	const result = checkLikeStatusSms.xhr.responseText;
	const resultJson = JSON.parse(result);

	if(resultJson) {
		const likeCountImg = app.lookup("likeCountImg");
		likeCountImg.src = "../../theme/images/heart.svg";
	}
}

/*
 * 그룹에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onLikeCountGroupClick(e){
	const recipeId = getRecipeId();
	
	const recipeLikeToggleSms = app.lookup("recipeLikeToggleSms");
	recipeLikeToggleSms.setRequestActionUrl(recipeLikeToggleSms.action+recipeId);
	recipeLikeToggleSms.send();
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onRecipeLikeToggleSmsSubmitSuccess(e){
	var recipeLikeToggleSms = e.control;
	const result = recipeLikeToggleSms.xhr.responseText;
	const resultJson = JSON.parse(result);
	const likeCountImg = app.lookup("likeCountImg");
	const likeCountValue = app.lookup("likeCountValue");
	const match = likeCountValue.value.match(/\d+/);
	const likeCountNumber = match ? parseInt(match[0], 10) : null;
	
	if(resultJson.message.liked) {
		likeCountImg.src = "theme/images/heart.svg";
		likeCountValue.value = `좋아요 ${likeCountNumber+1}개`;
	} else {
		likeCountImg.src = "theme/images/heart_empty.svg";
		likeCountValue.value = `좋아요 ${likeCountNumber-1}개`;
	}
	
	showToastModule.showToast(resultJson.message.message);
}

/*
 * 댓글 input에서 keydown 이벤트 발생 시 호출.
 * 사용자가 키를 누를 때 발생하는 이벤트. 키코드 관련 상수는 {@link cpr.events.KeyCode}에서 참조할 수 있습니다.
 */
function onCommentInputKeydown(e){
	const commentInput = e.control;
	// Enter를 눌렀다면 전송 버튼을 클릭한 이벤트를 불러온다
	if(e.keyCode == cpr.events.KeyCode.ENTER) { 
	  var vcBtnSend = app.lookup("commentSubmitBtn"); 
	  vcBtnSend.click(); 
	}
}

/*
 * 댓글 등록 버튼(commentSubmitBtn)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onCommentSubmitBtnClick(e){
	const commentSubmitBtn = e.control;
	const recipeId = getRecipeId();
	const commentInput = app.lookup("commentInput");
	const commentContents = commentInput.value;
	
	const recipeCommentSubmitSms = app.lookup("recipeCommentSubmitSms");
	recipeCommentSubmitSms.setRequestActionUrl(recipeCommentSubmitSms.action+recipeId);
	recipeCommentSubmitSms.addParameter("param", {commentContents:commentContents});
	recipeCommentSubmitSms.send();
	recipeCommentSubmitSms.removeAllParameters();
	commentInput.value = "";
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onRecipeCommentSubmitSmsSubmitSuccess(e){
	const recipeCommentSubmitSms = e.control;
	const result = recipeCommentSubmitSms.xhr.responseText;
	const comment = JSON.parse(result);
	
	const commentList = app.lookup("commentList");
	
	addCommentUDC(comment, commentList);
}


/**
 * 외부 라이브러리 'd3'를 통해 Chart를 구성합니다.
 * @param {cpr.events.CUIEvent} e
 */
function shellLoadD3Chart( /* cpr.events.CUIEvent */ e, jsonData) {
	var xmlns = "http://www.w3.org/2000/svg";
    var svgElem = document.createElementNS(xmlns, "svg");
    svgElem.setAttributeNS(null, "width", "100%");
    svgElem.setAttributeNS(null, "height", "100%");
    svgElem.setAttribute("id", "svgElem");
    svgElem.style.display = "block";

    e.content.appendChild(svgElem);

    var width = svgElem.getBoundingClientRect().width;
    var height = svgElem.getBoundingClientRect().height;
    var margin = {
        top: 10,
        right: 20,
        left: 20,
        bottom: 10
    };

    var svg = d3.select("svg#svgElem")
        .attr("text-anchor", "middle");

    /* 안쪽, 바깥쪽 반지름 값 설정 */
    var arc = d3.arc().innerRadius(0).outerRadius(Math.min(width, height) / 2.5);

    /* 차트의 값을 나타낼 위치 설정 */
    var arcLabel = (function() {
        var radius = Math.min(width, height) / 2.5 * 0.8;
        return d3.arc().innerRadius(radius).outerRadius(radius);
    })();

    /* 파이 차트 구성 */
    var pie = d3.pie()
        .sort(null)
        .value(function(b) {
            return b.value;
        });

    var arcs = pie(jsonData);

    var g = svg.append("g")
        .attr("id", "firstG")
        .attr("transform", "translate(" + (width / 2) + ", " + (height / 2) + ")");

    /* 파이 차트의 각 영역을 구분하는 선 */
    g.selectAll("path")
        .data(arcs)
        .enter()
        .append("path")
        .attr("fill", function(d) {
            return d.data.color;
        })
        .attr("stroke", "white")
        .attr("d", arc)
        .text(function(d) {
            return d.data.name + ": " + d.data.value;
        });

    /* 파이 차트의 각 영역 별 값을 나타냄 */
    if (jsonData.length === 1 && jsonData[0].name === '영양소 분석 중') {
	    g.append("text")
	        .attr("text-anchor", "middle")
	        .attr("dy", "0.35em")
	        .style("font-size", "14px")
	        .style("fill", "#333")
	        .text(jsonData[0].label);
	} else {
	    var text = g.selectAll("text")
	        .data(arcs)
	        .enter()
	        .append("text")
	        .attr("transform", function(d) {
	            return "translate(" + arcLabel.centroid(d) + ")"
	        })
	        .attr("dy", "0.35em");
	
	    text.append("tspan")
	        .attr("x", 0)
	        .attr("y", "-0.7em")
	        .style("font-weight", "bold")
	        .text(function(d) {
	            return d.data.name;
	        });
	
	    text.filter(function(d) {
	        return d.endAngle - d.startAngle > 0.25;
	    })
		    .append("tspan")
		    .attr("x", 0)
		    .attr("y", "0.7em")
		    .attr("fill-opacity", "0.7")
		    .text(function(d) {
		        return d.data.label;
	    	});
	 }
	
}

/*
 * 쉘에서 load 이벤트 발생 시 호출.
 * 쉘이 그려진 후 내용을 작성하는 이벤트.
 */
function onNutrientGraphLoad(e){
	const nutrientGraph = e.control;
	const shlContent = e.content;
	
	if(!nutrientGraph.userData("jsonData")) {
		const loadingData = [{
		    name: "영양소 분석 중",
		    value: 100,
		    label: "AI가 열심히 분석 중이에요!",
		    color: "#EAF1F3"
		}];
		nutrientGraph.userData("jsonData", loadingData);
	}
		
	shellLoadD3Chart(e, nutrientGraph.userData("jsonData"));
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onRecipeNutrientSmsSubmitSuccess(e){
	var recipeNutrientSms = e.control;
	
	const result = recipeNutrientSms.xhr.responseText;
	const nutrients = JSON.parse(result);
	
	// 영양소 분석 내용
	const fitnessScore = app.lookup("fitnessScore");
	const analysisResult = app.lookup("analysisResult");
	const aiGuide = app.lookup("aiGuide");
	
	fitnessScore.value = `건강관심사와의 적합도 ${nutrients.fitnessScore}점`;
	analysisResult.value = nutrients.analysisResult;
	aiGuide.value = "※ AI가 분석한 내용으로 일부 정보가 정확하지 않을 수 있어요. 참고용으로만 이용해 주세요.";
	
	// 영양소 분석 그래프
	const nutrientGraph = app.lookup("nutrientGraph");
	
	var jsonData = [{
	    "name": "탄수화물",
	    "value": Number(nutrients.carbohydrate),
	    "label": `${nutrients.carbohydrate}%`,
	    "color": "#EAF1F3",
	}, {
	    "name": "단백질",
	    "value": Number(nutrients.protein),
	    "label": `${nutrients.protein}%`,
	    "color": "#E5E6C7",
	}, {
	    "name": "지방",
	    "value": Number(nutrients.fat),
	    "label": `${nutrients.fat}%`,
	    "color": "#EFE4D3",
	}, {
	    "name": "비타민",
	    "value": Number(nutrients.vitamin),
	    "label": `${nutrients.vitamin}%`,
	    "color": "#EDA9EB",
	}, {
	    "name": "무기질",
	    "value": Number(nutrients.mineral),
	    "label": `${nutrients.mineral}%`,
	    "color": "#FFE79B",
	}];
	
	nutrientGraph.userData("jsonData", jsonData);
	nutrientGraph.redraw();
}
