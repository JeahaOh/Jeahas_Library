/**
 * 시작시 4번째 인자로 들어오는 값.
 * PORT 번호를 정한다.
 * 지정해 주지 않는다면 3000번 포트로 자동 지정 된다.
 */
let PORT = process.argv[2];
if( typeof(PORT) !== 'number' || isNaN(PORT) ) PORT = 3000;
/**
 * 시작시 4번째 인자로 들어오는 값.
 * 당장은 사용하지 않는 변수.
 * 서버로 작동시 내부망인지 아닌지 확인하기 위함.
 * true이면 내부망에서 돌리는 중.
 */
let isLocal = process.argv[3];
if( typeof(isLocal) === 'undefined' || isLocal.toString() !== 'true' ) isLocal = false;
else isLocal = true;

const express = require('express');
const app = express();
//  응답으로 보내주는 tiles를 Gzip과 Cached로 보내어 Server의 부하를 막기 위한 설정.
const staticGzip = require('express-static-gzip');

// ----> Middleware
app.use( '/tiles', express.static( __dirname + '/tiles') );
app.use( '/tiles', staticGzip( __dirname + '/tiles', {
  enableBrotli: true,
  customCompressions: [{
      encodingName: 'deflate',
      fileExtension: 'zz'
  }],
  orderPreference: ['br']
}) );

app.use( '/', express.static( __dirname + '/static') );
// <---- Middleware

app.listen( 3000, function() {
  console.log("\u001b[2J\u001b[0;0H");
  console.log( '=================== Map Tile Scrape And Servicer Start ===================');
  console.log( 'Service on local : ' + isLocal );
  console.log( 'Service Listen : ' + PORT );
  console.log( 'Service Root Dir : ' +  __dirname );
  console.log( '==========================================================================');
} );
