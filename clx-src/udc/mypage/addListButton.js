/************************************************
 * addListButton.js
 * Created at 2025. 4. 28. 오전 11:07:44.
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
 * "+" 버튼(addButton)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onAddButtonClick(e){
	var addButton = e.control;
	var listContainer = app.lookup("listForm");  
    // 전체 리스트 묶는 큰 Group이나 Container ID (예: listContainer)

    var newItem = new cpr.controls.Container();  // 새 줄 만들기 (혹은 Group)
    newItem.addClass("list-item"); // CSS 있으면 클래스 추가

    // 내부에 Output 추가
    var output = new cpr.controls.Output();
    output.value = "Output";
    output.style.addClass("output-style"); // 필요하면 스타일링

    // 내부에 X 버튼 추가
    var deleteButton = new cpr.controls.Button();
    deleteButton.value = "X";
    deleteButton.addEventListener("click", onDeleteButtonClick); // 삭제 이벤트 연결

    // 새 항목에 Output과 Button 추가
    newItem.addChild(output, {
        top: "calc(50% - 10px)", 
        left: "20px", 
        width: "200px", 
        height: "20px"
    });
    newItem.addChild(deleteButton, {
        top: "calc(50% - 10px)", 
        right: "20px", 
        width: "20px", 
        height: "20px"
    });

    // 리스트에 새 항목 추가
    listContainer.addChild(newItem, {
        top: undefined,
        left: undefined,
        width: "100%",
        height: "40px"
    });
}
