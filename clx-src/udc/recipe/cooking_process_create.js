/************************************************
 * cooking_process_create.js
 * Created at 2025. 4. 24. 오후 3:28:32.
 *
 * @author gyrud
 ************************************************/

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
exports.getText = function(){
	// TODO: 그리드의 뷰 모드에서 표시할 텍스트를 반환하는 하는 코드를 작성해야 합니다.
	return "";
};


exports.getCookingProcessValue=()=>{
	const stepDescriptionValue = app.lookup("stepDescription").value;
	const stepFile = app.lookup("fileUrl").file;
	
	return {stepDescription:stepDescriptionValue, fileUrl:stepFile};
}

function getImage() {
	var vcFit = app.lookup("fileUrl");
	
	//필요에 따라 파일인풋의 선택된 파일이 이미지인 경우에만 다음의 동작을 수행합니다.
	var vsFtype = vcFit.file.type;
	console.log(vsFtype);
	if (vsFtype.split("/")[0] == "image") {
		var voReader = new FileReader();
		voReader.onload = function(event) {
			console.log(vcFit.file);
			vcFit.style.css({
				"backgroundImage": `url(event.target.result)`,
			})
		}
		voReader.readAsDataURL(vcFit.file);	
	}
}

/*
 * 파일 인풋에서 value-change 이벤트 발생 시 호출.
 * FileInput의 value를 변경하여 변경된 값이 저장된 후에 발생하는 이벤트.
 */
function onFileUrlValueChange(e){
	var fileUrl = e.control;
	getImage();
}
