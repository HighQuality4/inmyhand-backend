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


function onSelectFileInputValueChange(e){
	
	var selectFileInput = e.control;
	// 파일 미리보기 이미지 구현 
	var vsFtype = selectFileInput.file.type;
	if (vsFtype.split("/")[0] == "image") {
		var voReader = new FileReader();
		voReader.onload = function(event) {
			var vcImage = app.lookup("sampleImg");
			vcImage.src = event.target.result;
		}
		
		voReader.readAsDataURL(selectFileInput.file);
	}	
}

/*
 * 이미지에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onSampleImgClick(e){
	var sampleImg = e.control;
	
	var fileInput = app.lookup("selectFileInput");
	// fileInput에 포커스 이동
	fileInput.focus();
	fileInput.openFileChooser();
}

/*
 * "x" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	
	var fileInput = app.lookup("selectFileInput");
	fileInput.file = null;
	var sampleImg = app.lookup("sampleImg");
	// 👉 기존 이미지 미리보기 제거
	sampleImg.src = "theme/images/heart_empty.svg";
	
}
