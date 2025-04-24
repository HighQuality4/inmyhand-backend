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




