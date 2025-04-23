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
	window.location.href = "http://localhost:7079/oauth2/authorization/naver";
}

/*
 * 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClickGoogle(e){
	window.location.href = "http://localhost:7079/oauth2/authorization/google";	
}

/*
 * 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClickKakao(e){
	window.location.href = "http://localhost:7079/oauth2/authorization/kakao";
	
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onSmsLoginSubmitSuccess(e){
	var smsLogin = e.control;
	var sub = e.control;

	// 쿠키에서 userId 꺼내서 localStorage에 저장
	var userId = document.cookie
		.split('; ')
		.find(function(row) {
    		return row.indexOf("userId=") === 0;  // "userId="로 시작하는 문자열
  		})
		?.split('=')[1];

	if (userId) {
		localStorage.setItem("userId", userId);
		console.log("userId 저장 완료: " + userId);
	}

	// 이후 페이지 이동 등
	location.href = "/users/mypage"; // 로그인 후 이동할 페이지(나중에 메인으로 고치세요!)
}
