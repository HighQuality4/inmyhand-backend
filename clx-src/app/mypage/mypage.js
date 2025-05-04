/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

/*
 * "로그아웃" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e) {
	
	var btn = e.control;
    app.lookup("smsLogout").send(); // 서버 전송
    
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onSmsLogoutSubmitSuccess(e){
	var smsLogout = e.control;
    // 원하는 경로로 이동
    location.href = "/auth/login";
}

/*
 * 서브미션에서 receive 이벤트 발생 시 호출.
 * 서버로 부터 데이터를 모두 전송받았을 때 발생합니다.
 */
function onSmsLogoutReceive(e){
	var smsLogout = e.control;
	
	var xhr = smsLogout.xhr;
	var res = JSON.parse(xhr.responseText);
	console.log(res);
	if (res === true) {
		alert("로그아웃 되었습니다!");
	} else {
		alert("로그아웃에 실패하였습니다!");
		e.preventDefault();
	}
	
}

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	var name = app.lookup("name");
	var smsName = app.lookup("smsName");
	
	 smsName.addEventListener("submit-success", function(e) {
        var nicknameValue = name.getValue("nickname");

        var nickname = app.lookup("nickname");

        nickname.value = nicknameValue;
        console.log("✅ 닉네임:", nicknameValue);
    });
	
	smsName.send();
	
	
}
