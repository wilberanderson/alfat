.ORIG x3000
Mscore	.BLKW 1
	LEA R3, Mscore
	AND R0, R0, #0
	ADD R0, R0, #10
	STR R0, R3, #0
	LEA R0, WELC
	PUTS
	BRnzp Next52
WELC .STRINGZ "Welcome! What is your name?"
Next52	JSR NAME
NAME	ST R7, RETAD
	LD R3, ENTER 		;10 is one of the ascii codes for enter
	LD R2, SAVE		;establishes where the string will be saved
	LD R0, AsciiNewLine1	;new line
	BRnzp Next0
AsciiNewLine1 .FILL x000A
Next0	OUT
LOOP	GETC			;this loop repeatedly gets a single-character
	OUT			;input from the user, until the user presses
	STR R0, R2, 0		;enter. The ascii codes of the characters are
	ADD R2, R2, #1		;stored in consecutive spaces in memory
	ADD R4, R3, R0
	BRnp LOOP
	AND R4, R4, #0		;these two lines add a null line after all
	STR R4, R2, 0		;the characters have been stored in memory
	LEA R0, HELLO
	PUTS
	LD R0, SAVE
	PUTS
	LD R7, RETAD		;I wanted to keep this in case I change to JSR
	BRnzp BACK
HELLO .STRINGZ "Hello, "
RETAD .BLKW 1
SAVE .BLKW 20
SAVELOC .BLKW 1
ENTER .FILL #-10
BACK	AND R2, R2, #0		;R2 will keep track of morality score
	ADD R2, R2, #1
	
START	BRnzp Next56
STAR .STRINGZ "You're in the middle of the town. Where would you like to go?"
Next56	LEA R0, STAR
	PUTS
	BRnzp Next2
STARTLOC .FILL START
Next2	LD R3, STARTLOC		;Saves address  of START in R3 for future use
	LD R0, AsciiNewLine
	BRnzp Next3
AsciiNewLine .FILL x000A
Next3	OUT
	LEA R0, OP1
	BRnzp Next53
OP1 .STRINGZ " (1) The tavern"
Next53	PUTS
	BRnzp Next4
AsciiNewLine42 .FILL x000A
Next4	LD R0, AsciiNewLine42
	OUT
	LEA R0, OP2
	BRnzp Next54
OP2 .STRINGZ "(2) The apothecary"
Next54	PUTS
	LD R0, AsciiNewLine42
	OUT
	BRnzp Next55
OPQ .STRINGZ "(3) Quit"
Next55	LEA R0, OPQ
	PUTS
	LD R0, AsciiNewLine42
	OUT
	GETC
	OUT
	AND R6, R6, #0
	AND R4, R4, #0
	ADD R6, R6, #-15
	ADD R6, R6, #-15
	ADD R6, R6, #-15
	ADD R6, R6, #-4		;49 is the decimal rep of 1
	ADD R4, R0, R6
	BRz TAVERN
CHOOSE	ADD R4, R4, #-1
	BRz APOTHZ
	HALT
APOTHZ	BRnzp APOTH1
APOTH0 .FILL APOTH
APOTH1	LD R5, APOTH0
	JMP R5
Next57	LD R0, AsciiNewLine2
	BRnzp Next5
AsciiNewLine2 .FILL x000A
Next5	OUT
	HALT
TAVERN 	LD R0, AsciiNewLine2
	OUT
	LEA R0, OP3
	PUTS
	BRnzp Skip
OP3 .STRINGZ "Inside the tavern, a large man wearing an apron slumps on the bar, crying softly. Before you can slowly back out the way you came, he spots you and cries, 'Thank goodness, "
	YOURNAME .FILL SAVE
Skip	LDI R0, YOURNAME
	PUTS
	BRnzp Next58
OP3C .STRINGZ ". My daughter has been kidnapped by an oversized squirrel, and I need your help to get her back. I can reward you with a medley of baked goods! He was headed towards the peanut-buttery, I think'"
Next58	LEA R0, OP3C
	PUTS
	LD R0, AsciiNewLine3
	OUT
	LEA R0, OP5
	BRnzp Next59
OP5 .STRINGZ "(1) Ignore the Miller and go to search in the forrest--everyone knows that squirrels live in the forrest."
Next59	PUTS
	BRnzp Next1
AsciiNewLine3 .FILL x000A
Next1	LD R0, AsciiNewLine3
	OUT
	LEA R0, OP6
	BRnzp Next60
OP6 .STRINGZ "(2) Trust the Miller's report, and go to the peanut-buttery"
Next60	PUTS
	LD R0, AsciiNewLine3
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp PEANUT0
	BRz FORREST
PEANUT0 BRnzp PEACHANGE1
PEACHANGE0 .FILL Mscore
PEACHANGE1 LD R0, PEACHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1
	LD R2, PEACHANGE0
	STR R0, R2, #0
	BRnzp PEANUT2
PEANUT1 .FILL PEANUT
PEANUT2	LD R5, PEANUT1
	JMP R5
FORREST	LD R0, AsciiNewLine48
	BRnzp Next47
AsciiNewLine48 .FILL x000A
Next47	OUT
	BRnzp FORCHANGE1
FORCHANGE0 .FILL Mscore
FORCHANGE1	LD R0, FORCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #-1		;Lower morality
	LD R2, FORCHANGE0
	STR R0, R2, #0
	LEA R0, FOR
	PUTS
	BRnzp Next48
FOR .STRINGZ "Once you reach the edge of the forrest, you see large, squirrelly paw prints leading into the woods. After a couple miles of following the tracks, however, the prints abruptly stop. What do you do?"
AsciiNewLine49 .FILL x000A
Next48	LD R0, AsciiNewLine49
	OUT
	LEA R0, OP7
	PUTS
	BRnzp Next6
OP7 .STRINGZ "(1) Climb a nearby tree to see if you can catch sight of him"
AsciiNewLine4 .FILL x000A
Next6	LD R0, AsciiNewLine4
	OUT
	BRnzp Next61
OP8 .STRINGZ "(2) Double back on the tracks--maybe you can find more clues"
Next61	LEA R0, OP8
	PUTS
	LD R0, AsciiNewLine4
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp DOUBLE0
	BRz CLIMB
DOUBLE0	BRnzp DOUBLE2
DOUBLE1 .FILL DOUBLE
DOUBLE2	LD R5, DOUBLE1
	JMP R5
CLIMB	LD R0, AsciiNewLine4
	OUT
	BRnzp Next49
CHECKIT0 .FILL Mscore
Next49	LD R0, CHECKIT0
	LDR R0, R0, #0
	AND R1, R1, #0
	ADD R1, R0, #-9
	BRn FAILCHECK
	BRzp PASSCHECK
FAILCHECK LD R0, AsciiNewLine4
	OUT
	LEA R0, POI
	PUTS
	BRnzp Next62
POI .STRINGZ "You start up a nearby tree, only to realize halfway up that it is covered in poison oak. You will be an itchy mess all day--you decide to abort this mission to get some calamine lotion. Bad luck! Work on boosting that karma with good deeds..."
AsciiNewLine52 .FILL x000A
Next62	LD R0, AsciiNewLine52
	OUT
	JMP R3
PASSCHECK	LEA R0, CLI
	PUTS
CLI .STRINGZ "You start up a nearby birch, panting with effort. This tree-climbing thing is harder than it looks! Suddenly, when you're about 20 yards up, the branch beneath you breaks with a sickening snap. You begin to drop when you feel a massive, furry paw close around your wrist. It's the squirrel! What do you do?"
	BRnzp Next7
AsciiNewLine7 .FILL x000A
Next7	LD R0, AsciiNewLine7
	OUT
	BRnzp Next63
OP11 .STRINGZ "(1) Hit him right in the buckteeth--he knows you've been following him, and he can't be happy!"
Next63	LEA R0, OP11
	PUTS
	LD R0, AsciiNewLine7
	OUT
	BRnzp Next64
OP12 .STRINGZ "(2) Regain your balance and attempt to thank him. That was close!"
Next64	LEA R0, OP12
	PUTS
AsciiNewLine8 .FILL x000A
	LD R0, AsciiNewLine8
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp THANK
	BRz HIT0
HIT0	BRnzp HIT0CHANGE1
HIT0CHANGE0 .FILL Mscore
HIT0CHANGE1 LD R0, HIT0CHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #-2		;Lower morality
	LD R2, HIT0CHANGE0
	STR R0, R2, #0
	BRnzp HIT2
HIT1 .FILL HIT
HIT2	LD R5, HIT1
	JMP R5
THANK	BRnzp THACHANGE1
THACHANGE0 .FILL Mscore
THACHANGE1 LD R0, THACHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1		;Boost morality
	LD R2, THACHANGE0
	STR R0, R2, #0
BRnzp Next8
AsciiNewLine23 .FILL x000A
Next8	LD R0, AsciiNewLine23
	OUT
	LEA R0, THA
	PUTS
