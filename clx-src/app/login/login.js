/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/
/*
 * "로그인" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var btn = e.control;
	
	var email = app.lookup("email").value;
	var password = app.lookup("password").value; // 요청 데이터 담는 DataMap
	
	var dm = app.lookup("dmLogin");
	dm.setValue("email", email);
	dm.setValue("password", password);
	
	app.lookup("smsLogin").send(); // 서버 전송
}