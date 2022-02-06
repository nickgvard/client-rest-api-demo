create table logbook (
    id bigint primary key generated always as identity,
    company_id bigint not null,
    old_price float4,
    current_price float4,
    foreign key (company_id) references companies(id) on delete cascade on update cascade
)