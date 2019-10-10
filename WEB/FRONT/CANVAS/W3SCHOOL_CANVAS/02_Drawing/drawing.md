# HTML Canvas Drawing

## Draw on the Canvas With JS
  
All drawing on the HTML canvas must be done with JavaScript:
  
```
<script>
var canvas = document.getElementById("myCanvas");
var ctx = canvas.getContext("2d");
ctx.fillStyle = "#FF0000";
ctx.fillRect(0, 0, 150, 75);
</script>
```
  
### Step 1: Find the Canvas Element
  
First of all, you must find the `<canvas>` element.  
This is done by using the HTML DOM method getElementById():  
  
```
var canvas = document.getElementById("myCanvas");
```
  
## Step 2: Create a Drawing Object
  
Secondly, you need a drawing object for the canvas.  
The getContext() is a built-in HTML object, with properties and methods for drawing:  
  
```
var ctx = canvas.getContext("2d");
```
  
## Step 3: Draw on the Canvas
Finally, you can draw on the canvas.  
Set the fill style of the drawing object to the color red:  
  
```
ctx.fillStyle = "#FF0000";
```
  
The fillStyle property can be a CSS color, a gradient, or a pattern. The default fillStyle is black.  
The fillRect(x,y,width,height) method draws a rectangle, filled with the fill style, on the canvas:  
  
```
ctx.fillRect(0, 0, 150, 75);
```