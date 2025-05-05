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
function onBodyLoad(e){
	
	var initValue = app.getHost().initValue;
	var fridgeId = initValue.fridgeIdParam; 
	var memberId = initValue.memberIdParam; 
	
	
	app.lookup("dmFridgeParam").setAttr("fridgeId", fridgeId);
	app.lookup("dmFridgeParam").setAttr("memberId", memberId);
	
	 // 냉장고에 참여중인 유저 목록
	const groupList = app.lookup("getJoinGroupList").send();
	
	//====================================================================	
	// 냉장고 초대받은 목록
	const inviteList = app.lookup("getPendIngGroupList").send();
	
}


function onGetJoinGroupListSubmitSuccess(e){
	// 서버 데이터 처리
	app.lookup("getRoleList").send();
	
	var getJoinGroupList = e.control;
	const result = getJoinGroupList.xhr.responseText;
	const resultJson = JSON.parse(result);
	
	
	// 데이터 가공
	const processedData = resultJson.searchGroupList.map((item, index) => {
	    const roleNames = item.roleName || [];
	
	    const roleStatus = roleNames.find(role => role === "leader" || role === "member") || "";
	    const permissionList = roleNames.filter(role => role !== "leader" && role !== "member");
	    const permissionName = permissionList.join(", ");
	
	    const rowData = {
	        memberName: item.nickname,
	        joinDate: item.joinDate,
	        roleStatus: roleStatus.toUpperCase(),
	        permissionName: permissionName,
	        fridgeMemberId: item.fridgeMemberId,
	        memberId: item.memberId
	    };
	
	    return rowData;
	});
//	
//	// 데이터셋 세팅
//	// 참여중인 그룹 리스트 데이터셋 저장하기

	const ds = app.lookup("searchGroupList");
	ds.clearData();
	ds.build(processedData);
	
	// 변경된 데이터셋 그리드에 뿌리기
	// 그리드 새로고침
	const grd = app.lookup("JoinGroupMemeberGrid")
	grd.redraw();
//	

}
/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onGetRoleListSubmitSuccess(e) {
	const ds = app.lookup("searchGroupList");
	const roleList = app.lookup("roleList");
	const rowCount = ds.getRowCount();

	for (let i = 0; i < rowCount; i++) {

		const permissionStr = ds.getValue(i, "permissionName");


		if (permissionStr) {
			const permissionArray = permissionStr.split(",").map(item => item.trim());


			const roleIds = permissionArray.map(roleName => {
				const roleRow = roleList.findFirstRow("roleName == '" + roleName + "'");
				if (roleRow) {
					return roleRow.getValue("roleId");
				}
				return null;
			}).filter(Boolean);


			const newPermissionValue = roleIds.join(",");

			ds.setValue(i, "permissionName", newPermissionValue);
		} else {
			console.log("permissionName이 없음 (스킵)");
		}
	}

	for (let i = 0; i < ds.getRowCount(); i++) {
        ds.setRowState(i, cpr.data.tabledata.RowState.UNCHANGED);
    }

    
	app.lookup("JoinGroupMemeberGrid").redraw();
}

