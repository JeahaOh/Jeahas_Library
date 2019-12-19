let svgWidth = 1000;
let svgHeight = 500;
let offsetX = 30;
let offsetY = 20;
let scale = 2.0;
// let margin = svgWidth / (dataSet.length - 1);

let dataSet = [
  { id: 'Unit_2004', data : [ 10, 90, 50 ] },
  { id: 'Unit_2005', data : [ 43, 77, 27 ] },
  { id: 'Unit_2006', data : [ 65, 55, 45 ] },
  { id: 'Unit_2007', data : [ 8, 48, 58 ] },
  { id: 'Unit_2008', data : [ 64, 64, 84 ] },
  { id: 'Unit_2009', data : [ 99, 90, 70 ] },
  { id: 'Unit_2010', data : [ 75, 85, 45 ] },
  { id: 'Unit_2011', data : [ 22, 62, 22 ] },
  { id: 'Unit_2012', data : [ 63, 13, 30 ] },
  { id: 'Unit_2013', data : [ 80, 40, 90 ] }
];

dataSet.forEach( (item, index, array) => {
  console.log( item.data )
  item.data.forEach( (it, idx, array) => {
    console.log( it, 'item' + idx, 'item' + index, )
    drawGraph( it, 'item' + idx, 'item' + index, 'basis' );
  });
});


// drawGraph( dataSet, 'item1', 'itemA', 'basis' );
// drawGraph( dataSet, 'item2', 'itemB', 'basis' );
// drawGraph( dataSet, 'item3', 'itemC', 'basis' );
// drawScale();

function drawGraph( dataSet, itemName, cssClassName, type ) {
  console.log( dataSet, itemName, cssClassName, type );
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

function drawScale( id ) {
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
  .domain([id])
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
dataSet.forEach( (item, index, array) => {
  console.log( item.data )
  drawScale(item.id)
});