
var currentId = 0;
var modeIds = ['start', 'subjunctive', 'conditional', 'imperative'];

function setNextId(){
  for(var i=0; i<modeIds.length; ++i){
    var val = document.getElementById(modeIds[i]).getBoundingClientRect().top;
    console.log("val[" + i + "]: " + val);
    
    if(val > 0){
      currentId = i;
      break;
    }
  }
}
function setPrevId(){
  for(var i=modeIds.length-1; i>=0; --i){
    var val = document.getElementById(modeIds[i]).getBoundingClientRect().top;
    console.log("val[" + i + "]: " + val);
    
    if(val < 0){
      currentId = i;
      return;
    }
  }
  
  currentId = modeIds.length-1;
}

function bDownClick(){
  // console.log('values: ' + window.innerHeight + ', ' + window.scrollY + ', ' + document.body.scrollHeight + ' ' );
  // console.log('values1: ' + document.body.scrollTop + ', ' + document.body.offsetHeight + ', ' + document.body.clientHeight + ' ' );
  // console.log('values2: ' + document.documentElement.scrollTop + ', ' + document.documentElement.offsetHeight + ', ' + document.documentElement.clientHeight + ' ' );
  // console.log('values3: ' + screen.height + ', ' + window.devicePixelRatio + ' ' );

  // if ( (window.innerHeight + window.scrollY) >= document.body.scrollHeight ){
  if ( (window.innerHeight + window.scrollY) >= document.body.scrollHeight ){
    scroll(0, 0);
    console.log('at-bottom');
    return;
  }
  
  setNextId();
  if(currentId == 0){
    currentId = 1;
  }
  
  var next = document.getElementById(modeIds[currentId]);
  next.scrollIntoView();
}
function bUpClick(){
  setPrevId();
  if(currentId == 0){
    scroll(0, 0);
    return;
  }
  
  var prev = document.getElementById(modeIds[currentId]);
  prev.scrollIntoView();
}