THA .STRINGZ "You attempt to convey your thankfulness by awkwardly patting him on his big, squirrel head. He makes a sound like a squirrel purr, and allows you to climb on his back to descend from the tree. When you reach the ground, the Miller's daughter has emerged from behind a tree. 'Don't make me go back!' she exclaims. 'My father keeps me pent up, and I'm learning so much from Cory' (she gestures toward the squirrel) 'about the woods!'"
	BRnzp Next9
AsciiNewLine24 .FILL x000A
Next9	LD R0, AsciiNewLine24
	OUT
	BRnzp Next65
OP23 .STRINGZ "(1) Allow her to stay with Cory. Her father keeps her pent up? What is it, the 15th century? D to the P!"
Next65	LEA R0, OP23
	PUTS
	LD R0, AsciiNewLine24
	OUT
	BRnzp Next66
OP24 .STRINGZ "(2) Take the girl back with you to the Miller--he did promise a tasty reward of baked goods!"
Next66	LEA R0, OP24
	PUTS
	LD R0, AsciiNewLine24
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp TOMILLER
	BRz DTOTHEP0
DTOTHEP0	BRnzp DTOCHANGE1
DTOCHANGE0 .FILL Mscore
DTOCHANGE1	LD R0, DTOCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1		;Boost Morality
	LD R2, DTOCHANGE0
	STR R0, R2, #0
	BRnzp DTOTHEP2
DTOTHEP1 .FILL DTOTHEP
DTOTHEP2	LD R5, DTOTHEP1
	JMP R5
TOMILLER	BRnzp TOMCHANGE1
TOMCHANGE0 .FILL Mscore
TOMCHANGE1 LD R0, TOMCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1		;Lower Morality
	LD R2, TOMCHANGE0
	STR R0, R2, #0
	BRnzp Next45
AsciiNewLine25 .FILL x000A
Next45	LD R0, AsciiNewLine25
	OUT
	LEA R0, TOM
	PUTS
TOM .STRINGZ "'Come on,' you say to the girl, 'I have to take you back.' She sighs, and, giving the squirrel's big head one last pat, turns around to follow you. The squirrel makes the mournful squirrel sound you've ever heard, and your stomach churns with guilt. You don't know how well you'll be able to enjoy the reward  buns..."
	BRnzp Next10
AsciiNewLine26 .FILL x000A
Next10	LD R0, AsciiNewLine26
	OUT
	LD R0, AsciiNewLine26
	OUT
	JMP R3			;Return to START
DTOTHEP				;Done
	BRnzp Next11
AsciiNewLine27 .FILL x000A
Next11	LD R0, AsciiNewLine27
	OUT
	LEA R0, DTO
	PUTS
DTO .STRINGZ "'Welll...' you say, 'I guess you can stay. I'll just tell your dad that the squirrel overpowered me.' The girl laughs happily and gives you a hug. The squirrel hands you a peanut--not quite as good as the Miller's rolls, but tasty enough. All in all, a successful mission!"
	BRnzp Next12
AsciiNewLine28 .FILL x000A
Next12	LD R0, AsciiNewLine28
	OUT
	LD R0, AsciiNewLine28
	OUT
	JMP R3			;Return to START
HIT				;Done
	BRnzp Next13
AsciiNewLine29 .FILL x000A
Next13	LD R0, AsciiNewLine29
	OUT
	LEA R0, HI
	PUTS
HI .STRINGZ "You nail him as solidly as you can in the chompers, and he lets out a squirrelly squeal of pain. He brings his paws up to his injured mouth, dropping you in the process. Clonk. Clonk. Clonk. Down you fall, hitting branches as you fall, into you land with a solid thud on the ground. You groan, and start limping back to the village. This is NOT worth any amount of baked goods."
	BRnzp Next14
AsciiNewLine30 .FILL x000A
Next14	LD R0, AsciiNewLine30
	OUT
	LD R0, AsciiNewLine30
	OUT
	JMP R3			;Return to START
DOUBLE	LD R0, AsciiNewLine10
	BRnzp Next15
AsciiNewLine10 .FILL x000A
Next15	OUT
	BRnzp Next68
DOU .STRINGZ "You start back, looking for signs of subterfuge. You're focusing so intently on the ground that you miss a large-rodent shaped shadow soaring gracefully from tree to tree overhead. Suddenly, you're on the ground, with the squirrel pinning you down."
Next68	LEA R0, DOU
	PUTS
	BRnzp Next16
AsciiNewLine9 .FILL x000A
Next16	LD R0, AsciiNewLine9
	OUT
	BRnzp Next69
OP13 .STRINGZ "(1) Elbow him in the squishy gut as hard as you can and attempt to get the upper hand"
Next69	LEA R0, OP13
	PUTS
	LD R0, AsciiNewLine9
	OUT
	BRnzp Next70
OP14 .STRINGZ "(2) Try to sneak your harmonica from your pocket--maybe a loud noise will scare him off of you"
Next70	LEA R0, OP14
	PUTS
	LD R0, AsciiNewLine9
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp HARMONICA
	BRz ELBOW0
ELBOW0	ADD R2, R2, #-1
	BRnzp ELBOW2
ELBOW1 .FILL ELBOW
ELBOW2	LD R5, ELBOW1
	JMP R5
HARMONICA	ADD R2, R2, #1
	LD R0, AsciiNewLine14
	OUT
	BRnzp Next17
AsciiNewLine14 .FILL x000A
Next17	LEA R0, HAR
	PUTS
HAR .STRINGZ "You manage to squirm your harmonica out of your pocket, and, panting, play a shrill note. The squirrel stops struggling and looks at you, mesmerized. Tentatively, you play another note. The squirrel relaxes entirely and hops off you, eagerly waiting for more."
	BRnzp Next18
AsciiNewLine15 .FILL x000A
Next18	LD R0, AsciiNewLine15
	OUT
	BRnzp Next71
OP19 .STRINGZ "(1) Play a little song for him--it's been a while since your harmonica skill has been so well appreciated!"
Next71	LEA R0, OP19
	PUTS
	LD R0, AsciiNewLine15
	OUT
	BRnzp Next72
OP20 .STRINGZ "(2) Take his calmness as an opportunity to clonk him on the head. This is a kidnapper we're dealing with!"
Next72	LEA R0, OP20
	PUTS
	LD R0, AsciiNewLine15
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp HIT3
	BRz PLAYON
HIT3	BRnzp HIT3CHANGE1
HIT3CHANGE0 .FILL Mscore
HIT3CHANGE1	LD R0, HIT3CHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #-2		;Lower Morality
	LD R2, HIT3CHANGE0
	STR R0, R2, #0
	BRnzp HIT5
HIT4 .FILL HIT
HIT5	LD R5, HIT4
	JMP R5
PLAYON	BRnzp PLACHANGE1
PLACHANGE0 .FILL Mscore
PLACHANGE1	LD R0, PLACHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1		;Boost Morality
	LD R2, PLACHANGE0
	STR R0, R2, #0
	BRnzp Next19
AsciiNewLine18 .FILL x000A
Next19	LD R0, AsciiNewLine18
	OUT
	LEA R0, PLA
	PUTS
PLA .STRINGZ "His delight increases as you play, and suddenly he moves toward a nearby tree. He tugs the Miller's daughter out (gasp!) and does his little wiggly dance with her. She laughs and ruffles his fuzzy head. 'Hey!' you say, astonished, 'You have to come back with me! Your father's looking for you!' 'No thanks,' says the girl, 'My father spends all his time at that dumb tavern, and I get lonely. Now I have the best pet of all time!' What do you do?"
	BRnzp Next20
AsciiNewLine19 .FILL x000A
Next20	LD R0, AsciiNewLine19
	OUT
	BRnzp Next73
OP21 .STRINGZ "(1) Allow them to stay together--who are you to stop a great friendship?"
Next73	LEA R0, OP21
	PUTS
	LD R0, AsciiNewLine19
	OUT
	BRnzp Next74
OP22 .STRINGZ "(2) Take the girl back to her father. He is her dad, after all, and he promised to reward you involving baked goods"
Next74	LEA R0, OP22
	PUTS
	LD R0, AsciiNewLine19
	OUT
	GETC
	OUT
	BRp RETURN
	BRz ALLOW0
ALLOW0	BRnzp ALLCHANGE1
ALLCHANGE0 .FILL Mscore
ALLCHANGE1	LD R0, ALLCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1
	LD R2, ALLCHANGE0
	STR R0, R2, #0
	BRnzp ALLOW2
ALLOW1 .FILL ALLOW
ALLOW2	LD R5, ALLOW1
	JMP R5
RETURN	BRnzp RETCHANGE1
RETCHANGE0 .FILL Mscore
RETCHANGE1	LD R0, RETCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #-1		;Lower Morality
	LD R2, RETCHANGE0
	STR R0, R2, #0
	BRnzp Next21
AsciiNewLine20 .FILL x000A
Next21	LD R0, AsciiNewLine20
	OUT
	LEA R0, RETU
	PUTS
RETU .STRINGZ "'Come on,' you say to the girl, 'It's time to go back.' She sighs, and, giving the squirrel's big head one last pat, turns around to follow you. The squirrel makes the mournful squirrel sound you've ever heard, and your stomach churns with guilt. You don't know how well you'll be able to enjoy the reward  buns..."
	BRnzp Next90
