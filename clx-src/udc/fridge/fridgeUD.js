/************************************************
 * fridgeUD.js
 * Created at 2025. 4. 24. 오전 9:29:13.
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
 * "삭제" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
		
	/** @type cpr.controls.Grid */
	var grd = app.getAppProperty("grdUDCtrl");
	
	console.log(grd);
	
	var rowIndex = grd.getSelectedRowIndex();
	grd.deleteRow(rowIndex);
	
	
}

/*
 * "저장" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick3(e){
	var button = e.control;
	
	// 저장 이벤트 (save-update-click) 전파하기
	var saveUpdateClick = new cpr.events.CUIEvent("save-update-click");
	app.dispatchEvent(saveUpdateClick);	
	
}

/*
 * "취소" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick4(e){
	var button = e.control;
		
	alert("데이터 입력/변경 전으로 돌아갑니다.");
	
	/** @type cpr.controls.Grid */
	var grd = app.getAppProperty("grdUDCtrl");
	
	grd.revertData(); 
	
}
