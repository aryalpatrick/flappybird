# flappybird
This is a single-player 2d game (replica of flappy bird) made using Java.
The player uses a mouse or keyboard to make the bird flap its wings and fly upward while it automatically moves along.

Libraries Used: Javax Swing, Java AWT, Java Util

Logic Building:
 The background moves along the 1st quadrant of the x axis to the second where the bird only moves along the Y-axis.
 The barriers are generated with random heights within a certain limit using random().
 The bird moves vertically if the spacebar is pressed otherwise the bird starts descending.
