/************************************************
 * infoBoard.js
 * Created at 2025. 4. 28. 오전 10:37:06.
 *
 * @author seongkwan
 ************************************************/

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
exports.getText = function(){
	// TODO: 그리드의 뷰 모드에서 표시할 텍스트를 반환하는 하는 코드를 작성해야 합니다.
	return "";
};

/*
 * "+" 버튼(add)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onAddClick(e){
	var grid = app.lookup("grdInfoList");
    // 1. 행 추가
    var newRowIndex = grid.getRowCount();
    grid.insertRow(newRowIndex, true); // 마지막에 새 줄 추가
	var newInput = new cpr.controls.TextInput(); // ✅ 새로 만든다!
    newInput.value = "";
//    // 2. TextInput 생성해서 1번째 셀에 넣기
//    var txtInput = new cpr.controls.TextInput();
//    txtInput.value = ""; // 비어있는 인풋 박스
//
//    // 3. 새 행의 1번째 셀(내용 입력 칸)에 TextInput 배치
//    grid.setValue()
//    setCellControl(newRowIndex, 2, txtInput);
}
/*
 * "-" 버튼(delete)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onDeleteClick(e){
	var grid = app.lookup("grdInfoList");
    var selectedRow = grid.getSelectedRowIndex();
    
    if (selectedRow >= 0) {
        grid.deleteRow(selectedRow);
    } else {
        alert("삭제할 줄을 선택하세요!");
    }
	
}
