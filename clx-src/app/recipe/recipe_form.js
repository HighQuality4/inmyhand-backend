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

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
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
}

/*
 * 매트릭스 서브미션에서 before-submit 이벤트 발생 시 호출.
 * 통신을 시작하기전에 발생합니다.
 */
function onRecipeCreateSmsBeforeSubmit(e){
	const recipeCreateSms = e.control;
	
	// 기본정보
	const recipeName = app.lookup("recipeNameInput").value;
	const receipSummary = app.lookup("recipeSummaryInput").value;
	const servings = app.lookup("servingsInput").value;
	const difficulty = app.lookup("difficultySelect").value;
	const cookingTime = app.lookup("cookingTimeSelect").value;
	const calories = app.lookup("caloriesInput").value;	
	
	// 완성사진
	const files = ["https://mybucket.s3.amazonaws.com/images/recipe1.jpg"];
	
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
	
	// 과정
	const cookingProcessGroup = app.lookup("cookingProcessCreateGroup");
	const cookingProcessForm = cookingProcessGroup.getChildren();
	let steps = []; // 결과
	
	for(let i=0; i<cookingProcessForm.length; i++){
		const cookingProcessResult = cookingProcessForm[i].getCookingProcessValue();
		const cookingProcessObj = {...cookingProcessResult, stepNumber:i+1};
		steps.push(cookingProcessObj);
	}
	
	const requestData = {
						    userId: 1,
						    parentRecipeId: null,
						    recipeName: recipeName,
						    cookingTime: cookingTime,
						    difficulty: difficulty,
						    calories: calories,
						    summary: receipSummary,
						    servings: servings,
						    files,
						    ingredients,
						    steps,
						    categories
						  };					  
  	 					  
	 recipeCreateSms.addParameter("param", requestData);
}

/*
 * "등록하기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	const recipeCreateSms = app.lookup("recipeCreateSms");
	recipeCreateSms.send();
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
