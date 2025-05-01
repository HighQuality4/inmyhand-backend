/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

const recipeCategoryModules = cpr.core.Module.require("data/recipeCategoryItems");
const typeCategoryItems = recipeCategoryModules.typeCategoryItems;
const situationCategoryItems = recipeCategoryModules.situationCategoryItems;
const methodCategoryItems = recipeCategoryModules.methodCategoryItems; 

const recipeInfoSelectItemsModule = cpr.core.Module.require("data/recipeInfoSelectItems");
const difficultyItems = recipeInfoSelectItemsModule.difficulty;
const cookingTimeItems = recipeInfoSelectItemsModule.cookingTime;

const isLastPathSegmentNumberMd = cpr.core.Module.require("module/common/isLastPathSegmentNumber");
const isLastPathSegmentNumber = isLastPathSegmentNumberMd.isLastPathSegmentNumber();

const requestEncoder=(api, data)=> {
	/**
	 * @type cpr.protocols.Submission
	 */
	const submission = api;

	return 	{
		key : "param" , //content에 해당하는 키값 설정
		content: data["param"]	//보낼 데이터
	}
}


// input 값 추출 및 json 파싱
const setRecipeRequestParam =(sms)=>{
	// 기본정보
	const recipeName = app.lookup("recipeNameInput").value;
	const receipSummary = app.lookup("recipeSummaryInput").value;
	const servings = app.lookup("servingsInput").value;
	const difficulty = app.lookup("difficultySelect").value;
	const cookingTime = app.lookup("cookingTimeSelect").value;
	const calories = app.lookup("caloriesInput").value;
	
	// 대표사진
	const recipeImg = app.lookup("recipeImg").file;
	
	// 카테고리
	const typeCategory = app.lookup("typeCategorySelect").value;
	const situationCategory = app.lookup("situationCategorySelect").value;
	const methodCategory = app.lookup("methodCategorySelect").value;
	
	const categories = [
					    {
					      recipeCategoryName: typeCategory,
					      recipeCategoryType: "종류별"
					    },
					    {
					      recipeCategoryName: situationCategory,
					      recipeCategoryType: "상황별"
					    },
					    {
					      recipeCategoryName: methodCategory,
					      recipeCategoryType: "방법별"
					    }
					  ];
	// 재료
	const ingredientCreateGroup = app.lookup("ingredientCreateGroup");
	const ingredientForms = ingredientCreateGroup.getChildren();
	let ingredients = []; // 결과
	
	for(let i=0; i<ingredientForms.length; i++){
		const ingredientResult = ingredientForms[i].getIngredientsList();
		ingredients.push(...ingredientResult);
	}
	
	// 과정 및 단계 이미지 파일 추출 
	const cookingProcessGroup = app.lookup("cookingProcessCreateGroup"); 
	const cookingProcessForm = cookingProcessGroup.getChildren();
	const steps = []; 
	const stepFiles = [];

	for (let i = 0; i < cookingProcessForm.length; i++) { 
		const form = cookingProcessForm[i]; 
		const stepData = form.getCookingProcessValue(); // { stepDescription, fileUrl (File) }
		const stepObj = {
  			stepDescription: stepData.stepDescription,
  			stepNumber: i + 1
		};

		steps.push(stepObj);

		if (stepData.fileUrl) {
  			stepFiles.push(stepData.fileUrl); // fileUrl이 실제 파일 객체여야 함
		}
	}
	console.log(stepFiles);
	
	const requestData = {
						    userId: 1,
						    parentRecipeId: null,
						    recipeName: recipeName,
						    cookingTime: cookingTime,
						    difficulty: difficulty,
						    calories: calories,
						    summary: receipSummary,
						    servings: servings,
						    ingredients,
						    steps,
						    categories
						  };					  
  	 
  	 sms.setRequestEncoder(requestEncoder);
	 sms.addParameter("param", requestData);
	 sms.addFileParameter("files", recipeImg);
	 // 여러 개의 stepFiles를 반복적으로 추가 
	 for (let i = 0; i < stepFiles.length; i++) { 
	 	sms.addFileParameter("stepFiles", stepFiles[i]); 
	 }
}

/*
 * 루트 컨테이너에서 init 이벤트 발생 시 호출.
 * 앱이 최초 구성될 때 발생하는 이벤트 입니다.
 */
