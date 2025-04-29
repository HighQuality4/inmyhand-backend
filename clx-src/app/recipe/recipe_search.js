/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

const setRecipeListModule = cpr.core.Module.require("module/recipe/setRecipeList");
const setRecipeList = setRecipeListModule.setRecipeList;

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */

const size = 10;

const submitSms=(page)=> {
	const searchRecipeListSms = app.lookup("searchRecipeListSms");
	const searchParams = new URLSearchParams(window.location.search);
	const keyword = searchParams.get('keyword');
	console.log(keyword);
	searchRecipeListSms.setRequestActionUrl(`${searchRecipeListSms.action}?keyword=${keyword}&page=${page}&size=${size}`);
	searchRecipeListSms.send();
}

function onBodyLoad(e){
	const searchRecipeListSms = app.lookup("searchRecipeListSms");
	submitSms(0);
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onSearchRecipeListSmsSubmitSuccess(e){
	const searchRecipeListSms = e.control;
	
	const recipeContainer = app.lookup("allRecipeList");
	const recipeGroup = app.lookup("allRecipeListPageGroup");
	const pageIndexer = app.lookup("allRecipePageIndexer");

	setRecipeList(searchRecipeListSms, recipeContainer, recipeGroup, pageIndexer);
}

/*
 * 페이지 인덱서에서 selection-change 이벤트 발생 시 호출.
 * Page index를 선택하여 선택된 페이지가 변경된 후에 발생하는 이벤트.
 */
function onAllRecipePageIndexerSelectionChange(e){
	var allRecipePageIndexer = e.control;
	
	const page = Number(allRecipePageIndexer.currentPageIndex)-1;
	submitSms(page);
}
