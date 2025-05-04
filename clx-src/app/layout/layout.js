/************************************************
 * layout.js
 * Created at 2025. 4. 10. 오후 6:42:47.
 *
 * @author gyrud
 ************************************************/

const embeddedRoutesModule = cpr.core.Module.require("data/embeddedRoutes");
const embeddedRoutes = embeddedRoutesModule.embeddedRoutes;


const embeddedAppChange = () => {
	app.lookup("isLoggedInSms").send();
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
		
		const oldAppInstance = vcEmb.getEmbeddedAppInstance();
		if(oldAppInstance) {
			oldAppInstance.dispose();
		}
		
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
	app.lookup("adminCheckSms").send();
	app.lookup("isLoggedInSms").send();
	//TODO: 로그인 여부 판단해서 버튼 교체 로직 넣을 것
}

/* nav바 버튼 클릭 시 화면 이동 */
const navClickEvent=(e)=>{
	history.pushState({}, '', e.control.userAttr("route"));
}
// footer
function onHomeClick(e){navClickEvent(e);}
function onFridgeClick(e){navClickEvent(e);}
function onRecipeClick(e){navClickEvent(e);}
function onAuthClick(e){navClickEvent(e);}
// header
function onReceiptPhotoClick(e){navClickEvent(e);}
function onRecipeRegistrationClick(e){navClickEvent(e);}


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
    embeddedAppChange();
});

/*
 * 그룹에서 click 이벤트 발생 시 호출.
 * 사용자가 컨트롤을 클릭할 때 발생하는 이벤트.
 */
function onAdminpotoClick(e){
	var adminpoto = e.control;
    window.location.href = "/admin/user";
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onAdminCheckSmsSubmitSuccess(e){
	var adminCheckSms = e.control;
	var adminGroup = app.lookup("adminpoto");
	let check = app.lookup("adcheck");
	let result = check.getValue("check");
	if (result === "1") {
		  adminGroup.visible = true;
        } else {
	     adminGroup.visible = false;
	}
	
	adminGroup.redraw();
}

/*
 * 서브미션에서 submit-success 이벤트 발생 시 호출.
 * 통신이 성공하면 발생합니다.
 */
function onIsLoggedInSmsSubmitSuccess(e){
	const isLoggedInSms = e.control;
	
	const result = isLoggedInSms.xhr.responseText;
	const resultJson = JSON.parse(result);
	
	if(resultJson){
		const auth = app.lookup("auth");
		const authOutput = app.lookup("authOutput");
		const authImg = app.lookup("authImg");
		authOutput.value = "마이페이지";
		authImg.src = "theme/images/user.svg";
		auth.userAttr("route", "/users/mypage");
	}
}
