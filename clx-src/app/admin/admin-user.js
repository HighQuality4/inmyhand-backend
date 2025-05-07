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
	
	radioInit();
	
}

const radioInit = () => {
		
	var rdb = app.lookup("rdb1");
    var ds1 = app.lookup("ds1");
    rdb.setItemSet(ds1, {
        label: "상태", 
        value: "값"    
    });
}


/*
 * 콤보 박스에서 item-click 이벤트 발생 시 호출.
 * 아이템 클릭시 발생하는 이벤트.
 */
function onCmb1ItemClick(e){
	var cmb1 = e.control;
	var combo1 = app.lookup("cmb1");
	var comboset = app.lookup("comboset");
	combo1.setItemSet(comboset, {label: "상태", value: "값"});

}


/*
 * 그리드에서 cell-click 이벤트 발생 시 호출.
 * Grid의 Cell 클릭시 발생하는 이벤트.
 */
function onGrd1CellClick(e){

	var grd1 = e.control;
    var usersDataset = app.lookup("content");
    
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
 * "검색" 버튼(btn1)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onBtn1Click(e){
	
	var btn1 = e.control;
	const rdb1 = app.lookup("rdb1").value;
	const cmb2 = app.lookup("cmb2").value;
	const ipb1 = app.lookup("ipb1").value;

	if (rdb1 === null && (!ipb1 || !ipb1.trim())) {
		
	} 
	else if (rdb1 === null || !ipb1?.trim()) {
	    alert("입력값을 확인해주세요.");
	    return;
	}

	const searchSubmission = app.lookup("admin_search");
	searchSubmission.removeAllParameters();

	if(rdb1 !== null){
		searchSubmission.setParameters(rdb1,ipb1);
	} 
	
	if(cmb2 !== null){
        searchSubmission.setParameters("combo", cmb2);
    } 
	
    searchSubmission.send();
}

/*
 * "검색" 버튼(btn3)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onBtn3Click(e){
	var btn3 = e.control;
	
	const rdb1 = app.lookup("rdb1");
	const cmb2 = app.lookup("cmb2");
	const ipb1 = app.lookup("ipb1");
	
	rdb1.clearSelection();
	cmb2.clearSelection();
	ipb1.clear();
	cmb2.value = "전체보기";
}

/*
 * 그룹에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onGroupClick(e){
	var group = e.control;
		window.location.href = "/server/grafana";
}

/*
 * 그룹에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onGroupClick2(e){
	var group = e.control;
		window.location.href = "/";
}

/*
 * "저장" 버튼(btn2)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onBtn2Click2(e){
	var btn2 = e.control;
	app.lookup("admin_update").send();
}