function getChangedRowsByState(state) {
    var ds = app.lookup("searchGroupList");
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
 * 사용자 정의 컨트롤에서 save-click 이벤트 발생 시 호출.
 * 저장버튼 클릭시 발생하는 이벤트
 */
function onFridgeCRUDSaveClick(e){
	var fridgeCRUD = e.control;
	
	  var ds = app.lookup("searchGroupList");
	  
	  console.log(ds.getRowDataRanged());


	var inserted = getChangedRowsByState(cpr.data.tabledata.RowState.INSERTED);
	var updated = getChangedRowsByState(cpr.data.tabledata.RowState.UPDATED);
	var deleted = getChangedRowsByState(cpr.data.tabledata.RowState.DELETED);

	
	console.log("추가됨: " + JSON.stringify(inserted) + 
	      "\n수정됨: " + JSON.stringify(updated) + 
	      "\n삭제됨: " + JSON.stringify(deleted));

	var editGroupSubmission = app.lookup("sendEditGroupList");
	
	
	const updateDataSet = app.lookup("updateGroupList");
	const deleteDataSet = app.lookup("deleteGroupList");
	
	updateDataSet.clearData();
	deleteDataSet.clearData();
	
	updated.forEach(function(rowData) {
		updateDataSet.addRowData(rowData);
	});
	
	deleted.forEach(function(rowData) {
		deleteDataSet.addRowData(rowData);
	});
	
	console.log("전송전 데이터셋 확인 =======")
	console.log(updateDataSet.getRowDataRanged());
	
	
	ds.clearData();
	
	editGroupSubmission.send();
	
	
}



/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onGetPendIngGroupListSubmitSuccess(e){
	var getPendIngGroupList = e.control;
	
	const grd = app.lookup("inviteMemberGrid").redraw();
	console.log("초대 리스트 서버연결 성공");
	
}


function getChangedRowsByStateOfInvite(state) {
    var ds = app.lookup("inviteList");
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
 * "저장" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	
	// 초대 수락/거절 저장버튼 클릭 시
	 
	app.lookup("inviteMemberGrid")
	var updated = getChangedRowsByStateOfInvite(cpr.data.tabledata.RowState.UPDATED);

	
	console.log("\n수정됨: " + JSON.stringify(updated) );
	      
	var inviteSubmission = app.lookup("inviteSubMission");
	
	const updateDataSet = app.lookup("resultInviteList");
	
	updateDataSet.clearData();
	
	updated.forEach(function(rowData) {
		updateDataSet.addRowData(rowData);
	});
	
	
	
	console.log("전송전 데이터셋 확인 =======")
	console.log(updateDataSet.getRowDataRanged());
	
}

/*
 * "검색" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick2(e){
	var button = e.control;
	
	const searchSms = app.lookup("sendSearchMemberName");
	
	const name = app.lookup("searchNameInput").value;  // 예: 사용자가 입력한 검색어
		// 1) 이전 파라미터 초기화
	  searchSms.removeAllParameters();
	
	  // 2) name 을 form-data 로 추가
	  searchSms.addParameter("name", name);
	
	  // 3) 요청 전송 (POST /api/fridges/search?name=…)
	  searchSms.send();
	
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onSendSearchMemberNameSubmitSuccess(e){
	var sendSearchMemberName = e.control;
	const sms  = e.control;
  	const raw  = sms.xhr.responseText;    // 문자열 형태의 JSON
  	const list = JSON.parse(raw);         // JS 객체(배열)로 변환

  	const paredDown = list.map(item => ({
	    memberId:   item.memberId,
	    email:      item.email,
	    memberName: item.memberName,
	    nickname:   item.nickname
	}));
	    // 2) DataSet lookup → 초기화 → 빌드
	const ds = app.lookup("searchMemberList");
	ds.clearData();
	ds.build(paredDown);
  
   	const grid = app.lookup("searchMemberGrid");
   	
   	
	grid.redraw();
		  
  // 3) 혹은 console.log 해서 확인만
  console.log("검색 결과:", list);

}


/*
 * 그리드에서 cell-click 이벤트 발생 시 호출.
 * Grid의 Cell 클릭시 발생하는 이벤트.
 */
function onSearchMemberGridCellClick2(e){
	var searchMemberGrid = e.control;
	const btn      = e.control;              
	const rowIdx = e.rowIndex;       
  	const columnName = e.columnName;       
  
	const grid = app.lookup("searchMemberGrid");
  	const cellIndx   = grid.getCellIndex(columnName);
  	// 버튼클릭시
  	if(cellIndx == 3){
  		console.log("버튼 클릭 >>");
  		// 3) 클릭된 행 전체의 바인딩 데이터 객체
		 console.log("로우 >> " );
		  
		const grid = app.lookup("searchMemberGrid");
	  	const memberName   = grid.getCellValue(rowIdx, "memberName")
	  	const memberId   = grid.getCellValue(rowIdx, "memberId")
  		
  		console.log("cellValue",memberName)
  		console.log("cellValue2",memberId)
  		
  		
  		const param = app.lookup("addGroupMemberParam");
  		param.setValue("memberId", memberId);
  		param.setValue("memberName", memberName);
  		
  		const submission = app.lookup("addGroup");
  		
  		submission.send();
  		app.lookup("searchNameInput").value = "";
  		const ds = app.lookup("searchMemberList")
  		ds.clearData();
  		
  		grid.redraw();
  		
  		
  	}

}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onAddGroupSubmitSuccess(e){
	var addGroup = e.control;
	alert("냉장고에 초대를 했습니다.")
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onSendEditGroupListSubmitSuccess(e){
	var sendEditGroupList = e.control;
	
	const ds = app.lookup("getJoinGroupList");
	ds.send();
	
	const grd = app.lookup("JoinGroupMemeberGrid").redraw();
	
  
}

/*
 * 그리드에서 cell-click 이벤트 발생 시 호출.
 * Grid의 Cell 클릭시 발생하는 이벤트.
 */
function onInviteMemberGridCellClick(e){
	var inviteMemberGrid = e.control;
	
	const btn      = e.control;              
	const rowIdx = e.rowIndex;       
  	const columnName = e.columnName;       
  

	const grid = app.lookup("inviteMemberGrid");
	
  	const cellIndx   = grid.getCellIndex(columnName);
  	  alert("rowIdx" + rowIdx + "cellIndx"+cellIndx  )
  	// 버튼클릭시
  	if(cellIndx == 0){
		  
		const grid = app.lookup("inviteMemberGrid");
	  	const fridgeId   = grid.getCellValue(rowIdx, "fridgeId")
	  	const fridgeName   = grid.getCellValue(rowIdx, "fridgeName")
  		
  		console.log("cellValue",fridgeId)
  		console.log("cellValue2",fridgeName)
  		
  		
  		const param = app.lookup("addInviteParam");
  		param.setValue("fridgeId", fridgeId);
  		param.setValue("fridgeName", fridgeName);
  		
  		const submission = app.lookup("inviteSubMission");
  		
  		submission.send();
  		const ds = app.lookup("inviteList")
  		ds.clearData();
  		
  		app.lookup("getPendIngGroupList").send();
  		grid.redraw();
  		
  		
  	}
}
