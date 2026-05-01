create table if not exists member
(
    id               bigint       not null primary key auto_increment comment '회원 ID (대리키)',
    name             varchar(50)  not null comment '회원 이름',
    email_address    varchar(150) not null comment '이메일 주소',
    phone_number     varchar(20)  not null comment '핸드폰 번호',
    encoded_password varchar(150) not null comment '비밀번호 (해시)',
    role             varchar(50)  not null comment '회원 유형 (강사, 수강생)',

    created_at       datetime     not null default current_timestamp comment '회원 생성 시간 (시스템 기준)',
    updated_at       datetime     not null default current_timestamp on update current_timestamp comment '수정된 시간',

    UNIQUE KEY uk_member_email_address (email_address),
    UNIQUE KEY uk_member_phone_number (phone_number)
) comment '회원 테이블';

create table if not exists course
(
    id            bigint       not null primary key auto_increment comment '강의 ID (대리키)',
    title         varchar(200) not null comment '강의명',
    capacity      int          not null comment '최대 수강 인원',
    price         bigint      not null comment '가격',
    instructor_id bigint       not null comment '강사 ID',
    enroll_count  int          not null default 0 comment '신청자 수 (역정규화)',

    created_at    datetime     not null default current_timestamp comment '강의 생성 시간 (시스템 기준)',
    updated_at    datetime     not null default current_timestamp on update current_timestamp comment '수정된 시간',

    CONSTRAINT fk_course_instructor_id FOREIGN KEY (instructor_id) REFERENCES member (id),

    KEY idx_course_created_at (created_at desc),
    KEY idx_course_enroll_count (enroll_count desc)
) comment '강의 테이블';

create table if not exists enrollment
(
    id          bigint   not null primary key auto_increment comment '수강 ID (대리키)',
    member_id   bigint   not null comment '수강 회원 ID',
    course_id   bigint   not null comment '수강 강의 ID',

    enrolled_at datetime not null default current_timestamp comment '회원이 강의를 수강 신청한 시간',
    created_at  datetime not null default current_timestamp comment '수강 신청 생성 시간 (시스템 기준)',
    updated_at  datetime not null default current_timestamp on update current_timestamp comment '수정된 시간',

    CONSTRAINT fk_enrollment_member_id FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_enrollment_course_id FOREIGN KEY (course_id) REFERENCES course (id)
) comment '수강 테이블';