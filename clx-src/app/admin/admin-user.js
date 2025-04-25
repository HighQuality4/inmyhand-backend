/************************************************
 * admin-user.js
 * Created at 2025. 4. 21. 오후 3:20:39.
 *
 * @author choeyeongbeom
 ************************************************/

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
		app.lookup("admin_sms1").send();
	
}
/*
 * 콤보 박스에서 item-click 이벤트 발생 시 호출.
 * 아이템 클릭시 발생하는 이벤트.
 */
function onCmb1ItemClick(e){
	var cmb1 = e.control;
	var comboBox_1 = new cpr.controls.ComboBox("cmb1");
	comboBox_1.setItemSet(comboset, {label: "상태", value: "값"});

}

///*
// * "Button" 버튼에서 click 이벤트 발생 시 호출.
// * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
// */
//function onButtonClick(e){
//	 var button = e.control; // 이벤트 발생 버튼 참조
//	  // 'getusers' 데이터셋 참조 가져오기
//        
//      // 가져온 id 값을 사용하여 페이지를 이동합니다
//      window.location.href = "/recipe/" +  app.lookup("getusers").getValue(0, "id");  
//}
//
//
////	var btn1 = app.lookup("getusers").getRowDataRanged().forEach(function(each){
////		  			window.location.href = 'http://localhost:7079/recipe/'+ btn1;
////
////	});

/*
 * 그리드에서 cell-click 이벤트 발생 시 호출.
 * Grid의 Cell 클릭시 발생하는 이벤트.
 */
function onGrd1CellClick(e){

	var grd1 = e.control;
    var usersDataset = app.lookup("getusers");
    
    // 클릭된 열의 이름 가져오기
    var clickedColumnName =  e.columnName;
    
    // "레시피 보러가기" 컬럼인지 확인
    if(clickedColumnName === "id") {
        var recipeId = usersDataset.getValue(e.rowIndex, clickedColumnName);
        if(recipeId) {
            window.location.href = "/admin/recipe/" + recipeId;
        }
    }
    
  
}

/*
 * "저장" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	app.lookup("admin_update").send();	
	
}

