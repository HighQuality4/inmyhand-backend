/************************************************
 * admin-recipe.js
 * Created at 2025. 4. 23. 오후 3:48:33.
 *
 * @author choeyeongbeom
 ************************************************/

/*
 * 페이지 인덱서에서 selection-change 이벤트 발생 시 호출.
 * Page index를 선택하여 선택된 페이지가 변경된 후에 발생하는 이벤트.
 */
/**
 * 레시피 목록 페이지 선택 변경 이벤트 핸들러
 * @param {cpr.events.CSelectionEvent} e - 선택 변경 이벤트
 */
function onRecipeIndexSelectionChange(e) {
    var recipeIndex = e.control;
   
}

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const numericValue = window.location.pathname.match(/\d+/);
	const recipe = app.lookup("admin-recipe-get");
	recipe.setParameters("id",numericValue );
	recipe.send();
}

/*
 * 페이지 인덱서에서 selection-change 이벤트 발생 시 호출.
 * Page index를 선택하여 선택된 페이지가 변경된 후에 발생하는 이벤트.
 */
function onRecipeIndex1SelectionChange(e){
	var recipeIndex1 = e.control;
	const page = Number(recipeIndex1.currentPageIndex)-1;
	const numericValue = window.location.pathname.match(/\d+/);
	const recipe = app.lookup("admin-recipe-get");
	recipe.setParameters("id",numericValue );
	recipe.setParameters("pageId",page);
	recipe.send();
	
}
	
