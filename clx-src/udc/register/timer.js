/************************************************
 * timer.js
 * Created at 2025. 4. 25. 오후 1:31:13.
 *
 * @author seongkwan
 ************************************************/

/**
 * UDC 컨트롤이 그리드의 뷰 모드에서 표시할 텍스트를 반환합니다.
 */
//exports.getText = function(){
//	// TODO: 그리드의 뷰 모드에서 표시할 텍스트를 반환하는 하는 코드를 작성해야 합니다.
//	return "";
//};

// 전역 타이머 변수
var timerInterval = null;

/**
 * 타이머 시작
 * @param {number} duration 초 단위 시간
 * @param {function=} onEnd 타이머 종료 시 콜백 함수 (선택)
 */
function start(duration, onEnd) {
    if (timerInterval) clearInterval(timerInterval);

    var label = app.lookup("timer_section");
    var remainingTime = duration;

    timerInterval = setInterval(function () {
        var minutes = padZero(Math.floor(remainingTime / 60));
        var seconds = padZero(remainingTime % 60);

        label.value = "남은 시간: " + minutes + ":" + seconds;

        if (--remainingTime < 0) {
            clearInterval(timerInterval);
            label.value = "⛔ 인증 시간이 만료되었습니다.";

            // 종료 콜백 호출
            if (typeof onEnd === "function") {
                onEnd();
            }
        }
    }, 1000);
}

/**
 * 숫자 앞 0채우기 유틸
 */
function padZero(num) {
    return (num < 10 ? '0' : '') + num;
}

/**
 * 타이머 초기화 (중단)
 */
function stop() {
    if (timerInterval) clearInterval(timerInterval);
    app.lookup("timer_section").value = "";
}

exports.startTimer = function () {
    start(180);  // 기존 타이머 함수
};
