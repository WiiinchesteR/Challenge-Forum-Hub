create table topicos (

    id bigint not null auto_increment,
    autor varchar(100) not null,
    titulo varchar(100) not null unique,
    mensagem varchar(255) not null unique,
    curso varchar(100) not null,

    primary key(id)

);