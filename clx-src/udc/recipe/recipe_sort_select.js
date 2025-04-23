/************************************************
 * recipe_sort_button.js
 * Created at 2025. 4. 22. 오후 1:44:34.
 *
 * @author gyrud
 ************************************************/
/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const recipeSortSelectBox = app.lookup("recipeSortSelectBox");
	
	recipeSortSelectBox.addItem(new cpr.controls.TreeItem("칼로리", "calories", "root"));
	recipeSortSelectBox.addItem(new cpr.controls.TreeItem("높은순", "calories_desc", "calories"));
	recipeSortSelectBox.addItem(new cpr.controls.TreeItem("낮은순", "calories_asc", "calories"));
	recipeSortSelectBox.addItem(new cpr.controls.TreeItem("난이도", "difficulty", "root"));
	recipeSortSelectBox.addItem(new cpr.controls.TreeItem("높은순", "difficulty_asc", "difficulty"));
	recipeSortSelectBox.addItem(new cpr.controls.TreeItem("낮은순", "difficulty_desc", "difficulty"));
}

/*
 * 링크드 콤보 박스에서 item-click 이벤트 발생 시 호출.
 * 아이템 클릭시 발생하는 이벤트.
 */
function onRecipeSortSelectBoxItemClick(e){
	const itemClickEvent = new cpr.events.CMouseEvent("item-click");
	app.dispatchEvent(itemClickEvent); 
}

exports.getSortSelectItemValue =()=> {
	const recipeSortSelectBox = app.lookup("recipeSortSelectBox");
	
	if(recipeSortSelectBox.getSelectedIndices().length == 2) {
		const sortValue = recipeSortSelectBox.getSelectionLast().value; 
		return sortValue.split("_");
	};
}

/*
 * "정렬 초기화" 버튼(sortReset)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onSortResetClick(e){
	const reset = new cpr.events.CMouseEvent("reset");
	app.dispatchEvent(reset); 
}

exports.resetRecipeSortSelectBoxItem=()=>{
	const recipeSortSelectBox = app.lookup("recipeSortSelectBox");
	recipeSortSelectBox.clearSelection();
}
