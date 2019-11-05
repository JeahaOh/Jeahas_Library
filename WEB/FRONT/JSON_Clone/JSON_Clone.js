/**
 * Jeaha's JSON Copy Machine.
 * Version : v0.0.1
 * Lisence : Beerware
 * Contact : jeaha1217@gmail.com
 */
const copier = {
  shallowCopy : function(obj) {
    return Object.assign({}, obj);
  },
  spreadCopy : function(obj) {
    return { ...obj }
  },
  JSONClone : function(obj) {
    //  stringify -> parse : Deep Copy. but, very slow
    return JSON.parse( JSON.stringify(obj) );
  },
  reflexiveClone : function(obj) {
    //  Deep Copy by reflexive use. 
    let clone = {};
    for(var i in obj) {
      if(typeof(obj[i])=="object" && obj[i] != null)
          clone[i] = reflexiveClone(obj[i]);
      else
          clone[i] = obj[i];
    }
    return clone;
  }
}