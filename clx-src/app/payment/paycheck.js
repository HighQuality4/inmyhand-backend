/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

/*
 * "Button" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	var name = "popup test";
    var option = "width = 500, height = 500, top = 100, left = 200, location = no"
    
	window.open(window.location.origin + "/payment/toss", name, option);
	
}

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	var date = app.lookup("paydate");
	 var today = new Date();
	  var today = new Date();
	  var yyyy = today.getFullYear();
	  var mm = String(today.getMonth() + 1).padStart(2, '0');
	  var dd = String(today.getDate()).padStart(2, '0');
	
	  var formatted = `${yyyy}-${mm}-${dd}`;
	
	  date.value = formatted;
}
