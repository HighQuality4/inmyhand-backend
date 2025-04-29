/************************************************
 * info_tag.js
 * Created at 2025. 4. 27. 오후 5:20:55.
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
 * 인풋 박스에서 keydown 이벤트 발생 시 호출.
 * 사용자가 키를 누를 때 발생하는 이벤트. 키코드 관련 상수는 {@link cpr.events.KeyCode}에서 참조할 수 있습니다.
 */
//function onIpbFirstKeydown(e){
//   var ipb = e.control;
//
//    if (e.keyCode == 13) {
//        var value = ipb.value.trim();
//
//        if (value) {
//            var parent = ipb.getParent();
//            var idx = parent.getChildren().indexOf(ipb);
//            
//              // 현재 Output 개수 세기 (제거 전에 세야 정확함)
//            var outputCountBefore = parent.getChildren().filter(function(child) {
//                return child instanceof cpr.controls.Output;
//            }).length;
//
//            if (outputCountBefore >= 3) {
//                alert("키워드는 3개까지만 입력 가능합니다!");
//                // 입력 막기
//                var parent = ipb.getParent(); // InputBox의 부모 가져오기
//    			parent.removeChild(ipb);      // InputBox 제거
//
//    			return;
//            }
//
//            // 기존 InputBox 제거
//            parent.removeChild(ipb);
//
//            // Output(태그) 생성
//            var output = new cpr.controls.Output();
//            output.value = "#" + value;
//            output.style.css("background-color", "#ffe4e1");
//            output.style.css("border-radius", "12px");
//            output.style.css("padding", "6px");
//            output.style.css("margin", "4px");
//            output.style.css("font-size", "14px");
//            output.style.css("vertical-align", "middle");
//
//			// Output 클릭하면 삭제 + Input 복구
//            output.addEventListener("click", function(e2) {
//                var outputComp = e2.control;
//                var parent = outputComp.getParent();
//                var idx = parent.getChildren().indexOf(outputComp);
//
//                parent.removeChild(outputComp);
//
//                // 삭제 후 현재 Output 개수 확인
//                var outputCountAfterDelete = parent.getChildren().filter(function(child) {
//                    return child instanceof cpr.controls.Output;
//                }).length;
//
//                // Output이 3개 미만이면 새 InputBox 추가
//                var hasInput = parent.getChildren().some(function(child) {
//                    return child instanceof cpr.controls.InputBox;
//                });
//
//                if (outputCountAfterDelete <= 3 && !hasInput) {
//                    var newInput = new cpr.controls.InputBox();
//                    newInput.style.css("padding", "6px");
//                    newInput.style.css("margin", "4px");
//                    newInput.style.css("width", "150px");
//                    newInput.style.css("vertical-align", "middle");
//                    //newInput.style.css("transform", "translateY(3px)");
//                    newInput.addEventListener("keydown", onIpbFirstKeydown);
//
//                    parent.insertChild(idx, newInput, {autoSize: "both"});
//                    newInput.focus();
//                }
//            });
//			
//
//            // Output 추가
//            parent.insertChild(idx, output, {autoSize: "both"});
//
//            // ✅ 여기서! Output 개수 다시 세기
//            var outputCount = parent.getChildren().filter(function(child) {
//                return child instanceof cpr.controls.Output;
//            }).length;
//
//            if (outputCount <= 3) {
//                // 아직 3개 미만이면 새 InputBox 추가
//                var newInput = new cpr.controls.InputBox();
//                newInput.style.css("padding", "6px");
//                newInput.style.css("margin", "4px");
//                newInput.style.css("width", "150px");
//                newInput.style.css("vertical-align", "middle");
//                newInput.addEventListener("keydown", onIpbFirstKeydown);
//
//                parent.insertChild(idx + 1, newInput, {autoSize: "both"});
//                newInput.focus();
//            }
//        }
//    }
//}

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	
	cpr.core.Module.require("module/quicksearch/quickSearch");
	
//	var ipbFirst = app.lookup("ipbFirst");
//    if (ipbFirst) {
//        ipbFirst.style.css("padding", "6px");
//        ipbFirst.style.css("margin", "4px");
//        ipbFirst.style.css("width", "150px");
//        ipbFirst.style.css("vertical-align", "middle");
//    }
}


function addHashtag(tagText) {
    var grpHashtags = app.lookup("tagArea"); // 해시태그를 모아둘 Group

    var tag = new cpr.controls.Output(); // 해시태그를 버튼으로
    tag.value = "#" + tagText;
    tag.style.css({
        "background-color": "#ffe4e1",
        "padding": "3px",
        "border-radius": "12px",
        "margin": "4px",
        "font-size": "13px",
        "border": "none",
        "cursor": "pointer"
    });

    grpHashtags.addChild(tag, {
        width: "auto",
        height: "30px"
    });
}

/*
 * 인풋 박스에서 keydown 이벤트 발생 시 호출.
 * 사용자가 키를 누를 때 발생하는 이벤트. 키코드 관련 상수는 {@link cpr.events.KeyCode}에서 참조할 수 있습니다.
 */
function onIpbFirstKeydown2(e){
	var ipbFirst = e.control;
	if (e.keyCode == 13) {
		var ipbFirst = e.control;
		var selectedValue = ipbFirst.value;
		
		addHashtag(selectedValue);
		
		ipbFirst.value = "";
	}
}
