create table if not exists member (
    id bigint not null primary key auto_increment comment '회원 ID (대리키)',
    name varchar(50) not null comment '회원 이름',
    email_address varchar(150) not null comment '이메일 주소',
    phone_number varchar(20) not null comment '핸드폰 번호',
    encoded_password varchar(150) not null comment '비밀번호 (해시)',
    role varchar(50) not null comment '회원 유형 (강사, 수강생)',

    UNIQUE KEY uk_member_email_address (email_address),
    UNIQUE KEY uk_member_phone_number (phone_number)
) comment '회원 테이블';