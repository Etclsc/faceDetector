String maxreact() {
  float max = 0;
  String maxCond = "None";
  for(int i = 0; i < 7; i++) {
    if(react[i] > max) {
      maxCond = r[i];
    }
    max = Math.max(max, react[i]);
  }
  return maxCond+":"+(int)(max*100)+"[%]";
}

void dispResult(JSONObject json)
{
  JSONObject fr = json.getJSONObject("faceRectangle");
  square[0] = fr.getFloat("left");
  square[1] = fr.getFloat("top");
  square[2] = fr.getFloat("width");
  square[3] = fr.getFloat("height");
  println("rect:["+square[0]+" "+square[1]+" "+square[2]+" "+square[3]+"]");

  JSONObject sc = json.getJSONObject("scores");
  result
  = "anger: " + sc.getFloat("anger")
  + "\ncontempt: " + sc.getFloat("contempt")
  + "\ndisgust: " + sc.getFloat("disgust")
  + "\nhappiness: " + sc.getFloat("happiness")
  + "\nneutral: " + sc.getFloat("neutral")
  + "\nsadness: " + sc.getFloat("sadness")
  + "\nsurprise: " + sc.getFloat("surprise");
  println(result);

  react[0] = sc.getFloat("anger");
  react[1] = sc.getFloat("contempt");
  react[2] = sc.getFloat("disgust");
  react[3] = sc.getFloat("happiness");
  react[4] = sc.getFloat("neutral");
  react[5] = sc.getFloat("sadness");
  react[6] = sc.getFloat("surprise");
}
