/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/
//로그아웃 처리하는 메서드가 인자로 액세스 토큰 값을 받는데 js에선 그걸 못하니까 유저 아이디 값을 로컬 스토리지에 저장.


function getCookieValue(name) {
   const cookie = document.cookie
        .split('; ')
        .find(row => row.indexOf(name + '=') === 0);
    
    return cookie ? cookie.split('=')[1] : null;
}

/*
 * "로그아웃" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e) {
	
	var btn = e.control;
	
	const userId = localStorage.getItem("userId") || getCookieValue("userId");
	
	if (!userId) {
        alert("로그인 정보가 없습니다. (401 Unauthorized)");
        return;
    }
	
    var dm = app.lookup("dmLogout");
    dm.setValue("userId", userId);
    app.lookup("smsLogout").send(); // 서버 전송
    
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onSmsLogoutSubmitSuccess(e){
	var smsLogout = e.control;
	localStorage.removeItem("userId");
    console.log("✅ userId 삭제됨");

    // 원하는 경로로 이동
    location.href = "/users/login";
}
