___

{
	"name": "three in a row",
	"description": "three pieces diagonal, vertical, or horizontal wins the game"
	"condition":  \[
		{
			"condition-type": "PIECE_LOCATION",
			"number-of-pieces": "3",
			"orientation": "VERTICAL",
			"sequence": "ADJACENT"
		},
		{
			"condition-type": "PIECE_LOCATION",
			"number-of-pieces": "3",
			"orientation": "HORIZONAL",
			"sequence": "ADJACENT"
		},
		{
			"condition-type": "PIECE_LOCATION",
			"number-of-pieces": "3",
			"orientation": "DIAGONAL",
			"sequence": "ADJACENT"
		}
	]
}