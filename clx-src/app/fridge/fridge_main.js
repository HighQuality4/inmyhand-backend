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
function onBodyLoad2(e){
	// 냉장고 목록
	const getFridgeList= app.lookup("getFridgeList"); 
	// 식재료 목록
	const getFoodList = app.lookup("getFoodList");
	
	// 식재료 그리드 
	const foodListGrid = app.lookup("foodListGrid");
	
	// 서브미션 보내기
	getFridgeList.send().then(function(pbSuccess){
		if(pbSuccess){
			app.lookup("fridgNavbar").redraw();
			app.lookup("fridgNavbar").selectItemByValue("1");
		}
	});
	getFoodList.send();
	
	// 그룹 접기 이벤트 추가하기 
	 getFoodList.addEventListener("submit-success", function(e) {
        alert("식재료 정보 가져오기 성공"); 
        
        foodListGrid.redraw(); 
        setTimeout(function() {
            foodListGrid.collapseAll(); // 0.1초 뒤에 접기 (collapseAll) 실행
        }, 100);
        
    });
    
//    // 선택된 냉장고 목록 효과 추가하기
//    getFridgeList.addEventListener("submit-success", function(e){
//    	alert("냉장고 목록 불러오기 성공");
//    	
//    	
//    });
	
}


/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
//function onGetFridgeListSubmitSuccess(e){
//	var getFridgeList = e.control;
//	
////	app.lookup("fridgeList").getRowDataRanged().forEach(function(each){ 
////		// NavigationBar에 사용할 올바른 객체는 MenuItem입니다
////		var item = new cpr.controls.MenuItem(each.fridgeName, each.fridgeId, null);
////		// NavigationBar에 아이템 추가
////		app.lookup("fridgNavbar").addItem(item);
////	});
//
//}


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
    
    // 현재 냉장고 정보 및 멤버 아이디
    app.lookup("fridgeIdParam").setValue("fridgeId", e.item.value);
    app.lookup("fridgeIdParam").setValue("memberId", 1);
	
}

//----------------------------------------

/*
 * 사용자 정의 컨트롤에서 save-click 이벤트 발생 시 호출.
 * 저장버튼 클릭시 발생하는 이벤트
 */
function onFridgeCRUDSaveClick(e){
	var fridgeCRUD = e.control;
	
	var foodDataSet = app.lookup("insertFoodList");
	console.log("저장된 식재료 데이터 셋 구조")
	console.log(foodDataSet.getRowDataRanged());
	
	var foodDataMap = app.lookup("fridgeIdParam");	
	
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
//function onFridgeUDSaveUpdateClick(e){
//	var fridgeUD = e.control;
//	
//	var foodDataList = app.lookup("foodList"); // 식재료 DataSet
//	var foodGrid = app.lookup("foodListGrid"); // 식재료 Grid
//
//	var inserted = getChangedRowsByState(cpr.data.tabledata.RowState.INSERTED);
//	var updated = getChangedRowsByState(cpr.data.tabledata.RowState.UPDATED);
//	var deleted = getChangedRowsByState(cpr.data.tabledata.RowState.DELETED);
//
//	alert("추가됨: " + JSON.stringify(inserted) + 
//      "\n수정됨: " + JSON.stringify(updated) + 
//      "\n삭제됨: " + JSON.stringify(deleted));
//    
//    app.lookup("updateFoodList").send();
//    
//    
//    
//	// 새로 그리기
//	app.lookup("getFoodList").send();
//		
//	
//}

function getChangedRowsByState(state) {
    var ds = app.lookup("foodList");
    var rows = [];

    for (var i = 0; i < ds.getRowCount(); i++) {
        if (ds.getRowState(i) === state) {
            var rowData = {};
            var cols = ds.getColumnNames();
            cols.forEach(function(col) {
                rowData[col] = ds.getValue(i, col);
            });

            rows.push(rowData);
        }
    }
    return rows;
}

/*
 * "식재료 추가하기" 아웃풋에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onOutputClick(e){
	var output = e.control;

	var insertFoodgrd = app.lookup("insertFoodgrd");
	var fridgeCRUD = app.lookup("fridgeCRUD");
  
	insertFoodgrd.visible = !insertFoodgrd.visible;
	fridgeCRUD.visible = !fridgeCRUD.visible;
	
	
}

/*
 * "그룹원관리" 아웃풋에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onOutputClick2(e){
	var output = e.control;
//	window.location.href = "";
	alert("다이얼로그 시작");
	app.dialogManager.openDialog("app/fridge/fridge_group","dialogName",{width:500, height:500},function(dialog){

		//다이얼로그가 로드 되었을때 처리
		dialog.modal = false;
		dialog.headerMin = true;
		dialog.headerTitle = "냉장고 그룹원 관리";
		
		const myfridgeId  = app.lookup("fridgeIdParam").getValue("fridgeId");
		const myId = app.lookup("fridgeIdParam").getValue("memberId");
		
		dialog.initValue = {fridgeIdParam: myfridgeId, memberIdParam: myId};
		
		
		console.log(dialog.app.id);
		}).then(function(returnValue){
			
		//닫기 했을때 반환되는 값에 대한 처리
	});
}

/*
 * 사용자 정의 컨트롤에서 save-click 이벤트 발생 시 호출.
 * 저장버튼 클릭시 발생하는 이벤트
 */
function onFridgeCRUDForUDSaveClick(e){
	var fridgeCRUDForUD = e.control;
	

	var inserted = getChangedRowsByState(cpr.data.tabledata.RowState.INSERTED);
	var updated = getChangedRowsByState(cpr.data.tabledata.RowState.UPDATED);
	var deleted = getChangedRowsByState(cpr.data.tabledata.RowState.DELETED);

	alert("추가됨: " + JSON.stringify(inserted) + 
	      "\n수정됨: " + JSON.stringify(updated) + 
	      "\n삭제됨: " + JSON.stringify(deleted));

	// 식재료 업데이트 서브미션
	var submission = app.lookup("updateFoodList");
	
	const updateDataSet = app.lookup("updateList");
	const deleteDataSet = app.lookup("deleteList");
	// 기존 데이터 초기화 (필수)
	updateDataSet.clearData();
	deleteDataSet.clearData();
	
	// updated 데이터를 DataSet에 추가
	updated.forEach(function(rowData) {
		updateDataSet.addRowData(rowData);
	});
	
	// deleted 데이터를 DataSet에 추가
	deleted.forEach(function(rowData) {
		deleteDataSet.addRowData(rowData);
	});

	console.log("전송전 데이터셋 확인 =======")
	console.log(updateDataSet.getRowDataRanged());
	
	
	// ✅ 서버로 전송
	submission.send();

	submission.addEventListener("submit-done", function() {
	    const getList = app.lookup("getFoodList");
	    getList.send();
	
	    getList.addEventListener("submit-done", function() {
		        app.lookup("foodListGrid").redraw();
	    });
	});
}
