let svgWidth = 320;
let svgHeight = 240;
let offsetX = 30;
let offsetY = 20;
let scale = 2.0;
let dataSet = [10, 47, 68, 8, 64, 99, 75, 22, 63, 80];
let margin = svgWidth / (dataSet.length - 1);

let line = d3.svg.line()
.x((d, i)=> {
  return offsetX + i * margin;
})
.y((d, i) => {
  return svgHeight - ( d * scale ) - offsetY;
})

let lineElement = d3.select('#myGraph')
.append('path')
.attr('class', 'line')
.attr('d', line( dataSet ))

let yScale = d3.scale.linear()
.domain([0, 100])
.range([scale * 100, 0])

d3.select('#myGraph')
.append('g')
.attr('class', 'axis')
.attr( 'transform', 'translate(' + offsetX + ', ' + offsetY + ')' )
.call(
  d3.svg.axis()
  .scale(yScale)
  .orient('left')
)

d3.select('#myGraph')
.append('rect')
.attr('class', 'axis_x')
.attr('width', svgWidth)
.attr('height', 1)
.attr('transform', 'translate(' + offsetX + ', ' + (svgHeight - offsetY - 0.5) +')')