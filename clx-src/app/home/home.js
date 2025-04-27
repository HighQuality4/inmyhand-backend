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
	
	slide.autoPlay(true);
	slide.start();

	recipeContainer.redraw();
}



