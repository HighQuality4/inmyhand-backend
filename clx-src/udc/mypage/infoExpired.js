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
        var foodname = ds.getValue("foodName");
        var remain = ds.getValue("expdate");

        // UDC 내 children(group) 반복
	    var groups = app.lookup())
	
	    for (var i = 0; i < ds.getRowCount(); i++) {
	        var foodName = ds.getValue(i, "foodName");
	        var expdate = ds.getValue(i, "expdate");
	
	        var group = groups[i];
	        if (!group) break;
	
	        // 번호 출력 (output-2b5a5dcd)
	        var outputNum = group.lookup("output-2b5a5dcd");
	        outputNum.value = (i + 1) + ".";
	
	        // 음식 이름 출력 (output-350fef9e)
	        var outputName = group.lookup("output-350fef9e");
	        outputName.value = foodName;
	
	        // 남은 날짜 출력 (output-5973ee9a)
	        var outputRemain = group.lookup("output-5973ee9a");
	        var remainText = Number(expdate) < 0 ? "버리셈" : expdate + "일";
	        outputRemain.value = remainText;
	    }

    console.log("✅ 냉장고 식재료 출력 완료");
    });

    // ✅ 그 다음 send 호출
    sms.send();
}