AsciiNewLine21 .FILL x000A
Next90	LD R0, AsciiNewLine21
	OUT
	LD R0, AsciiNewLine21
	OUT
	JMP R3			;Return to START
ALLOW	LD R0, AsciiNewLine21
	OUT
	LEA R0, ALL
	PUTS
ALL .STRINGZ "You sigh, 'Oh, okayy. You can stay.' The girl beams, and you start up another song on your harmonica. The squirrel and the girl do the wiggle dance, and never complain about your playing...they, unlike your neighbors, AHEM, seem to appreciate true genius. You play until dark, and you're too tired to go on. You return to the village tired but happy."
	BRnzp Next23
AsciiNewLine22 .FILL x000A
Next23	LD R0, AsciiNewLine22
	OUT
	LD R0, AsciiNewLine22
	OUT
	JMP R3			;Return to START
ELBOW	ADD R4, R4, #0		;Done
	BRnzp Next24
AsciiNewLine16 .FILL x000A
Next24	LD R0, AsciiNewLine16
	OUT
	LEA R0, ELB
	PUTS
ELB .STRINGZ "The minute your elbow makes contact with his roly-poly tummy, the squirrel lets out a roar of agony. He flies into a rage, and his squirrel-talons are surprisingly sharp. You wildly flail toward the village, the squirrels roars and some light laughter (could that be the Miller's daughter?) echoing in your ears."
	BRnzp Next25
AsciiNewLine17 .FILL x000A
Next25	LD R0, AsciiNewLine17
	OUT
	LD R0, AsciiNewLine17
	JMP R3			;Return to START
PEANUT	LD R0, AsciiNewLine6
	Brnzp Next26
AsciiNewLine6 .FILL x000A
Next26	OUT
	LEA R0, PEA
	PUTS
PEA .STRINGZ "When you reach the peanut buttery, the big oaken door is swinging ominously on its hinges. Cautiously you enter. Tubs of peanut butter line the floors, and on a raised catwalk, there are large bags labeled 'Peanuts'. Out of the corner of your eye, you see a shaddow move on the catwalk. What do you do?"
	BRnzp Next27
AsciiNewLine5 .FILL x000A
Next27	LD R0, AsciiNewLine5
	OUT
	LEA R0, OP9
	PUTS
	BRnzp Next75
OP9 .STRINGZ "(1) Climb up onto the catwalk"
Next75	LD R0, AsciiNewLine5
	OUT
	LEA R0, OP10
	PUTS
	BRnzp Next76
OP10 .STRINGZ "(2) Explore the ground floor"
Next76	LD R0, AsciiNewLine5
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp GROUND0
	BRz CATWALK
GROUND0	AND R5, R5, #0
	BRnzp GROUND2
GROUND1 .FILL GROUND
GROUND2	LD R5, GROUND1
	JMP R5
	BRz CATWALK
CATWALK	LD R0, AsciiNewLine5
	OUT
	BRnzp Next77
CAT .STRINGZ "You crawl up onto the catwalk. Crunch. Crunch. You notice in horror that you've stepped on a bag of peanuts, alerting the squirrel to your position. In a flash, the giant rodent is in front of you, its large, button eyes bright. What do you do?"
Next77	LEA R0, CAT
	PUTS
	BRnzp Next28
AsciiNewLine11 .FILL x000A
Next28	LD R0, AsciiNewLine11
	OUT
	BRnzp Next78
OP15 .STRINGZ "(1) Bean the squirrel in the noggin as hard as you can with the bag of peanuts. You don't like the glint in those eyes"
Next78	LEA R0, OP15
	PUTS
	LD R0, AsciiNewLine11
	OUT
	BRnzp Next79
OP16 .STRINGZ "(2) Try to earn his trust by offering him the cracked peanuts in your outstretched hand"
Next79	LEA R0, OP16
	PUTS
	LD R0, AsciiNewLine11
	OUT
	GETC
	OUT
	LD R0, AsciiNewLine11
	OUT
	BRp TRUST
	BRz NOGGIN0
NOGGIN0	BRnzp NOGCHANGE1
NOGCHANGE0 .FILL Mscore
NOGCHANGE1 LD R0, NOGCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #-1		;Lower Morality
	LD R2, NOGCHANGE0
	STR R0, R2, #0
	BRnzp NOGGIN2
NOGGIN1 .FILL NOGGIN
NOGGIN2	LD R5, NOGGIN1
	JMP R5
TRUST	BRnzp TRUCHANGE1
TRUCHANGE0 .FILL Mscore
TRUCHANGE1 LD R0, TRUCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1		;Boost Morality
	LD R2, TRUCHANGE0
	STR R0, R2, #0
	BRnzp Next29
AsciiNewLine31 .FILL x000A
Next29	LD R0, AsciiNewLine31
	OUT
	LEA R0, TRU
	PUTS
TRU .STRINGZ "You reach out a shakey hand with the nuts on it, and quiver a little when his ginormous buckteeth nibble on the nut. But his tongue on your hand...kind of...tickles! You forget your hero comportment as you feed him another nut, giggling. Suddenly you hear a female voice: 'He's fun, isn't he?' It's the Miller's daughter! 'Don't ask me to go back. Dad spends all his time in that stupid tavern, and I have to make all the baked goods. Cory here' she motions towards the squirrel, 'is helping me escape.'"
	BRnzp Next30
AsciiNewLine32 .FILL x000A
Next30	LD R0, AsciiNewLine32
	OUT
	BRnzp Next80
OP30 .STRINGZ "(1) Take her back to her father. How will the village get the necessary supplies of buns and cupcakes without her?"
Next80	LEA R0, OP30
	PUTS
	LD R0, AsciiNewLine32
	OUT
	BRnzp Next81
OP25 .STRINGZ "(2) Let her stay. With all this peanut-feeding and hand-tickling, Cory has kind of grown on you."
Next81	LEA R0, OP25
	PUTS
	LD R0, AsciiNewLine32
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp STAY
	BRz FATHER0
FATHER0	BRnzp FATCHANGE1
FATCHANGE0 .FILL Mscore
FATCHANGE1	LD R0, FATCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #-1		;Lower Morality
	LD R2, FATCHANGE0
	STR R0, R2, #0
	BRnzp FATHER2
FATHER1 .FILL FATHER
FATHER2	LD R5, FATHER1
	JMP R5
STAY	BRnzp STACHANGE1
STACHANGE0 .FILL Mscore
STACHANGE1	LD R0, STACHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1		;Boost Morality
	LD R2, STACHANGE0
	STR R0, R2, #0
	BRnzp Next31
AsciiNewLine33 .FILL x000A
Next31	LD R0, AsciiNewLine33
	OUT
	LEA R0, STA
	PUTS
STA .STRINGZ "'Oh alright,' you sigh, 'I guess you can stay.' In fact, you decide to spend a while longer with them, feeding peanuts to Cory and petting his roly-poly stomach. The Miller's daughter even gives you a profiterole from the bakery. You head back to the village full and in good spirits. All in all, a successful mission!"
	BRnzp Next32
AsciiNewLine34 .FILL x000A
Next32	LD R0, AsciiNewLine34
	OUT
	LD R0, AsciiNewLine34
	OUT
	JMP R3			;Return to START
FATHER
	LD R0, AsciiNewLine34
	OUT
	BRnzp Next50
CHECKIT1 .FILL Mscore
Next50	LD R0, CHECKIT1
	LDR R0, R0, #0
	OUT
	ADD R1, R0, #-8
	BRnz CHECKFAIL1
	BRp CHECKPASS10
CHECKPASS10	BRnzp CHECKPASS12
CHECKPASS11 .FILL CHECKPASS1
CHECKPASS12	LD R5, CHECKPASS11
	JMP R5
CHECKFAIL1	LEA R0, REP
	PUTS
REP .STRINGZ "'Nope,' you say, 'C'mon we have to go back.' To your surprise, the girl makes no movement to follow you. 'I've heard about you,' she sneers scornfully, 'You're mean, and I don't respect you. I'm not going anywhere with you.' You take a step towards her, but Cory bares his bares his buckteeth at you. As you head back to the village emptyhanded, you think glumly that your PR campaign needs some work."
	BRnzp Next51
AsciiNewLine50 .FILL x000A
Next51	LD R0, AsciiNewLine50
	OUT
	JMP R3
CHECKPASS1	LEA R0, FAT
	PUTS
FAT .STRINGZ "'Nope,' you say, 'C'mon, we have to go back.' Cory spits the peanut he's currently eating at you, and it ricochets painfully off your head. The Miller's daughter reluctantly follows you back to the village, but not without several reluctant glances back toward the forrest. To top it all off, you have a peanut-shaped battle scar on your head now."
	BRnzp Next33
AsciiNewLine35 .FILL x000A
Next33	LD R0, AsciiNewLine35
	OUT
	JMP R3			;Return to START
NOGGIN
	BRnzp Next34
AsciiNewLine36 .FILL x000A
Next34	LD R0, AsciiNewLine36
	OUT
	LEA R0, NOG
	PUTS
