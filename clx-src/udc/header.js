/************************************************
 * header.js
 * Created at 2025. 4. 10. 오후 6:43:53.
 *
 * @author gyrud
 ************************************************/

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
exports.getText = function(){
	// TODO: 그리드의 뷰 모드에서 표시할 텍스트를 반환하는 하는 코드를 작성해야 합니다.
	return "";
};

/*
 * "냉장고 관리" 버튼(fridge)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onFridgeClick(e){
	var fridge = e.control;
	window.location.pathname = "/testtt";
}

/*
 * "레시피" 버튼(recipe)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onRecipeClick(e){
	var recipe = e.control;
	
	window.location.pathname = "/recipe";
}
