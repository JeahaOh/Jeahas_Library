/**
 * 서버로 작동시 내부망인지 아닌지 확인하기 위한 변수
 * true이면 내부망에서 돌리는 중.
 */
let isLocal = process.argv[3];
if( typeof(isLocal) === 'undefined' || isLocal.toString() !== 'true' ) isLocal = false;
else isLocal = true;

let PORT = process.argv[2];
if( typeof(PORT) !== 'number' || isNaN(PORT) ) PORT = 3000;


// const http = require('http');
// const fs = require('fs');
// const querystring = require('querystring');
// const path = require('path');
// const dir = __dirname + '/tiles';

const express = require('express');
const staticGzip = require('express-static-gzip');
const app = express();


app.use( '/tiles', express.static( __dirname + '/tiles') );
app.use( '/tiles', staticGzip( __dirname + '/tiles', {
  enableBrotli: true,
  customCompressions: [{
      encodingName: 'deflate',
      fileExtension: 'zz'
  }],
  orderPreference: ['br']
}) );




app.listen( 3000, function() {
  console.log( '=================== Map Tile Scrape And Servicer Start ===================');
  console.log( 'Service on local : ' + isLocal );
  console.log( 'Service Listen : ' + PORT );
  console.log( 'Service Root Dir : ' +  __dirname );
  console.log( '==========================================================================');
} );
