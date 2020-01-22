const FS = require('fs');
const PATH = require('path');
const AXIOS = require('axios');

const destDir = __dirname + '\\tiles\\';
const originUrl = 'http://xdworld.vworld.kr:8080/2d/Base/service/';

// const zMin = 6, zMax = 19;
// const xMin = 50, xMax = 447053;
const yMin = 0, yMax = 210332;


let z = parseInt( process.argv[2] );
let x = parseInt( process.argv[3] );
let yStart = parseInt( process.argv[4] );
let yEnd = parseInt( process.argv[5] );

let nameList = [];

/*
!isNaN(yEnd) && console.log( 'required value is missing.' )
if( !isNaN(z) || !isNaN(x) || !isNaN(yStart) || !isNaN(yEnd) ) {
  console.log( 'required value is missing.' )
}
*/

console.log("\u001b[2J\u001b[0;0H");
console.log( '=================== Map Tile Scraper Start ===================');
console.log( 'zoom level : ' + z );
console.log( 'x level : ' + x );
console.log( 'y start : ' + yStart );
console.log( 'y end : ' + yEnd );
console.log( 'destDir : ' + destDir + '\n\n' );





const imgDownload = async function (url, dest, filename ) {
  url = url + filename;
  let res;
  try {
    res = await AXIOS({
      url,
      method: 'GET',
      responseType: 'stream'
    });
  } catch ( e ) {
    console.log( '\t!! ' + filename + '\tREQUEST ' + e.response.status + ' !!' );
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

z = 6;
x = 58;
yStart = 22;
yEnd = 26

const loop = async function() {
  for( let y = yStart; y <= yEnd; y++ ) {
    let filename = '' + z + '\\' + x + '\\' + y +'.png';
    if(!FS.existsSync(destDir + filename) ) {
      console.log( filename );
    // console.log( dest );
    // !FS.existsSync(dest) && FS.mkdirSync(dest);

      imgDownload( originUrl, destDir, filename )
      .then( (filename) => {
        // console.log( '\tFile Saving Complete : ' + filename );
        console.log( filename.replace('.png', '').trim() );
      })
      .catch( (filename) => {
        console.log( '\t!! SOMETHING CRASHED !! ' + filename + '\n' );
      });
    } 
  }
}
loop();
