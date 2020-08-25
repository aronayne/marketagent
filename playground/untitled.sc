val s = ",,,,,,,,"
s.split("," , -1).map(m => {
  if(m.equals("")){
    "-"
  }
  else {
    m
  }
}).mkString(",").replace("," , "")