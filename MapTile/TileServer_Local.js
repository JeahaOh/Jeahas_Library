/**
 * 시작시 4번째 인자로 들어오는 값.
 * PORT 번호를 정한다.
 * 지정해 주지 않는다면 3000번 포트로 자동 지정 된다.
 */
let PORT = process.argv[3];
PORT = parseInt(PORT);
if(typeof(PORT) !== 'number') PORT = 3000;
if( isNaN(PORT)) PORT = 3000;
//  ----------------> Server Variable ----------------
const os = require('os');
const express = require('express');
//  CORS 허용을 위한 Middleware
const cors = require('cors');
const app = express();
//  <---------------- Server Variable ----------------


// ----------------> Middlewares ----------------
//  CORS Setting
app.use(cors({
  //  특정 도메인
  "origin": "*",
  //  허용 요청
  "methods": "GET",
  //  Pass the CORS preflight response to the next handler.
  "preflightContinue": true,
  //  Provides a status code to use for successful OPTIONS request
  "optionsSuccessStatus": 204,
  //  Configures the Access-Control-Max-Age CORS header
  "maxAge" : 3600
}));

//  Static Files
app.use( '/', express.static( __dirname + '/static') );
app.use( '/tiles/6', express.static( __dirname + '/tiles/6') );
app.use( '/tiles/7', express.static( __dirname + '/tiles/7') );
app.use( '/tiles/8', express.static( __dirname + '/tiles/8') );
app.use( '/tiles/9', express.static( __dirname + '/tiles/9') );
app.use( '/tiles/10', express.static( __dirname + '/tiles/10') );
app.use( '/tiles/11', express.static( __dirname + '/tiles/11') );
app.use( '/tiles/12', express.static( __dirname + '/tiles/12') );
app.use( '/tiles/13', express.static( __dirname + '/tiles/13') );
app.use( '/tiles/14', express.static( __dirname + '/tiles/14') );
app.use( '/tiles/15', express.static( __dirname + '/tiles/15') );
app.use( '/tiles/16', express.static( __dirname + '/tiles/16') );
app.use( '/tiles/17', express.static( __dirname + '/tiles/17') );
app.use( '/tiles/18', express.static( __dirname + '/tiles/18') );
app.use( '/tiles/19', express.static( __dirname + '/tiles/19') );
// <---------------- Middlewares ----------------

let status = "\u001b[2J\u001b[0;0H";
status += '============================ TileServer ON AIR ===========================\n';
status += 'Service At ' + ((os.type() == 'Darwin') ? 'Mac OS' : os.type()) + '\n';
status += 'Service Listen : ' + PORT + '\n';
status += 'Service Root Dir : ' + __dirname + '\n';
status += '==========================================================================\n';

const CronJob = require('cron').CronJob;
// CronJob(작동 타이밍, 실행할 함수, 종료 시 실행할 함수, 자동 시작 여부, TimeZone);
const job = new CronJob('0 0/30 * * * *', () => console.log( status + new Date() ), null, true, 'Asia/Seoul');

app.listen( PORT, () => console.log( status + new Date() ) );

// env PATH=$PATH:/Users/Jeaha/git/Jeahas_Library/MapTile/node_modules/.bin pm2 start TileServer.js -i max
// env PATH=$PATH:/Users/Jeaha/git/Jeahas_Library/MapTile/node_modules/.bin pm2 startup TileServer.js -i max
// env PATH=$PATH:/Users/Jeaha/git/Jeahas_Library/MapTile/node_modules/.bin pm2 start TileServer.js -i 4 --max-memory-restart 500M
// env PATH=$PATH:/Users/Jeaha/git/Jeahas_Library/MapTile/node_modules/.bin pm2 monit
// env PATH=$PATH:/Users/Jeaha/git/Jeahas_Library/MapTile/node_modules/.bin pm2 stop all
// pm2 logs || TileServer
// env PATH=$PATH:/Users/Jeaha/git/Jeahas_Library/MapTile/node_modules/.bin pm2 logs
// https://superuser.com/questions/587582/how-start-powershell-from-cmd-by-specific-path