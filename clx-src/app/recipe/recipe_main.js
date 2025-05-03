/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

const createRecipeCardModule = cpr.core.Module.require("module/recipe/createRecipeCard");
const createRecipeCard = createRecipeCardModule.createRecipeCard;
const setRecipeListModule = cpr.core.Module.require("module/recipe/setRecipeList");
const setRecipeList = setRecipeListModule.setRecipeList;
const slidify = cpr.core.Module.require("module/common/Slidifiy").slidify;

let sortBy = null;
let sortType = null;
const size = 6;

// 레시피 정렬 조회 요청
const onSortRecipeListSmsSubmit=(page)=>{
	const sortRecipeListSms = app.lookup("sortRecipeListSms");
	sortRecipeListSms.addParameter("page", page);
	sortRecipeListSms.addParameter("size", size);
	sortRecipeListSms.addParameter("sortBy", sortBy);
	sortRecipeListSms.addParameter("sortType", sortType);
	sortRecipeListSms.send();
	sortRecipeListSms.removeAllParameters();
}

// 레시피 목록 조회 초기화
const initRecipeList = () => {
	const allRecipeListSms = app.lookup("allRecipeListSms");
	allRecipeListSms.addParameter("page", 0);
	allRecipeListSms.addParameter("size", size);
	allRecipeListSms.send();
	allRecipeListSms.removeAllParameters();
}


/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const popularRecipeListSms = app.lookup("popularRecipeListSms");
	popularRecipeListSms.send();
	
	initRecipeList();
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

	const slide = slidify(recipeContainer);
	slide.start();
	
	recipeContainer.redraw();
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onAllRecipeListSmsSubmitSuccess(e){
	const allRecipeListSms = e.control;
	
	const recipeContainer = app.lookup("allRecipeList");
	const recipeGroup = app.lookup("allRecipeListPageGroup");
	const pageIndexer = app.lookup("allRecipePageIndexer");

	setRecipeList(allRecipeListSms, recipeContainer, recipeGroup, pageIndexer);
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onSortRecipeListSmsSubmitSuccess(e){
	const sortRecipeListSms = e.control;
	
	const recipeContainer = app.lookup("allRecipeList");
	const recipeGroup = app.lookup("allRecipeListPageGroup");
	const pageIndexer = app.lookup("allRecipePageIndexer");
	
	setRecipeList(sortRecipeListSms, recipeContainer, recipeGroup, pageIndexer);
}

/*
 * 페이지 인덱서에서 selection-change 이벤트 발생 시 호출.
 * Page index를 선택하여 선택된 페이지가 변경된 후에 발생하는 이벤트.
 */
function onAllRecipePageIndexerSelectionChange(e){
	var allRecipePageIndexer = e.control;
	
	const page = Number(allRecipePageIndexer.currentPageIndex)-1;
	
	if(sortType==null){
		const allRecipeListSms = app.lookup("allRecipeListSms");
		allRecipeListSms.addParameter("page", page);
		allRecipeListSms.addParameter("size", size);
		allRecipeListSms.send();
		allRecipeListSms.removeAllParameters();	
	} else {
		onSortRecipeListSmsSubmit(page);
	}
}

/*
 * 사용자 정의 컨트롤에서 item-click 이벤트 발생 시 호출.
 */
function onRecipe_sort_selectItemClick(e){
	const recipeSortSelect = e.control;
	
	const recipeSortSelectItem = recipeSortSelect.getSortSelectItemValue();
		
	if(recipeSortSelectItem) {
		[sortBy, sortType] = recipeSortSelectItem;
		onSortRecipeListSmsSubmit(0, 6);
	}
}

/*
 * 사용자 정의 컨트롤에서 reset 이벤트 발생 시 호출.
 */
function onRecipe_sort_selectReset(e){
	const recipeSortSelect = e.control;
	
	recipeSortSelect.resetRecipeSortSelectBoxItem();
	initRecipeList();
}
