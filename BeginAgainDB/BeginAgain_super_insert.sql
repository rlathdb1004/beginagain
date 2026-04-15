-- 테스트용 최초변경X
INSERT INTO EMP (
    EMP_ID,
    EMP_NO,
    EMP_NAME,
    PASSWORD_HASH,
    DEPT_CODE,
    POSITION_NAME,
    EMAIL,
    PHONE,
    STATUS,
    ROLE_NAME,
    USE_YN,
    TEMP_PWD_YN,
    REMARK,
    CREATED_AT,
    UPDATED_AT
) VALUES (
    SEQ_EMP.NEXTVAL,
    'test',
    '테스트관리자',
    '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
    'ADMIN',
    '관리자',
    'test@mes.com',
    '010-0000-0000',
    'ACTIVE',
    'ADMIN',
    'Y',
    'N',
    '슈퍼계정',
    SYSDATE,
    SYSDATE
);

-- 테스트용 최초변경O
INSERT INTO EMP (
    EMP_ID,
    EMP_NO,
    EMP_NAME,
    PASSWORD_HASH,
    DEPT_CODE,
    POSITION_NAME,
    EMAIL,
    PHONE,
    STATUS,
    ROLE_NAME,
    USE_YN,
    TEMP_PWD_YN,
    REMARK,
    CREATED_AT,
    UPDATED_AT
) VALUES (
    SEQ_EMP.NEXTVAL,
    'test2',
    '초기비밀번호계정',
    '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
    'USER',
    '사원',
    'test2@mes.com',
    '010-1111-1111',
    'ACTIVE',
    'USER',
    'Y',
    'Y',
    '초기비밀번호테스트',
    SYSDATE,
    SYSDATE
);

COMMIT;