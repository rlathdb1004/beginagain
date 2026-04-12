<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <button type="button" class="btn-primary" onclick="openModal('registerModal')">
        등록
    </button>

    <div id="registerModal" class="modal-overlay">
        <div class="modal-box">
            <div class="modal-header">
                <span class="modal-title">등록</span>
                <span class="modal-close" onclick="closeModal('registerModal')">&times;</span>
            </div>

            <form action="${pageContext.request.contextPath}/sample/insert" method="post">
                <div class="modal-body">
                    <div class="modal-form-group">
                        <label for="regName">이름</label>
                        <input type="text" id="regName" name="name" required>
                    </div>

                    <div class="modal-form-group">
                        <label for="regCode">코드</label>
                        <input type="text" id="regCode" name="code" required>
                    </div>

                    <div class="modal-form-group">
                        <label for="regUseYn">사용여부</label>
                        <select id="regUseYn" name="useYn">
                            <option value="Y">Y</option>
                            <option value="N">N</option>
                        </select>
                    </div>

                    <div class="modal-form-group">
                        <label for="regRemark">비고</label>
                        <textarea id="regRemark" name="remark"></textarea>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn-secondary" onclick="closeModal('registerModal')">
                        취소
                    </button>
                    <button type="submit" class="btn-primary">
                        등록
                    </button>
                </div>
            </form>
        </div>
    </div>