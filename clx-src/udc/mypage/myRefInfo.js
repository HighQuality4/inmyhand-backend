/************************************************
 * myRefInfo.js
 * Created at 2025. 5. 2. 오후 2:17:51.
 *
 * @author seongkwan
 ************************************************/

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
exports.getText = function(){
	// TODO: 그리드의 뷰 모드에서 표시할 텍스트를 반환하는 하는 코드를 작성해야 합니다.
	return "";
};

exports.setFoodName = function(val) {
  app.lookup("foodname").value = val;
};

exports.setExpdate = function(val) {
  app.lookup("expdate").value = val;
};

exports.setNum = function(val) {
  app.lookup("num").value = val + ".";
};