NOG .STRINGZ "You struggle to lift the bag of peanuts and whip it towards the squirrel. It bounces of his corpulent flank harmlessly, but he narrows his beady black eyes at you. As you try to back away from him, you fall off the catlwalk into a tub of peanut butter, blacking out. You awake to find the giant squirrel licking peanut butter off your face. You decide to cut your losses in this disastrous mission and head back to the village."
	BRnzp Next35
AsciiNewLine37 .FILL x000A
Next35	LD R0, AsciiNewLine37
	OUT
	JMP R3
GROUND				;Done
	BRnzp Next36
AsciiNewLine12 .FILL x000A
Next36	LD R0, AsciiNewLine12
	OUT
	LEA R0, GRO
	PUTS
GRO .STRINGZ "You creep forward slowly, hiding behind the vats of peanut butter. After scurrying to the vat in the middle of the room, you suddenly feel heavy, peanut-buttery breath on your neck. You look up into the black, beady eyes of the scquirrel, who is crouched on the vat. What do you do?"
	BRnzp Next37
AsciiNewLine13 .FILL x000A
Next37	LD R0, AsciiNewLine13
	OUT
	BRnzp Next83
OP17 .STRINGZ "(1) Grab a handful of peanut butter from the vat, and attempt to glue him down for questioning."
Next83	LEA R0, OP17
	PUTS
	LD R0, AsciiNewLine13
	OUT
	BRnzp 84
OP18 .STRINGZ "(2) Hop up on the vat--you will need to be on equal ground for this man-on-squirrel fight"
Next84	LEA R0, OP18
	PUTS
	LD R0, AsciiNewLine13
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp FIGHT
	BRz GLUE0
GLUE0	BRnzp GLUCHANGE1
GLUCHANGE0 .FILL Mscore
GLUCHANGE1	LD R0, GLUCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1
	LD R2, GLUCHANGE0
	STR R0, R2, #0
	BRnzp GLUE2
GLUE1 .FILL GLUE
GLUE2	LD R5, GLUE1
	JMP R5
FIGHT	BRnzp FIGCHANGE1
FIGCHANGE0 .FILL Mscore
FIGCHANGE1	LD R0, FIGCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #-1		;Lower Morality
	LD R2, FIGCHANGE0
	STR R0, R2, #0
	BRnzp Next38
AsciiNewLine38 .FILL x000A
Next38	LD R0, AsciiNewLine38
	OUT
	LEA R0, FIG
	PUTS
FIG .STRINGZ "You climb up onto the vat and look the squirrel squre in the beady eye. 'C'mon, Squirrelly!' you say, 'Hit me with your best shot!' He nonchalantly reaches out one paw and pushes you into the vat. You sputter and choke on peanut butter. Humiliated, you decide to head back to the town to cut your losses. On your way back, woodland creatures stop to lick you off. Nooottt your best mission."
	BRnzp Next39
AsciiNewLine39 .FILL x000A
Next39	LD R0, AsciiNewLine39
	OUT
	LD R0, AsciiNewLine39
	OUT
	JMP R3
GLUE	LD R0, AsciiNewLine40
	BRnzp Next40
AsciiNewLine40 .FILL x000A
Next40	OUT
	LEA R0, GLU
	PUTS
GLU .STRINGZ "You dip your hand into the peanut butter, and try to glue down his feet. Noticing the salty snack on your hands, he starts to lick you. 'Hey! That tickles' you chuckle, forgetting your macho mission. Suddenly, the Miller's daughter steps out from behind the vats, beaming. 'See? He's friendly once you get to know him! Please don't make me go back to my father--he wants me to become a baker, when all I really want to be is a woodland explorer! Cory here is teaching me the ways of the forrest.'"
	BRnzp Next41
AsciiNewLine41 .FILL x000A
Next41	LD R0, AsciiNewLine41
	OUT
	BRnzp Next85
OP28 .STRINGZ "(1) Take her back to her father. The baked-goods reward is at stake here, people."
Next85	LEA R0, OP28
	PUTS
	LD R0, AsciiNewLine41
	OUT
	BRnzp Next86
OP29 .STRINGZ "(2) Let her stay--woodland adventuring is an admirable pursuit. Maybe you could team up for your future forrest adventures!."
Next86	LEA R0, OP29
	PUTS
	LD R0, AsciiNewLine41
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRp ADVEN
	BRz BAKED0
BAKED0	BRnzp BAKCHANGE1
BAKCHANGE0 .FILL Mscore
BAKCHANGE1	LD R0, BAKCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #-1		;Lower Morality
	LD R2, BAKCHANGE0
	STR R0, R2, #0
	BRnzp BAKED2
BAKED1 .FILL BAKED
BAKED2	LD R5, BAKED1
	JMP R5
ADVEN	BRnzp ADVCHANGE1
ADVCHANGE0 .FILL Mscore
ADVCHANGE1	LD R0, ADVCHANGE0
	LDR R0, R0, #0
	ADD R0, R0, #1		;Boost Morality
	LD R2, ADVCHANGE0
	STR R0, R2, #0
	LD R0, AsciiNewLine44
	BRnzp Next42
AsciiNewLine44 .FILL x000A
Next42	OUT
	LEA R0, ADV
	PUTS
ADV .STRINGZ "'Oh, okay,' you sigh. 'I guess you and Cory can stay. But who will be the village baker?' She shrugs. 'I don't know...you were pretty resourceful with that peanut butter. Maybe you could do it.' You think about this for a little while. Not a terrible idea! Maybe once you're done with this adventuring business, baking could be a good back up. The girl was right, of course--your handling of the peanut butter was nothing short of masterful. You wave goodbye to your new friends and go back to the village to look for new missions."
	BRnzp Next43
AsciiNewLine45 .FILL x000A
Next43	LD R0, AsciiNewLine45
	OUT
	LD R0, AsciiNewLine45
	OUT
	JMP R3
BAKED LD R0, AsciiNewLine45
	OUT
	LEA R0, BAK
	PUTS
BAK .STRINGZ "'Nope,' you say decisively, 'You're coming with me.' The girl hangs her head and pretends to follow you, but then suddenly whips around and sprints back to Cory, yelling 'Nope yourself!' She hops aboard the giant squirrel, and before you can react, he scuttles past you and into the forrest. You chase them for a while, but squirrels are skittish and quick--and yes, maybe you've been hitting the baked-goods a little hard these days. You go back to the village without anything to show for your mission."
	BRnzp Next44
AsciiNewLine46 .FILL x000A
Next44	LD R0, AsciiNewLine46
	OUT
	LD R0, AsciiNewLine46
	OUT
	JMP R3

APOTH	BRnzp J1
AL1 	.FILL x000A
	AND R6, R6, #0
	AND R4, R4, #0
	ADD R6, R6, #-15
	ADD R6, R6, #-15
	ADD R6, R6, #-15
	ADD R6, R6, #-4		;49 is the decimal rep of 1
J1	LD R0, AL1
	OUT
	LEA R0, PL1
	PUTS

PL1 .STRINGZ	"As you walk down the street in the direction the sign pointed everyone you see is gaunt and downtrodden.  Everyone grimaces, or shies away in fear fromwhen they see the sword at your hip. Something is wrong in this town\nThe sign for the apothecary is above a set of steps that lead down to a door at basement level. The shop is filled with bottles of mysterious liquids, curious knick-knacks and bunches of herbs and roots. As you enter a bell tinkles. Out of the gloom of the shop pops a woman, no taller than 5'. 'What can I do for ya?' she chirps."	
	LD R0, AL2
	OUT
	LEA R0, PL2
PL2 .STRINGZ "(1) Ask about the shop"
	PUTS
AL2 .FILL x000A
	LD R0, AL2
	OUT
	LEA R0, PL3
PL3 .STRINGZ "(2) Ask about the town: why is everyone so miserable?"
	PUTS
	LD R0, AL2
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz SMALTK
	BRnzp WHYSAD1
WHYSAD0	.FILL WHYSAD
WHYSAD1	LD R5, WHYSAD0
	JMP R5
	
SMALTK	LD R0, AL2
	OUT
	LEA R0, PL4
	PUTS
PL4 .STRINGZ "'Oh, well if you came in for some of my wares, I'd be glad to make some suggestions.  You're looking rather peaky...' And she reaches for a bottle of bright green liquid that has turned an impressive shade of purple around the cork where it has dried and caked on the bottle.\nSuddenly you feel just fine, no matter how you felt before.\n'No, please, I'm fine.' You say, 'Please, can you tell me why everyone in town looks so miserable?'"

	LD R0, AL3
	OUT

WHYSAD	BRnzp CHECK21
CHECK20	.FILL Mscore
AL3	.FILL X000A
CHECK21	LD R0, CHECK20
	LDR R0, R0, #0
	OUT
	AND R1, R1, #0
	ADD R1, R0, #-9
	BRnz NOTRUST
TRUST0	.FILL TRUSEE
TRUST1	LD R5, TRUST0
	JMP R5

NOTRUST LD R0, AL2
	OUT
	LEA R0, PL5
	PUTS
