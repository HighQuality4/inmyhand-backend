/************************************************
 * confirmForm.js
 * Created at 2025. 4. 25. 오후 3:42:17.
 *
 * @author seongkwan
 ************************************************/

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
exports.startTimer = function () {
    var timer = app.lookup("timer");
    timer.callAppMethod("startTimer");  // 내부 timer로 중계
};

function onSubmitConfirmClick(e){
	var code = app.lookup("code_input").value;
	var email = localStorage.getItem("userEmail");
	
	var dm = app.lookup("dmEmailCode");
	var sms = app.lookup("smsEmailAuth");
	dm.setValue("email", email);
	dm.setValue("code", code);
	
	sms.send();
}

/*
 * 서브미션에서 receive 이벤트 발생 시 호출.
 * 서버로 부터 데이터를 모두 전송받았을 때 발생합니다.
 */
function onSmsEmailAuthReceive(e){
	var smsEmailAuth = e.control;
	var xhr = smsEmailAuth.xhr;
	var res = JSON.parse(xhr.responseText);
	console.log(res);
	if (res === true) {
		alert("인증되었습니다!");
		
	} else {
		alert("인증에 실패했습니다!");
	}
	exports.getRes = function() {
		return res;
	}
}
