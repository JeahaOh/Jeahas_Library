const http = require('http');
const fs = require('fs');
const path = require('path');
const dir = __dirname + '/tiles/';

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



const url = 'http://gall.dcinside.com/board/view/?id=hit&no=13779';

download(url, dir + 'filename.html', null)

console.log(  );