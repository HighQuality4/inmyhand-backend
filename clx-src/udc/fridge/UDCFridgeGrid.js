/************************************************
 * UDCFridgeGrid.js
 * Created at 2025. 4. 27. 오전 12:16:40.
 *
 * @author user
 ************************************************/

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
exports.getText = function(){
	// TODO: 그리드의 뷰 모드에서 표시할 텍스트를 반환하는 하는 코드를 작성해야 합니다.
	return "";
};

/*
 * "x" 버튼(removeResult)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onRemoveResultClick(e){
	var removeResult = e.control;
	
	// 삭제이벤트 전파하기
	var event = new cpr.events.CUIEvent("removeResultCtrlBtn");
	app.dispatchEvent(event);
	
}

/*
 * 루트 컨테이너에서 property-change 이벤트 발생 시 호출.
 * 앱의 속성이 변경될 때 발생하는 이벤트 입니다.
 */
function onBodyPropertyChange(e){
	/** @type cpr.data.DataSet */
	var ds = app.getAppProperty("dataSet");
	/** @type cpr.data.DataSet */
	var originDs = app.lookup("insertFoodList");
	
	ds.copyToDataSet(originDs);
	app.lookup("grd1").redraw();
//	var answer = app.getAppProperty("myDataset");
	
}

/*
 * "Button" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	
	
	
}
