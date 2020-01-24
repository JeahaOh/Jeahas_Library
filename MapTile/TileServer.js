//  ----------------> Server Variable ----------------
/**
 * 시작시 3번째 인자로 들어오는 값.
 * 서버로 작동시 내부망인지 아닌지 확인하기 위함.
 * true, t, local, l 이면 내부망에서 돌리는 중.
 */
let isLocal = process.argv[2];
if( typeof(isLocal) === 'undefined' ) isLocal = "false";
switch(isLocal.toLowerCase().trim()){
  case "true": case "t": case "local": case "l": case "-l": isLocal = true; break;
  default: isLocal = false;
}
/**
 * 시작시 4번째 인자로 들어오는 값.
 * PORT 번호를 정한다.
 * 지정해 주지 않는다면 3000번 포트로 자동 지정 된다.
 */
let PORT = process.argv[3];
PORT = parseInt(PORT);
if(typeof(PORT) !== 'number') PORT = 3000;
if( isNaN(PORT)) PORT = 3000;

const express = require('express');
//  응답으로 보내주는 tiles를 Gzip과 Cached로 보내어 Server의 부하를 막기 위한 설정.
const staticGzip = require('express-static-gzip');
//  CORS 허용을 위한 Middleware
const cors = require('cors');
const app = express();
//  <---------------- Server Variable ----------------

//  -----------------> Scraper Variable ----------------
//  서버 운영체제 종류에 따라 파일 경로 식별자가 달라지므로 사용.
const os = require('os');
const idtfr = ( os.type().toLowerCase().indexOf('windows') < 0 ) ? '/' : '\\';
//  아래 변수들은 내부망에서 사용할 경우 필요 없는 변수이므로 Local이라면 할당하지 않는다.
const FS = isLocal ? null : require('fs');
const PATH = isLocal ? null : require('path');
const AXIOS = isLocal ? null : require('axios');

const destDir = isLocal ? null : __dirname + idtfr + 'tiles' + idtfr;
const originUrl = isLocal ? null : 'http://xdworld.vworld.kr:8080/2d/Base/service/';
//  <---------------- Scraper Variable ----------------

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
//  Response File Compression.
app.use( '/tiles', staticGzip( __dirname + '/tiles', {
    enableBrotli: true,
    maxAge: 86400000,
    customCompressions: [{
      encodingName: 'deflate',
      fileExtension: 'gz'
    }],
    orderPreference: ['br'],
    serveStatic: {
      maxAge: 86400000,
      //  1000 - 1초
      //  1분은 60초
      //  1시간은 60분
      //  1일은 24시간
      cacheControl: true,
    },
  })
);
//  Static Files
app.use( '/', express.static( __dirname + '/static') );
app.use( '/tiles', express.static( __dirname + '/tiles') );

// <---------------- Middlewares ----------------

// ----------------- Scraper ---------------->
const imgDownload = isLocal ? null : async function (url, dest, filename ) {
  url = url + filename;
  // console.log( 'REQ TO : ' + url );
  let res;
  try {
    console.log( 'REQ TO ORIGIN --->\n' );
    res = await AXIOS({
      url,
      method: 'GET',
      responseType: 'stream'
    });
  } catch ( e ) {
    console.log( '\t!! ' + filename + '\tREQUEST ' + e.response.status + ' !!' );
  } finally {
    console.log( '\nRES FROM ORIGIN <---' );
  }

  if( typeof(res) !=='undefined' && res.status === 200 ) {
    console.log( 'res.status --> ' + res.status + '\n' );
    let path = PATH.resolve( dest, filename )
    let writer = FS.createWriteStream( path );
    res.data.pipe( writer );

    return new Promise( (resolve, reject) => {
      console.log( ' TO THEN OR CATCH ');
      writer.on('finish', resolve(filename));
      writer.on('error', reject(filename));
    });
  } else {
    return new Promise ( (resolve, reject) => {
      reject(res);
    });
  }
}

!isLocal && app.get('/tiles/:z/:x/:y', (req, res) => {
  let z = req.params.z, x = req.params.x, y = req.params.y;
  // console.log( '\nreq.url : ' + params + '\n' );
  console.log( '\n\n\tz : ' + z + '\tx : ' + x + '\ty : ' + y );
  let subDir = z + idtfr + x;
  // console.log( 'subDir : ' + subDir );
  let filename = subDir + idtfr + y //+ '.png';
  // console.log( 'filename : ' + filename );

  try {
    if(!FS.existsSync(destDir + filename) ) {
      //  요청 파일이 없다면 없는 파일이 무엇인지 보여줌.
      // console.log( destDir + filename, FS.existsSync(destDir + filename) );

      //  없는 파일의 부모 경로가 있는지 확인
      // console.log( destDir + subDir, FS.existsSync( destDir + subDir) );
      if( !FS.existsSync( destDir + subDir) ) {
        // console.log( destDir + subDir );
        //  부모경로가 없다면 부모 경로 생성
        FS.mkdirSync(destDir + subDir, { recursive: true }, (err) => { console.log( err.message ); });
      }

      imgDownload( originUrl, destDir, filename )
      .then( (filename) => {
        console.log( '\tFile Saving Complete : ' + filename );
        // console.log( filename );
      })
      .catch( (err) => {
        console.log( '\t!! SOMETHING CRASHED !! ' + err + '\n' );
      });
    }
  } catch ( err ) {
    console.log( 'err: ' + err );
  } finally {
    if( FS.existsSync(destDir + filename) ) res.sendFile( destDir + filename );
    else res.sendStatus(404); res.end();
  }
});
//  <----------------- Scraper ----------------


let status = "\u001b[2J\u001b[0;0H";
status += '============================ TileServer ON AIR ===========================\n';
status += 'Service At ' + ((os.type() == 'Darwin') ? 'Mac OS' : os.type()) + '\n';
status += 'Service On ' + ( isLocal ? 'Local' : 'Public' ) + '\n';
status += 'Service Listen : ' + PORT + '\n';
status += 'Service Root Dir : ' + __dirname + '\n';
status += '==========================================================================\n';

const CronJob = require('cron').CronJob;
// CronJob(작동 타이밍, 실행할 함수, 종료 시 실행할 함수, 자동 시작 여부, TimeZone);
const job = new CronJob('0 0/10 * * * *', () => console.log( status + new Date() ), null, true, 'Asia/Seoul');

app.listen( PORT, () => console.log( status + new Date() ) );

