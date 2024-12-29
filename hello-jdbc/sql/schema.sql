DROP TABLE member IF EXISTS CASCADE;
CREATE TABLE member (
    member_id   VARCHAR(10),
    money       INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY(member_id)
);

-- 임시 데이터
INSERT INTO member(member_id, money) VALUES ('hi1', 10000);
INSERT INTO member(member_id, money) VALUES ('hi2', 20000);

-- 자동 커밋 설정
SET autocommit TRUE;
-- 수동 커밋 설정
SET autocommint FALSE;