/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/
const showToastModule = cpr.core.Module.require("module/common/showToast");
/*
 * "이메일 인증" 버튼(email_submit)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onEmail_submitClick(e){
	var email_submit = e.control;
	
	var emailValue = app.lookup("email_input").value;
    if (!emailValue) {
        showToastModule.showToast("이메일을 입력해주세요.", 2000);
        return;
    }

    var email = app.lookup("smsEmailSend");
    var dmEmail = app.lookup("dmEmailAuth");
    localStorage.setItem("userEmail", emailValue);
    dmEmail.setValue("email", emailValue);
    console.log(dmEmail.getValue("email"));
    email.send();
	
	showToastModule.showToast("인증 코드가 발송되었습니다.", 2000);
	
	var formGroup = app.lookup("confirmForm");
    formGroup.visible = true;
    
    app.setAppProperty("isFormVisible", true);
    
    // 이메일 인증 타이머 시작 (3분 = 180초)
    var confirmForm = app.lookup("confirmForm");
	confirmForm.callAppMethod("startTimer");
}

/*
 * "회원가입" 버튼(register_submit)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onRegister_submitClick(e){
	var register_submit = e.control;
	var email = app.lookup("email_input").value;
	var name = app.lookup("name_input").value;
	var nickname = app.lookup("nickname_input").value;
	var password = app.lookup("password_input").value;
	var password_confirm = app.lookup("password_confirm").value;
	var dm = app.lookup("dmRegister");
	
	if (password !== password_confirm) {
		showToastModule.showToast("비밀번호가 일치하지 않습니다!!", 2000);
		e.preventDefault(); //서버로 전송 막음
		return;
	}
	var confirmForm = app.lookup("confirmForm");
	var res = confirmForm.callAppMethod("getRes");  // 인증 플래그 읽기
    if (!res) {
        showToastModule.showToast("이메일 인증을 완료해 주세요.", 2000);
        e.preventDefault();  // 서버 전송 막기
        return;
    }
	
	dm.setValue("email", email);
	dm.setValue("memberName", name);
	dm.setValue("nickname", nickname);
	dm.setValue("password", password);
	
	var sms = app.lookup("smsRegister");
	sms.send();
}

/*
 * 서브미션에서 receive 이벤트 발생 시 호출.
 * 서버로 부터 데이터를 모두 전송받았을 때 발생합니다.
 */
function onSmsRegisterReceive(e){
	var smsRegister = e.control;
	var xhr = smsRegister.xhr;
	var res = JSON.parse(xhr.responseText);
	console.log(res);
	if (res === true) {
		showToastModule.showToast("가입에 성공했습니다!", 2000);
	} else {
		showToastModule.showToast("가입에 실패했습니다!", 2000);
		e.preventDefault();
	}
	
}
