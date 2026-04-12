function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = "flex";
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = "none";
    }
}

/* 수정 모달 데이터 채우기용 */
function openUpdateModal(modalId, data) {
    const modal = document.getElementById(modalId);
    if (!modal) return;

    for (const key in data) {
        const input = modal.querySelector(`[name="${key}"]`);
        if (input) {
            input.value = data[key] ?? "";
        }
    }

    modal.style.display = "flex";
}

/* 바깥 클릭하면 닫기 */
document.addEventListener("click", function (e) {
    if (e.target.classList.contains("modal-overlay")) {
        e.target.style.display = "none";
    }
});