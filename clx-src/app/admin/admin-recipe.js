/************************************************
 * admin-recipe.js
 * Created at 2025. 4. 23. 오후 3:48:33.
 *
 * @author choeyeongbeom
 ************************************************/

/**
 * URL에서 마지막 세그먼트를 추출하는 함수
 * @return {String} URL의 마지막 세그먼트
 */
function getLastUrlSegment() {
    const path = window.location.pathname; 
    return path.substring(path.lastIndexOf('/') + 1);
}

/**
 * 레시피 데이터 조회 함수
 * @param {Number} pageIdx 페이지 인덱스 (선택적)
 */
function loadRecipeData(pageIdx) {
    const recipe = app.lookup("adminRecipeGet");
    const lastSegment = getLastUrlSegment();
    const ipb1 = app.lookup("ipb1").value;
    
    if (ipb1?.trim()) {
   		recipe.setParameters("name", ipb1);   
	}
	
	if (ipb1.trim().length === 0) {
       recipe.removeAllParameters();
    }
	
    
    // 요청 URL 설정
    recipe.setRequestActionUrl(recipe.action + "/" + lastSegment);
    
    // 페이지 파라미터가 있는 경우 설정
    if (pageIdx !== undefined) {
        recipe.setParameters("page", pageIdx);
    }
    
    // 서브미션 전송
    recipe.send();
}

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e) {
    // 초기 데이터 로드
    loadRecipeData();
}

/*
 * 페이지 인덱서에서 selection-change 이벤트 발생 시 호출.
 * Page index를 선택하여 선택된 페이지가 변경된 후에 발생하는 이벤트.
 */
function onRecipeIndexSelectionChange(e) {
    // 페이지 인덱스 업데이트
    var dmPage = app.lookup("pageIndex");
    dmPage.setValue("pageIdx", e.newSelection);
    
    
    // 페이지 인덱스로 데이터 로드
    loadRecipeData(dmPage.getDatas().pageIdx);
}

/*
 * "검색" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을.클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e) {
    var button = e.control;
    const searchValue = app.lookup("ipb1");
    
    // 검색어 유효성 검사
    if (!searchValue || searchValue.value.trim() === "") {
        // 검색어가 비어있는 경우
        app.lookup("ipb1").value = ""; // 입력 필드 초기화
        loadRecipeData(); // 모든 데이터 로드
        return;
    }
    
    
    // 검색어 길이 검사
    if (searchValue.value.trim().length === 1) {
        alert("2글자 이상 입력해주세요.");
        return;
    }
    
    // 검색 실행
    loadRecipeData();
}

/*
 * 그리드에서 cell-click 이벤트 발생 시 호출.
 * Grid의 Cell 클릭시 발생하는 이벤트.
 */
function onGrd1CellClick(e){
	var grd1 = e.control;
	var usersDataset = app.lookup("content");
	   // 클릭된 열의 이름 가져오기
    var clickedColumnName =  e.columnName;
    
  console.log("clickedColumnName : " + clickedColumnName);
    // "레시피 보러가기" 컬럼인지 확인
    if(clickedColumnName === "id") {
        var recipeId = usersDataset.getValue(e.rowIndex, clickedColumnName);
        if(recipeId) {
            window.location.href = "/recipe/" + recipeId;
        }
    }
}

/*
 * 그룹에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onGroupClick(e){
	var group = e.control;
	window.location.href = "/server/grafana";
}

/*
 * 그룹에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onGroupClick2(e){
	var group = e.control;
	window.location.href = "/";
}
