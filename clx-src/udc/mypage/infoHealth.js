/************************************************
 * info.js
 * Created at 2025. 4. 27. 오후 4:59:03.
 *
 * @author seongkwan
 ************************************************/
const showToastModule = cpr.core.Module.require("module/common/showToast");

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
exports.getText = function(){
	// TODO: 그리드의 뷰 모드에서 표시할 텍스트를 반환하는 하는 코드를 작성해야 합니다.
	return "";
};

/*
 * "내 건강 정보 수정하기" 버튼(healthInfo_submit)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onHealthInfo_submitClick(e){
	var healthInfo_submit = e.control;
	var allergy = app.lookup("allergy").callAppMethod("getSelectedTag");
	var hatefood =   app.lookup("hatefood").callAppMethod("getSelectedTag");
	var healthinterest = app.lookup("healthinterset").callAppMethod("getSelectedTag");
	
	console.log("선택된 태그:", allergy);
	console.log("선택된 태그2", hatefood);
	console.log("선택된 태그3", healthinterest);
	
	var dmhealthInfo = app.lookup("dmHealthInfo");
	var smshealthInfo = app.lookup("smsHealthInfo"); 
	dmhealthInfo.setValue("allergy",allergy);
	dmhealthInfo.setValue("hateFood", hatefood);
	dmhealthInfo.setValue("interestInfo", healthinterest);
	
	smshealthInfo.addEventListenerOnce("submit-success", function(e) {
	    console.log("✅ 저장 완료 → 최신 정보 다시 불러옴");
	    alert("저장 완료되었습니다!")
	    showToastModule.showToast("저장 완료되었습니다!", 2000);
	    app.lookup("smsHealthInfo").send();  // GET 요청으로 다시 가져오기
  	});
	
	smshealthInfo.send();
		
}
