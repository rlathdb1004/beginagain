<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <!-- 예시 버튼 -->
    <button type="button" class="btn-secondary" onclick="openUpdateModal('updateModal', {
            id: '1',
            name: '황산코발트',
            code: 'RM-CO-001',
            useYn: 'Y',
            remark: '원자재'
        })">
        수정
    </button>

    <div id="updateModal" class="modal-overlay">
        <div class="modal-box">
            <div class="modal-header">
                <span class="modal-title">수정</span>
                <span class="modal-close" onclick="closeModal('updateModal')">&times;</span>
            </div>

            <form action="${pageContext.request.contextPath}/sample/update" method="post">
                <input type="hidden" name="id">

                <div class="modal-body">
                    <div class="modal-form-group">
                        <label for="updName">이름</label>
                        <input type="text" id="updName" name="name" required>
                    </div>

                    <div class="modal-form-group">
                        <label for="updCode">코드</label>
                        <input type="text" id="updCode" name="code" required>
                    </div>

                    <div class="modal-form-group">
                        <label for="updUseYn">사용여부</label>
                        <select id="updUseYn" name="useYn">
                            <option value="Y">Y</option>
                            <option value="N">N</option>
                        </select>
                    </div>

                    <div class="modal-form-group">
                        <label for="updRemark">비고</label>
                        <textarea id="updRemark" name="remark"></textarea>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn-secondary" onclick="closeModal('updateModal')">
                        취소
                    </button>
                    <button type="submit" class="btn-primary">
                        수정
                    </button>
                </div>
            </form>
        </div>
    </div>