# Canvas Coordinates
The HTML canvas is a two-dimensional grid.  
The upper-left corner of the canvas has the coordinates (0,0)  
In the previous chapter, you saw this method used: fillRect(0,0,150,75).  
This means: Start at the upper-left corner (0,0) and draw a 150x75 pixels rectangle.  
  
## Coordinates Example
Mouse over the rectangle below to see its x and y coordinates:
  
```
마우스를 올리면 마우스의 좌표값을 보여주는 예제.
```
  
## Draw a Line
To draw a straight line on a canvas, use the following methods:
- moveTo(x,y) - defines the starting point of the line
- lineTo(x,y) - defines the ending point of the line
To actually draw the line, you must use one of the "ink" methods, like stroke().  

### Example : line.html
Define a starting point in position (0,0), and an ending point in position (200,100). Then use the stroke() method to actually draw the line:  
  
```
var canvas = document.getElementById("myCanvas");
var ctx = canvas.getContext("2d");
ctx.moveTo(0, 0);
ctx.lineTo(200, 100);
ctx.stroke();
```
  
## Draw a Circle
To draw a circle on a canvas, use the following methods:  
- beginPath() - begins a path
- arc(x,y,r,startangle,endangle) - creates an arc/curve.  
  To create a circle with arc(): Set start angle to 0 and end angle to 2*Math.PI.  
  The x and y parameters define the x- and y-coordinates of the center of the circle.  
  The r parameter defines the radius of the circle.
  
### Example : circle.html
Define a circle with the arc() method. Then use the stroke() method to actually draw the circle:  
  
```
var canvas = document.getElementById("myCanvas");
var ctx = canvas.getContext("2d");
ctx.beginPath();
ctx.arc(95, 50, 40, 0, 2 * Math.PI);
ctx.stroke();
```