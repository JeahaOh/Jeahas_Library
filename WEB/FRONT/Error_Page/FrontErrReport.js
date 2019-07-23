const getTimeStamp = function() {
  var d = new Date();
  var s = leadingZeros(d.getFullYear(), 4)
      + leadingZeros(d.getMonth() + 1, 2) + leadingZeros(d.getDate(), 2)
      + "_" + leadingZeros(d.getHours(), 2)
      + leadingZeros(d.getMinutes(), 2) + leadingZeros(d.getSeconds(), 2);
  return s;
};

const leadingZeros = function(n, digits) {
  var zero = '';
  n = n.toString();

  if (n.length < digits) {
    for (i = 0; i < digits - n.length; i++)
      zero += '0';
  }
  return zero + n;
}

window.onerror = function(msg, url, line) {
  console.log("MESSAGE : " + msg);
  console.log("URL : " + url);
  console.log("LINE NO. " + line);

  var $ref = document.referrer;
  var $date = getTimeStamp();
  var $pathname = location.pathname;
  var $query = location.search;
  var $trigger = $('#email').val;

  var err = {
    'date' : $date,
    'msg' : msg,
    'prv' : $ref,
    'pathname' : $pathname,
    'query' : $query,
    'trigger' : $trigger,
    'url' : url,
    'line' : line
  };
  
  console.log(err);
  
  $.ajax({
    url : "/api/err.do",
    contentType : "application/json",
    data : JSON.stringify(err),
    type : "POST",
    dataType : "json",
    processData : false,
    success : function(data) {
      console.log(data);
    },
    error : function(request, status, error) {
      console.log(request);
      console.log(status);
      console.log(error);
    }
  });
}