create table book (
    guid varchar(36) not null,
    title varchar(255) not null,
    year_of_publication integer not null,
    created_by varchar(36) not null,
    primary key (guid)
);

create table person (
    guid varchar(36) not null,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(255) not null,
    primary key (guid)
);

create table rating (
    guid varchar(36) not null,
    grade integer not null,
    rated_book varchar(36) not null,
    rated_by varchar(36) not null,
    primary key (guid)
);

alter table if exists person 
    drop constraint if exists UK_fwmwi44u55bo4rvwsv0cln012;

alter table if exists person 
    add constraint UK_fwmwi44u55bo4rvwsv0cln012 unique (email);
 
alter table if exists book 
    add constraint FK4bu9lx7jwmbh2apm6ae1mxe5j 
    foreign key (created_by) 
    references person;

alter table if exists rating 
    add constraint FK5bj3crqbrpj1c2q5ij1dkdvvi 
    foreign key (rated_book) 
    references book;

alter table if exists rating 
    add constraint FKq4rtdcbk1vpmsxo0qvhp3iic9 
    foreign key (rated_by) 
    references person;

insert 
    into
        person
        (email, name, password, guid) 
    values
        ('marcelo@example.com', 'Marcelo Teixeira', '$2a$10$6jN0.NqnvcOFsPnbNig.BOVm4RWVWIbGAdWVuBmgFY8DprcucOwc.', '18a114b0-502f-438c-836e-cad4757eecf4');

insert 
    into
        person
        (email, name, password, guid) 
    values
        ('teste@example.com', 'Teste', '$2a$10$6jN0.NqnvcOFsPnbNig.BOVm4RWVWIbGAdWVuBmgFY8DprcucOwc.', '18a124b0-502f-438c-836e-cad4757eecf4');

insert 
    into
        book
        (created_by, title, year_of_publication, guid) 
    values
        ('18a114b0-502f-438c-836e-cad4757eecf4', 'Padrões de Projetos', 2000, '700aa2ed-da4c-4438-8263-b89e73432359');


