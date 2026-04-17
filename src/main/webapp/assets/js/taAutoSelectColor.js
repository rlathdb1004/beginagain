document.addEventListener("DOMContentLoaded", function () {
    function syncSelectColor(selectEl) {
        if (!selectEl) return;

        const selectedOption = selectEl.options[selectEl.selectedIndex];
        const isPlaceholder = selectedOption && selectedOption.hidden && selectEl.value === "";

        if (isPlaceholder || selectEl.value === "") {
            selectEl.classList.add("taSelectPlaceholder");
        } else {
            selectEl.classList.remove("taSelectPlaceholder");
        }
    }

    const autoSelects = document.querySelectorAll(".taAutoSelectColor");
    autoSelects.forEach(function (selectEl) {
        syncSelectColor(selectEl);
    });

    document.addEventListener("change", function (e) {
        const target = e.target;
        if (target.classList && target.classList.contains("taAutoSelectColor")) {
            syncSelectColor(target);
        }
    });
});