PL5 .STRINGZ "'Well... Don't take this the wrong way dear, but I've certainly never seen you around here before, and after all my years you get pretty good at reading people, I'm not sure I like the look of you.  Folks around here are having some tough times, go ask damn Lord Crake up at the castle if you want to know the official line.' and she scowls and disappears back into the depths of her shop."
	LD R0, AL4
	OUT	
PL6 .STRINGZ "(1) Go up to the castle on the hill and ask for an audience with the lord"
	LEA R0, PL6
	PUTS
	LD R0, AL4
	OUT
	LEA R0, PL6
PL7 .STRINGZ "(2) Go back to the center of town and look for a better adventure"
	LEA R0, PL7
	PUTS
	LD R0, AL4
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz CASTLE1	
	BRnzp START1
AL4 .FILL x000A
START0	.FILL START
START1	LD R5, START0
	JMP R5
CASTLE0 .FILL CASTLE
CASTLE1 LD R5,CASTLE0
	JMP R5
	
TRUSEE LD R0, AL2
	OUT
	LEA R0, PL8
	PUTS
PL8	.STRINGZ "'Well... you seem like a decent sort, so I'll tell you about our problems.  There's been a rebellion of a large number of the peasants who work out on the farms. You see Lord Crake's a hard unfair man, and he's had taxes so damn high for the last three harvests that people are starving and not being left with enough to replant next season.\n 'The lord's son Bulric is sympathetic to the peoples' desires.  Only he vanished three weeks ago.  The castle blames the rebels, says he must already be dead.  But I think it's a lie!\n'In fact...you seem trustworthy, and a stranger could be useful right now, would you mind doing me a favor?'\nShe reaches into her sleeve and pulls out a small letter, sealed in wax.\n'Please take this up to the castle, but go by the servants' wicket gate around the west side.  Ask for Wendil the cook and give this to him, say it's from his Auntie P. then return with whatever he tells you.'\n And with that she vanishes into the depths of the shop, leaving you holding the note."
	LD R0, AL5
	OUT
	LEA R0, PL9
PL9 .STRINGZ "(1) Follow her instructions and take her note to Wendil the cook"
	PUTS
	LD R0, AL5
	OUT
	LEA R0, PL10
PL10 .STRINGZ "(2) Take the note up to the castle, but go to the Lord with it instead."
	PUTS
	LD R0, AL5
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz COOK
	BRnzp CASTLEN1
AL5 	.FILL X000A
CASTLEN0 .FILL CASTLEN
CASTLEN1 LD R5, CASTLEN0
	JMP R5

COOK LD R0, AL2
	OUT
	LEA R0, PL11
	PUTS
PL11	.STRINGZ "You walk from the apothecary up the high street to the castle, you walk around to the west side of the outer wall and see a small wicket gate.  You knock and a small peephole opens \n'Yeah?' says a gruff voice.\n'Family message for Wendil' you reply. \n'Alright then, one moment' replies the voice and the peephole slides back into place.  A minute or so later a tall, surprisingly skinny for a castle cook, man comes out of the door and approackes you. \n'I'm Wendil, what's up, then?'\nYou hand him the note.  He reads it with a stoic expression on his face.'Mmhmm, I see, well I see Vania trusts you so I suppose I'll do the same. Please return to Vania with the message that the sparrow is in the hawk's nest. Thank you stranger, for now I think it's best I don't know your name.  Farewell!'\nAnd with that Wendil knocks on the gate and is readmitted, leaving you with a perplexing riddle and a choice."
	LD R0, AL6
	OUT
	LEA R0, PL12
PL12 .STRINGZ "(1) Go back to Vania with the message"
	PUTS
	LD R0, AL6
	OUT
	LEA R0, PL13
PL13 .STRINGZ "(2) Go around to the front gate and tell the guards you have urgent information for Lord Crake, something's afoot"
	PUTS
	LD R0, AL6
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz APOG
	BRnzp CASTLER1
AL6 	.FILL X000A
CASTLER0 .FILL CASTLER
CASTLER1 LD R5, CASTLER0
	JMP R5

APOG LD R0, AL2
	OUT
	LEA R0, PL14
	PUTS
PL14	.STRINGZ "You go back down the high street to Vania's apothecary.  As you enter the bell rings again and Vania pops up again from out of nowhere.\n'I was hoping you'd be back!  What did Wendil have to say?'You tell her.\n'Aha!' She pumps her fist, 'I just knew it was an inside job...so they're keeping him in the North Tower then...hard to get to...no one talented enough...'\n'Excuse me' you say, 'But what exactly did the message mean?'\n'Oh yes, you're still here.  The message meant that Bulric is being held in the north tower of Crake's keep.  The Hawk's what we call Crake in our messages, you see I'm part of the resistance. Hmmm...Looking at you, you seem more capable than any of the ploughhands and shepherds we've got for a rescue mission.  What do you say?"
	LD R0, AL7
	OUT
	LEA R0, PL15
PL15 .STRINGZ "(1) Agree to rescue the Lord's son Bulric"
	PUTS
	LD R0, AL7
	OUT
	LEA R0, PL16
PL16 .STRINGZ "(2) No, I'm not interested in getting involved. "
	PUTS
	LD R0, AL7
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz RESCUE
	BRnzp FIGHT1
AL7 	.FILL X000A
FIGHT0	.FILL FIDGET
FIGHT1	LD R5, FIGHT0
	JMP R5

RESCUE 	LEA R0, PL17
	PUTS
PL17	.STRINGZ "'Excellent! you'll do great!' Vania says 'OK, I'll tell you all we know about Crake's castle.  Wendil can let you into the wicket gate, the guard there is famously narcoleptic. From there your only major obstacle should be the one or two soldiers guarding the tower room.  If you're halfway decent with that sword of yours they should be no trouble, but that might be too loud. Here, take this dagger, I've enchanted it with a potent venom, if you need to subdue any guards, this will do it quietly. Also take this rope, it might be useful getting back.  Wait here until midnight, then go to the castle, Wendil will be waiting.\n\nAfter passing the hours left until midnight exploring Vania's shop, playing with the curios and trying not to break the more unstable looking potion bottles, you steal back up to the castle and knock lightly on the servant gate.  It opens without a sound and Wendil beckons you inside.\n'I see it's you again.' He makes no expression 'I can take you as far as the base of the tower, but from there you're on your own.' and with that Wendil trots quitely down the torchlit hall and takes several turns and several staircases then stops.  'This is as far as I go, I'm more useful as an informer.  Ahead are two guards by his door.  Good luck!' And with that he turns down a short corridor and is gone.  You peer out around a corner and see two guards standing there."
	LD R0, AL8
	OUT
	LEA R0, PL18
PL18 .STRINGZ "(1) Throw a rock down the hall past the guards and try to sneakget by while they investigate"
	PUTS
	LD R0, AL8
	OUT
	LEA R0, PL19
PL19 .STRINGZ "(2) Sneak forward and kill them with the dagger from Vania. "
	PUTS
	LD R0, AL8
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz SNEAK
	BRnzp KILL1
AL8	.FILL X000A
KILL0 	.FILL KILL
KILL1 	LD R5, KILL0
	JMP R5
	
SNEAK 	LEA R0, PL20
	PUTS
PL20	.STRINGZ "You pick up a small piece of rubble that's conveniently lying there and you throw it 40 feet past the guards down the hall where it clatters and makes a ruckus.  Both guards drop their hands to their swords and run to the sound.  When they leave their post you sprint nimbly to the door, quickly undo the bolt and duck inside.  WHEW! you made it. Sitting in a chair at a desk in front of you is a large man of about 20, still with some of the rounded looks of youth.  He starts as you enter, and you have to hold your finger to your lips to keep him from speaking.'My name is"
	BRnzp Sk1
	YOURNAME1 .FILL SAVE
Sk1	LDI R0, YOURNAME1
	PUTS
	LEA R0, PL200
	PUTS
PL200	.STRINGZ ".I'm here to rescue you' you whisper as you hear the guards moving back into place.  You hear one of them relock the bolt, 'Dammit Jim, you always forget to lock the door when it's your turn to bring him food.'\n'But I swaer I did it this time!'\n'Yeah, you and your swearing, I'll let it go this time.'  And that's it.  They go back to the silence of really boring late night guarding.\nBulric whispers back to you 'Finally, I've been stuck here for 3 weeks, though it's felt like 3 years.  What's the plan?'"
	LD R0, AL9
	OUT
	LEA R0, PL21
PL21 .STRINGZ "(1) Try to climb out the window and down the outside of the castle"
	PUTS
	LD R0, AL9
	OUT
	LEA R0, PL22
PL22 .STRINGZ "(2) Subdue the guards and try to make a run for it "
	PUTS
	LD R0, AL9
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz WIND
	BRnzp SUBD1
AL9 	.FILL X000A
SUBD0 	.FILL SUBD
SUBD1 	LD R5, SUBD0
	JMP R5

WIND	LEA R0, PL23
	PUTS
