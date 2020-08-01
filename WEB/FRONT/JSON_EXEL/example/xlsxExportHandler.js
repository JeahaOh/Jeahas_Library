/**
 * @author: JEAHA
 * @at : 2020.07.08
 * @cmmt : 배열과 시트 이름, 파일 이름을 받아서 엑셀로 변환 다운로드 한다.
 * <script type="text/javascript" src="${pageContext.request.contextPath}/js/outer/sheet.js"></script>
 * <script type="text/javascript" src="${pageContext.request.contextPath}/js/outer/fileSaver.js"></script>
 * <script type="text/javascript" src="${pageContext.request.contextPath}/js/outer/xlsxExportHandler.js"></script>
 */
function exportExcel(data, sheetName, excelFileName) {
  //	엑셀 데이터를 압축하기 위한 함수.
  function s2ab(s) {
    //console.log( s );
    // convert s to arrayBuffer
    var buf = new ArrayBuffer(s.length);
    // create uint8array as viewer
    var view = new Uint8Array(buf);
    //convert to octet
    for (var i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xFF;
    //console.log( 'buf : ', buf);
    return buf;
  }
  console.group('exportExcel');
  //	입력 데이터에 대한 유효성 검사
  console.group('Validation');
  if (typeof sheetName !== 'string' && typeof excelFileName !== 'string') {
    alert('파일 이름 및 시트 이름이 문자열이 아님.')
    return;
  }
  var isArray = (data instanceof Array);
  if (!isArray || (isArray && data.length < 1)) {
    alert('데이터가 유효하지 않음.');
    return;
  }
  console.groupEnd('Validation');

  //	엑셀 데이터 생성 시작
  // step 1. workbook 생성
  var wb = XLSX.utils.book_new();
  //console.log( 'step 1 wb : '/*, wb */);

  // step 2. 시트 만들기 
  var newWorksheet = XLSX.utils.json_to_sheet(data);
  //console.log( 'step 2 newWorksheet : '/*, newWorksheet */);

  // step 3. workbook에 새로만든 워크시트에 이름을 주고 붙인다.  
  XLSX.utils.book_append_sheet(wb, newWorksheet, sheetName);
  //console.log( 'step 3' );

  // step 4. 엑셀 파일 만들기 
  var wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
  //console.log( 'step 4 : '/*, wbout*/);

  // step 5. 엑셀 파일 내보내기 
  saveAs(new Blob([s2ab(wbout)], { type: "application/octet-stream" }), excelFileName);
  //console.groupEnd('exportExcel');
}


const xlsxExportHandler = {
  MAKE_SHEET: function (data) {
    return XLSX.utils.json_to_sheet(data);
  }
}