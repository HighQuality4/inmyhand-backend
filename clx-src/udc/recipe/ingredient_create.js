/************************************************
 * ingredient_create.js
 * Created at 2025. 4. 24. 오후 3:14:43.
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
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	/** @type cpr.controls.Grid */
	const ingredientGrid = app.lookup("ingredientGrid");
	ingredientGrid.insertRow(0, true);
}

/*
 * "재료 추가하기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonInsertClick(e){
	const button = e.control;
	
	/** @type cpr.controls.Grid */
	const ingredientGrid = app.lookup("ingredientGrid");
	const rowCountIndex = ingredientGrid.getRowCount();
	
	ingredientGrid.insertRow(rowCountIndex, true);
}

/*
 * "재료 삭제하기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonDeleteClick(e){
	const button = e.control;
	
	/** @type cpr.controls.Grid */
	const ingredientGrid = app.lookup("ingredientGrid");
	// 선택된 로우 수
	const selectRow = ingredientGrid.getSelectedRowIndex();
	
	if(selectRow != -1){
		ingredientGrid.deleteRow(selectRow);
		ingredientGrid.revertRowData(selectRow);
	}else{
		alert("삭제할 재료를 선택해주세요!");
	}
}

/*
 * "재료 그룹 삭제하기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onGroupDeleteButtonClick(e){
	const deleteEvent = new cpr.events.CMouseEvent("delete");
	app.dispatchEvent(deleteEvent);
}

exports.getIngredientsList=()=>{
	const ingredientGroupValue = app.lookup("ingredientGroupValue");
	const ingredientGrid = app.lookup("ingredientGrid");
	const group = ingredientGroupValue.value;
	
	const ingredientsJson = ingredientGrid.getExportData();
	let ingredientsList = ingredientsJson.rowgroups[1].data;
	
	for(let i=0; i<ingredientsList.length; i++){
		ingredientsList[i].push(group);
	}
	
	console.log(ingredientsList);
	
	const ingredientsResult = ingredientsList.map(item => ({
		ingredientName: item[1],
	    ingredientQuantity: item[2],
	    ingredientUnit: item[3],
	    ingredientGroup: item[4]
	}));
	
	return ingredientsResult;
}
