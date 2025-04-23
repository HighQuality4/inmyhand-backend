/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

const createRecipeCardModule = cpr.core.Module.require("module/recipe/createRecipeCard");
const createRecipeCard = createRecipeCardModule.createRecipeCard;

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const popularRecipeListSms = app.lookup("popularRecipeListSms");
	popularRecipeListSms.send();
	
	const allRecipeListSms = app.lookup("allRecipeListSms");
	allRecipeListSms.addParameter("page", 0);
	allRecipeListSms.addParameter("size", 6);
	allRecipeListSms.send();
	allRecipeListSms.removeAllParameters();
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onPopularRecipeListSmsSubmitSuccess(e){
	const popularRecipeListSms = e.control;
	
	const recipeContainer = app.lookup("popluarRecipeList"); 

	const result = popularRecipeListSms.xhr.responseText;
	const resultJson = JSON.parse(result);
	
	createRecipeCard(resultJson, recipeContainer);

	recipeContainer.redraw();
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onAllRecipeListSmsSubmitSuccess(e){
	var allRecipeListSms = e.control;
	
	const recipeContainer = app.lookup("allRecipeList");
	
	if(recipeContainer.getChildrenCount()){
		recipeContainer.removeAllChildren();
	}
	
	const recipeGroup = app.lookup("allRecipeListPageGroup");
	const pageIndexer = app.lookup("allRecipePageIndexer");
	
	const result = allRecipeListSms.xhr.responseText;
	const resultJson = JSON.parse(result);
	
	createRecipeCard(resultJson.content, recipeContainer);
	
	pageIndexer.init(resultJson.totalElements, resultJson.size, (resultJson.number)+1);
		
	recipeGroup.redraw();
}

/*
 * 페이지 인덱서에서 selection-change 이벤트 발생 시 호출.
 * Page index를 선택하여 선택된 페이지가 변경된 후에 발생하는 이벤트.
 */
function onAllRecipePageIndexerSelectionChange(e){
	var allRecipePageIndexer = e.control;
	
	const allRecipeListSms = app.lookup("allRecipeListSms");
	allRecipeListSms.addParameter("page", Number(allRecipePageIndexer.currentPageIndex)-1);
	allRecipeListSms.addParameter("size", 6);
	allRecipeListSms.send();
	allRecipeListSms.removeAllParameters();
}
