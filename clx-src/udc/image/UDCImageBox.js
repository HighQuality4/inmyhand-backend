/************************************************
 * image.js
 * Created at 2025. 4. 25. 오전 7:43:21.
 *
 * @author user
 ************************************************/

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
exports.getText = function(){
	// TODO: 그리드의 뷰 모드에서 표시할 텍스트를 반환하는 하는 코드를 작성해야 합니다.
	return "";
};


/*
 * 파일 인풋에서 value-change 이벤트 발생 시 호출.
 * FileInput의 value를 변경하여 변경된 값이 저장된 후에 발생하는 이벤트.
 */
function onSelectFileInputValueChange2(e){
	var selectFileInput = e.control;
	
	
	// 파일 미리보기 이미지 구현 
	var vsFtype = selectFileInput.file.type;
	if (vsFtype.split("/")[0] == "image") {
		var voReader = new FileReader();
		voReader.onload = function(event) {
			var imgctl = app.lookup("sampleImg");
			imgctl.src = event.target.result;
		}
		
		voReader.readAsDataURL(selectFileInput.file);
	}	
	
	// 변경된 파일 객체 전달
	var event = new cpr.events.CUIEvent("test", {file:selectFileInput.file, control: selectFileInput} );
	var result = app.dispatchEvent(event); 
}



/*
 * 이미지에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onSampleImgClick2(e){
	var sampleImg = e.control;
	var fileInput = app.lookup("selectFileInput");

	
	
	// fileInput에 포커스 이동
	fileInput.focus();
	fileInput.openFileChooser();
}

/*
 * "x" 버튼(deleteBtn)에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onDeleteBtnClick(e){
	var deleteBtn = e.control;

	// 이미지 보내기
//	fileInput.send(app.lookup("sendImage"));
	
	var event = new cpr.events.CUIEvent("removeImageCtrlBtn");
	
	var result = app.dispatchEvent(event); // 이벤트를 던져보고
	
	// e.preventDefault() 하면 
	// - result = false
	// - 아무도 안 하면 result = undefined
	// preventDefault() 가 아닐때 기본동작 실행
	// - result => false가 아닐때
	if(result !== false){
		// 기본 동작 실행 (예: 진짜 삭제 코드)
		alert("기본 삭제 동작 실행!");
		
		var fileInput = app.lookup("selectFileInput");	
		fileInput.file = null;
		var sampleImg = app.lookup("sampleImg");
		// 기존 이미지 미리보기 제거 (기본 사진으로 변경)
		sampleImg.src = "theme/images/heart_empty.svg";
	}
}

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad2(e){
	
	var event = new cpr.events.CUIEvent("mutiImagesCtrl");
	var result = app.dispatchEvent(event);
	
	
}
