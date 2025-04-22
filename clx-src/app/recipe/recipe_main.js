/************************************************
 * contents_container.js
 * Created at 2025. 4. 17. 오후 1:52:50.
 *
 * @author gyrud
 ************************************************/

const createRecipeCardModule = cpr.core.Module.require("module/recipe/createRecipeCard");
const createRecipeCard = createRecipeCardModule.createRecipeCard;

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad2(e){
	const popularRecipeListSms = app.lookup("popularRecipeListSms");
	popularRecipeListSms.send();
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onPopularRecipeListSmsSubmitSuccess2(e){
	const popularRecipeListSms = e.control;
	
	const recipeContainer = app.lookup("popluarRecipeList"); 

	const result = popularRecipeListSms.xhr.responseText;
	const resultJson = JSON.parse(result);
	
	createRecipeCard(resultJson, recipeContainer);

	app.getContainer().redraw();
}
