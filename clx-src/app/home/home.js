/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

const createRecipeCardModule = cpr.core.Module.require("module/recipe/createRecipeCard");
const createRecipeCard = createRecipeCardModule.createRecipeCard;

const slidify = cpr.core.Module.require("module/common/Slidifiy").slidify;

/*
 * 루트 컨테이너에서 init 이벤트 발생 시 호출.
 * 앱이 최초 구성될 때 발생하는 이벤트 입니다.
 */
function onBodyInit(e){
	const slide = slidify(app.lookup("bannerGroup"));
	slide.showCount = 1;
	slide.autoPlay(true);
	slide.start();
}

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const popularRecipeListSms = app.lookup("popularRecipeListSms");
	popularRecipeListSms.send();
	app.lookup("popularSearchSms").send();
}

/*
 * "레시피 더보기" 버튼에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onButtonClick(e){
	var button = e.control;
	window.location.href = "/recipe";
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onPopularRecipeListSmsSubmitSuccess(e){
	const popularRecipeListSms = e.control;
	
	const recipeContainer = app.lookup("popluarRecipeList"); 

	const result = popularRecipeListSms.xhr.responseText;
	const resultJson = JSON.parse(result);
	
	createRecipeCard(resultJson, recipeContainer);
	
	const slide = slidify(recipeContainer);
	slide.start();

	recipeContainer.redraw();
}

// 데이터셋의 모든 행을 순회하며 아웃풋 컨트롤에 값 할당하기
function setDataToOutputs() {
    // 데이터셋 객체 참조
    var ds = app.lookup("popularSearch");  // 사용하려는 데이터셋 ID로 변경
    
    // 데이터셋의 행 수 확인
    var rowCount = ds.getRowCount();
    console.log("Row Count: " + rowCount);
    
    // 출력할 컨트롤들이 있는 배열 또는 컨트롤 ID 패턴 정의
    var outputPrefix = "out";
    
    // 데이터셋의 각 행을 순회하며 해당 컨트롤에 값 할당
    for(var i = 0; i < rowCount; i++) {
        // 키워드와 recipeId 값 가져오기 (id 대신 recipeId 사용)
        var keyword = ds.getValue(i, "keyword");
        var recipeId = ds.getValue(i, "recipeId");  // 컬럼명 수정
        
        // 해당 값을 i번째 아웃풋 컨트롤에 설정
        var outputId = outputPrefix + (i + 1);
        let outputControl = app.lookup(outputId);
        
        if (outputControl) {
            // 값 설정 (순위 표시 추가)
            outputControl.value = (i + 1) + ". " + keyword;
            
            // 사용자 데이터에 recipeId 저장
            outputControl.userData("recipeId", recipeId);
            
            // 클릭 이벤트 핸들러 추가
            outputControl.removeEventListeners("click");
            outputControl.addEventListener("click", function(e) {
                var control = e.control;
                var recipeId = control.userData("recipeId");
                
                if (recipeId) {
                    // 수정된 부분: history.pushState 사용하여 SPA 방식으로 이동
                    history.pushState({}, '', `/recipe/${recipeId}`);
                    
                    // 필요한 경우 세션 스토리지에 레시피 ID 저장 (선택사항)
                    sessionStorage.setItem('currentRecipeId', recipeId);
                    
                    console.log("이동: /recipe/" + recipeId);
                }
            });
            
            // 클릭 가능함을 시각적으로 표시
            outputControl.style.css({
                "cursor": "pointer",
                "text-decoration": "underline"
            });
        }
    }
}





/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onPopularSearchSmsSubmitSuccess(e){
	var popularSearchSms = e.control;
	setDataToOutputs();
	app.lookup("popularSearchBoard").redraw();
}
