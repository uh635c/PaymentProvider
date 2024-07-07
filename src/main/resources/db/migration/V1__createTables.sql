CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table merchants
(
    id         varchar(36) primary key default uuid_generate_v4(),
    name       varchar(50)  not null unique,
    secret_key varchar(100) not null,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    status     varchar(10)  not null
);

insert into merchants(id, name, secret_key, created_at, updated_at, status)
values ('f4c55d34-355d-445a-8ad3-9d61042d88d0', 'merchant1',
        '$2a$10$0APXRHr/McLp7F5qqxixTu/JhXkOKjTZt0eipA/bc/Zb7AgWaPyFO', '2024-03-01', '2024-03-01', 'ACTIVE');

create table accounts
(
    id          varchar(36) primary key default uuid_generate_v4(),
    currency    varchar(50) not null,
    balance     int         not null,
    created_at  timestamp   not null,
    updated_at  timestamp   not null,
    status      varchar(10) not null,
    merchant_id varchar(36) not null,
    foreign key (merchant_id) references merchants (id)
);

insert into accounts(id, currency, balance, created_at, updated_at, status, merchant_id)
values ('9677f7a4-3245-4a2d-bb80-ddaefbd0038c','RUB', 50000, '2024-03-01', '2024-03-01', 'ACTIVE', 'f4c55d34-355d-445a-8ad3-9d61042d88d0');

create table customers
(
    id         varchar(36) primary key default uuid_generate_v4(),
    first_name varchar(50) not null,
    last_name  varchar(50) not null,
    country    varchar(50) not null,
    created_at timestamp   not null,
    updated_at timestamp   not null,
    status     varchar(10) not null
);

insert into customers(id, first_name, last_name, country, created_at, updated_at, status)
values ('b045d21f-9618-4b5b-8dd5-2a937968508f', 'John', 'Doe', 'RUSSIA', '2024-03-01', '2024-03-01', 'ACTIVE');

create table cards
(
    id              varchar(36) primary key default uuid_generate_v4(),
    card_number     bigint      not null,
    expiration_date date        not null,
    cvv             int         not null,
    currency        varchar(50) not null,
    customer_id     varchar(36) not null,
    created_at      timestamp   not null,
    updated_at      timestamp   not null,
    status          varchar(10) not null,
    foreign key (customer_id) references customers (id)
);

insert into cards(id, card_number, expiration_date, cvv, currency, customer_id, created_at, updated_at, status)
values ('8ca3a0de-324a-43b2-a69b-b167deb5c861', 4102778822334893, '2024-03-01', 555, 'RUB', 'b045d21f-9618-4b5b-8dd5-2a937968508f', '2024-03-01', '2024-03-01', 'ACTIVE');

create table transactions
(
    id             varchar(36) primary key default uuid_generate_v4(),
    account_id     varchar(36)  not null,
    created_at     timestamp    not null,
    updated_at     timestamp    not null,
    status         varchar(50)  not null,
    type           varchar(50)  not null,
    amount         int          not null,
    card_id        varchar(36)  not null,
    language       varchar(50)  not null,
    url            varchar(100) not null,
    payment_method varchar(50)  not null,
    foreign key (card_id) references cards (id),
    foreign key (account_id) references accounts (id)
);

insert into transactions(id, account_id, created_at, updated_at, status, type, amount, card_id, language, url, payment_method)
values('6e1ee036-3b44-4cb8-b82d-f55906d1bfa4', '9677f7a4-3245-4a2d-bb80-ddaefbd0038c', '2024-03-01', '2024-03-01',
       'IN_PROGRESS', 'TOPUP', 500, '8ca3a0de-324a-43b2-a69b-b167deb5c861', 'ENGLISH', 'https://proselyte.net/webhook/transaction','CARD');

create table notifications
(
    id             varchar(36) primary key default uuid_generate_v4(),
    attempt        int          not null,
    url            varchar(500) not null,
    created_at     timestamp    not null,
    transaction_id varchar(36)  not null,
    message        varchar(500) not null,
    response_code  int          not null,
    response_body  varchar(500),
    foreign key (transaction_id) references transactions (id)
);
