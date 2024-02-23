DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS virtual_account;


CREATE TABLE virtual_account(
    id varchar(36) primary key,
    user_id int8,
    bank_code varchar(36),
    amount bigint,
    expired_date date,
    created_at timestamp default now(),
    updated_at timestamp default now()
--     foreign key (user_id) references users(id)
);

CREATE TABLE payment(
    id varchar(36) primary key,
--     no_kwitansi varchar(100),
    tipe_pembayaran varchar(50),
    tanggal_pembayaran date,
    jumlah_bayar bigint,
    image varchar(100),
    status varchar(20),
    siswa_id int8,
    virtual_account_id varchar(36),
    created_at timestamp default now(),
    updated_at timestamp default now(),
    foreign key (virtual_account_id) references virtual_account(id)
);