function onBodyInit(e){
	// 카테고리 아이템 지정
	const typeCategorySelect = app.lookup("typeCategorySelect");
	const situationCategorySelet = app.lookup("situationCategorySelect");
	const methodCategorySelect = app.lookup("methodCategorySelect");
	const difficultySelect = app.lookup("difficultySelect");
	const cookingTimeSelect = app.lookup("cookingTimeSelect");
	
	for (let i=0; i<typeCategoryItems.length; i++){
		typeCategorySelect.addItem(new cpr.controls.Item(typeCategoryItems[i], typeCategoryItems[i]));	
	}
	
	for (let i=0; i<situationCategoryItems.length; i++){
		situationCategorySelet.addItem(new cpr.controls.Item(situationCategoryItems[i], situationCategoryItems[i]));	
	}
	
	for (let i=0; i<methodCategoryItems.length; i++){
		methodCategorySelect.addItem(new cpr.controls.Item(methodCategoryItems[i], methodCategoryItems[i]));	
	}
	
	for (let i=0; i<difficultyItems.length; i++){
		difficultySelect.addItem(new cpr.controls.Item(difficultyItems[i], difficultyItems[i]));	
	}
	
	for (let i=0; i<cookingTimeItems.length; i++){
		cookingTimeSelect.addItem(new cpr.controls.Item(cookingTimeItems[i], cookingTimeItems[i]));	
	}
	
	// 레시피 form 타이틀 수정
	const isNumber = isLastPathSegmentNumber;
	if(isNumber[0]){
		const formTitle = app.lookup("formTitle");
		formTitle.value = "레시피 수정";
		
		const submitBtn = app.lookup("submitBtn");
		submitBtn.value = "수정하기";
	}
}


/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const isNumber = isLastPathSegmentNumber;
	if(isNumber[0]){
	    const recipeInfoSms = app.lookup("recipeInfoSms");
	    
	    recipeInfoSms.setRequestActionUrl(recipeInfoSms.action + "/" + isNumber[1]);
	    recipeInfoSms.send();
	}
}

/*
 * 서브미션에서 before-submit 이벤트 발생 시 호출.
 * 통신을 시작하기전에 발생합니다.
 */
function onRecipeCreateSmsBeforeSubmit(e){
	const recipeCreateSms = e.control;
	setRecipeRequestParam(recipeCreateSms);
}

/*
 * 서브미션에서 before-submit 이벤트 발생 시 호출.
 * 통신을 시작하기전에 발생합니다.
 */
function onRecipeUpdateSmsBeforeSubmit(e){
	const recipeUpdateSms = e.control;
	recipeUpdateSms.setRequestActionUrl(recipeUpdateSms.action+"/"+isLastPathSegmentNumber[1]);
	setRecipeRequestParam(recipeUpdateSms);
}

/*
 * "등록하기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	const isNumber = isLastPathSegmentNumber;
	if(isNumber[0]){
		const recipeUpdateSms = app.lookup("recipeUpdateSms");
		recipeUpdateSms.send();
	} else {
		const recipeCreateSms = app.lookup("recipeCreateSms");
		recipeCreateSms.send();
	} 
}

/*
 * "재료 그룹 추가하기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onIngredientGroupAdd(e){
	const button = e.control;
	const ingredientCreateGroup = app.lookup("ingredientCreateGroup");
	
	const ingredientForm = new udc.recipe.ingredient_create();
	ingredientForm.addEventListener("delete", onIngredientCreateFormDelete);
	ingredientCreateGroup.addChild(ingredientForm, {
		  width: "100%",
		  height: "auto",
	});	
}

/*
 * 사용자 정의 컨트롤에서 delete 이벤트 발생 시 호출.
 */
function onIngredientCreateFormDelete(e){
	const ingredientCreateForm = e.control;
	const ingredientCreateGroup = app.lookup("ingredientCreateGroup");
	ingredientCreateGroup.removeChild(ingredientCreateForm);
}

