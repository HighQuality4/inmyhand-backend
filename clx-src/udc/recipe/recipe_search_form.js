/************************************************
 * recipe_search_form.js
 * Created at 2025. 4. 28. 오후 5:39:42.
 *
 * @author gyrud
 ************************************************/

/*
 * 서치 인풋에서 search 이벤트 발생 시 호출.
 * Searchinput의 enter키 또는 검색버튼을 클릭하여 인풋의 값이 Search될때 발생하는 이벤트
 */
function onRecipeSearchFormSearch(e){
	const recipeSearchForm = e.control;	
	history.pushState({}, '', `/recipe/search?keyword=${recipeSearchForm.value}`);
}