PL23 	.STRINGZ "'Let's go out the window' you say.\n'One sec, let me put good boots for climbing on first. Bulric replies. 'There we go. Ok, ready.'\nAnd you and Bulric climb out the window and onto the ledge. 'There's a small eyelet just above the window for hanging flags' says Bulric. You take the rope Vania gave you and you thread it through the eye.  'I'll go down first, you belay me' you say.   Luckily the castle is old and there are plenty of hand holds.  The going is tough, but you're strong and you make it down without anyone noticing. You then belay Bulric as he rappels down the tower."
SUCCESS	LEA R0, PL230
	PUTS
PL230 	.STRINGZ "You make your way to Vania's shop.  When you arrive Vania greets you enthusiastically 'My lord, it is good to see you, we are in need of your leadership.  Thank you stranger. If you would join us, we would welcome you into the resistance with open arms.'"
	LD R0, AL10
	OUT
	LEA R0, PL24
PL24 .STRINGZ "(1) 'No thanks, I'm not really interested in your rebellion'"
	PUTS
	LD R0, AL10
	OUT
	LEA R0, PL25
PL25 .STRINGZ "(2) 'I would be honored to fight for the good of the people'"
	PUTS
	LD R0, AL10
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz FAREWEL
	BRnzp SURRO1
AL10 	.FILL X000A
SURRO0	.FILL SURRO
SURRO1	LD R5, SURRO0
	JMP R5

FAREWEL	LEA R0, PL26
	PUTS
PL26	.STRINGZ "Vania looks disappointed, but Bulric offers you his hand 'Thank you "
	BRnzp Sk2
	YOURNAME2 .FILL SAVE
Sk2	LDI R0, YOURNAME2
	PUTS
	LEA R0, PL260
	PUTS
PL260	.STRINGZ ", I will not forget the service you have done me today. We wish you all the luck in whatever your next endeavour may be.'\nAnd with that he turns to Vania and they begin discussing some boring logistical blah blah blah. You slip quietly out of the shop and leave the town in your wake, maybe there's something exciting at the next one.  But you feel good that you've made the world a slightly better place."
	BRnzp SUNRI1
AL11	.FILL X000A
SUNRI0	.FILL SUNRIS
SUNRI1	LD R5, SUNRI0
	JMP R5

SURRO 	LEA R0, PL27
	PUTS
PL27	.STRINGZ "As you say the words both of your companions' faces light up 'Excellent! Now, we need to get out of town, there's a farm loyal to the cause a few miles-' Bulric begins to say, but before he can finish the door is bashed off of its hinges and palace guards begin rushing in."
	LD R0, AL12
	OUT
	LEA R0, PL28
PL28 .STRINGZ "(1) Draw your sword and try to buy Bulric and Vania enough time to escape"
	PUTS
	LD R0, AL12
	OUT
	LEA R0, PL29
PL29 .STRINGZ "(2) Flee for the back door and try to escape first "
	PUTS
	LD R0, AL12
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz DEFEN	
	BRnzp ABAN1
AL12 	.FILL X000A
ABAN0	.FILL ABAN
ABAN1	LD R5, ABAN0
	JMP R5
DEFEN	LEA R0, PL30
	PUTS
DEFCH0 .FILL Mscore
DEFCH1	LD R0, DEFCH0
	LDR R0, R0, #0		;+2 morality points
	ADD R0, R0, #2
	LD R3, DEFCH0
	STR R0, R3, #0
	
PL30	.STRINGZ "You draw your sword and begin to fence with two guards simultaneously.  'Go!' you shout at Vania and Bulric as she pulls him toward a large weardrobe.  You buy them enough timem for Vania to pull it open and both of your friends disappear into its depths. You keep fighting and manage to wound one of your opponents before more and more flow in through the broken-in door and you are surrounded.  You brandish your sword but you hear a crack, see a blinding flash and then everything goes dark."
	LD R0, AL13
	OUT
SLP	AND R2, R2, #0
SLPLP	LEA R0, PL31
PL31 .STRINGZ "(1) 'Feel like trying to wake up?'"
	PUTS
	LD R0, AL13
	OUT
	LEA R0, PL32
PL32 .STRINGZ "(2) No, I like sleep, let me sleep"
	PUTS
	LD R0, AL13
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz WAKET
	BRnzp SLEEP
AL13 	.FILL X000A
SLEEP	ADD R2, R2, #1
	BRnzp SLPLP
WAKET	ADD R2, R2, #1
	ADD R1, R2, #-2
	BRp PRSN1
	BRnzp SLPLP
PRSN0	.FILL PRSN
PRSN1	LD R5, PRSN0
	JMP R5
;;; Wraps into PRSN
	
ABAN	BRnzp ABCH1
ABCH0 	.FILL Mscore		;-2 morality
ABCH1	LD R0, ABCH0
	LDR R0, R0, #0
	ADD R0, R0, #-2
	LD R3, ABCH0
	STR R0, R3, #0
	
	LEA R0, PL33
	PUTS	
	
PL33	.STRINGZ "You sprint for the back of the apothecary, knocking curios and bottles flying.  Vania shouts some insult at you as you hurtle by, you don't have time to see what becomes of her and Bulric.  You see a door ahead, you tear it open and take two steps out into the night and feel a blinding pain on the back of your head and the world fades out."
	BRnzp SLP01
SLP00	.FILL SLP
SLP01	LD R5, SLP00
	JMP R5
;;; Wrap up of this line into prison escape story

	
SUBD	LEA R0, PL34
	PUTS	
PL34	.STRINGZ "You go back to the door, clutching Vania's dagger, Bulric right behind you.  As you open the door both guards give a start.  You stab one of them and he falls down silently stone dead.  But the other guard, seeing his comrade fall so quickly runs off down the corridor screaming 'Help! Help!"
	LD R0, AL14
	OUT
	LEA R0, PL35
PL35 .STRINGZ "(1) Run for it"
	PUTS
	LD R0, AL14
	OUT
	LEA R0, PL36
PL36 .STRINGZ "(2) Try to catch him"
	PUTS
	LD R0, AL14
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRnzp NOCHOI
AL14 	.FILL X000A
NOCHOI	LEA R0, PL37
	PUTS
PL37	.STRINGZ "You sprint off down the corridor, but you come to an intersection and don't know which way to go.\n'Follow me' cries Bulric, and he sprints off down the left path.  You follow him and take several turns and run smack into the captain of a group of ten guards coming the opposite direction.  Before you can pick yourself off the floor a heavy boot kicks you squarely in the head.  As your vision fades you see Bulric being hauled away by three guards.\n\n...You failed him.\n"
	BRnzp SLP11
SLP10	.FILL SLP
SLP11	LD R5, SLP10
	JMP R5
;;; Wraps this thread into the PRSN story.	

	
KILL	BRnzp FICH1
	
FICH0 .FILL Mscore		;-2
FICH1	LD R0, FICH0
	LDR R0, R0, #0
	ADD R0, R0, #-2
	LD R3, FICH0
	STR R0, R3, #0
	LEA R0, PL38
	PUTS
PL38	.STRINGZ "You get a running start and sprint down the corridor toward the guards.  Before they even have time to realize what's going on one has fallen to Vania's dagger.  The other's cry of surprise is silenced half-formed in his throat as the dagger claims him as well, his sword only half way out of its scabbard. You draw the bolt on the door and drag one of the guards' bodies into the room. A young man of about 20 gives a start and rises from the chair he was siting in writing something at a desk. \n'What the hell are you doing?' he exclaims seeing the body.\n'I'm here to rescue you! Vania sent me.' you reply, 'Quick, help me with the other body before someone sees it.'\nBulric comes to the door and grabs the other corpse into the room.\n'I knew these men, they weren't bad men' he sighs, 'What's your name stranger?'\n'My name is " 
	BRnzp Sk3
	YOURNAME3 .FILL SAVE
Sk3	LDI R0, YOURNAME3
	PUTS
	LEA R0, PL380
	PUTS
PL380	.STRINGZ ". Vania sent me to get you out so that you can lead the resistance.' you respond\n'Fair enough. So what's the plan?' Bulric asks, leaning in conspiritorially. "
	LD R0, AL15
	OUT
	LEA R0, PL39
PL39 .STRINGZ "(1) We could try to climb out the window and down the outside of the castle with this rope I have"
	PUTS
	LD R0, AL15
	OUT
	LEA R0, PL40
PL40 .STRINGZ "(2) We take the guards' uniforms, hide them in here, and stroll out the front gate on a routine patrol "
	PUTS
	LD R0, AL15
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz WIND2
	BRnzp DISG1
AL15	.FILL X000A
DISG0 	.FILL DISG
DISG1 	LD R5, DISG0
	JMP R5

WIND2	LEA R0, PL41
	PUTS
