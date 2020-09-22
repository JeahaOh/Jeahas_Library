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
    for (let i = 0, len = this.length; i < len; ++i) {
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
  try {
    //console.log( s );
    // convert s to arrayBuffer
    const buf = new ArrayBuffer(s.length);
    // create uint8array as viewer
    const view = new Uint8Array(buf);
    //convert to octet
    for (let i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xFF;
    //console.log('buf : ', buf);
    return buf;
  } catch (e) {
    console.error(e.message);
  }

}


//test delay
const delay = function (ms) {
  //return new Promise((resolve) => setTimeout(resolve, ms));
  return new Promise(function (resolve) {
    setTimeout(resolve, ms);
  });
}
/**
 * URL과 METHOD를 받아 동기 식으로 데이터를 불러온다.
 * 성공 여부와 데이터 배열을 담고있는 객체를 리턴한다.
 */
const getDataByAxios = async function (URL, METHOD, PARAMS) {
  METHOD = METHOD ? METHOD : 'POST';
  $('.modalbody').notify('데이터 가져오기 시작', 'info');
  //  테스트용. 로컬 속도가 너무 빨라서 시간 지연을 줌.
  await delay(3000);

  const result = {
    data: undefined,
    isSuccess: false
  };

  const data = await axios({
    url: URL,
    method: METHOD,
    params : PARAMS
  }).then(function (res) {
    //console.log(res);
    //console.log( typeof res.data );

    //  데이터가 빈 배열이거나 빈 객체라면 실패
    const isResultArray = Array.isArray(res.data) && res.data.length === 0;
    const isResultObject = typeof res.data == 'object' && Object.keys(res.data).length > 0;
    const isValidResult = isResultArray || isResultObject;

    if ( !isValidResult ) {
      !isTest && alert("조회된 데이터가 없습니다.");
      return result;
    }

    result.data = res.data;
    result.isSuccess = true;
    console.info('AXIOS RESULT : ', result);
    $('.modalbody').notify(result.data.length + '개의 데이터 가져오기 성공', 'info');

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
const requestWrapper = async function (URL, METHOD, PARAMS) {
  console.group('requestWrapper');
  URL = URL.trim();
  METHOD = METHOD.toUpperCase().trim();

  const isURLValid = typeof URL == 'string' && URL.length > 0;
  const isMethodValid = typeof METHOD == 'string' && (METHOD == 'GET' || METHOD == 'POST');

  if (!isURLValid || !isMethodValid) {
    console.error("Parameter 값이 유효하지 않음.");
    console.error(URL, isURLValid);
    console.error(METHOD, isMethodValid);
    console.groupEnd('requestWrapper');
    return;
  }

  const resData = await getDataByAxios(URL, METHOD, PARAMS)
    .then(function (res) {
      //  응답은 성공적으로 받았지만, 응답 안에 데이터가 없을 경우.
      if (!res.isSuccess) {
        console.error("requestWrapper FAIL!! response isSuccess is false!!");
        console.groupEnd('requestWrapper');
        throw new Error("requestWrapper FAIL");
      }
      //   성공했다면 응답 받은 데이터의 배열만 리턴한다.
      //console.log(res);
      console.info( 'response data length : ', res.data.length );
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

/**
 * XLSX을 만들 대상 JSON ARRAY를 받아,
 * 각 COLUMN 별 WIDTH를 계산하여 담은 ARRAY를 반환한다.
 * @param {*} json 
 */
const jsonSheetWidthMaker = async function(json) {
  //  우선 KEY 값의 길이 + 10인 WIDTH를 만든다.
  try {
    let widthArr = Object.keys(json[0]).map(key => {
      return { width: key.length + 10 }
    });
  
    //  KEY 값으로 만든 WIDTH 보다 VALUE 값이 길다면,
    //  VALUE 값의 길이에 맞는 WIDTH 를 만든다.
    for (let i = 0; i < json.length; i++) {
      let value = Object.values(json[i]);
      
      for (let j = 0; j < value.length; j++) {
        if (value[j] !== null && value[j].length > widthArr[j].width) {
          widthArr[j].width = (value[j].length + 10);
        }
      }
    }
    return widthArr
  } catch ( e ) {
    isTest && console.error( e );
  }
}

/**
 * 대상 <table/>의 id 값과 이름을 받아 sheet를 리턴한다.
 * @param {STRING} TABLE_ID   : html table id
 * @param {STRING} SHEETNAME  : sheet name
 */
const table2Sheet = async function (TABLE_ID, SHEETNAME) {
  console.group('table2Sheet');
  TABLE_ID = TABLE_ID.trim();
  SHEETNAME = SHEETNAME.replace('/', ',').trim();
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
  SHEETNAME = SHEETNAME.replace('/', ',').trim();
  const isSheetNameValid = typeof SHEETNAME == 'string' && SHEETNAME.length > 1;
  let isDataValidArray = Array.isArray(JSON_DATA);

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
    let widths = await jsonSheetWidthMaker(JSON_DATA);
    console.info('sheets widths : ', widths);

    const sheetData = await XLSX.utils.json_to_sheet(JSON_DATA);
    if(widths != null && typeof widths != 'undefined') {
      sheetData["!cols"] = widths;
    }


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
    //console.info(wbout);

    return wbout;
  } catch (e) {
    console.error(e);
  } finally {
    console.groupEnd('SHEETS2XLSX');
  }
}
//SHEETS2XLSX
