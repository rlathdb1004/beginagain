window.addEventListener("load", function () {
    const menuToggle = document.getElementById("menuToggle");
    const sidebar = document.querySelector(".sidebar");

    if (menuToggle && sidebar) {
        menuToggle.addEventListener("click", function () {
            sidebar.classList.toggle("open");
        });
    }

    const menuTitles = document.querySelectorAll(".menu-title");
    for (let i = 0; i < menuTitles.length; i++) {
        menuTitles[i].addEventListener("click", function () {
            this.classList.toggle("open");

            const menuItems = this.nextElementSibling;
            if (menuItems) {
                menuItems.classList.toggle("open");
            }
        });
    }

    const liveCalendar = document.getElementById("liveCalendar");
    const liveClock = document.getElementById("liveClock");

    function updateClock() {
        const now = new Date();
        const y = now.getFullYear();
        const m = String(now.getMonth() + 1).padStart(2, "0");
        const d = String(now.getDate()).padStart(2, "0");
        const h = String(now.getHours()).padStart(2, "0");
        const mi = String(now.getMinutes()).padStart(2, "0");
        const s = String(now.getSeconds()).padStart(2, "0");

        const week = ["일", "월", "화", "수", "목", "금", "토"];
        const w = week[now.getDay()];

        if (liveCalendar) {
            liveCalendar.textContent = y + "-" + m + "-" + d + " " + w + "요일";
        }

        if (liveClock) {
            liveClock.textContent = h + " : " + mi + " : " + s;
        }
    }

    updateClock();
    setInterval(updateClock, 1000);
});