PL41 	.STRINGZ "'OK, One sec, let me put good boots for climbing on first. Bulric replies. 'There we go. Ok, ready.'\nAnd you and Bulric climb out the window and onto the ledge. 'There's a small eyelet just above the window for hanging flags' says Bulric. You take the rope Vania gave you and you thread it through the eye.  'I'll go down first, you belay me' you say.   Luckily the castle is old and there are plenty of hand holds.  The going is tough, but you're strong and it seems to be going fine. But then you hear a ruckus up above.  The line goes slack for a second and you fall several feet before it jerks taught again.  You look up to see a helmetted head looking over the window ledge at you.  Before you can grab a better handhold you see the figure above make a slashing motion at the rope, it goes completely slack and the tower flies past you as you plummet and everything goes black."

	;; Insert value placement for failed to get Bulric.
	LD R0, AL16
	OUT
	BRnzp FALL1
AL16 	.FILL X000A
FALL0 	.FILL PRSN
FALL1 	LD R5, FALL0
	JMP R5

;;;Wraps into PRSN start as well

DISG	LEA R0, PL42
	PUTS
PL42	.STRINGZ "'I don't like disrespecting these men that way, but sounds like a decent plan' Bulric responds.\nBulric helps you remove the tunics from the two guards.  Luckily the tunic is pretty standard and the  helmet covers your hair and face pretty thoroughly, no one will look twice at the two of you. You step out into the corridor.  Just as you push the latch closed a troop of five guards rounds a far corner coming toward you."
	LD R0, AL17
	OUT
	LEA R0, PL43
PL43 .STRINGZ "(1) Stand attention outside the door and act like the guards on duty"
	PUTS
	LD R0, AL17
	OUT
	LEA R0, PL44
PL44 .STRINGZ "(2) Run away as fast as you can in the opposite direction "
	PUTS
	LD R0, AL17
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz STAND
	BRnzp RUN1
AL17	.FILL X000A
RUN0 	.FILL NOCHOI
RUN1 	LD R5, RUN0
	JMP R5

STAND	LEA R0, PL45
	PUTS
PL45	.STRINGZ "You and Bulric stand to either side of the door and try to look like bored night guards.  As the troop approaches you take a deep breath, but the sergeant just nods at you 'Bob, Jim' and walks past.\nWHEW!\nThat was lucky! You and Bulric wait for their footsteps to die away then Bulric says 'That should be the only serious patrol for about ten minutes, I'll get us back to the kitchen servants' gate.' So the two of you march off down the corridor back the way you came and without seeing another soul the two of you make it back to the small gate, open it, and slip into the night."
	BRnzp SUC1
SUC0	.FILL SUCCESS
SUC1	LD R5, SUC0
	JMP R5
;;; Wraps back to successful escape point.

FIDGET LEA R0, PL46
	PUTS
PL46	.STRINGZ "'Well, I hate to hear you say that... it would have been so nice for all of us.  Unfortunately now you know too much.' Vania sighs, then jumps toward you lunging with a dagger she pulled from her sleeve. You just barely dodge her first lunge and drag your sword out to parry the second."
	LD R0, AL18
	OUT
	LEA R0, PL47
PL47 .STRINGZ "(1) Try to escape out the door and go to the castle"
	PUTS
	LD R0, AL18
	OUT
	LEA R0, PL48
PL48 .STRINGZ "(2) Stand and fight her, you have a sword and she's an old woman"
	PUTS
	LD R0, AL18
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz FLEE
	BRnzp SLAY1
AL18	.FILL X000A
SLAY0 	.FILL SLAY
SLAY1 	LD R5, SLAY0
	JMP R5

FLEE	LEA R0, PL49
	PUTS
	LD R0, AL18
	OUT
PL49	.STRINGZ "You push Vania back a few feet into her shop keeping your sword between you, then when you have enough space you wrench the door open and sprint out into the street, hearing Vania's cries of frustration behind you."
	BRnzp CSTLER1
CSTLER0 .FILL CASTLER
CSTLER1	LD R5, CSTLER0
	JMP R5

SLAY	LEA R0, PL50
	PUTS
PL50	.STRINGZ "You wait for Vania to make another attack and easily parry it, leaving her open for a counter, which you make.  You strike Vania down and her last words are only: 'You fool...'\nYou clean your blade and walk back outside and up to the castle."
	BRnzp SLCH1
	SLCH0 .FILL Mscore		;-2
SLCH1	LD R0, SLCH0
	LDR R0, R0, #0
	ADD R0, R0, #-2
	LD R3, SLCH0
	STR R0, R3, #0
	BRnzp CSTLER3
CSTLER2 .FILL CASTLER
CSTLER3	LD R5, CSTLER2
	JMP R5

;;; Wraps into Lord's Law

	
CASTLE LEA R0, PL51
	PUTS
PL51	.STRINGZ "You wander up the high street of the town toward the large castle at the top of the hill. At the gate you inquire whetheer the lord is granting audiences today.  The sergeant on duty says he is and asks your business."
	LD R0, AL19
	OUT
	LEA R0, PL52
PL52 .STRINGZ "(1) 'I am an adventurer looking for employment'"
	PUTS
	LD R0, AL19
	OUT
	LEA R0, PL53
PL53 .STRINGZ "(2) 'I am inquiring after why the people in this town are so miserable and downtrodden'"
	PUTS
	LD R0, AL19
	OUT
	GETC
	OUT
	BRnzp PL54
AL19 	.FILL X000A


PL54	.STRINGZ "'Ha, alright, I'll inform his lordship that you're here.' The sergeant replies.  He nods to the two guards on the gate and walks across the yard into the keep. A few minutes later he comes back.\n'Lord Crake will see you now' and he motions you to follow him to the keep.  As you walk across the yard you see a couple of covered wagons with what look like bags of grain in them.  You enter the keep and down a wide corridor you enter the lord's study.  Lord Crake is a rather gaunt looking man with cold blue eyes that are constantly shifting. The sergeant salutes and leaves.\n'It isn't too often I have a stranger in my halls.  What's your name then?'\n'"
	BRnzp Sk4
	YOURNAME4 .FILL SAVE
Sk4	LDI R0, YOURNAME4
	PUTS
	LEA R0, PL540
	PUTS
PL540 .STRINGZ " is my name, my lord.'\n'Ah, indeed. Jenkins tells me you look like a strapping capable sort, I suppose I agree. I could use your help with something.  It's these damn peasants who are revolting. They refuse to pay taxes, they waylay carts on the roads, they pilfer and deface my property! They're hurting the realm and I need them dealt with. Now, what do you say?"
HECR	LD R0, AL20
	OUT
	LEA R0, PL55
PL55 .STRINGZ "(1) 'Yes sir, I'd be happy to help.'"
	PUTS
	LD R0, AL20
	OUT
	LEA R0, PL56
PL56 .STRINGZ "(2) 'No sir, with respect, I don't fight the weak'"
	PUTS
	LD R0, AL20
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz PEAS
	DACH0 .FILL Mscore		;+2
DACH1	LD R0, DACH0
	LDR R0, R0, #0
	ADD R0, R0, #2
	LD R3, DACH0
	STR R0, R3, #0
	BRnzp DISAG1
AL20	.FILL X000A
DISAG0 	.FILL DISAGREE
DISAG1	LD R5, DISAG0
	JMP R5

PEAS	LEA R0, PL57
	PUTS
PL57	.STRINGZ "'Excellent! Now, what I need done is a bit messy.  I have found through my sources the location of a rebel stockpile of grain from the last harvest. I need you to take a contingent of guards and claim it for the good of the realm... and also back taxes, I am clever' He smiles to himself (it's not a nice smile), 'And punish those involved. You will take Jenkins, whom you've already met and four of his men, that should do fine. Return with the grain, kill the peasants.'\nAnd with that he motions you out. You meet Jenkins outside and he tells you that the other men will be ready momentarily and he is having a horse saddled for you.\nYou and the group of soldiers ride out of town about three miles East and come to a small farm. You dismount and approach the main building. As you approach a man comes outside brandishing a pitchfork and shouting 'What's this all about then?'\nJenkins turns to you, 'the report says that this family is hiding grain for the resistance under their barn floor, how do you want to proceed?'"
	LD R0, AL21
	OUT
	LEA R0, PL58
PL58 .STRINGZ "(1) Ask the man 'If we might search her barn for cotraband goods we've been tipped off to'"
	PUTS
	LD R0, AL21
	OUT
	LEA R0, PL59
PL59 .STRINGZ "(2) 'Restrain the man and search the barn forcefully'"
	PUTS
	LD R0, AL21
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz KIND
	FOCH0 .FILL Mscore		;-1
FOCH1	LD R0, FOCH0
	LDR R0, R0, #0
	ADD R0, R0, #-1
	LD R3, FOCH0
	STR R0, R3, #0
	BRnzp FORCE1
AL21	.FILL X000A
FORCE0 	.FILL FORCE
FORCE1	LD R5, FORCE0
	JMP R5

KIND	LEA R0, PL60
	PUTS
PL60	.STRINGZ "The man sighs in a way that makes you think he's seen this all before. 'Alright then, but please don't hurt my family, we've done nothing wrong.'\nYou leave two soldiers guarding the man and take the rest to search the barn.  You search thoroughly and find a hidden area under a pile of hay, but it's empty.  One of the soldiers does find a one sack of flour in the loft.\n'This 'ere is untaxed goods, this is.' he says, flinging the sack down.\nJenkins turns to you with a sneer of disgust on his face 'I know these people are helping the resistance, look at the smugglers hole they have.' We should question them then kill them as an example to the rest.'"
BARN	LD R0, AL22
	OUT
	LEA R0, PL61
