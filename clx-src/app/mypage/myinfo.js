/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

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
