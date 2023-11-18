drop database if exists chronicles_test;
create database chronicles_test;
use chronicles_test;


create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    email varchar (100) not null unique,
    password_hash varchar(2048) not null,
    enabled bit not null default(1),
	color varchar(75) null,
    avatar varchar(250) null
);

create table app_role (
    app_role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);

create table board_game (
	board_game_id int primary key auto_increment,
    board_game_name varchar(100) not null,
    height int not null,
    board_slots int not null,
    places_per_board int not null,
    skin varchar (250) null
    );
    

create table board (
	board_id int primary key auto_increment,
    board_game_id int not null,
    category varchar(50) not null,
    board_name varchar(75) not null, 
    board_skin varchar (250) null,
    constraint fk_board_board_game_id
        foreign key (board_game_id)
        references board_game(board_game_id)
);

create table figure (
	figure_id int primary key auto_increment,
    board_game_id int not null,
    category varchar(50) not null,
    figure_name varchar(75) not null, 
    figure_skin varchar (250) null,
	color varchar(50) not null,
    scale decimal (2,1) not null,
    constraint fk_figure_board_game_id
        foreign key (board_game_id)
        references board_game(board_game_id)
);

create table die (
	die_id int primary key auto_increment,
    board_game_id int not null,
    category varchar(50) not null,
    die_name varchar(75) not null, 
    color varchar(50) not null,
	background varchar(50) not null,
    side1 varchar(5) null, 
	side2 varchar(5) null, 
    side3 varchar(5) null, 
    side4 varchar(5) null, 
    side5 varchar(5) null, 
    side6 varchar(5) null, 
	rolling bit not null default(0),
    winning_side varchar(5) null,
	constraint fk_die_board_game_id
        foreign key (board_game_id)
        references board_game(board_game_id)
);

create table card (
	card_id int primary key auto_increment,
    board_game_id int not null,
    category varchar(50) not null,
    card_name varchar(75) not null, 
    card_type varchar(75) not null, 
	card_front varchar (250) null,
	card_back varchar (250) null,
	card_show bit not null default(0),
    card_text varchar (500) not null,
    in_hand int null,
    card_variables varchar(500) null,
	constraint fk_card_board_game_id
        foreign key (board_game_id)
        references board_game(board_game_id)
);

create table game_instance (
	game_instance_id int primary key auto_increment,
    board_game_id int not null,
    save_state mediumtext null,
    log text null,
    constraint fk_game_instance_board_game_id
        foreign key (board_game_id)
        references board_game(board_game_id)
);

create table app_user_game_instance (
	game_instance_id int not null,
    app_user_id int not null,
    primary key (game_instance_id, app_user_id),
    constraint fk_app_user_game_instance_game_instance_id
        foreign key (game_instance_id)
        references game_instance(game_instance_id),
    constraint fk_app_user_game_instance_user_id
        foreign key (app_user_id)
        references app_user(app_user_id)
);

delimiter //
create procedure set_known_good_state()
begin
    
    delete from board;
    alter table board auto_increment = 1;
    
    delete from card;
    alter table card auto_increment = 1;
    
    delete from die;
    alter table die auto_increment = 1;
    
    delete from figure;
    alter table figure auto_increment = 1;
    
    
	delete from app_user_game_instance;
    alter table app_user_game_instance auto_increment = 1;
    
    delete from game_instance;
    alter table game_instance auto_increment = 1;
    

	delete from app_user_role;
    delete from app_role;
    alter table app_role auto_increment = 1;
    delete from app_user;
    alter table app_user auto_increment = 1;
    
    delete from board_game;
    alter table board_game auto_increment = 1;
    
    
	



	insert into app_role (`name`) values
		('TEST_ROLE_1'),
		('TEST_ROLE_2');

	-- passwords are set to "P@ssw0rd!"
	insert into app_user (username, email, password_hash, enabled, color, avatar)
		values
		('username_1', 'appuser1@app.com', 'password_hash_1', 1, 'color_1', 'avatar_1'),
		('username_2', 'appuser2@app.com', 'password_hash_2', 1, 'color_2', 'avatar_2');

	insert into app_user_role
		values
		(1, 1),
		(2, 2);
        
	insert into board_game (board_game_name, height, board_slots, places_per_board)
		values 
		('name_1', 801, 1, 1),
		('name_2', 802, 2, 2);
        
	
	insert into board (board_game_id, category, board_name, board_skin)
		values
		(1, 'category_1', 'name_1', 'skin_1'),
		(2, 'category_2', 'name_2', 'skin_2');
        
	insert into card (board_game_id, category, card_name, card_type, card_front, card_back, card_show, card_text, in_hand, card_variables)
		values 
        (1, 'category_1', 'name_1', 'type_1', 'front_1', 'back_1', 0, 'text_1', 0, 'variables_1'),
		(2, 'category_1', 'name_2', 'type_2', 'front_2', 'back_2', 0, 'text_2', 0, 'variables_2');
        
	insert into die (board_game_id, category, die_name, color, background, side1, side2, side3, side4, side5, side6, rolling, winning_side)
		values
		 (1, 'category_1', 'name_1', 'color_1', 'background_1', null, null, null, null, null, null, 0, null),
		 (2, 'category_2', 'name_2', 'color_2', 'background_2', null, null, null, null, null, null, 0, null);
         
	insert into figure (board_game_id, category, figure_name, figure_skin, color, scale)
		values
		(1, 'category_1', 'name_1', 'skin_1', 'color', 1.0),
		(2, 'category_2', 'name_2', 'skin_2', 'color', 1.0);
        
	insert into game_instance (board_game_id, save_state, log)
		values
        (1, 'save_state_1', 'log_1'),
		(1, 'save_state_2', 'log_2');
        
	insert into app_user_game_instance
		values
        (1, 1),
        (1, 2);




end//
delimiter ;