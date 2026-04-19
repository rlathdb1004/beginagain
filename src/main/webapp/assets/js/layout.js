document.addEventListener("DOMContentLoaded", function () {
    const siMenuToggle = document.getElementById("menuToggle");
    const siSidebar = document.querySelector(".sidebar");
    const siMenuTitles = Array.from(document.querySelectorAll(".menu-title"));
    const siStorageKey = "siSidebarOpenMenus";

    if (siMenuToggle && siSidebar) {
        siMenuToggle.addEventListener("click", function () {
            siSidebar.classList.toggle("open");
        });
    }

    let siOpenMenus = [];
    try {
        siOpenMenus = JSON.parse(localStorage.getItem(siStorageKey)) || [];
    } catch (e) {
        siOpenMenus = [];
    }

    // 저장된 열린 메뉴 복원
    siMenuTitles.forEach(function (siTitle, siIndex) {
        const siMenuItems = siTitle.nextElementSibling;
        if (!siMenuItems || !siMenuItems.classList.contains("menu-items")) return;

        const siIsDashboard = siIndex === 0;

        if (siIsDashboard || siOpenMenus.includes(siIndex)) {
            siTitle.classList.add("open");
            siMenuItems.classList.add("open");
        } else {
            siTitle.classList.remove("open");
            siMenuItems.classList.remove("open");
        }

        // 대제목 클릭
        siTitle.addEventListener("click", function (e) {
            e.preventDefault();

            const siIsCurrentlyOpen = siTitle.classList.contains("open");

            // 대시보드는 항상 열림 유지
            if (siIsDashboard) {
                siTitle.classList.add("open");
                siMenuItems.classList.add("open");
                return;
            }

            if (siIsCurrentlyOpen) {
                siTitle.classList.remove("open");
                siMenuItems.classList.remove("open");
            } else {
                siTitle.classList.add("open");
                siMenuItems.classList.add("open");
            }

            const siUpdatedOpenMenus = [];
            siMenuTitles.forEach(function (siEachTitle, siEachIndex) {
                if (siEachTitle.classList.contains("open")) {
                    siUpdatedOpenMenus.push(siEachIndex);
                }
            });

            localStorage.setItem(siStorageKey, JSON.stringify(siUpdatedOpenMenus));
        });

        // 소제목 클릭 시 부모 메뉴 열린 상태 저장
        const siLinks = siMenuItems.querySelectorAll("a");
        siLinks.forEach(function (siLink) {
            siLink.addEventListener("click", function () {
                const siUpdatedOpenMenus = [];
                siMenuTitles.forEach(function (siEachTitle, siEachIndex) {
                    if (siEachIndex === 0 || siEachTitle.classList.contains("open") || siEachIndex === siIndex) {
                        siUpdatedOpenMenus.push(siEachIndex);
                    }
                });

                localStorage.setItem(siStorageKey, JSON.stringify(siUpdatedOpenMenus));
            });
        });
    });
});