PL61 .STRINGZ "(1) Don't listen to Jenkins, go back outside and tell the guards to let the man go back to his business"
	PUTS
	LD R0, AL22
	OUT
	LEA R0, PL62
PL62 .STRINGZ "(2) Jenkins is right, these people need to be made an example"
	PUTS
	LD R0, AL22
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz LETGO
	EVCH0 .FILL Mscore		;-6
EVCH1	LD R0, EVCH0
	LDR R0, R0, #0
	ADD R0, R0, #-6
	LD R3, EVCH0
	STR R0, R3, #0
	BRnzp BRUTE1
AL22	.FILL X000A
BRUTE0 	.FILL BRUTE
BRUTE1	LD R5, BRUTE0
	JMP R5

LETGO	LEA R0, PL63
	PUTS
PL63	.STRINGZ "'These people know nothing, we should let them get back to their hard lives.' you say to Jenkins.\n'And the bag of grain?' asks the guard that found it.\n'Leave the miserable little thing' you respond.\n'Now wait just one moment' says Jenkins 'I was given express orders to assist in making these people an example, who gives a dam if they were actually with the rebels or not? We can always say they were. And as for you, I'm beginning to doubt your commitment to our task here.'"
	LD R0, AL23
	OUT
	LEA R0, PL64
PL64 .STRINGZ "(1) Run out of the barn, steal one of the horses and ride off as fast as you can?"
	PUTS
	LD R0, AL23
	OUT
	LEA R0, PL65
PL65 .STRINGZ "(2) Attack the soldiers and try to give the peasant family enough time to run for the woods"
	PUTS
	LD R0, AL23
	OUT
	GETC
	OUT
	AND R5, R5, #0
	ADD R5, R0, R6
	BRz FAILDR
	PTCH0 .FILL Mscore		;+3
PTCH1	LD R0, PTCH0
	LDR R0, R0, #0
	ADD R0, R0, #2
	LD R3, PTCH0
	STR R0, R3, #0
	BRnzp PROTP1
AL23	.FILL X000A
PROTP0 	.FILL PROTP
PROTP1	LD R5, PROTP0
	JMP R5

FAILDR	LEA R0, PL66
	PUTS
PL66	.STRINGZ "You shrug, then when Jenkins turns away you sprint for the door, knocking down one of the guards on the way. Jenkins yells 'Stop him!'\nYou get outside, but as you run toward the horses the soldiers who were guarding the man move to intercept you.  You draw your sword and manage to wound one of them, but by now the rest of the soldiers have come out of the barn and encircled you.  As you pivot to try to keep them all at bay, you hear one directly behind you step forward and the pommel of his sword crashes down on your head.  As the world fades out you see the farmer and his family far off, close to the treeline... at least they got away."
	BRnzp SLP31
SLP30	.FILL SLP
SLP31	LD R5, SLP30
	JMP R5
;;; Tie into PRSN story.

PROTP	LEA R0, PL67
	PUTS
PL67	.STRINGZ "You shrug and say 'As his lord commands.' But when Jenkins turns around you punch him and sprint from the barn, drawing your sword.  The two soldiers guarding the peasant man see you coming and both draw their swords and step forward to engage.\n'Run!' you yell at the man 'Get your family out of here, I'll hold them off!' and you begin to fence with the two soldiers.\nYou manage to wound one of them, but by now the rest of the soldiers have come out of the barn and encircled you, including a very angry looking Jenkins.  As you pivot to try to keep them all at bay, you hear one directly behind you step forward and the pommel of his sword crashes down on your head.  As the world fades out you see the farmer and his family far off, close to the treeline... at least they got away."
	BRnzp SLP41
SLP40	.FILL SLP
SLP41	LD R5, SLP40
	JMP R5
;;; Tie into PRSN story.
	
BRUTE 	LEA R0, PL68
	PUTS
PL68	.STRINGZ "Well, player, this is a rather sociopathic choice you've made.  The details are rather unpleasant, so suffice to say you do what's asked of you and go back to the castle for your reward. Lord Crake pays you for your services, and you go on your way."
	BRnzp SUNRI21
SUNRI20	.FILL SUNRIS
SUNRI21	LD R5, SUNRI20
	JMP R5
	
FORCE	LEA R0, PL69
	PUTS
PL69	.STRINGZ "You tell two of the soldiers to grab the man, and you inform him that he has no choice in the matter of your search. You take the rest of the men and search thoroughly and find a hidden area under a pile of hay, but it's empty.  One of the soldiers does find a one sack of flour in the loft.\n'This 'ere is untaxed goods, this is.' he says, flinging the sack down.\nJenkins turns to you with a sneer of disgust on his face 'I know these people are helping the resistance, look at the smugglers hole they have.' We should question them then kill them as an example to the rest.'"
	BRnzp BARN1
BARN0	.FILL BARN
BARN1 	LD R5, BARN0
	JMP R5
	;; Ties back into end of this story.

DISAGREE LEA R0, PL70
	PUTS
PL70	.STRINGZ "'Aha! Well if you're not with me you're against me. Might is right! Everyone knows this.  I'll have you hanged for treason! Guards! Guards!'\nAnd before you have time to drag your sword out or even say 'But wait...' or 'On second thought...' you're surrounded by guards in the castle regalia.  Just as you raise your fists in self defense a heavy mailed hand crunches down on the back of your head.  As your vision fades out you see Lord Crake standing there, a cruel malicious smile on his face."
	BRnzp SLP51
SLP50	.FILL SLP
SLP51	LD R5, SLP50
	JMP R5
;;; Tie into PRSN story.	
	
CASTLEN LEA R0, PL71
	PUTS
PL71	.STRINGZ "You take the note and you walk briskly up to the castle.  At the gate you tell the sergeant that you have information for his lordship.  The sergeant asks you to wait one moment and walks acros the yard and disappears into the keep.  A minute or so later he comes briskly back and tells you to follow him.  As you walk across the yard you see a couple of covered wagons with what look like bags of grain in them.  You enter the keep and down a wide corridor you enter the lord's study.  Lord Crake is a rather gaunt looking man with cold blue eyes that are constantly shifting. The sergeant salutes and leaves.\n'It isn't too often I have a stranger in my halls.  What's your name then?'\n'"
	BRnzp Sk5
	YOURNAME5 .FILL SAVE
Sk5	LDI R0, YOURNAME5
	PUTS
	LEA R0, PL710
	PUTS
PL710 .STRINGZ " is my name, my lord.'\n'Ah, indeed. Jenkins tells me you claim to have information useful to me.' Crake says.\nYou hand him the note which he slits open with the authority and impunity of a lord.  As he reads the contents he asks 'Where did you get this?'\n'From Vania the apothecary, it was meant for Wendil your cook'\n'Ah, I see, Vania has been under light surveillance, but Wendil... now there's a rat we didn't know about. I will have them dealt with. Thank you, you have been most helpful.  I wonder, would you be willing to help me with a task to prove your loyalty? This note has given us some useful information that I need followed."
	BRnzp HECR1
HECR0	.FILL HECR
HECR1	LD R5, HECR0
	JMP R5

CASTLER	LEA R0, PL72
	PUTS
PL72	.STRINGZ "You take the note and you walk briskly up to the castle.  At the gate you tell the sergeant that you have information for his lordship.  The sergeant asks you to wait one moment and walks acros the yard and disappears into the keep.  A minute or so later he comes briskly back and tells you to follow him.  As you walk across the yard you see a couple of covered wagons with what look like bags of grain in them.  You enter the keep and down a wide corridor you enter the lord's study.  Lord Crake is a rather gaunt looking man with cold blue eyes that are constantly shifting. The sergeant salutes and leaves.\n'It isn't too often I have a stranger in my halls.  What's your name then?'\n'"
	BRnzp Sk6
	YOURNAME6 .FILL SAVE
Sk6	LDI R0, YOURNAME6
	PUTS
	LEA R0, PL720
	PUTS
PL720 .STRINGZ " is my name, my lord.'\n'Ah, indeed. Jenkins tells me you claim to have information useful to me.' Crake says.\nYou tell him all about Vania and Wendil, and the note, and the curious phrase 'The sparrow is in the hawk's nest.'\n'Interesting... Vania has been under light surveillance, we have thought she might be part of the resistance for some time, but Wendil... now there's a rat we didn't know about. I will have them dealt with. Thank you, you have been most helpful in providing this information to foil whatever plot they were cooking.  I wonder, would you be willing to help me with a task to prove your loyalty? This note has given us some useful information that I need followed."
	BRnzp HECR3
HECR2	.FILL HECR
HECR3	LD R5, HECR2
	JMP R5
	

PRSN HALT			;Prisoner Escape story

	
SUNRIS LEA R0, END1
	PUTS
END1	.STRINGZ "You wander off into the sunrise. Were all of your choices good? Did you take the easy route or the right one?"
	HALT

.END
