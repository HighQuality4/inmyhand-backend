/************************************************
 * infoExpired.js
 * Created at 2025. 4. 28. 오전 1:03:04.
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

// 한 group을 세 파트로 나누어서 각각 idx, 음식 이름, 남은 유통기한 일수를 넣는다.

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	var ds = app.lookup("dsMyRef");
    var sms = app.lookup("smsMyRef");

    // ✅ 먼저 이벤트 리스너 등록
    sms.addEventListener("submit-success", function(e) {
        var ds = app.lookup("dsMyRef");
		var container = app.lookup("refInfoGroup"); // UDC들이 들어갈 부모 Container
		
		container.removeAllChildren(); // 기존 것 제거
		
		for (var i = 0; i < ds.getRowCount(); i++) {
		    const foodName = ds.getValue(i, "foodName");
		    const expdate = ds.getValue(i, "expdate");
		
		    const refUdc = new udc.mypage.myRefInfo();
		    refUdc.id = "myRefInfo_" + i;
		
		    const group = udc.lookup("foodinfogroup");
		    group.lookup("num").value = (i + 1) + ".";
		    group.lookup("foodname").value = foodName;
		    group.lookup("expdate").value = Number(expdate) < 0 ? "버리셈" : expdate + "일";
		
		    container.addChild(refUdc, {
		        top: i * 45 + "px",
		        left: "0px",
		        width: "460px",
		        height: "42px"
		    });
		}
	    console.log("✅ 냉장고 식재료 출력 완료");
	    });

    // ✅ 그 다음 send 호출
    sms.send();
}
