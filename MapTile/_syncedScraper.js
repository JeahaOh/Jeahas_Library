//  X

const http = require('http');
const fs = require('fs');

const zMin = 6, zMax = 19;
const xMin = 50, xMax = 447053;
const yMin = 0, yMax = 210332;

const originUrl = 'http://xdworld.vworld.kr:8080/2d/Base/service/';
const destDir = __dirname + '\\tiles\\';
let zDir, xDir, yFilename;
console.log("\u001b[2J\u001b[0;0H");
console.log( '=================== Map Tile Scraper Start ===================');
console.log( 'destDir : ' + destDir + '\n\n' );


const download = async (url, dest, filename, cb) => {
  //  경로가 없다면 만든다.
  !fs.existsSync(dest) && fs.mkdirSync(dest);

  console.log( '\ndownload URL : ' + url + filename );
  
  let request = http.get( url + filename, async (res) => {
    console.log( 'res ---> ' + res.statusCode );
    if( res.statusCode === 200 ) {
      let file = await fs.createWriteStream(dest + filename );
      res.pipe(file);
      
      file.on('finish', () => {
        console.log( '\tdownload Success' );
        console.log( 'save at : ' + file.path );
        console.log( 'file size : ' + file.bytesWritten + ' bytes' );
        console.log( '\n' )
        file.close( cb );
      });
    }
  }).on('error', (err) => {
    console.log( 'download on err : ' + err.name );
    console.log( err.message );
    fs.unlink(dest + filename);
    if( cb ) cb(err.message);
  })
  
}

for( let z = zMin; z <= zMax; z++ ) {
  
  for( let x = xMin; x <= xMax; x++ ) {

    for( let y = yMin; y <= yMax; y++ ) {
      let filename = '' + z + '/' + x + '/' + y +'.png';
      !fs.existsSync(destDir + filename) && download( originUrl, destDir, filename, null );
    }
  }
}

// for( var i = 360; i <= 420; i++) {
//   let filename = '10/889/' + i +'.png';
//   !fs.existsSync(destDir + filename) && download( originUrl, destDir, filename, null );
// }













