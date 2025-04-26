/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
/*
 * "Button" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	alert("동작 테스트합니다");

}


/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad2(e){

	app.lookup("getFridgeList").send();
	app.lookup("getFoodList").send();
}


/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onGetFridgeListSubmitSuccess(e){
	var getFridgeList = e.control;
	
//	app.lookup("fridgeList").getRowDataRanged().forEach(function(each){
//	 
//		// NavigationBar에 사용할 올바른 객체는 MenuItem입니다
//		var item = new cpr.controls.MenuItem(each.fridgeName, each.fridgeId, null);
//	
//		// NavigationBar에 아이템 추가
//		app.lookup("fridgNavbar").addItem(item);
//	});

}


/*
 * 내비게이션 바에서 item-click 이벤트 발생 시 호출.
 * 아이템 클릭시 발생하는 이벤트.
 */
function onFridgNavbarItemClick(e){

	var fridgNavbar = e.control;

	alert("클릭됨 "+e.item.value);
  	
  	var setClickLabelValue = e.item.value; 
  	
  	// 정적의 nav값 추출했음.
  	var vcNav = app.lookup("fridgNavbar");
  	
    var voItem = vcNav.getItemByValue(setClickLabelValue);
    vcNav.focusItem(voItem);
    alert(voItem.value);	
	
}

function focusItem(vsValue) {
    
    var vcNav = app.lookup("fridgNavbar");
    var voItem = vcNav.getItemByValue(vsValue);
    vcNav.focusItem(voItem);
    alert(voItem);
    
}

function fixGridSetting(){
	
	var grid = app.lookup("foodListGrid");
	// 좌측 틀고정을 가로사이즈 100px, 고정되는 셀 수는 2개로 설정합니다.
	grid.leftSplitWidth = 100;
	grid.rightSplit = 1;

}

function onRowAdded(event) {
	
	/** @type cpr.controls.provider.GridRow */
    var grid = event.control;
    var addedRow = event.row;
    var index = event.rowIndex;
    var target = event.relativeTargetName;

    console.log("추가된 행의 인덱스:", index);
    console.log("타겟 이름:", target);

    // 추가된 행에 특정 데이터 설정
    addedRow.setValue("columnName", "기본값");

    // 필요에 따라 UI 업데이트 또는 다른 로직 실행
    
}




//----------------------------------------

/*
 * 사용자 정의 컨트롤에서 save-click 이벤트 발생 시 호출.
 * 저장버튼 클릭시 발생하는 이벤트
 */
function onFridgeCRUDSaveClick(e){
	var fridgeCRUD = e.control;
	
	
	var foodDataSet = app.lookup("insertFoodList");
	
	var foodDataMap = app.lookup("fridgeIdParam");
	
//	fridgeId 
// 	foodDataMap.setAttr("fridgeId", "1");
	
	
	
	var totalInsertRow = foodDataSet.getRowCount();
		
	alert("변경된 저장 방법");

	app.lookup("sendFoodList").send();
	app.lookup("getFoodList").send();
	
	app.lookup("foodListGrid").redraw();
	app.lookup("insertFoodgrd").revertData(); 
	
	
}



/*
 * 사용자 정의 컨트롤에서 save-update-click 이벤트 발생 시 호출.
 * update서버연결저장
 */
function onFridgeUDSaveUpdateClick(e){
	var fridgeUD = e.control;
	app.lookup("updateFoodList").send();
	
	
	app.lookup("getFoodList").send();
	app.lookup("foodListGrid").redraw();	
	
}
