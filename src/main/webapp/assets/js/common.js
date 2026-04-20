document.addEventListener("DOMContentLoaded", function () {
    const allModals = Array.from(document.querySelectorAll(".taModal"));

    function syncBodyState() {
        const hasOpen = document.querySelector(".taModal.open:not([hidden])");
        document.body.classList.toggle("modal-open", !!hasOpen);
    }

    function hideModal(modal) {
        if (!modal) return;
        modal.classList.remove("open");
        modal.setAttribute("hidden", "hidden");
        modal.setAttribute("aria-hidden", "true");
        syncBodyState();
    }

    function initializeModalForm(modal) {
        if (!modal) return;
        const form = modal.querySelector("form");
        if (!form) return;
        try { form.reset(); } catch (e) {}

        form.querySelectorAll("input[type='date']").forEach(function(input) {
            if (!input.readOnly && !input.disabled && !input.value) {
                const today = new Date();
                const yyyy = today.getFullYear();
                const mm = String(today.getMonth() + 1).padStart(2, "0");
                const dd = String(today.getDate()).padStart(2, "0");
                input.value = yyyy + "-" + mm + "-" + dd;
            }
        });

        const firstSelect = form.querySelector("select");
        if (firstSelect) {
            firstSelect.dispatchEvent(new Event("change", { bubbles: true }));
        }
    }

    function showModal(modal) {
        if (!modal) return;
        initializeModalForm(modal);
        modal.removeAttribute("hidden");
        modal.setAttribute("aria-hidden", "false");
        modal.classList.add("open");
        syncBodyState();
    }

    function updateClock() {
        const calendarEl = document.getElementById("liveCalendar");
        const clockEl = document.getElementById("liveClock");
        if (!calendarEl && !clockEl) return;

        const now = new Date();
        const days = ["일", "월", "화", "수", "목", "금", "토"];
        const yyyy = now.getFullYear();
        const mm = String(now.getMonth() + 1).padStart(2, "0");
        const dd = String(now.getDate()).padStart(2, "0");
        const day = days[now.getDay()];
        const hh = String(now.getHours()).padStart(2, "0");
        const mi = String(now.getMinutes()).padStart(2, "0");
        const ss = String(now.getSeconds()).padStart(2, "0");

        if (calendarEl) calendarEl.textContent = `${yyyy}-${mm}-${dd} ${day}요일`;
        if (clockEl) clockEl.textContent = `${hh} : ${mi} : ${ss}`;
    }

    allModals.forEach(hideModal);
    updateClock();
    window.setInterval(updateClock, 1000);

    document.addEventListener("click", function (e) {
        const closeBtn = e.target.closest(".taModalClose");
        if (closeBtn) {
            e.preventDefault();
            e.stopPropagation();
            hideModal(closeBtn.closest(".taModal"));
            return;
        }

        const openBtn = e.target.closest("[data-modal-target]");
        if (openBtn) {
            const targetId = openBtn.getAttribute("data-modal-target");
            if (targetId) {
                const modal = document.getElementById(targetId);
                if (modal && modal.classList.contains("taModal")) {
                    e.preventDefault();
                    showModal(modal);
                }
            }
            return;
        }

        if (e.target.classList && e.target.classList.contains("taModal")) {
            hideModal(e.target);
        }
    });

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") {
            document.querySelectorAll(".taModal.open").forEach(hideModal);
        }
    });

    document.querySelectorAll("input[id='checkAll']").forEach(function (checkAll) {
        const scope = checkAll.closest("form") || checkAll.closest(".taTableShell") || document;
        const itemBoxes = Array.from(scope.querySelectorAll("tbody input[type='checkbox']"));

        checkAll.addEventListener("change", function () {
            itemBoxes.forEach(function (box) {
                box.checked = checkAll.checked;
            });
        });

        itemBoxes.forEach(function (box) {
            box.addEventListener("change", function () {
                checkAll.checked = itemBoxes.length > 0 && itemBoxes.every(function (item) { return item.checked; });
            });
        });
    });

    document.querySelectorAll(".taModal form, .taPageContent form, .page-content form").forEach(function(form) {
        form.querySelectorAll("input[required], select[required], textarea[required]").forEach(function(field) {
            const row = field.closest(".form-row, .modal-form-group, td, div");
            if (!row) return;
            const label = row.querySelector("label");
            if (label) label.classList.add("taRequiredLabel");
        });

        form.querySelectorAll("input[type='date']").forEach(function(input) {
            if (!input.readOnly && !input.disabled && !input.value && input.closest('.taModal')) {
                const today = new Date();
                const yyyy = today.getFullYear();
                const mm = String(today.getMonth() + 1).padStart(2, "0");
                const dd = String(today.getDate()).padStart(2, "0");
                input.value = yyyy + "-" + mm + "-" + dd;
            }
        });
    });

    document.querySelectorAll(".taLocalSearchForm").forEach(function (searchForm) {
        const tableId = searchForm.getAttribute("data-table-id");
        const table = tableId ? document.getElementById(tableId) : null;
        if (!table) return;

        const tbody = table.querySelector("tbody");
        const keywordInput = searchForm.querySelector("input[name='keyword']");
        const searchTypeSelect = searchForm.querySelector("select[name='searchType']");
        const resetBtn = searchForm.querySelector(".taSearchReset");
        if (!tbody || !keywordInput || !searchTypeSelect) return;

        function getDataRows() {
            return Array.from(tbody.querySelectorAll("tr")).filter(function (row) {
                return !row.classList.contains("taSearchEmptyRow");
            });
        }

        function getOrCreateEmptyRow() {
            let emptyRow = tbody.querySelector(".taSearchEmptyRow");
            if (!emptyRow) {
                emptyRow = document.createElement("tr");
                emptyRow.className = "taTableBodyRow taSearchEmptyRow";
                emptyRow.style.display = "none";
                const td = document.createElement("td");
                td.className = "taTableBodyCell taLastCol";
                td.colSpan = table.querySelectorAll("thead th").length || 1;
                td.style.textAlign = "center";
                td.textContent = "검색 결과가 없습니다.";
                emptyRow.appendChild(td);
                tbody.appendChild(emptyRow);
            }
            return emptyRow;
        }

        function applyLocalSearch() {
            const keyword = (keywordInput.value || "").trim().toLowerCase();
            const searchType = searchTypeSelect.value || "all";
            const rows = getDataRows();
            const emptyRow = getOrCreateEmptyRow();
            let visibleCount = 0;

            rows.forEach(function (row) {
                const targetCell = searchType === "all" ? null : row.querySelector("[data-search-key='" + searchType + "']");
                const text = searchType === "all"
                    ? row.textContent.toLowerCase()
                    : ((targetCell && targetCell.textContent) || "").toLowerCase();

                const matched = !keyword || text.indexOf(keyword) > -1;
                row.style.display = matched ? "" : "none";
                if (matched) visibleCount++;
            });

            emptyRow.style.display = visibleCount === 0 ? "" : "none";
        }

        searchForm.addEventListener("submit", function (e) {
            e.preventDefault();
            applyLocalSearch();
        });

        if (resetBtn) {
            resetBtn.addEventListener("click", function () {
                keywordInput.value = "";
                searchTypeSelect.value = "all";
                applyLocalSearch();
            });
        }

        keywordInput.addEventListener("search", applyLocalSearch);
    });

});
