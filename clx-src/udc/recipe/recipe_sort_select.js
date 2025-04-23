/************************************************
 * recipe_sort_button.js
 * Created at 2025. 4. 22. 오후 1:44:34.
 *
 * @author gyrud
 ************************************************/
/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const recipeSortSelectBox = app.lookup("recipeSortSelectBox");
	
	recipeSortSelectBox.addItem(new cpr.controls.Item("칼로리 낮은순", "value1"));
	recipeSortSelectBox.addItem(new cpr.controls.Item("칼로리 높은순", "value2"));
	recipeSortSelectBox.addItem(new cpr.controls.Item("소요시간 낮은순", "value3"));
	recipeSortSelectBox.addItem(new cpr.controls.Item("소요시간 높은순", "value4"));
	recipeSortSelectBox.addItem(new cpr.controls.Item("난이도 낮은순", "value5"));
	recipeSortSelectBox.addItem(new cpr.controls.Item("난이도 높은순", "value6"));
	
}
