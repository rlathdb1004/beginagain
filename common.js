window.addEventListener('load', init)

function init() {

    const logoutBtn = document.getElementById('logoutBtn');
    const pageMainTitle = document.getElementById('pageMainTitle');
    const pageSubTitle = document.getElementById('pageSubTitle');

    const navHome = document.querySelector('.nav-home');
    const menuTitles = document.querySelectorAll('.menu-title');
    const pageSections = document.querySelectorAll('.page-section');
    const submenuButtons = document.querySelectorAll('.menu-items button');

    const profileModal = document.getElementById('profileModal');
    const openProfileBtn = document.getElementById('openProfileBtn');
    const closeProfileBtn = document.getElementById('closeProfileBtn');
    const toast = document.getElementById('toast');


    /* 실시간 시계 */
    const liveCalendar = document.getElementById('liveCalendar');
    const liveClock = document.getElementById('liveClock');

    function updateClock() {
        const now = new Date();
        const y = now.getFullYear();
        const m = String(now.getMonth() + 1).padStart(2, '0');
        const d = String(now.getDate()).padStart(2, '0');
        const h = String(now.getHours()).padStart(2, '0');
        const mi = String(now.getMinutes()).padStart(2, '0');
        const s = String(now.getSeconds()).padStart(2, '0');

        const week = ['일', '월', '화', '수', '목', '금', '토']
        const w = week[now.getDay()]

        liveCalendar.textContent = `${y}-${m}-${d} ${w}요일`
        liveClock.textContent = `${h} : ${mi} : ${s}`
    }
    updateClock();
    setInterval(updateClock, 1000);

    document.getElementById('menuToggle').addEventListener('click', () => {        
        document.querySelector('.sidebar').classList.toggle('open')
    })

    /*  
        토스트 알림 함수
        로그인 성공, 메뉴 이동 등 짧은 알림 메시지를 우측 하단에 띄운다.       
    */
    let toastTimer = null;
    function showToast(message) {
        toast.textContent = message;
        toast.classList.add('show');

        clearTimeout(toastTimer);
        toastTimer = setTimeout(() => {
            toast.classList.remove('show');
        }, 2200);
    }

    /*
       페이지 전환 함수
        - 좌측 메뉴를 눌렀을 때 해당 섹션만 보이게 처리한다.
        - 상단 제목 / 부제목도 섹션 dataset 값으로 자동 교체한다.        
    */
    function activatePage(pageKey) {
        pageSections.forEach(section => {
            section.classList.remove('active');
        });

        const target = document.getElementById(`page-${pageKey}`);
        if (!target) return;

        target.classList.add('active');

        pageMainTitle.textContent = target.dataset.title || 'Begin Again MES';
        pageSubTitle.textContent = target.dataset.subtitle || '';

        navHome.classList.toggle('active', pageKey === 'dashboard');

        submenuButtons.forEach(button => {
            button.classList.toggle('active', button.dataset.page === pageKey);
        });

        const sectionTop = document.querySelector('.content-area');
        if (sectionTop) sectionTop.scrollIntoView({ behavior: 'smooth', block: 'start' });

        requestAnimationFrame(refreshTableCellTooltips);
        showToast(`${pageMainTitle.textContent} 화면으로 이동했습니다.`);
    }

    /* 로그아웃 버튼 눌렀을 때   */

    logoutBtn.addEventListener('click', () => {
        document.querySelector('.app').classList.add('hidden');        
        showToast('로그아웃되었습니다.');
    });

    /*
        좌측 메뉴 클릭 이벤트
        대시보드 단일 버튼
        각 소메뉴 버튼       
    */
    navHome.addEventListener('click', () => {
        activatePage('dashboard');
    });

    submenuButtons.forEach(button => {
        button.addEventListener('click', () => {
            activatePage(button.dataset.page);
        });
    });

    /*
        메뉴 그룹 열기 / 닫기       
    */
    menuTitles.forEach(titleBtn => {
        titleBtn.addEventListener('click', () => {
            titleBtn.classList.toggle('open');
            const menuItems = titleBtn.nextElementSibling;
            if (menuItems) {
                menuItems.classList.toggle('open');
            }
        });
    });

    /* 내 정보 모달 */
    openProfileBtn.addEventListener('click', () => {
        profileModal.classList.remove('hidden');
    });

    closeProfileBtn.addEventListener('click', () => {
        profileModal.classList.add('hidden');
    });

    profileModal.addEventListener('click', (event) => {
        if (event.target === profileModal) {
            profileModal.classList.add('hidden');
        }
    });
}


