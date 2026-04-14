document.addEventListener("DOMContentLoaded", function () {

    const itemType = document.getElementById("itemType");
    const dateType = document.getElementById("dateType");
    const startDate = document.getElementById("startDate");
    const endDate = document.getElementById("endDate");
    const searchField = document.getElementById("searchField");
    const keyword = document.getElementById("keyword");

    const table = document.getElementById("table");

    // 공통 조회 함수
    function loadData() {

        const params = new URLSearchParams({
            type: itemType.value,
            dateType: dateType.value,
            startDate: startDate.value,
            endDate: endDate.value,
            field: searchField.value,
            keyword: keyword.value
        });

        console.log(params.toString());

        fetch(`/beginagain/item-search?${params.toString()}`)
            .then(res => {
                if (!res.ok) throw new Error("서버 오류");
                return res.json();
            })
            .then(data => {
                renderTable(data);
            })
            .catch(err => {
                console.error(err);
            });
    }

    // 테이블 렌더링
    function renderTable(data) {

        let html = `
            <tr>
                <th>품목 ID</th>
                <th>품목 코드</th>
                <th>품목명</th>
                <th>품목 구분</th>
                <th>단위</th>
                <th>규격</th>
                <th>공급업체</th>
                <th>안전재고</th>
                <th>사용 여부</th>
                <th>비고</th>
                <th>등록일</th>
                <th>수정일</th>
            </tr>
        `;

        if (data.length === 0) {
            html += `<tr><td colspan="12">데이터 없음</td></tr>`;
        } else {
            data.forEach(item => {
                html += `
                    <tr>
                        <td>${item.item_id}</td>
                        <td>${item.item_code}</td>
                        <td>${item.item_name}</td>
                        <td>${item.item_type}</td>
                        <td>${item.unit}</td>
                        <td>${item.spec}</td>
                        <td>${item.supplier_name}</td>
                        <td>${item.safety_stock}</td>
                        <td>${item.use_yn}</td>
                        <td>${item.remark}</td>
                        <td>${item.created_at}</td>
                        <td>${item.updated_at}</td>
                    </tr>
                `;
            });
        }

        table.innerHTML = html;
    }

    // 분류 변경
    itemType.addEventListener("change", loadData);

    // 날짜 변경
    startDate.addEventListener("change", loadData);
    endDate.addEventListener("change", loadData);

    // 검색 
    keyword.addEventListener("keyup", evt => {
        loadData();
    });

    // 🔍 아이콘 클릭
    document.querySelector(".search-box .icon").addEventListener("click", loadData);

    //등록 버튼
    document.querySelector(".taBtnPrimary").addEventListener("click", evt => {
        document.querySelector(".modal-overlay").style.display = "flex"
    })

    //모달창 등록 버튼
    document.querySelector(".add").addEventListener("click", evt => {

        const form = document.querySelector(".modal-form-group");

        params.append("item_code", form.item_code.value);
        params.append("item_name", form.item_name.value);
        params.append("item_type", form.item_type.value);
        params.append("unit", form.unit.value);
        params.append("spec", form.spec.value);
        params.append("supplier_name", form.supplier_name.value);
        params.append("safety_stock", form.safety_stock.value);

        fetch("/master-item", {
            method: "POST",
            body: params
        })
            .then(res => res.text())
            .then(result => {
                document.querySelector(".modal-overlay").style.display = "none"
                document.querySelector(".modal-form-group").reset()
                location.reload();
            })
            .catch(err => console.error(err));
    });

    //모달창 닫기
    document.querySelector(".modal-close").addEventListener("click", evt => {
        document.querySelector(".modal-overlay").style.display = "none"
        document.querySelector(".modal-form-group").reset();
    })
});