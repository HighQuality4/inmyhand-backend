/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/
const showToastModule = cpr.core.Module.require("module/common/showToast");
/*
 * "비밀번호 재발급" 버튼(emailBtn)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onEmailBtnClick(e){
	var emailBtn = e.control;
	
	var email = app.lookup("email");
	var dmEmail = app.lookup("dmEmail");
	dmEmail.setValue("email", email);
	
	var smsEmail = app.lookup("smsEmail");
	smsEmail.send();
}

/*
 * 서브미션에서 receive 이벤트 발생 시 호출.
 * 서버로 부터 데이터를 모두 전송받았을 때 발생합니다.
 */
function onSmsEmailReceive(e){
	var smsEmail = e.control;
	
	var xhr = smsEmail.xhr;
	var response = JSON.parse(xhr.responseText);
	if (response === "true") {
		showToastModule.showToast("이메일로 임시 비밀번호를 발송하였습니다.", 2000);
		
	} else {
		showToastModule.showToast("이메일이 틀렸습니다!", 2000);
	}
}
