/**
 * 서버로 작동시 내부망인지 아닌지 확인하기 위한 변수
 * true이면 내부망에서 돌리는 중.
 */
let isLocal = process.argv[3];
if( typeof(isLocal) === 'undefined' || isLocal.toString() !== 'true' ) isLocal = false;
else isLocal = true;

let PORT = process.argv[2];
if( typeof(PORT) !== 'number' || isNaN(PORT) ) PORT = 3000;


const http = require('http');
const fs = require('fs');
const querystring = require('querystring');
const path = require('path');
const dir = __dirname + '/tiles';
const vworldUrl = 'http://xdworld.vworld.kr:8080/2d/Base/service/';




const download = (url, dest, cb) => {
  let file = fs.createWriteStream(dest);
  let request = http.get( url, (res) => {
    res.pipe(file);
    
    file.on('finish', () => {
      file.close( cb );
    });
  }).on('error', (err) => {
    fs.unlink(dest);
    if( cb ) cb(err.message);
  })
}
//  MAX ZOOM = 19
//  MAX X = 


var app = http.createServer( function( request, response ) {
  var url = request.url;
  // console.log( url );
  let result;
  if( !isLocal && url.indexOf('tiles') > 0 ) {
    url = url.replace('/tiles/', '').replace('.png', '');
    let req = querystring.parse(url)
    let z = req.z, x = req.x, y = req.y;
    // console.log( 'z : ' + z );
    // console.log( 'x : ' + x );
    // console.log( 'y : ' + y );

    let tileDir = [
      dir + '/' + z,
      dir + '/' + z + '/' + x,
      dir + '/' + z + '/' + x + '/' + y + '.png'
    ];

    for( let zxy in tileDir ) {
      console.log( zxy + ' : ' + tileDir[zxy] )
      if( zxy != 2) {
        fs.access(tileDir[zxy], fs.F_OK, (err) => {
          console.log( zxy + ' not exist');
          fs.mkdirSync(tileDir[zxy]);
        })
        console.log( zxy + ' exist');
      } else if (zxy = 2) {
        /**/
        fs.access( tileDir[zxy], fs.F_OK, (err) => {
          if( err ) {
            //  file not exist
            console.log( 'y file not exist');
            // console.error( err );
            let originUrl = vworldUrl +  z + '/' + x + '/' + y + '.png';
            console.log( originUrl );
            download( originUrl, tileDir[zxy], null );
          }
          //  file exist
          console.log( 'y file exist');
          result = fs.readFileSync( tileDir[zxy] );
          response.writeHead( 200 );
          console.log( __dirname + url );
          console.log( 'result' );
          console.log( result );
          response.end( result );
          console.log( 'y file exist');
        });
        /**/
      }
    }

  }
  else if( request.url == '/favicon.ico' ) {
    response.writeHead( 200 );
    response.end(  );
    
  } else if( request.url == '/' ) {
    result = fs.readFileSync( __dirname + '/static/main.html' );
    response.writeHead( 200 );
    // response.end( result );
  } else {
    response.writeHead( 404 );
    response.end( '404' );
    return;
  }
  response.writeHead( 200 );
  console.log( __dirname + url );
  console.log( 'result' );
  console.log( result );
  response.end( result );
});



app.listen( 3000, function() {
  console.log( '=================== Map Tile Scrape And Servicer Start ===================');
  console.log( 'Service on local : ' + isLocal );
  console.log( 'Service Listen : ' + PORT );
  console.log( 'Service Root Dir : ' +  __dirname );
  console.log( '==========================================================================');
} );

// const url = 'http://gall.dcinside.com/board/view/?id=hit&no=13779';

// download(url, dir + 'filename.html', null)

