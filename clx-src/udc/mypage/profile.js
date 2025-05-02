/************************************************
 * profile.js
 * Created at 2025. 4. 27. 오후 3:51:40.
 *
 * @author seongkwan
 ************************************************/

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
exports.getText = function(){
	// TODO: 그리드의 뷰 모드에서 표시할 텍스트를 반환하는 하는 코드를 작성해야 합니다.
	return "";
};

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	
	var dm = app.lookup("dmProfile");
    var sms = app.lookup("smsProfile");

    // ✅ 먼저 이벤트 리스너 등록
    sms.addEventListener("submit-success", function(e) {
        var nicknameValue = dm.getValue("nickname");
        var createdAtValue = dm.getValue("createdAt");

        var nickname = app.lookup("nickname");
        var createdAt = app.lookup("createdAt");

        nickname.value = nicknameValue;
        createdAt.value = createdAtValue;

        console.log("✅ 닉네임:", nicknameValue);
        console.log("✅ 가입일:", createdAtValue);
    });

    // ✅ 그 다음 send 호출
    sms.send();
}
