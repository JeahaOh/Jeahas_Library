$.ajax({
  type : "POST",
  url : "your url",
  dataType : "json",
  data : "your Data",
  traditional : true,    // or false, your choice
  async : false,    // or true, your choice
  beforeSend : function(xhr, opts) {
      // when validation is false
      if (false) {
          xhr.abort();
      }
  },
  success : function() {
      // success code
  },
  error : function() {
      // error code
  }
});