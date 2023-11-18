# Capstone Proposal
## Problem Statement
Board games rock, there’s no denying it. When we were younger and unburdened from adult responsibilities like family, work, and time consuming tasks like house work and cooking, playing board games was easy. We all lived near each other, had time to kill, and wanted to to relax and have a good time. Our favorite board game to play was Mutant Chronicles, but there were other gems as well. Now, playing board games is almost impossible. Think about it. Even scheduling a cup of coffee on the weekend with a buddy can take weeks of planning, how can we possibly schedule and play a board game with an entire group? Wouldn’t it be great if we can relive our shared experiences, but from our modern prisons we call home? 

## Technical Solution
Create a web application where groups can play virtual board games together. 

### Scenario 1: 

Matt lives a nomadic life as his way of fighting “The Man”, but his removal from a consistent and structured world has left him with little opportunity to meet with friends and family in person. Danny is off fighting wars for “The Man”, and his precarious job sends him around the world. In his down time, he wishes to reconnect with pals. Jim has a family. He has to step and be “The Man”. This requires being at home and taking care of the ones he loves. However, he would also like to see play games with his friends too. With the virtual board games application, we can make more opportunities to hang and play games together with friends far apart. 

### Scenario 2:

Mutant Chronicles has become a relic to us, and do to its over use and abuse, it has become fragile, pieces have broken, and parts are missing. It’s also a rare board game, so finding a replacement can be a difficult task. However, thanks to the virtual board game app, pieces can be replaced with a click of a button and updates to an old game require only your imagination (and a computer).   

### Scenario 3:

Some board games take a long time to play. D&D can go on for years, Mutant Chronicles can go on for days, and Monopoly…Oh boy. What’s worse is, there can be a ton of set up involved, and its difficult to save your game and pick up from where you left off. The virtual board game app makes it easy to set up by having user created game instances as well as the ability to save and load games that have not been completed. 

## Glossary:

Board Game: A defined set of pieces and boards that make up the main components of a board game.

Game Instance: Playing a board game. Includes players and a board game.

Players: Users who are part of a game instance.

Game Area: The playable area overall. Controls board sizes and places.

Pieces: Broadly categorizes all non board components of a board game.

Cards: Pieces that are cards. Usually have a front with information and a blank back.

Figures: Pieces that players can move on boards.

Character data: Pieces that are relevant to individual players and contain variable information about them. Example: Character data can hold a figure’s hp among other rpg-like stats.

Die: Different die pieces.

Boards: Flat, usually cardboard “map” of a board game. Must be tiled with square tiles marking coordinates for figures. 

Places: The sub-squares of each board. Pieces can occupy a place, maybe more than one, depending on the game (optional goal)

Move: A player changing a piece in the game.

## High Level Requirement:
MATT: 

	add/delete ADMIN status from users. 
ADMIN: 

	add/update/delete board games in the collection
	add/update/delete pieces in a board game
	add/update/delete boards in a board game

USER: 

	start/complete a game instance
	add/remove players from a game instance
	join a game instance
	log in
	log out
	recover password (optional goal)

NON_USER:

	sign up


## User Stories/Scenarios

A non user wants to sign up so they can join their friends in playing board games online.

A user wants to select a board game to play.

A user wants to add friends to play a game instance with them.

A user wants to commit a move in the game.

A user wants to view a log of completed moves.	

A user wants to move a piece onto the board.

A user wants to roll the dice.

A user wants to set the dice roll.

A user wants to draw a card into their hand.

A user wants to show their card to everyone.

A user wants to remove a piece from the board.

A user wants to save certain parts of the game so they can be picked up later.

A user wants to rotate and zoom into a game area.

An admin wants to add a new board game.

An admin wants to set up the Game Area for a board game that sets the amount of board 
slots and how many places are on each board.

An admin wants to save a new board with a skin hosted from their Google drive or similar.

An admin wants to add a pieces to a game such as the player tokens. 
An admin want to add card pieces to a game.



	

 
