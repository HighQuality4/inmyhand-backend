/************************************************
 * fridgeCRUD.js
 * Created at 2025. 4. 16. 오후 12:34:21.
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
 * "추가" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick2(e){
	var button = e.control;

	// ctrl + shift + / : (Grid,control,...) type 지정하기
	// 타입 지정 이유 -> 타입 지정을 안하면 관련 함수 사용이 안됨
	
	/** @type cpr.controls.Grid */
	var grd = app.getAppProperty("grdCtrl");

	// 전체 로우 수
	var totalIndex = grd.getContentRowCount();
	// 선택된 로우 수
	var rowIndex = grd.getSelectedRowIndex();
	
	if(rowIndex != -1){
		grd.insertRow(rowIndex,true);
	}else{
		grd.insertRow(totalIndex+1,true);
		console.log("totalIndex : "+totalIndex);
	}
	
}

/*
 * "삭제" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	
	/** @type cpr.controls.Grid */
	var grd = app.getAppProperty("grdCtrl");
	
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
	
	// 저장 이벤트 (save-click) 전파하기
	var saveClick = new cpr.events.CUIEvent("save-click");
	app.dispatchEvent(saveClick);	
	
}

//외부에서 해당 함수 사용하고 싶을때 
function outBasicExample(){
	alert("외부에서 내가 정의한 api, 사용 방법");
	alert("exports.함수이름");
}

exports.outBasicExample = outBasicExample;

/*
 * "취소" 버튼(cancelDummy)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onCancelDummyClick(e){
	var cancelDummy = e.control;
	alert("데이터 입력/변경 전으로 돌아갑니다.");
	
	/** @type cpr.controls.Grid */
	var grd = app.getAppProperty("grdCtrl");

	grd.revertData(); 
}

/*
 * 루트 컨테이너에서 property-change 이벤트 발생 시 호출.
 * 앱의 속성이 변경될 때 발생하는 이벤트 입니다.
 */
function onBodyPropertyChange(e){
	// 로드 이벤트와 같이 이벤트 탐
	// udc 앱속성이 변경되는 경우
	if(e.property == "visibleAddButton"){
		if(app.getAppProperty("visibleAddButton")){
			// 추가버튼 보이게
			app.lookup("updateGroup").getLayout().setColumnVisible(0, true);
		}else{
			// 추가버튼 안보이게
			app.lookup("updateGroup").getLayout().setColumnVisible(0, false);
		}
	}
	
	if(e.property == "deleteStr"){
		if(app.getAppProperty("deleteStr").length > 0){
			app.lookup("deleteDummy").value("퇴출");
		}
	}
}



