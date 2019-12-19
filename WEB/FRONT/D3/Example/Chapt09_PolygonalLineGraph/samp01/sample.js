let svgWidth = 320;
let svgHeight = 240;
let dataSet = [10, 46, 68, 8, 64, 99, 85, 22, 63, 80];
let margin = svgWidth / (dataSet.length - 1);

let line = d3.svg.line()
.x((d, i)=> {
  return i * margin;
})
.y((d, i) => {
  return svgHeight - d;
})

let lineElement = d3.select('#myGraph')
.append('path')
.attr('class', 'line')
.attr('d', line( dataSet ))