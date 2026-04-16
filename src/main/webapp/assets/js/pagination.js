function paMovePage(paPageValue) {
    var paSearchForm =
        document.getElementById("paSearchForm") ||
        document.querySelector(".taLocalSearchForm") ||
        document.querySelector('form[method="get"]');

    if (!paSearchForm) {
        return;
    }

    var paPageInput =
        document.getElementById("paPage") ||
        paSearchForm.querySelector('input[name="page"]');

    if (!paPageInput) {
        paPageInput = document.createElement("input");
        paPageInput.type = "hidden";
        paPageInput.name = "page";
        paPageInput.id = "paPage";
        paSearchForm.appendChild(paPageInput);
    }

    paPageInput.value = paPageValue;
    sessionStorage.setItem("paMoveToTable", "Y");
    paSearchForm.submit();
}

document.addEventListener("DOMContentLoaded", function () {
    var paMoveFlag = sessionStorage.getItem("paMoveToTable");

    if (paMoveFlag === "Y") {
        var paTableBox =
            document.getElementById("paTableBox") ||
            document.querySelector(".taTableShell");

        if (paTableBox) {
            paTableBox.scrollIntoView({ behavior: "auto", block: "start" });
        }

        sessionStorage.removeItem("paMoveToTable");
    }
});