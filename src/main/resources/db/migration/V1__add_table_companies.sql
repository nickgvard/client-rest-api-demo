create table companies (
    id bigint primary key generated always as identity,
    symbol varchar(50) not null,
    is_enabled smallint not null,
    previous_volume int,
    volume int,
    latest_price float4
)