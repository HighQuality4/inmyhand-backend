/************************************************
 * showToast.module.js
 * Created at 2025. 5. 2. 오전 11:30:33.
 *
 * @author gyrud
 ************************************************/

const showToast=(message, duration = 3000)=>{
  // 이미 스타일이 추가되어 있다면 중복 방지
  if (!document.getElementById('toast-style')) {
    const style = document.createElement('style');
    style.id = 'toast-style';
    style.textContent = `
      .toast-container {
        position: fixed;
        top: 80px;
        left: 50%;
        transform: translateX(-50%);
        background-color: #F59760;
        color: #ffffff;
        padding: 12px 20px;
        border-radius: 8px;
        font-size: 14px;
		font-weight: bold;
        z-index: 9999;
        opacity: 0;
        transition: opacity 0.3s ease, transform 0.3s ease;
        pointer-events: none;
      }
      .toast-container.show {
        opacity: 1;
        transform: translateX(-50%) translateY(-10px);
      }
    `;
    document.head.appendChild(style);
  }

  const toast = document.createElement('div');
  toast.className = 'toast-container';
  toast.textContent = message;
  document.body.appendChild(toast);

  // 토스트 보여주기
  requestAnimationFrame(() => {
    toast.classList.add('show');
  });

  // 자동 제거
  setTimeout(() => {
    toast.classList.remove('show');
    setTimeout(() => {
      document.body.removeChild(toast);
    }, 300); // CSS transition 시간과 일치
  }, duration);
};

exports.showToast = showToast;