/*
 * "과정 추가하기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onCookingProcessAdd(e){
	const button = e.control;
	const cookingProcessCreateGroup = app.lookup("cookingProcessCreateGroup");
	
	const cookingProcessForm = new udc.recipe.cooking_process_create();
	cookingProcessForm.seq = cookingProcessCreateGroup.getChildrenCount()+1;
	
	cookingProcessCreateGroup.addChild(cookingProcessForm, {
		  width: "100%",
		  height: "80px",
	});	
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onRecipeInfoSmsSubmitSuccess(e){
	var recipeInfoSms = e.control;
	
	const result = recipeInfoSms.xhr.responseText;
	const resultJson = JSON.parse(result);
	
	// 기본 정보
	const recipeName = app.lookup("recipeNameInput");
	const receipSummary = app.lookup("recipeSummaryInput");
	const servings = app.lookup("servingsInput");
	const difficulty = app.lookup("difficultySelect");
	const cookingTime = app.lookup("cookingTimeSelect");
	const calories = app.lookup("caloriesInput");
	
	recipeName.value = resultJson.recipeName;
	receipSummary.value = resultJson.summary;
	servings.value = resultJson.servings;
	difficulty.value = resultJson.difficulty;
	cookingTime.value = resultJson.cookingTime;
	calories.value = resultJson.calories;
	
	// TODO: 완성사진
	const recipeImg = app.lookup("recipeImg");
	recipeImg.value = resultJson.fileUrl;
	
	// 카테고리
	const typeCategory = app.lookup("typeCategorySelect");
	const situationCategory = app.lookup("situationCategorySelect");
	const methodCategory = app.lookup("methodCategorySelect");
	
	const categories = resultJson.categories;
	for (let j = 0; j < categories.length; j++) {
	  const category = categories[j];
	  const categoryType = category.recipeCategoryType;
	  switch (categoryType) {
	    case "종류별":
      	  typeCategory.value = category.recipeCategoryName;	
	      break;
	    case "상황별":
	      situationCategory.value = category.recipeCategoryName;
	      break;
	    case "방법별":
	      methodCategory.value = category.recipeCategoryName;
	      break;
	    default:
	      break;
	  }
	}
	
	// 재료
	const ingredientCreateGroup = app.lookup("ingredientCreateGroup");
	ingredientCreateGroup.removeAllChildren();
	
	// 1. ingredientGroup별로 묶기
	const groupedIngredients = {};
	
	for (let i = 0; i < resultJson.ingredients.length; i++) {
	  const ing = resultJson.ingredients[i];
	  const groupName = ing.ingredientGroup || "재료";
	
	  if (!groupedIngredients[groupName]) {
	    groupedIngredients[groupName] = [];
	  }
	
	  groupedIngredients[groupName].push({
	  	id: ing.id,
	    ingredientName: ing.ingredientName,
	    ingredientQuantity: ing.ingredientQuantity,
	    ingredientUnit: ing.ingredientUnit
	  });
	}
	
	const groupNames = Object.keys(groupedIngredients); 
	
	// 재료 넣기
	for(let i=0; i<groupNames.length; i++){
		const groupName = groupNames[i];
	    const ingredientForm = new udc.recipe.ingredient_create();
	    ingredientForm.setIngredientsList({
	        [groupName]: groupedIngredients[groupName]
	    });
	    ingredientForm.addEventListener("delete", onIngredientCreateFormDelete);
	    ingredientCreateGroup.addChild(ingredientForm, {
	        width: "100%",
	        height: "auto",
	    });
	}
	
	// 요리 과정
	const cookingProcessCreateGroup = app.lookup("cookingProcessCreateGroup");
	cookingProcessCreateGroup.removeAllChildren();
	for (let i = 0; i < resultJson.steps.length; i++) {
		const step = resultJson.steps[i];
		const cookingProcessForm = new udc.recipe.cooking_process_create();
		cookingProcessForm.seq = step.stepNumber;
		cookingProcessForm.explanation = step.stepDescription;
		cookingProcessForm.img = step.fileUrl; 
				
		cookingProcessCreateGroup.addChild(cookingProcessForm, {
		  width: "100%",
		  height: "80px",
	});	
	}
}

/*
 * 레시피 등록 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onRecipeCreateSmsSubmitSuccess(e){
	var recipeCreateSms = e.control;
	alert("레시피 등록이 완료되었습니다");
	history.pushState({}, '', `/recipe`);
}

/*
 * 레시피 수정 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onRecipeUpdateSmsSubmitSuccess(e){
	var recipeUpdateSms = e.control;
	// TODO: 마이페이지의 내가 등록한 레시피로 이동
	alert("레시피 수정이 완료되었습니다");
//	history.pushState({}, '', `/auth/mypage`);
}
