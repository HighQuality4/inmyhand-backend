/************************************************
 * layout.js
 * Created at 2025. 4. 10. 오후 6:42:47.
 *
 * @author gyrud
 ************************************************/

const embeddedRoutesModule = cpr.core.Module.require("data/embeddedRoutes");
const embeddedRoutes = embeddedRoutesModule.embeddedRoutes;

const embeddedAppChange = () => {
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
			vcEmb.app = loadedApp; 
		});
		
		app.getContainer().redraw();
	}
}

/*
 * 루트 컨테이너에서 load 이벤트 발생 시 호출.
 * 앱이 최초 구성된후 최초 랜더링 직후에 발생하는 이벤트 입니다.
 */
function onBodyLoad(e){ 
	embeddedAppChange();
	
	//TODO: 로그인 여부 판단해서 버튼 교체 로직 넣을 것
}

/* nav바 버튼 클릭 시 화면 이동 */
function onHomeClick(e){history.pushState({}, '', `/`);}
function onFridgeClick(e){history.pushState({}, '', `/fridge`);}
function onRecipeClick(e){history.pushState({}, '', `/recipe`);}
function onAuthClick(e){history.pushState({}, '', `/users/login`);}

// URL 변경 감지
(function(history) {
    var pushState = history.pushState;
    var replaceState = history.replaceState;

    history.pushState = function(state) {
        var result = pushState.apply(history, arguments);
        if (typeof history.onpushstate == "function") {
            history.onpushstate({state: state});
        }
        return result;
    };

    history.replaceState = function(state) {
        var result = replaceState.apply(history, arguments);
        if (typeof history.onreplacestate == "function") {
            history.onreplacestate({state: state});
        }
        return result;
    };
})(window.history);

// pushState 감지
history.onpushstate = function(e) {
    embeddedAppChange();
};

// replaceState 감지
history.onreplacestate = function(e) {
    embeddedAppChange();
};


// 뒤로 가기, 앞으로 가기 감지
window.addEventListener('popstate', function(event) {
    console.log("popstate triggered", location.pathname, event.state);
    embeddedAppChange();
});
