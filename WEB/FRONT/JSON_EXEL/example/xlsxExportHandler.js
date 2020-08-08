/**
 * @author: JEAHA
 * @at : 2020.07.08
 * @cmmt : HTML TABLE 또는 AXIOS RESPONSED JSON 데이터로 엑셀 파일을 만들어 다운로드 시킨다.
 * <script type="text/javascript" src="${pageContext.request.contextPath}/js/outer/sheet.js"></script>
 * <script type="text/javascript" src="${pageContext.request.contextPath}/js/outer/fileSaver.js"></script>
 * <script type="text/javascript" src="${pageContext.request.contextPath}/js/outer/xlsxExportHandler.js"></script>
 */

//  Array.forEach 함수가 없을 경우 대비.
if (!Array.prototype.forEach) {
  Array.prototype.forEach = function (fn, scope) {
    for (var i = 0, len = this.length; i < len; ++i) {
      fn.call(scope || this, this[i], i, this);
    }
  }
}
//  Array.forEach

/**
 * String to ArrayBuffer
 * @param {*} String
 */
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


/**
 * 대상 <table/>의 id 값과 이름을 받아 sheet를 리턴한다.
 * @param {STRING} TABLE_ID   : html table id
 * @param {STRING} SHEETNAME  : sheet name
 */
const table2Sheet = async function (TABLE_ID, SHEETNAME) {
  console.group('table2Sheet');
  TABLE_ID = TABLE_ID.trim();
  SHEETNAME = SHEETNAME.trim();
  const isTableIDValid = typeof TABLE_ID == 'string' && TABLE_ID.length > 1;
  const isSheetNameValid = typeof SHEETNAME == 'string' && SHEETNAME.length > 1;

  if (!isTableIDValid || !isSheetNameValid) {
    console.error("Parameter 값이 유효하지 않음.");
    console.error(TABLE_ID, isTableIDValid);
    console.error(SHEETNAME, isSheetNameValid);
    console.groupEnd('table2Sheet');
    return;
  }

  try {
    $('.modalbody').notify('테이블 데이터 변환중', 'info');
    const targetTable = document.getElementById(TABLE_ID);
    const sheetData = await XLSX.utils.table_to_sheet(targetTable);

    const sheet = {
      data: sheetData,
      name: SHEETNAME
    }

    console.log(sheet);
    return sheet;
  } catch (e) {
    console.error(e.message);
  } finally {
    console.groupEnd('table2Sheet');
  }
}
//table2Sheet

/**
 * 배열 형태의 JSON 데이터와 이름을 받아 sheet를 리턴한다.
 * @param {*} JSON_DATA 
 * @param {*} SHEETNAME 
 */
const json2Sheet = async function (JSON_DATA, SHEETNAME) {
  console.group('json2Sheet');
  SHEETNAME = SHEETNAME.trim();
  const isSheetNameValid = typeof SHEETNAME == 'string' && SHEETNAME.length > 1;
  const isDataValidArray = Array.isArray(JSON_DATA);

  if (isDataValidArray) {
    JSON_DATA.forEach(function (data, idx) {
      $('.modalbody').notify((idx + 1) + '개의 데이터 유효성 검사중', 'info');
      if (!isDataValidArray) return false;
      if (typeof data != 'object') isDataValidArray = false;
    });
  }

  if (!isSheetNameValid || !isDataValidArray) {
    console.error("Parameter 값이 유효하지 않음.");
    console.error('isDataValidArray', isDataValidArray);
    console.error(SHEETNAME, isSheetNameValid);
    console.groupEnd('table2Sheet');
    return;
  }

  try {
    $('.modalbody').notify('Spread Sheet 생성중', 'info');
    const sheetData = await XLSX.utils.json_to_sheet(JSON_DATA);

    const sheet = {
      data: sheetData,
      name: SHEETNAME
    }

    console.log(sheet);
    return sheet;
  } catch (e) {
    console.error(e.message);
  } finally {
    console.groupEnd('json2Sheet');
  }

}
/**
 * [
 *   {SHEETDATA, SHEETNAME}
 *   , {SHEETDATA, SHEETNAME}
 *   , ...
 * ] 배열 형태의 sheet 데이터와 파일 이름을 받아 xlsx 파일로 변환한다.
 */
