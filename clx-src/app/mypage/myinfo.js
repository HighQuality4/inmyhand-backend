/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/
const showToastModule = cpr.core.Module.require("module/common/showToast");
/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	// 내 데이터 불러오기
	var dmGet = app.lookup("dmGetMyInfo");
	var smsGet = app.lookup("smsGetMyInfo");
	
	smsGet.addEventListenerOnce("submit-success", function(ev) {
    // 1. DataMap에서 값 꺼내기
    var nickname = dmGet.getValue("nickname");
    var phoneNum = dmGet.getValue("phoneNum");
    var email = dmGet.getValue("email");

    // 2. input에 값 설정
    app.lookup("nickname").value = nickname;
    app.lookup("phoneNum").value = phoneNum;
    app.lookup("email").value = email;
  });

  smsGet.send();
}

/*
 * "내 정보 수정하기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	
	var dmSet = app.lookup("dmSetMyInfo");
	var smsSet = app.lookup("smsSetMyInfo");
	
	var email = app.lookup("email").value;
	var nickname = app.lookup("nickname").value;
	var phoneNum = app.lookup("phoneNum").value;
	
	dmSet.setValue("email", email);
	dmSet.setValue("nickname", nickname);
	dmSet.setValue("phoneNum", phoneNum);
	
	smsSet.send();
}

/*
 * 서브미션에서 receive 이벤트 발생 시 호출.
 * 서버로 부터 데이터를 모두 전송받았을 때 발생합니다.
 */
function onButtonClick2(e) {
  var button = e.control;
  
  // 먼저 로컬 회원인지 확인
  var smsCheckLocal = app.lookup("smsCheckLocal");
  smsCheckLocal.send();
}

function onSmsCheckLocalReceive(e) {
  var smsCheckLocal = e.control;
  var xhr = smsCheckLocal.xhr;
  
  // 응답 형식에 따라 적절히 처리
  try {
    // 응답이 JSON 형식인 경우
    var response = JSON.parse(xhr.responseText);
    
    // 문자열 비교 시 주의 (따옴표가 포함된 경우도 있음)
    if (response === false || response === "false" || response == false) {
      showToastModule.showToast("로컬 가입 회원만 변경이 가능합니다!", 2000);
      history.back();
      return;
    } else {
    	showToastModule.showToast("비밀번호가 수정되어 로그아웃합니다!", 2000);
    	 setTimeout(() => {
	      location.reload();
	    }, 4000); 
    }
  } catch (error) {
    // 응답이 JSON이 아닌 경우
    console.error("응답 처리 오류:", error);
    console.log("원본 응답:", xhr.responseText);
    
    // 텍스트 응답 처리
    if (xhr.responseText.indexOf("false") !== -1) {
      showToastModule.showToast("로컬 가입 회원만 변경이 가능합니다!", 2000);
      history.back();
      return;
    }
  }
  
  // 로컬 회원인 경우 비밀번호 변경 로직 진행
  proceedWithPasswordChange();
}

function proceedWithPasswordChange() {
  var pass1 = app.lookup("chPassword").value;
  var pass2 = app.lookup("chPassword2").value;
  
  if (pass1 === "" || pass2 === "") {
    showToastModule.showToast("비밀번호를 입력해주세요.", 2000);
    return;
  }
  
  if (pass1 !== pass2) {
    showToastModule.showToast("비밀번호가 서로 일치하지 않습니다.", 2000);
    return;
  }
  
  var dmPass = app.lookup("dmChangePw");
  var smsPass = app.lookup("smsChangePw");
  dmPass.setValue("password1", pass1); // 또는 필요한 key로 설정
  dmPass.setValue("password2", pass2);
  smsPass.send();
}