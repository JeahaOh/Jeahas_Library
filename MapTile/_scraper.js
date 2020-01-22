// X

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


const download = (url, dest, filename, cb) => {
  //  경로가 없다면 만든다.
  !fs.existsSync(dest) && fs.mkdirSync(dest);

  console.log( '\ndownload URL : ' + url + filename );
  let request = http.get( url + filename, (res) => {
    console.log( 'res ---> ' + res.statusCode );
    if( res.statusCode === 200 ) {
      let file = fs.createWriteStream(dest + filename );
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



// download( originUrl, destDir, '10/889/413.png', null );

/*
for( let z = zMin; z <= zMax; z++ ) {
  // zDir = destDir + '\\' + z;
  // console.log( 'zDir : ' + zDir );
  // !fs.existsSync(zDir) && fs.mkdirSync(zDir);
  
  for( let x = xMin; x <= xMax; x++ ) {
    // xDir = zDir + '\\' + x;
    // console.log( 'xDir : ' + xDir );
    // !fs.existsSync(xDir) && fs.mkdirSync(xDir);

    for( let y = yMin; y <= yMax; y++ ) {
      // yFilename = xDir + '\\' + y + '.png';
      // console.log( 'yFilename : ' + yFilename );
      // if( !fs.existsSync(yFilename) ) {
      //   let url = originUrl + z + '/' + x + '/' + y + '.png';
      //   download( url, yFilename, null );
      // } 
      let filename = '' + z + '/' + x + '/' + y +'.png';
      !fs.existsSync(destDir + filename) && download( originUrl, destDir, filename, null );
    }
  }
}
*/
for( var i = 300; i <= 400; i++) {
  let filename = '10/889/' + i +'.png';
  !fs.existsSync(destDir + filename) && download( originUrl, destDir, filename, null );
}













