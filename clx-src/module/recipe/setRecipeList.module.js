/************************************************
 * setRecipeList.module.js
 * Created at 2025. 4. 28. 오후 7:17:04.
 *
 * @author gyrud
 ************************************************/

const createRecipeCardModule = cpr.core.Module.require("module/recipe/createRecipeCard");
const createRecipeCard = createRecipeCardModule.createRecipeCard;

const setRecipeList=(submission, recipeContainer, recipeGroup, pageIndexer)=> {
	if(recipeContainer.getChildrenCount()){
		recipeContainer.removeAllChildren();
	}
		
	const result = submission.xhr.responseText;
	const resultJson = JSON.parse(result);
	
	createRecipeCard(resultJson.content, recipeContainer);
	pageIndexer.totalRowCount = resultJson.totalElements;
	pageIndexer.startPageIndex = resultJson.size;
	pageIndexer.currentPageIndex = (resultJson.number)+1;
		
	recipeGroup.redraw();
}

exports.setRecipeList = setRecipeList;