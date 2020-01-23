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
//  CORS 허용을 위한 Middleware
const cors = require('cors');
const app = express();
//  응답으로 보내주는 tiles를 Gzip과 Cached로 보내어 Server의 부하를 막기 위한 설정.
const staticGzip = require('express-static-gzip');
//  <---------------- Server

//  -----------------> Scraper
const FS = require('fs');
const PATH = require('path');
const AXIOS = require('axios');

const destDir = __dirname + '\\tiles\\';
const originUrl = 'http://xdworld.vworld.kr:8080/2d/Base/service/';
//  <----------------- Scraper

// ----> Middleware

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
// <---- Middleware


const imgDownload = async function (url, dest, filename ) {
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

app.get('/tiles/:z/:x/:y', (req, res) => { 
  let params = req.url.replace('/tiles/', '').replace('.png', '').trim().split('/');
  let z = params[0], x = params[1], y = params[2];
  // console.log( '\nreq.url : ' + params + '\n' );
  console.log( '\n\tz : ' + z + '\n\tx : ' + x + '\n\ty : ' + y );
  let subDir = z + '\\' + x;
  let filename = subDir + '\\' + y +'.png';

  try {
    if(!FS.existsSync(destDir + filename) ) {
      //  요청 파일이 없다면 없는 파일이 무엇인지 보여줌.
      console.log( destDir + filename, FS.existsSync(destDir + filename) );

      //  없는 파일의 부모 경로가 있는지 확인
      if( !FS.existsSync( destDir + subDir) ) {
        // console.log( destDir + subDir );
        //  부모경로가 없다면 부모 경로 생성
        FS.mkdirSync(destDir + subDir, { recursive: true }, (err) => { console.log( err.message ); });
      }

      imgDownload( originUrl, destDir, filename )
      .then( (filename) => {
        // console.log( '\tFile Saving Complete : ' + filename );
        console.log( filename );
      })
      .catch( (filename) => {
        console.log( '\t!! SOMETHING CRASHED !! ' + filename + '\n' );
      });
    }
  } catch ( err ) {
    console.log( err );
  } finally {
    if( FS.existsSync(destDir + filename) ) res.sendFile( destDir + filename );
    else res.sendStatus(404);

    res.send();
    res.end();
  }
});


app.listen( 3000, function() {
  console.log("\u001b[2J\u001b[0;0H");
  console.log( '=================== Map Tile Scrape And Servicer Start ===================');
  console.log( 'Service on local : ' + isLocal );
  console.log( 'Service Listen : ' + PORT );
  console.log( 'Service Root Dir : ' +  __dirname );
  console.log( '==========================================================================');
} );
