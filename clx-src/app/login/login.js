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

/*
 * 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClickNaver(e){
	window.location.href = window.location.origin + "/oauth2/authorization/naver";
}

/*
 * 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClickGoogle(e){
	window.location.href = window.location.origin + "/oauth2/authorization/google";
}

/*
 * 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClickKakao(e){
	window.location.href = window.location.origin + "/oauth2/authorization/kakao";
}

/*
 * 서브미션에서 receive 이벤트 발생 시 호출.
 * 서버로 부터 데이터를 모두 전송받았을 때 발생합니다.
 */
function onSmsLoginReceive(e){
	var smsLogin = e.control;
	
	var xhr = smsLogin.xhr;
	var res = JSON.parse(xhr.responseText);
	if (res.success === true) {
	    alert("로그인 되었습니다!");
	    history.pushState({}, '', `/users/mypage`);
	} else {
	    alert("아이디나 비밀번호가 틀렸습니다!");
	}
}
