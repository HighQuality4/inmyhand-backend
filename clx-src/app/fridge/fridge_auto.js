/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/




/*
 * "사진
 * 업로드" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	
	//----------------------------1. UDC [생성] ---------------------------------
	//------------------------- 이미지 업로드 UDC ------------------------------
	
	// 1.이미지 업로드 UDC 컨트롤을 동적으로 생성할 컨테이너 찾기
	var ImageContainer = app.lookup("addImageArea"); // 컨테이너 ID로 찾기
	
	// 2. 이미지 UDC 인스턴스 생성
    var udcItem = new udc.image.UDCImageBox();
    udcItem.id = "photo_" + new Date().getMilliseconds(); 

    //---------------------------- 그리드 UDC----------------------------------- 
    // 1. ocr 결과값 그리드 UDC 컨트롤을 동적으로 생성할 컨테이너 찾기
	var resultContainer = app.lookup("resultImageArea"); // 컨테이너 ID로 찾기
	// 2. UDC 인스턴스 생성
   
   console.log("그리드 생성")
	
	 // 4. 컨테이너에 이미지 UDC 추가
    ImageContainer.insertChild(0, udcItem, {
        width: "100px",
        height: "100px"
    });

   //---------------------------- 3. 동적 UDC의 삭제 이벤트 부여 / 동적 추가 -----------------------
   //----------------------------- 이미지 업로드 UDC------------------------------
    // 3. 이미지 삭제 이벤트 부여
  	udcItem.addEventListener("removeImageCtrlBtn", function(e) {
		e.preventDefault();
//		alert(udcItem.app.uuid + " 등록을 취소합니다!");
		ImageContainer.removeChild(udcItem);
	
		var linkedResultId = udcItem.userAttr("linkedResultId");
		if(linkedResultId){
			var linkedGrid = resultContainer.getChildren().find(function(child) {
			    return child.id === linkedResultId;
			});
			if (linkedGrid) {
			    resultContainer.removeChild(linkedGrid);
			}
		}
		
		
		// ✅ ocrAllList 데이터 삭제
		deleteOcrRowsById(linkedResultId);
	});
    

    // ------------------------------4. 이미지 선책 후 그리드 생성하기-----------------------------
	// 파일 업로드 성공 이벤트나 파일 변경 이벤트를 감지
    udcItem.addEventListener("test", function(e){
    	
    	// 그리드 생성하기
    	var resultItem = new udc.fridge.UDCFridgeGrid("result_" + new Date().getMilliseconds());
		    	    // 4. 컨테이너에 이미지 UDC 추가
			resultContainer.insertChild(0, resultItem, {
			    width: "570px",
			    height: "200px"
			});
			resultItem.userAttr("linkedImageId", String(udcItem.id));
			udcItem.userAttr("linkedResultId", String(resultItem.id));

			// 파일 서버로 전송하는 서브미션
			var submission = app.lookup("sendImageOcr");
		    var files = e.option.file;
		    if (files) {
		        
		    } else {
		        console.log("선택된 파일이 없습니다.");
		        return;
		    }
			
			// 파일 인풋에 넣어서 파일 객체로 서버로 보내기
			var input = app.lookup("fi1")
			input.file = null;
			input.file = files;
			input.redraw();
			
			input.send(submission)
			
			// 서브 미션을 두번 탐,,,? 그래서 addEventListenerOnce 이벤트 걸어줌
	   		submission.addEventListenerOnce("submit-success", function(e){
	   			console.log("서버로 이미지 전송 성공 시 그리드에 뿌리기")
	   			if(e){
	   				var ds = app.lookup("ocrResultList");
		    	
			    	//------------------------- 저장로직 -------------------------
			    	for(var i = 0; i < ds.getRowCount(); i++){
			    		ds.setValue(i, "id", resultItem.id)
			    	}
			    	
			    	// 전체 식재료 정보 저장 데이터셋에 전달하기 (UDC 데이터셋-> 최종 ALL 
			    	app.lookup("ocrResultList").copyToDataSet(app.lookup("ocrAllList"));

					console.log("그리드에 뿌리고 데이터셋 확인 ===========")
			    	console.log(app.lookup("ocrAllList").getRowDataRanged())
			    	if(resultItem){
			    		resultItem.setAppProperty("dataSet", ds);
			    	}

	   			}
	   			
	   		});
    });

}	


/*
 * "다시 하기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick2(e){
	var button = e.control;
	
	var container = app.lookup("addImageArea");
	container.removeAllChildren();	
	
	
	var resultContainer = app.lookup("resultImageArea");
	resultContainer.removeAllChildren();	
}



function deleteOcrRowsById(targetId) {
	var ocrAllList = app.lookup("ocrAllList");
//	console.log(ocrAllList.getRowDataRanged());
//	console.log(targetId);
	var rows = ocrAllList.findAllRow("id == '" + targetId + "'");
//	console.log(rows)
	rows.forEach(row => {
//		console.log(row.getIndex())
		
//	    ocrAllList.realDeleteRow(row.getIndex());
	});
	for(var i = rows.length-1 ; i >= 0  ; i-- ){
		app.lookup("ocrAllList").realDeleteRow(rows[i].getIndex());			
	}
		
//	console.log("지움")
//	console.log(ocrAllList.getRowDataRanged());
}

/*
 * 사용자 정의 컨트롤에서 save-update-click 이벤트 발생 시 호출.
 * update서버연결저장
 */
function onFridgeUDSaveUpdateClick(e){
	var fridgeUD = e.control;
	var ds1 = app.lookup("aaa").callAppMethod("getDataSet");
//	console.log(ds1.getRowDataRanged());
}


/*
 * "저장" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick3(e){
	var button = e.control;
//	var result = app.lookup("ocrAllList");
	console.log("저장버튼 누르고 데이터셋 확인 ===========")
	console.log(app.lookup("ocrResultList").getRowDataRanged());
	
	console.log("저장버튼 누르고 데이터셋 확인 ===========")
	console.log(app.lookup("ocrAllList").getRowDataRanged());
	
	
	app.lookup("ocrAllList").deleteColumn("id");
	
	console.log("필요없는값 삭제===========")
	console.log(app.lookup("ocrAllList").getRowDataRanged());
	
	
	console.log("서버로 보내기 >>>" );
	
	app.lookup("sendOcrResultInfo").send();
	
	
}


/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onSendOcrResultInfoSubmitSuccess(e){
	var sendOcrResultInfo = e.control;
	
		alert("저장에 성공했습니다.")
	window.location.href = "/fridge/auto";
}

/*
 * 서브미션에서 submit-error 이벤트 발생 시 호출.
 * 통신 중 문제가 생기면 발생합니다.
 */
function onSendOcrResultInfoSubmitError(e){
	var sendOcrResultInfo = e.control;
	alert("저장에 실패했습니다. 다시 시도해주세요")
	window.location.href = "/fridge/auto";
}
