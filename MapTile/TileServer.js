/**
 * 시작시 3번째 인자로 들어오는 값.
 * 서버로 작동시 내부망인지 아닌지 확인하기 위함.
 * true, t, local, l 이면 내부망에서 돌리는 중.
 */
let isLocal = process.argv[2];
if( typeof(isLocal) === 'undefined' ) isLocal = "false";
switch(isLocal.toLowerCase().trim()){
   case "t": case "local": case "l": case "-l": isLocal = true; break;
   case "f": case "public": case "p": isLocal = false; break;
  default: Boolean(isLocal);
}

/**
 * 시작시 4번째 인자로 들어오는 값.
 * PORT 번호를 정한다.
 * 지정해 주지 않는다면 3000번 포트로 자동 지정 된다.
 */
let PORT = process.argv[3];
// console.log( 'parseInt(PORT) : ' + (parseInt(PORT)) + ' ' + PORT );
PORT = parseInt(PORT);
if(typeof(PORT) !== 'number') PORT = 3000;
// console.log( "(typeof(PORT) === 'number') : " + (typeof(PORT) !== 'number') + ' ' + PORT );
// console.log( '(isNaN(PORT)) : ' + (isNaN(PORT)) + ' ' + PORT );
if( isNaN(PORT)) PORT = 3000;

const express = require('express');
const app = express();
//  CORS 허용을 위한 Middleware
const cors = require('cors');
//  응답으로 보내주는 tiles를 Gzip과 Cached로 보내어 Server의 부하를 막기 위한 설정.
const staticGzip = require('express-static-gzip');
//  서버 운영체제 종류에 따라 파일 경로 식별자가 달라지므로 사용.
const os = require('os');
const idtfr = ( os.type().toLowerCase().indexOf('windows') < 0 ) ? '/' : '\\';
//  <---------------- Server

//  -----------------> Scraper
//  내부망에서 사용할 경우 필요 없는 변수이므로.
const FS = isLocal ? null : require('fs');
const PATH = isLocal ? null : require('path');
const AXIOS = isLocal ? null : require('axios');

const destDir = isLocal ? null : __dirname + idtfr + 'tiles' + idtfr;
const originUrl = isLocal ? null : 'http://xdworld.vworld.kr:8080/2d/Base/service/';
//  <----------------- Scraper

// ----> Middlewares

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
//  CORS Setting

app.use( '/tiles', express.static( __dirname + '/tiles') );
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

app.use( '/', express.static( __dirname + '/static') );
// <---- Middlewares


const imgDownload = isLocal ? null : async function (url, dest, filename ) {
  console.log( 'START IMG SCRAPE' );
  url = url + filename;
  let res;
  try {
    console.log( '---> INTO TRY' );
    res = await AXIOS({
      url,
      method: 'GET',
      responseType: 'stream'
    });
  } catch ( e ) {
    console.log( '\t!! ' + filename + '\tREQUEST ' + e.response.status + ' !!' );
  } finally {
    console.log( '<--- EXIT TRY' );
  }

  if( typeof(res) !=='undefined' && res.status === 200 ) {
    console.log( 'res --> ' + res.status );
    let path = PATH.resolve( dest, filename )
    let writer = FS.createWriteStream( path );
    
    res.data.pipe( writer );

    return new Promise( (resolve, reject) => {
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
  let params = req.url.replace('/tiles/', '').replace('.png', '').trim().split('/');
  let z = params[0], x = params[1], y = params[2];
  // console.log( '\nreq.url : ' + params + '\n' );
  console.log( '\n\n\tz : ' + z + '\n\tx : ' + x + '\n\ty : ' + y );
  let subDir = z + idtfr + x;
  // console.log( 'subDir : ' + subDir );
  let filename = subDir + idtfr + y +'.png';
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
        // console.log( '\tFile Saving Complete : ' + filename );
        // console.log( filename );
      })
      .catch( (filename) => {
        console.log( '\t!! SOMETHING CRASHED !! ' + filename + '\n' );
      });
    }
  } catch ( err ) {
    console.log( err );
  } finally {
    setTimeout( () => {
      // console.log( 'finally' );
      // console.log( res );
      // console.log( destDir + filename );
      if( FS.existsSync(destDir + filename) ) res.sendFile( destDir + filename );
      else res.sendStatus(404); res.end();
  
      // res.send();
      res.end();
    }, 2000);
  }
});


app.listen( PORT, function() {
  console.log("\u001b[2J\u001b[0;0H");
  console.log( '=================== Map Tile Scrape And Servicer Start ===================');
  console.log( 'Service At ' + ((os.type() == 'Darwin') ? 'Mac OS' : os.type()) );
  console.log( 'Service On ' + ( isLocal ? 'Local' : 'Public' ) );
  console.log( 'Service Listen : ' + PORT );
  console.log( 'Service Root Dir : ' +  __dirname );
  console.log( '==========================================================================');
} );