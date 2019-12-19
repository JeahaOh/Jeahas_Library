let svgWidth = 320;
let svgHeight = 240;
let offsetX = 30;
let offsetY = 20;
let scale = 2.0;
let dataSet1 = [10, 47, 65, 8, 64, 99, 75, 22, 63, 80];
let dataSet2 = [90, 77, 55, 48, 64, 90, 85, 42, 13, 40];
let dataSet3 = [50, 27, 45, 58, 84, 70, 45, 22, 30, 90];
let margin = svgWidth / (dataSet1.length - 1);

drawGraph( dataSet1, 'itemA', 'linear');
drawGraph( dataSet2, 'itemB', 'basis' );
drawGraph( dataSet3, 'itemC', 'step' );
drawScale();

function drawGraph( dataSet, cssClassName, type ) {
  let line = d3.svg.line()
  .x((d, i)=> {
    return offsetX + i * margin;
  })
  .y((d, i) => {
    return svgHeight - ( d * scale ) - offsetY;
  })
  .interpolate(type)

  let lineElement = d3.select('#myGraph')
  .append('path')
  .attr('class', 'line ' + cssClassName )
  .attr('d', line( dataSet ))
}

function drawScale() {
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
}
