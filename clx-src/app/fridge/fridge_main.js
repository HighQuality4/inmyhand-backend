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
	app.lookup("foodListGrid").redraw();
	
}


/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad2(e){
	app.lookup("getFoodList").send();
	app.lookup("getFridgeList").send();
	
}



/*
 * "Button" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick2(e){
	var button = e.control;
	
//	app.lookup("getFridgeList").send();
	
	
	app.lookup("fridgeList").getRowDataRanged().forEach(function(each){
		 
		// NavigationBar에 사용할 올바른 객체는 MenuItem입니다
		var item = new cpr.controls.MenuItem(each.fridgeName, each.fridgeId, null);
	
		// NavigationBar에 아이템 추가
		app.lookup("fridgNavbar").addItem(item);
	});

	alert("동작 테스트합니다");
	
}


function focusItem(vsValue) {
    var vcNav = app.lookup("fridgNavbar");
    var voItem = vcNav.getItemByValue(vsValue);
    vcNav.focusItem(voItem);

}

function fixGridSetting(){
	
	var grid = app.lookup("foodListGrid");
	// 좌측 틀고정을 가로사이즈 100px, 고정되는 셀 수는 2개로 설정합니다.
	grid.leftSplitWidth = 100;
	grid.rightSplit = 1;
	
	
//	var email = app.lookup("email").value;
//	var password = app.lookup("password").value; // 요청 데이터 담는 DataMap
//	
//	var dm = app.lookup("dmLogin");
//	dm.setValue("email", email);
//	dm.setValue("password", password);
//	
//	app.lookup("smsLogin").send();
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


function controlFoodInsertGrid(e){
}


//----------------------------------------
/*
 * 그리드에서 insert 이벤트 발생 시 호출.
 * Grid의 행이 추가되었을 때 이벤트.
 */
	/*
	function onFoodListGridInsert(e){
		
		// 현재 이벤트 중인 음식 그리드
		var foodListGrid = e.control;
		
		// 현재 클릭된 row 번호
		var totalGridIndex = e.rowIndex;
		
		// 
		var countSelectGridrow = totalGridIndex-1;
		
		
		var formatGrid = app.lookup("foodListGrid");
		
	//	
	//	formatGrid.focusCell(, );
	//	
	//	
		if(countSelectGridrow > 7){
			countSelectGridrow = 7;	
		}
		
		alert(e.relativeTargetName + "countSelectGridrow" + countSelectGridrow);
		
		
	//	for (var i = countSelectGridrow; i < totalGridIndex; i++) {
	//	  var rowData = {
	//	    id: ds.getValue(i, "id"),
	//	    foodName: ds.getValue(i, "foodName")
	//	    // 필요한 컬럼 계속 추가
	//	  };
	//
	//	}
		
//	var foodDataSet = app.lookup("insertFoodList");
//	
//	var totalInsertRow = foodDataSet.getRowCount();
//		
//	alert(totalInsertRow);	
//	for (var i = 0; i < totalInsertRow; i++) {
//	  var rowData = {
//	    id: foodDataSet.getValue(i, "id"),
//	    foodName: foodDataSet.getValue(i, "foodName"),
//	    foodAmount: foodDataSet.getValue(i, "foodAmount")
//	    // 필요한 컬럼 계속 추가
//	  };
//	    alert("rowData[" + i + "]\n" +
//        "id: " + rowData.id + "\n" +
//        "foodName: " + rowData.foodName + "\n" +
//        "foodAmount: " + rowData.foodAmount);
//	}
		
	}
	*/

/*
 * 사용자 정의 컨트롤에서 save-click 이벤트 발생 시 호출.
 * 저장버튼 클릭시 발생하는 이벤트
 */
function onFridgeCRUDSaveClick(e){
	var fridgeCRUD = e.control;
	
	
	var foodDataSet = app.lookup("insertFoodList");
	
	var totalInsertRow = foodDataSet.getRowCount();
		
	alert(totalInsertRow);
	
//	for (var i = 0; i < totalInsertRow; i++) {
//	  var rowData = {
//	    id: foodDataSet.getValue(i, "id"),
//	    foodName: foodDataSet.getValue(i, "foodName"),
//	    foodAmount: foodDataSet.getValue(i, "foodAmount")
//	    // 필요한 컬럼 계속 추가
//	  };
//	    alert("rowData[" + i + "]\n" +
//        "id: " + rowData.id + "\n" +
//        "foodName: " + rowData.foodName + "\n" +
//        "foodAmount: " + rowData.foodAmount);
//	}
	app.lookup("sendFoodList").send();	
	
}
