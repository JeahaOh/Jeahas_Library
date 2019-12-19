let svgWidth = 320;
let svgHeight = 240;
let offsetX = 30;
let offsetY = 20;
let scale = 2.0;
let dataSet = [
  [
    { year : 2004, value : 10 },
    { year : 2005, value : 43 },
    { year : 2006, value : 65 },
    { year : 2007, value : 8 },
    { year : 2008, value : 64 },
    { year : 2009, value : 99 },
    { year : 2010, value : 75 },
    { year : 2011, value : 22 },
    { year : 2012, value : 63 },
    { year : 2013, value : 80 },
  ],
  [
    { year : 2004, value : 90 },
    { year : 2005, value : 77 },
    { year : 2006, value : 55 },
    { year : 2007, value : 48 },
    { year : 2008, value : 64 },
    { year : 2009, value : 90 },
    { year : 2010, value : 85 },
    { year : 2011, value : 62 },
    { year : 2012, value : 13 },
    { year : 2013, value : 40 },
  ],
  [
    { year : 2004, value : 50 },
    { year : 2005, value : 27 },
    { year : 2006, value : 45 },
    { year : 2007, value : 58 },
    { year : 2008, value : 84 },
    { year : 2009, value : 70 },
    { year : 2010, value : 45 },
    { year : 2011, value : 22 },
    { year : 2012, value : 30 },
    { year : 2013, value : 90 },
  ],
]
let margin = svgWidth / (dataSet[0].length - 1);

drawGraph( dataSet[0], 'itemA', 'basis' );
drawGraph( dataSet[1], 'itemB', 'basis' );
drawGraph( dataSet[2], 'itemC', 'basis' );
drawScale();

function drawGraph( dataSet, cssClassName, type ) {
  let line = d3.svg.line()
  .x((d, i)=> {
    return offsetX + i * margin;
  })
  .y((d, i) => {
    return svgHeight - ( d.value *scale ) - offsetY;
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