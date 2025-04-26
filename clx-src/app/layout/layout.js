/************************************************
 * layout.js
 * Created at 2025. 4. 10. 오후 6:42:47.
 *
 * @author gyrud
 ************************************************/

const embeddedRoutesModule = cpr.core.Module.require("data/embeddedRoutes");
const embeddedRoutes = embeddedRoutesModule.embeddedRoutes;
/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){ //여기에 로그인 여부 판단해서 버튼 교체 로직 넣을 것
	const pathName = window.location.pathname;
	
	const match = embeddedRoutes.find(route => {
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
