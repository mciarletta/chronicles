drop database if exists chronicles;
create database chronicles;
use chronicles;


create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    email varchar (100) not null unique,
    password_hash varchar(2048) not null,
    enabled bit not null default(1),
	color varchar(50) null,
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



insert into app_role (`name`) values
    ('USER'),
    ('ADMIN');

-- passwords are set to "P@ssw0rd!"
insert into app_user (username, email, password_hash, enabled, color, avatar)
    values
    ('HazMat', 'matt@gmail.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1, '#10501d', 'https://i.pinimg.com/originals/4d/86/5e/4d865ea47a8675d682ff35ad904a0af6.png'),
    ('LisaBella', 'lisa@msn.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1, '#7a1a1a', 'https://www.fightersgeneration.com/characters4/taki-head-hd.jpg'),
	('Dan-the-man', 'dan@gmail.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1, '#000099', null);


insert into app_user_role
    values
    (1, 2),
    (2, 1),
    (3, 1);

insert into board_game (board_game_name, height, board_slots, places_per_board, skin)
	values
    ('scrample', 800, 1, 225, null),
    ('Mutant Chroniples', 800, 12, 64, 'https://capstone-stuff.s3.us-east-2.amazonaws.com/mutantBox.png');
    

insert into board (board_game_id, category, board_name, board_skin)
	values
	(1, 'boards', 'game board', 'https://i.pinimg.com/originals/9a/91/ab/9a91abcf38624a17c3b158a56eaa7e84.jpg'),
    (2, 'boards', 'sector 1', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/sector1.png'),
    (2, 'boards', 'sector 2', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/sector2.png'),
	(2, 'boards', 'sector 3', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/sector3.png'),
    (2, 'boards', 'sector 4', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/sector4.png'),
    (2, 'boards', 'sector 5', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/sector5.png'),
    (2, 'boards', 'sector 6', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/sector6.png'),
    (2, 'boards', 'sector 7', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/sector7.png'),
    (2, 'boards', 'sector 8', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/sector8.png');

    
insert into figure (board_game_id, category, figure_name, figure_skin, color, scale)
	values
    (1, 'figures', 'a', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2Xb9HxZ4SEvQ_DPtmnMjLjd26L0copwd4h_XcPRK3yr9ydCbzl99s7uc4nYCf4ykIiLM&usqp=CAU', '#663300', 1.0),
	(1, 'figures', 'b', 'https://slidetiles.com/cdn/shop/products/wooden-scrabble-letter-b_thumb-1-tiles-8x8.png?v=1607659075', '#663300', 1.0),
	(1, 'figures', 'c', 'https://slidetiles.com/cdn/shop/products/wooden-scrabble-letter-c_thumb-1-tiles-8x8_300x.png?v=1607659089', '#663300', 1.0),
    (2, 'figures', 'Tatsu', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/tatsu.png', '#990000', 1.0),
	(2, 'figures', 'Yojimbo', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/yojimbo.png', '#990000', 1.0),
    ('2', 'figures', 'Atilla III', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/attillaiii.png', '#1d1593', '1.0'),
	('2', 'figures', 'Coral Beach', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/coralbeach.png', '#1d1593', '1.0'),
	('2', 'figures', 'Gallagher', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/gallagher.png', '#076305', '1.0'),
	('2', 'figures', 'Murdoch', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/murdoch.png', '#076305', '1.0'),
	('2', 'figures', 'Big Bob', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/bigbob.png', '#9c9c9c', '1.0'),
	('2', 'figures', 'Hunter', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/hunter.png', '#9c9c9c', '1.0'),
    ('2', 'figures', 'Centaurion', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/centaurion.png', '#c40e0e', '1.0'),
    ('2', 'figures', 'Centaurion', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/centaurion.png', '#c40e0e', '1.0'),
    ('2', 'figures', 'Centaurion', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/centaurion.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Necromutant', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/necromutant.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Necromutant', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/necromutant.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Necromutant', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/necromutant.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Necromutant', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/necromutant.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Necromutant', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/necromutant.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Necromutant', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/necromutant.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Necromutant', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/necromutant.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Necromutant', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/necromutant.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Necromutant', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/necromutant.png', '#c40e0e', '1.0'),
	('2', 'figures', 'Ezoghoul', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/ezoghoul.png', '#0a0800', '2.0'),
	('2', 'figures', 'Nepharite', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/nepharite.png', '#0a0800', '1.5'),
	('2', 'figures', 'Razide', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/razide.png', '#0a0800', '1.2'),
	('2', 'figures', 'Razide', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/razide.png', '#0a0800', '1.2'),
	('2', 'figures', 'Target', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/target.png', '#10e7eb', '1'),
	('2', 'figures', 'Target', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/target.png', '#10e7eb', '1'),
	('2', 'figures', 'Steiner', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/steiner.png', '#070808', '1.0'),
	('2', 'figures', 'Valerie', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/valerie.png', '#070808', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0'),
	('2', 'figures', 'Legionnaire', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/figures/legionnaire.png', '#18750f', '1.0');


    
insert into card (board_game_id, category, card_name, card_type, card_front, card_back, card_text, card_variables)
	values
	(2, 'cards', 'Cybernetic Power Arm', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/cyberneticPowerArm.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'Your arm is replaced by a cybernetic arm, more powerful than the orginal. It is connected to your neural system and functions like a normal arm. Effect: Add 1 die to your close combat attacke rolls. You may also re-roll one attack die in each close combat attack. Cost: 2', null),
	(2, 'cards', 'Coagulant Auto-Injector', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/coagulantAutoInjector.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This medical item is saved for the most desperate situations where it can save the life of a Doomtrooper. Roll 3 red dice. For every "hit" one wound is removed. Discard after use. Cost: 1.', null),
	(2, 'cards', 'Deathlockdrum', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/deathlockdrum.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This heavy weapon fires high-powered ammo and is feared for its superior ability to destroy antyhing within its range. Close Combat: 1 die. Fire Combat: 4 dice. Rank Required: 2. Cost: 3.', null),
	(2, 'cards', 'Deathlockdrum', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/deathlockdrum.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This heavy weapon fires high-powered ammo and is feared for its superior ability to destroy antyhing within its range. Close Combat: 1 die. Fire Combat: 4 dice. Rank Required: 2. Cost: 3.', null),
	(2, 'cards', 'Deathlockdrum', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/deathlockdrum.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This heavy weapon fires high-powered ammo and is feared for its superior ability to destroy antyhing within its range. Close Combat: 1 die. Fire Combat: 4 dice. Rank Required: 2. Cost: 3.', null),
	(2, 'cards', 'Disruptor', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/disruptor.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'A fearsome weapon it is placed underneath the barrel of any firearm and alters the atomic structures of targets, causing severe wounds. Effect: All targets get -1 to their Armor. Cost: 3.', null),
	(2, 'cards', 'Explosive Ammo', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/explosiveAmmo.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'You have been issued explosive ammo to inflict even more wounds on teh Dark Legion hordes. Effect: Add 1 die to your Doomtroopers firearm attacks. Cost: 2', null),
	(2, 'cards', 'Gehenna Puker', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/gehennaPuker.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This high-tech flamethrower spews out a cloud of ignited plasma that reduces everything in its path to a sizzling cinder. See special rules in the Rules Book. Close Combat: 1 die. Fire Combat: 3 dice. Rank Required: 2. Cost: 2', null),
	(2, 'cards', 'Gehenna Puker', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/gehennaPuker.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This high-tech flamethrower spews out a cloud of ignited plasma that reduces everything in its path to a sizzling cinder. See special rules in the Rules Book. Close Combat: 1 die. Fire Combat: 3 dice. Rank Required: 2. Cost: 2', null),
	(2, 'cards', 'Grenade Launcher', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/grenadeLauncher.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'Mounted under your standard firearm the launcher delivers a powerful punch to your attacks. May only be mounted on a Plasma Carbine. Effect: Attacks with 3 red dice and a special effect (see Rules Book). Cost: 2', null),
	(2, 'cards', 'Laser Sight', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/laserSight.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The sight projects a red dot on your target, directing your fire. Can be mounted on any missile weapon. Effect: You may re-roll 1 attack die in every firearm attack. Cost: 1', null),
	(2, 'cards', 'Molecular Phaser', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/molecularPhaser.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'A prototype molecular phaser, released for field testing. Effect: Your unit may pass through walls during this combat round. You may not move between different levels in the Stairs sector. Discard after use.', null),
	(2, 'cards', 'Plasma Carbine', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/plasmaCarbine.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This reliable carbine is the standard firearm for the Doomtroopers. Close Combat: 1 die. Fire Combat: 3 dice. Rank Required: 1. Cost: 0', null),
	(2, 'cards', 'Plasma Carbine', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/plasmaCarbine.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This reliable carbine is the standard firearm for the Doomtroopers. Close Combat: 1 die. Fire Combat: 3 dice. Rank Required: 1. Cost: 0', null),
	(2, 'cards', 'Plasma Carbine', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/plasmaCarbine.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This reliable carbine is the standard firearm for the Doomtroopers. Close Combat: 1 die. Fire Combat: 3 dice. Rank Required: 1. Cost: 0', null),
	(2, 'cards', 'Plasma Carbine', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/plasmaCarbine.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This reliable carbine is the standard firearm for the Doomtroopers. Close Combat: 1 die. Fire Combat: 3 dice. Rank Required: 1. Cost: 0', null),
	(2, 'cards', 'Plasma Carbine', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/plasmaCarbine.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This reliable carbine is the standard firearm for the Doomtroopers. Close Combat: 1 die. Fire Combat: 3 dice. Rank Required: 1. Cost: 0', null),
	(2, 'cards', 'Punisher Shortsword', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/punisher.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Punishwer is the standard sword of the close combat specialists. Close Combat: 3 dice. Fire Combat: 0 dice. Rank Required: 1. Cost: 0.', null),
	(2, 'cards', 'Punisher Shortsword', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/punisher.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Punishwer is the standard sword of the close combat specialists. Close Combat: 3 dice. Fire Combat: 0 dice. Rank Required: 1. Cost: 0.', null),
	(2, 'cards', 'Punisher Shortsword', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/punisher.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Punishwer is the standard sword of the close combat specialists. Close Combat: 3 dice. Fire Combat: 0 dice. Rank Required: 1. Cost: 0.', null),
	(2, 'cards', 'Punisher Shortsword', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/punisher.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Punishwer is the standard sword of the close combat specialists. Close Combat: 3 dice. Fire Combat: 0 dice. Rank Required: 1. Cost: 0.', null),
	(2, 'cards', 'Punisher Shortsword', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/punisher.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Punishwer is the standard sword of the close combat specialists. Close Combat: 3 dice. Fire Combat: 0 dice. Rank Required: 1. Cost: 0.', null),
	(2, 'cards', 'Punisher Combo', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/punisherCombo.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This effective combination of an armor-piercing sword and a reliable gun gives the Doomtrooper a deadly flexibility. Close Combat: 3 dice. Fire Combat: 3 dice. Rank Required: 2. Cost: 2.', null),
	(2, 'cards', 'Punisher Combo', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/punisherCombo.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This effective combination of an armor-piercing sword and a reliable gun gives the Doomtrooper a deadly flexibility. Close Combat: 3 dice. Fire Combat: 3 dice. Rank Required: 2. Cost: 2.', null),
	(2, 'cards', 'Punisher Combo', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/punisherCombo.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This effective combination of an armor-piercing sword and a reliable gun gives the Doomtrooper a deadly flexibility. Close Combat: 3 dice. Fire Combat: 3 dice. Rank Required: 2. Cost: 2.', null),
	(2, 'cards', 'Targeting Eye', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/targetingEye.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'Your eye has been replaced by an artificial device with a targeting function. It locks on targetst and greatly improves your chances of hitting. Effect: You may re-roll two attack dice in every fire-arm attack.', null),
	(2, 'cards', 'Combat Medic Unit', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/medic.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'This trusted Doomtrooper servant is a computer robot medic that diagnoses and treats wounds even during combat. Effect: Automatically removeds 4 wounds from the Doomtrooper. Must be used before the Doomtrooper is eliminated. Cost: 3', null),
	(2, 'cards', 'Nimrod Autocannon', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/nimrod.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'Two barrels are always better than one, and the Nimbrod is the most fearsome weapon in the Doomtrooper arsenal because of its awesome destructive power. Close Combat: 0 dice. Fire Combat: 3+3 dice. Rank Required: 4. Cost: 4', null),
	(2, 'cards', 'Nimrod Autocannon', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/nimrod.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'Two barrels are always better than one, and the Nimbrod is the most fearsome weapon in the Doomtrooper arsenal because of its awesome destructive power. Close Combat: 0 dice. Fire Combat: 3+3 dice. Rank Required: 4. Cost: 4', null),
	(2, 'cards', 'Teleportal', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/teleportal.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'A prototype teleportation unit issued for emergency use. Effect: Teleports one Doomtrooper to any sector. Discard after use. Cost: 3', null),
	(2, 'cards', 'Violator Sword', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/violator.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Violator is an electro-charged battlesword. Its lethal effect often brings down many enemies with one mightly blow. Close combat: 4 dice. Fire Combat: 0 dice. Rank Required: 3. Cost: 3', null),
	(2, 'cards', 'Violator Sword', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/violator.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Violator is an electro-charged battlesword. Its lethal effect often brings down many enemies with one mightly blow. Close combat: 4 dice. Fire Combat: 0 dice. Rank Required: 3. Cost: 3', null),
	(2, 'cards', 'Violator Sword', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/violator.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Violator is an electro-charged battlesword. Its lethal effect often brings down many enemies with one mightly blow. Close combat: 4 dice. Fire Combat: 0 dice. Rank Required: 3. Cost: 3', null),
	(2, 'cards', 'Violator Combo', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/violatorCombo.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Violator combo consists of a hyper-charged Violator sword for extra impact and a standard carbine. Close combat: 5 dice. Fire Combat: 3 dice. Rank Required: 4. Cost: 4', null),
	(2, 'cards', 'Violator Combo', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/violatorCombo.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'The Violator combo consists of a hyper-charged Violator sword for extra impact and a standard carbine. Close combat: 5 dice. Fire Combat: 3 dice. Rank Required: 4. Cost: 4', null),
	(2, 'cards', 'Command Helmet', 'Equipment', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipment/commandHelmet.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/equipmentBack.png', 'An integrated communication system enables better contact with the combat coordinators. Effect: You may spend 1 Extra Action per round during the mission. Cost: 3', null),

	(2, 'cards', 'Attack The Citadel', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/attack.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: Your superiors are worried about Legion activity in the Citadel. Your unit must disrupt the Legion. Mission: Enter the gameboard and collect 10 Promotion Points. Rewards: 2 Credits, 1 Credit to the Legion player if the missino is not accomplished.', null),
	(2, 'cards', 'Capture Alilen Sepcimen', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/capture.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: Your scientists want to study the psyche of the enemy. Capture a Legion creature, and bring it out alive. Mission: Move next to either a Centurion or Razide and drug it (automatic). It is under your control from now on, and you may move it as normal beginning with your next Combat Round. It may be attacked by all other figures on the gameboard, but cannot attack back. Bring it out alive. Rewards: 2 Credits, 1 Credit to the Legion player if the missino is not accomplished.', null),
	(2, 'cards', 'Convey Important Message', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/convey.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: The combat coordinates have placed a secret agent among the Legion troops. Contact him and convey a secret message back to the coordinators. Mission: Make your way to one of the entrances leading out from the Stairs sector (in Mission 5: substitute sector 4). End your round there and then get out again. The message is represented by this upturned card, and can be trasferred between your Heroes when they stand next to each other.', null),
	(2, 'cards', 'Destroy The Legion', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/destroy.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: Your superiors want your group to decimate the hordes of the Legion. Mission: Collect 15 Promotion Points. Rewards: 2 Credits, 1 Credit to the Legion player if the missino is not accomplished.', null),
	(2, 'cards', 'Instigate Legionnaire Mutiny', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/instigate.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: You think that you have demoralized the enemy Legionnaires to the point that they could turn against their own officers. Make sure this mutiny succeeds. Mission: Collect at least 10 Promotion Points and do not eliminate one single Legionnaire. Rewards: 2 Credits, 1 Credit to the Legion player if the missino is not accomplished.', null),
	(2, 'cards', 'Map The Citadel', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/map.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: Your Chiefs of Staff need a map of the Citadel. Your orders are to perform recon in the Citadel. Mission: Your unit must enter each sector on the game-board at least once during the mission. Rewards: 2 Credits, 1 Credit to the Legion player if the missino is not accomplished.', null),
	(2, 'cards', 'Prepare For Main Attack', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/prepare.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: Your attack upon the Citadel will be followed up by a second wave. These troops, however, are struck with Legionnaire fear. You have to clear their way. Mission: Make sure the Stairs sector is empty of Legionnaires at the end of your turn in the last Combat Round. Rewards: 2 Credits, 1 Credit to the Legion player if the missino is not accomplished.', null),
	(2, 'cards', 'Prove Your Worth', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/prove.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: Your superiors are not convinced that you have fought with true dedication the last few missions. This time they want proof that you carry your own weight. Mission: See to it that your troopers have at least 5 wounds between them, and that both are alive at end of mission. Rewards: 2 Credits, 1 Credit to the Legion player if the missino is not accomplished.', null),
	(2, 'cards', 'Replenish Collection', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/replenish.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: The pride of your corporation, your collection of Legion weapons on Luna, has been stolen. You are to eliminate Legion troops, take their arms and upply the basis for a new collection. Mission: Put enemies of at least four different types out of action. Rewards: 2 Credits, 1 Credit to the Legion player if the missino is not accomplished.', null),
	(2, 'cards', 'Support Examination', 'Secondary Mission', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/support.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/secondary/secondaryBack.png', 'Briefing: The Chiefs of Staff want to make a scientific examination of alien cell structure. Supply them with material. Mission: Move adjacent to either a Razide, a Nepharite, or an Ezoghoul and spend one action scraping cell samples from it (You cannot attack during that action). Bring the Doomtrooper out again, alive. Rewards: 2 Credits, 1 Credit to the Legion player if the missino is not accomplished.', null),

    (2, 'cards', 'Mishima Tray', 'Coorporation Tray', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/mishimia.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/coorporationTrayBack.png', 'Mishima Doomtroopers wear a light-wieght compact armor which allows them to move 4 steps per Movement Action', 'Yojimbo Hp, 0, Tatsu Hp, 0, Pomotion Points, 0, Extra Actions, 0, Credits, 0, Rank, 0'),
	(2, 'cards', 'Capitol Tray', 'Coorporation Tray', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/capitol.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/coorporationTrayBack.png', 'Capitol Doomtroopers are superior tactiticians. Start each mission with one extra Doomtrooper Card', 'Hunter Hp, 0, Big Bob Hp, 0, Pomotion Points, 0, Extra Actions, 0, Credits, 0, Rank, 0'),
	(2, 'cards', 'Imperial Tray', 'Coorporation Tray', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/imperial.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/coorporationTrayBack.png', 'Imperial Doomtroopers have a keen sense of intuition. They gain one Extra Action at the start of each othe their turns.', 'Murdoch Hp, 0, Gallagher Hp, 0, Pomotion Points, 0, Extra Actions, 0, Credits, 0, Rank, 0'),
    (2, 'cards', 'Cybertronic Tray', 'Coorporation Tray', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/cybertronic.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/coorporationTrayBack.png', 'Cybertronic Doomtroopers wear an advanced form of armor. Roll 2 dice to absorb hits.', 'Attila III Hp, 0, Coral Beach Hp, 0, Pomotion Points, 0, Extra Actions, 0, Credits, 0, Rank, 0'),
   	(2, 'cards', 'Bauhaus Tray', 'Coorporation Tray', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/bauhaus.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/coorporationTrayBack.png', 'Bauhaus Doomtroopers are crack shots. Roll 1 extra die when attacking with firearms.', 'Steiner Hp, 0, Valerie Hp, 0, Pomotion Points, 0, Extra Actions, 0, Credits, 0, Rank, 0'),
    
    (2, 'cards', 'Capitol Token', 'Coorporation Token', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/CorporationCptEmb.jpg', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/corporationTokenBack.jpg', 'Capitol Token', null),
	(2, 'cards', 'Mishima Token', 'Coorporation Token', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/CorporationMshEmb.jpg', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/corporationTokenBack.jpg', 'Mishima Token', null),
	(2, 'cards', 'Cybertronic Token', 'Coorporation Token', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/CorporationCybEmb.jpg', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/corporationTokenBack.jpg', 'Cybertronic Token', null),
	(2, 'cards', 'Bauhaus Token', 'Coorporation Token', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/CorporationHasEmb.jpg', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/corporationTokenBack.jpg', 'Bauhaus Token', null),
	(2, 'cards', 'Imperial Token', 'Coorporation Token', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/CorporationImpEmb.jpg', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/corporationTokenBack.jpg', 'Imperial Token', null),
	
	(2, 'cards', 'Combat Aura - Legionnaire Fear', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/combat.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. Legion player may not attack your Doomtroopers this Round. 2. One freely chosen Doomtrooper pair may not attack Legionnaires in close combat for the duration of the mission.', null),
	(2, 'cards', 'Commanding Voice - Steal Initiative', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/commanding.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. Take command over a freely chosen Legion figure and perform two actions with it. 2. Take a random Doomtrooper card from any player.', null),
	(2, 'cards', 'Control Defense System - Remote Controlled Door', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/defense.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. You may attack one freely chosen Legion figure once with three black dice. 2. Place a door across a cooridor not wider than one space. Takes three hits to destroy.', null),
	(2, 'cards', 'Combat Frenzy - Combat Report', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/frenzy.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. You may spend two free extra actions during this combat round. 2. Take 5 pp from another freely chosen player.', null),
	(2, 'cards', 'Heroic Luck - Necrofear', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/heroic.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. You can re-roll one die in all your remaining attacks this combat round. May be played immediately after an attack. 2. One freely chosen Doomtrooper Pair may not attack Necromutants in close combat for the duration of the mission.', null),
	(2, 'cards', 'Command Interference - Hurt Leg', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/interference.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. Move a force card to an adjacent sector and flip it if the sector contains Doomtroopers. 2. A freely chosen Doomtrooper can move one space less per Round for the duration of the mission.', null),
	(2, 'cards', 'Limited Teleportation - Dud Round', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/limited.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. Place one of your Doomtroopers in any adjacent sector. 2. Fails a successfull firearm attack on a Doomtrooper.', null),
	(2, 'cards', 'Medic - Uncalibrated Targeter', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/medic.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. One of your Doomtroopers heals two wounds. 2. A freely chosen Doomtrooper must use dice two Ranks lower when using firearm attacks for the duration of the mission.', null),
	(2, 'cards', 'Medicine Injector - Combat Neurosis', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/medicine.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. One of your Doomtroopers heals two wounds. 2. A freely chosen Doomtrooper pair may only perform one Action each this round. No extra actions may be used.', null),
	(2, 'cards', 'Movement Boost - Faulty Teleportation', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/moovement.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. Your Doomtroopers get one free movement action each immediately. Can be used at any time. 2. A freely chosen Doomtrooper is teleported to a different sector of your choice.', null),
	(2, 'cards', 'Molecular Phasing - Coordinating Error', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/phasing.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. Your Doomtroopers can pas through walls this combat round. They cannot move through different levels in the stairs. 2. A freely chosen Doomtrooper pair loses 2 extra actions immediately.', null),
	(2, 'cards', 'Spectral Displacement - False Orders', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/spectral.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. No attacks can be made against your Doomtroopers this combat round. 2. Immediatley perform two movement actions with a freely chosen Doomtrooper pair other than your own.', null),
	(2, 'cards', 'Weak Spot - Lost Initiative', 'Doomtrooper Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/weak.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/doomtrooperCards/doomBack.png', '1. Armor of a freely chosen Legion figure is lowered by one when your Doomtroopers attack this combat round. 2. Chose a Doomtrooper Card randomly from any freely chosen player and discard it.', null),

    (2, 'cards', '1: Legionnaire Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc1.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '3 legionnaires', null),
	(2, 'cards', '2: Legionnaire Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc2.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '3 legionnaires', null),
	(2, 'cards', '3: Legionnaire Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc3.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '3 legionnaires', null),
	(2, 'cards', '4: Legionnaire Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc4.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '3 legionnaires', null),
	(2, 'cards', '5: Necromutant Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc5.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '1 necromutnat 1 legionnaire', null),
	(2, 'cards', '6: Necromutant Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc6.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '1 necromutnat 1 legionnaire', null),
	(2, 'cards', '7: Centurion Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc7.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '1 centurion 1 necromutnat 1 legionnaire', null),
	(2, 'cards', '8: Razide Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc8.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '1 razide 2 legionnaires', null),
	(2, 'cards', '9: Razide Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc9.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '1 razide 2 legionnaires', null),
	(2, 'cards', '10: Nepharite Overlord', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc10.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '1 nepharite', null),
	(2, 'cards', '11: Ezoghoul Beastmaster', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc11.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', '1 ezoghoul', null),
	(2, 'cards', '12: Diversion Cohort', 'Force Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fc12.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/force-cards/fcback.png', 'Blank', null),

	(2, 'cards', 'The Legionnaires Charge', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/charge.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'The forces of the Legion make a massed charge. The Legionnaire reinforcements may enter through any entrance on the gameboard. REINFORCEMENTS:1 Centurion 1 Necromutant 8 Legionnaires', null),
	(2, 'cards', 'Close Combat Frenzy', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/closeCombatFrenzy.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'Close combat frenzy grips the Legion troops. All Legion figures are allowed to re-roll one die when attacking in close combat this round. REINFORCEMENTS: 1 Centurion 1 Necromutant 1 Legionnaire', null),
	(2, 'cards', 'Close Combat Phobia', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/closeCombatPhobia.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'The Lord of the Citadel induces a phobia against close combat among ters. They may not attack in close combat during the next round. REINFORCEMENTS: 1 Centurion 1 Necromutant', null),
	(2, 'cards', 'Dark Energy Wave', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/darkEnergyWave.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'A wave of dark power sweeps through the Citadel. Ali Legion figures are allowed to re-roll one attack die in all their attacks this Combat Round. REINFORCEMENTS: 1 Necromutant 2 Legionnaires', null),
	(2, 'cards', 'Dark Influence', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/darkInfluence.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'The Legion senses the presence of the Dark Symmetry. The Legion player may use two extra Actions freely this round. REINFORCEMENTS: 2 Legionnaires', null),
	(2, 'cards', 'Dark Teleportation', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/darkTele.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'This Combat Round you are allowed to move one of your figures to any square on the board instead of one normal movement action. REINFORCEMENTS: 1 Razide', null),
	(2, 'cards', 'Ezoghoul Attack', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/ezoghoul.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'A huge Ezoghoul attacks the intruding Doomtrooper units. The Ezoghoul can enter through any entrance on the gameboard. REINFORCEMENTS: 1 Ezoghoul 3 Legionnaires', null),
	(2, 'cards', 'Mental Block', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/mentalBlock.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'The Lord of the Citadel mentally blocks all Doomtroopers. No Doomtroopers may attack with firearms during the next Combat Round, REINFORCEMENTS: 2 Centurions', null),
	(2, 'cards', 'Misinterpreted Orders', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/misinterpreted.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'The Legion disrupts the communication between a freely chosen Doomtrooper pair and their combat coordinators. The pair can only use a total of two Actions between them in the next Combat Round. Extra actions can be used normally. REINFORCEMENTS: 3 Legionnaires', null),
	(2, 'cards', 'Necrofrenzy', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/necrofrenzy.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'A wave of Necroenergy pulsates through the field of combat, All Necromutants and Centurions are allowed one extra Action this Combat Round. REINFORCEMENTS: 2 Necromutants 1 Legionnaire', null),
	(2, 'cards', 'Nepharite Altered Reality', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/nephariteAlteredReality.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'A Nepharites dark power changes Reality. You are allowed to turn any one sector 90 deg in either direction or switch two sectors with each other, without rotating either. Exception: Sector 7 cannot be moved. REINFORCEMENTS:1 Nepharite 2 Necromutants', null),
	(2, 'cards', 'Temporary Defense System', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/temporaryDefense.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'A defense system is activated. You are allowed to roll three black dice once and direct the effect at any Doomtrooper on the board, including your own. REINFORCEMENTS: 1 Necromutant 1 Razide', null),
	(2, 'cards', 'Nepharite Timerift', 'Event Card', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/timerift.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/eventCards/eventBack.png', 'A Nepharite creates a timerift and moves through it while time around him stands still. A Nepharite figure may make one extra move ment action this Combat Round. REINFORCEMENTS: 1 Nepharite 1 Legionnaire 1 Necromutant', null),

	(2, 'cards', 'Special Weapons', 'Reference', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/reference-cards/refereneceCard1.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/reference-cards/refBack.png', 'Shows Grenade Launcher, Puker, Violator Sword, and Nimrod Autocannon attack patterns.', null),
	(2, 'cards', 'Rank 1-2', 'Reference', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/reference-cards/rank1.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/reference-cards/refBack.png', 'Legionnaire: cc:2r, fc:3w, a:0, pp:1, Necromutant: cc:3w, fc:2r, a:1, pp:2, Centurion: cc:3w, fc:4w, a:1, pp:3, Razide: cc:3r, fc:4w, a:2, pp:5, Nepharite: cc:4r, fc:5w, pp:7, a:2, Ezoghoul: cc:4r fc:5w, a:3, pp:10', null),
	(2, 'cards', 'Rank 3-4', 'Reference', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/reference-cards/rank3.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/reference-cards/refBack.png', 'Legionnaire: cc:2r, fc:3w, a:1, pp:1, Necromutant: cc:3r, fc:2b, a:1, pp:2, Centurion: cc:3r, fc:4w, a:2, pp:3, Razide: cc:3b, fc:4r, a:2, pp:5, Nepharite: cc:5r, fc:4r, a:3, pp:7, Ezoghoul: cc:4r fc:5w, a:3, pp:10', null),
	(2, 'cards', 'Rank 5-6', 'Reference', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/reference-cards/rank5.png', 'https://capstone-stuff.s3.us-east-2.amazonaws.com/reference-cards/refBack.png', 'Legionnaire: cc:2b, fc:3r, a:1, pp:1, Necromutant: cc:3r, fc:2b, a:2, pp:2, Centurion: cc:3b, fc:4r, a:2, pp:3, Razide: cc:3b, fc:4r, a:3, pp:5, Nepharite: cc:4b, fc:5r, a:3, pp:7, Ezoghoul: cc:4b fc:5r, a:4, pp:10', null);


insert into game_instance (board_game_id)
	values
	(1),
	(2);


insert into app_user_game_instance (game_instance_id, app_user_id)
values
(1, 1),
(1, 2),
(2, 1),
(2, 2),
(2, 3);

insert into die (board_game_id, category, die_name, color, background, side1, side2, side3, side4, side5, side6)
values
(2, 'die', 'White Die', 'black', 'white', 'HIT', null, null, 'HIT', null, null),
(2, 'die', 'White Die', 'black', 'white', 'HIT', null, null, 'HIT', null, null),
(2, 'die', 'White Die', 'black', 'white', 'HIT', null, null, 'HIT', null, null),
(2, 'die', 'Red Die', 'white', 'red', 'HIT', null, null, 'HIT', null, 'HIT'),
(2, 'die', 'Red Die', 'white', 'red', 'HIT', null, null, 'HIT', null, 'HIT'),
(2, 'die', 'Red Die', 'white', 'red', 'HIT', null, null, 'HIT', null, 'HIT'),
(2, 'die', 'Black Die', 'white', 'black', 'HIT', 'HIT', null, 'HIT', 'HIT', null),
(2, 'die', 'Black Die', 'white', 'black', 'HIT', 'HIT', null, 'HIT', 'HIT', null),
(2, 'die', 'Black Die', 'white', 'black', 'HIT', 'HIT', null, 'HIT', 'HIT', null);





