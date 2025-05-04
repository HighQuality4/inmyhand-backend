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
	var email = app.lookup("email").value;
  var dmEmail = app.lookup("dmEmail");
  dmEmail.setValue("email", email);

  var smsEmail = app.lookup("smsEmail");

  // 서버 응답 이벤트를 버튼 클릭 시점에 바인딩
  smsEmail.addEventListenerOnce("receive", function (e) {
    var xhr = smsEmail.xhr;
    try {
      var response = JSON.parse(xhr.responseText);
      if (response === true || response === "true" || response == true) {
        showToastModule.showToast("이메일로 임시 비밀번호를 발송하였습니다.", 2000);
        // 2초 후 로그인 페이지로 이동
	      setTimeout(function () {
	        location.href = "/users/login";
	      }, 2000);
      } else {
        showToastModule.showToast("이메일이 틀렸습니다!", 2000);
      }
    } catch (err) {
      console.error("응답 파싱 실패:", xhr.responseText);
      showToastModule.showToast("서버 오류가 발생했습니다.", 2000);
    }
  });

  smsEmail.send();
}