const SHEETS2XLSX = async function (SHEETS) {
  console.group('SHEETS2XLSX');
  $('.modalbody').notify('XLSX 파일 생성중', 'info');
  //  테스트용. 로컬 속도가 너무 빨라서 시간 지연을 줌.
  await delay(3000);
  const isSheetsArray = Array.isArray(SHEETS);

  if (!isSheetsArray) {
    console.error("Parameter 값이 유효하지 않음.");
    console.error('isSheetsArray', isSheetsArray);
    console.groupEnd('SHEETS2XLSX');
    return;
  }

  try {

    const wb = XLSX.utils.book_new();

    SHEETS.forEach(function (row, idx) {
      if (row.data.length < 1) return true;
      XLSX.utils.book_append_sheet(wb, row.data, row.name);
    });

    const wbout = await XLSX.write(wb, {
      bookType: 'xlsx',
      type: 'binary'
    });

    return wbout;
  } catch (e) {
    console.error(e);
  } finally {
    console.groupEnd('SHEETS2XLSX');
  }
}
//SHEETS2XLSX

//test delay
const delay = function (ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

/**
 * URL과 METHOD를 받아 동기 식으로 데이터를 불러온다.
 * 성공 여부와 데이터 배열을 담고있는 객체를 리턴한다.
 */
const getDataByAxios = async function (url, method) {
  method = method ? method : 'GET';
  $('.modalbody').notify('데이터 가져오기 시작', 'info');
  //  테스트용. 로컬 속도가 너무 빨라서 시간 지연을 줌.
  await delay(3000);

  const result = {
    data: undefined,
    isSuccess: false
  };

  const data = await axios({
    url: url,
    method: method
  }).then(function (res) {
    //console.log(res);

    //  데이터가 배열이 아니라면 실패.
    if (!Array.isArray(res.data) || res.data.length === 0) {
      return result;
    }

    result.data = res.data;
    result.isSuccess = true;
    console.info('AXIOS RESULT : ', result);
    $('.modalbody').notify('데이터 가져오기 성공', 'info');

    return result;
  }).catch(function (err) {
    //  요청에 실패했을 경우.
    console.error('getDataByAxios status : ', err.response.status);
    result.status = err.response.status;

    return result;
  });

  //  테스트용. 로컬 속도가 너무 빨라서 시간 지연을 줌.
  await delay(3000);

  return result;
}
//  getDataByAxios

/**
 * getDataByAxios를 에러 처리를 위해 한번 더 감싸는 함수.
 * axios로 데이터를 가져온 뒤,
 * 성공했을 경우 데이터를 반환한다.
 */
const requestWrapper = async function (URL, METHOD) {
  console.group('requestWrapper');
  URL = URL.trim();
  METHOD = METHOD.trim();
  const isURLValid = typeof URL == 'string' && URL.length > 1;
  const isMethodValid = typeof METHOD == 'string' && METHOD.length > 1;

  if (!isURLValid || !isMethodValid) {
    console.error("Parameter 값이 유효하지 않음.");
    console.error(URL, isURLValid);
    console.error(METHOD, isMethodValid);
    console.groupEnd('requestWrapper');
    return;
  }

  const resData = await getDataByAxios(URL, METHOD)
    .then(function (res) {
      //  응답은 성공적으로 받았지만, 응답 안에 데이터가 없을 경우.
      if (!res.isSuccess) {
        console.error("requestWrapper FAIL!!");
        console.groupEnd('requestWrapper');
        throw new Error("requestWrapper FAIL");
      }
      //   성공했다면 응답 받은 데이터의 배열만 리턴한다.
      //console.log(res);
      return res.data;
    }).catch(function (err) {
      //  요청이 실패했을 경우.
      console.groupEnd('requestWrapper');
      console.error('requestWrapper ON ERROR : ', err);
      return err;
    });

  console.groupEnd('requestWrapper');
  return resData;
}