# brainmotherfuck

It's like brainfuck but you have functions, oh and **_2 dimensions_**.

---

All standard BF code is compatible with BMF, and so we have the 8 vanilla commands

`>` : move the pointer to the right

`<` : move the pointer to the left

`+` : increase the value in the cell under the pointer by 1

`-` : decrease the value in the cell under the pointer by 1 <!-- NEGATIVITY IS LEGAL -->

`[` : jump to after the matching bracket if the cell under the pointer is 0

`]` : jump back to the matching bracket

`.` : output character with ascii value of the cell under the pointer

`,` : read ascii value of input character into cell under the pointer


### But this isn't your Mother's brainfuck.
---

We've also got

`^` : move the pointer upward <!-- OFF OF THE PLANE OF REALITY -->

`v` : move the pointer downward <!-- INTO THE DEPTHS OF ALL POSSIBILITY -->

### SYNTAX FOR FUNCTIONS

`(` : begin naming a function 

> All non-command characters will work in function names (though that does mean that you can't have names with lower case v) <!-- BRAINFUCK DOESN'T KNOW HOW TO LOVE -->

`)` : calls the function that has been named

> Don't call functions that don't exist. <!-- BRAINFUCK HAS NO IMAGINATION -->

For definitions, use

`:` : switches from a function name to a function definition

> In between parentheses, the colon jumps to the matching closing parenthesis and grabs all that's in between as the named function's definition.

So your function definiton will look something like this

`(expletive:++[>++++[^+++++v-]<-]>^....)`

and the matching function call is this

`(expletive)`

---
<!-- `#` : debug command that displays the current [x y] position and the value in the cell -->
