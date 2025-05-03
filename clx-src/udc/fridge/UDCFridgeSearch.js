/************************************************
 * UDCFridgeSearch.js
 * Created at 2025. 4. 27. 오후 10:33:47.
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
 * "Button" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	
	// 검섹 이벤트 (search-click) 전파하기
	var searchClick = new cpr.events.CUIEvent("search-click-event");
	app.dispatchEvent(searchClick);	
	
}
