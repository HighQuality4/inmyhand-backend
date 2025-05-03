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
	
	alert("가져온값" + initValue +  " " + memberId + " " + fridgeId);
	
	
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
	const processedData = resultJson.searchGroupList.map(item => {
	    const roleNames = item.roleName || [];
	
	    const roleStatus = roleNames.find(role => role === "leader" || role === "member") || "";
	    const permissionList = roleNames.filter(role => role !== "leader" && role !== "member");
	    const permissionName = permissionList.join(", ");
	
	    return {
	        memberName: item.nickname,
	        joinDate: item.joinDate,
	        roleStatus: roleStatus.toUpperCase(),
	        permissionName: permissionName,
	        fridgeMemberId : item.fridgeMemberId,
	        memberId : item.memberId 
	    };
	    
	});
	
	// 데이터셋 세팅
	// 참여중인 그룹 리스트 데이터셋 저장하기
	const ds = app.lookup("searchGroupList");
	ds.clearData();
	ds.build(processedData);
	
	// 변경된 데이터셋 그리드에 뿌리기
	// 그리드 새로고침
	const grd = app.lookup("JoinGroupMemeberGrid")
	grd.redraw();
	

}
/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onGetRoleListSubmitSuccess(e){
	var getRoleList = e.control;
	
	// 문제가 있었음
	// 데이터는 받아오는데 check가 안됨
	// 1. 제발 필히 그리드에 연결되어 있는 dataset 변경 시켜줘야한다.
	// 2. 그리고 그 그리드를 다시 그려야한다. 

	
	const grd = app.lookup("JoinGroupMemeberGrid")
	grd.redraw();
	
	
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
	
	editGroupSubmission.send();
	
	const grd = app.lookup("JoinGroupMemeberGrid").redraw();
}



/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onGetPendIngGroupListSubmitSuccess(e){
	var getPendIngGroupList = e.control;
	
	const grd = app.lookup("inviteMemberGrid").redraw();
	alert("초대 리스트 서버연결 성공");
	
}

/*
 * "저장" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	
	// 초대 수락/거절 저장버튼 클릭 시
	 
	app.lookup("inviteMemberGrid")
	
}

