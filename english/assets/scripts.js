
var currentId = 0;
var modeIds = ['start', 'subjunctive', 'conditional'];

function setNextId(){
  for(var i=0; i<modeIds.length; ++i){
    var val = document.getElementById(modeIds[i]).getBoundingClientRect().top;
    
    if(val > 0){
      currentId = i;
      break;
    }
  }
}
function setPrevId(){
  for(var i=modeIds.length-1; i>=0; --i){
    var val = document.getElementById(modeIds[i]).getBoundingClientRect().top;
    
    if(val < 0){
      currentId = i;
      return;
    }
  }
  
  currentId = modeIds.length-1;
}

function bDownClick(){
  var winHeight = Android.getWinHeight();
  
  console.log('values: ' + window.innerHeight + ', ' + window.scrollY + ', ' + document.body.scrollHeight + ' ' );
  console.log('winHeight: ' + winHeight);
  
  if ( (winHeight + window.scrollY) >= document.body.scrollHeight ){
    scroll(0, 0);
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
