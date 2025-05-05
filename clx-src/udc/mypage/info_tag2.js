/************************************************
 * info_tag.js
 * Created at 2025. 4. 27. 오후 5:20:55.
 *
 * @author seongkwan
 ************************************************/
cpr.core.Module.require("module/quicksearch/quickSearch");
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

if (typeof exports === "undefined") exports = {};

// ✅ UDC 스코프 전체에서 공유되는 상태
var selectedTags = [];


function onBodyLoad(e){
	
	var sms = app.lookup("smsHateFoodTags");
	sms.send();
	
	
}

function addHashtag(tagText) {
	
    var grpHashtags = app.lookup("tagArea"); // 해시태그를 모아둘 Group
    if (selectedTags.indexOf(tagText) !== -1) return; // 중복 방지
    var tag = new cpr.controls.Output(); // 해시태그를 버튼으로
    tag.value = "#" + tagText;
    console.log("태그: " + tagText);
    selectedTags.push(tagText);
    exports.getSelectedTag=()=>{
		
			var res = selectedTags;
		
			return res;
		}
    tag.style.css({
        "background-color": "#ffe4e1",
        "padding": "3px",
        "border-radius": "12px",
        "margin": "4px",
        "font-size": "13px",
        "border": "none",
        "cursor": "pointer"
    });
    
    // 3. 클릭 시 삭제 이벤트
    tag.addEventListener("click", function(e2) {
        var outputComp = e2.control;
        var parent = outputComp.getParent();

        // 태그 텍스트에서 "#" 제거 후 selectedTags에서 제거
        var valueWithoutHash = outputComp.value.replace(/^#/, "");
        selectedTags = selectedTags.filter(function(t) {
            return t !== valueWithoutHash;
        });

        parent.removeChild(outputComp);

        // 삭제 후 남은 태그 수 출력
        var remaining = parent.getChildren().filter(function(child) {
            return child instanceof cpr.controls.Output;
        }).length;

        console.log("❌ 태그 삭제됨. 현재 개수:", remaining);
        console.log("🧾 현재 selectedTags:", selectedTags);
    });

    grpHashtags.addChild(tag, {
        width: "auto",
        height: "30px"
    });
    
    // ✅ 길이 3이면 출력
    if (selectedTags.length === 3) {
        console.log("✅ 태그 3개 완성됨:", selectedTags);
    }
    
    
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

/*
 * 서브미션에서 receive 이벤트 발생 시 호출.
 * 서버로 부터 데이터를 모두 전송받았을 때 발생합니다.
 */
function onSmsHateFoodTagsReceive(e){
	var smsHateFoodTags = e.control;
	var xhr = smsHateFoodTags.xhr;
	var response = JSON.parse(xhr.responseText);
	
	response.forEach(function(tagText) {
		addHashtag(tagText);
	})
	
}
