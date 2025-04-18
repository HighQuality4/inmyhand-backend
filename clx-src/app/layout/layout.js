/************************************************
 * layout.js
 * Created at 2025. 4. 10. 오후 6:42:47.
 *
 * @author gyrud
 ************************************************/

const embeddedRoutesTest = [
  { path: "/recipe", loadApp: "app/recipe/recipe_main" },
  { path: /^\/recipe\/(\d+)$/, loadApp: "app/recipe/recipe_detail" },
];

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){
	const subList = app.lookup("sms1");
	subList.send();
	
	const pathName = window.location.pathname;
	
	const match = embeddedRoutesTest.find(route => {
	  if (typeof route.path === "string") {
	    return route.path === pathName;
	  } else if (route.path instanceof RegExp) {
	    return route.path.test(pathName);
	  }
	  return false;
	});
	
	if (match != null) {
		const vsAppId = match.loadApp;
		const vcEmb = app.lookup("ea1");
		
		cpr.core.App.load(vsAppId, function(loadedApp){ 
			if(loadedApp){
				vcEmb.app = loadedApp; 
			} 
		});
		
		app.getContainer().redraw();	
	}
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
//function onSms1SubmitSuccess(e){
//
//	var embeddedRoutes = app.lookup("embeddedRoutes");
//	var voRow = embeddedRoutes.findFirstRow("path == '" + pathName + "'");
//	
//	if (voRow != null) {
//		const vsAppId = voRow.getValue("app");
//		const vcEmb = app.lookup("ea1");
//		
//		cpr.core.App.load(vsAppId, function(loadedApp){ 
//			if(loadedApp){
//				vcEmb.app = loadedApp; 
//			} 
//		});
//		
//		app.getContainer().redraw();	
//	}
//	
//   
//}
