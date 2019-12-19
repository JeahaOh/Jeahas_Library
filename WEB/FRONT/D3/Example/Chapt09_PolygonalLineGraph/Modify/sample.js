let svgWidth = 320;
let svgHeight = 240;
let offsetX = 30;
let offsetY = 20;
let scale = 2.0;

let dataSet = [
    { year : 2004, item1 : 10, item2 : 90, item3 : 50 },
    { year : 2005, item1 : 43, item2 : 77, item3 : 27 },
    { year : 2006, item1 : 65, item2 : 55, item3 : 45 },
    { year : 2007, item1 : 8, item2 : 48, item3 : 58 },
    { year : 2008, item1 : 64, item2 : 64, item3 : 84 },
    { year : 2009, item1 : 99, item2 : 90, item3 : 70 },
    { year : 2010, item1 : 75, item2 : 85, item3 : 45 },
    { year : 2011, item1 : 22, item2 : 62, item3 : 22 },
    { year : 2012, item1 : 63, item2 : 13, item3 : 30 },
    { year : 2013, item1 : 80, item2 : 40, item3 : 90 },
]
let margin = svgWidth / (dataSet.length - 1);

drawGraph( dataSet, 'item1', 'itemA', 'basis' );
drawGraph( dataSet, 'item2', 'itemB', 'basis' );
drawGraph( dataSet, 'item3', 'itemC', 'basis' );
drawScale();

function drawGraph( dataSet, itemName, cssClassName, type ) {
  let line = d3.svg.line()
  .x((d, i)=> {
    return offsetX + i * margin;
  })
  .y((d, i) => {
    return svgHeight - ( d[itemName] * scale ) - offsetY;
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
  
  let xScale = d3.scale.linear()
  .domain([2004, 2013])
  .range([0, svgWidth])

  d3.select('#myGraph')
  .append('g')
  .attr('class', 'axis')
  .attr('transform', 'translate(' + offsetX + ', ' + (svgHeight - offsetY) + ')')
  .call(
    d3.svg.axis()
      .scale( xScale )
      .orient('bottom')
      .ticks(5)
  )
}