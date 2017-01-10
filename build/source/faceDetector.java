import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.io.File; 
import java.net.URI; 
import org.apache.http.HttpEntity; 
import org.apache.http.HttpHeaders; 
import org.apache.http.HttpResponse; 
import org.apache.http.client.HttpClient; 
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.client.utils.URIBuilder; 
import org.apache.http.entity.ContentType; 
import org.apache.http.entity.FileEntity; 
import org.apache.http.impl.client.HttpClients; 
import org.apache.http.util.EntityUtils; 
import processing.video.*; 

import org.apache.commons.logging.impl.*; 
import org.apache.commons.logging.*; 
import org.apache.http.auth.*; 
import org.apache.http.auth.params.*; 
import org.apache.http.cookie.*; 
import org.apache.http.cookie.params.*; 
import org.apache.http.client.*; 
import org.apache.http.client.methods.*; 
import org.apache.http.client.params.*; 
import org.apache.http.client.utils.*; 
import org.apache.http.client.protocol.*; 
import org.apache.http.client.config.*; 
import org.apache.http.client.entity.*; 
import org.apache.http.conn.*; 
import org.apache.http.conn.routing.*; 
import org.apache.http.conn.params.*; 
import org.apache.http.conn.socket.*; 
import org.apache.http.conn.ssl.*; 
import org.apache.http.conn.scheme.*; 
import org.apache.http.conn.util.*; 
import org.apache.http.impl.auth.*; 
import org.apache.http.impl.cookie.*; 
import org.apache.http.impl.client.*; 
import org.apache.http.impl.execchain.*; 
import org.apache.http.impl.conn.*; 
import org.apache.http.impl.conn.tsccm.*; 
import org.apache.http.message.*; 
import org.apache.http.concurrent.*; 
import org.apache.http.*; 
import org.apache.http.annotation.*; 
import org.apache.http.params.*; 
import org.apache.http.protocol.*; 
import org.apache.http.ssl.*; 
import org.apache.http.pool.*; 
import org.apache.http.config.*; 
import org.apache.http.util.*; 
import org.apache.http.impl.*; 
import org.apache.http.impl.bootstrap.*; 
import org.apache.http.impl.pool.*; 
import org.apache.http.impl.io.*; 
import org.apache.http.impl.entity.*; 
import org.apache.http.io.*; 
import org.apache.http.entity.*; 
import org.apache.http.entity.mime.*; 
import org.apache.http.entity.mime.content.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class faceDetector extends PApplet {















Capture cap;

String result = null;
boolean isDetected = false;
float square[] = new float[4];
float react[] = new float[7];
String r[] = {"anger", "contempt", "disgust", "happiness", "neutral", "sadness", "surprise"};

int width = 480; int height = 320;

public void settings() {
  size (width, height*2);

}
public void setup(){
  cap = new Capture(this, width, height, "FaceTime HD Camera");
  cap.start();
}

public void draw()
{
  cap.read();
  image(cap, 0, 0);

  if(isDetected) {
    // \u8868\u793a\u304c\u305d\u308c\u3063\u307d\u304f\u306a\u308b\u3088\u3046\u306b\u5ea7\u6a19\u3092\u6307\u5b9a
    // background of text
    fill(255,128,72);
    rect( square[0]+(square[2]/2)-50, square[1]+square[3]+height, 100, 20, 5);

    // writting max reaction
    fill(255);  String mr = maxreact();
    text(mr, square[0]+(square[2]/2)-3*mr.length(), square[1]+square[3]+height+15);

    // rectangle(face)
    noFill();
    strokeWeight(3); stroke(255, 128, 72);
    rect(square[0], height+square[1], square[2], square[3], 10);
  }
}

public void captureEvent(Capture cap) {
  cap.read();
}

public void keyPressed()
{
  // repaint(gray)
  fill(128); noStroke();
  rect(0, height, width, height);

  // showSnapshot(filtered)
  tint(255,128);
  image(cap, 0, height);
  noTint();

  upload(cap);
}
public void upload(PImage img)
{
  img.save("data/tmp.png");

  try
  {
    URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/emotion/v1.0/recognize");
    URI uri = builder.build();
    HttpPost request = new HttpPost(uri);
    request.setHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
    request.setHeader("Ocp-Apim-Subscription-Key", "YOUR_SUBSCRIPTION_KEY");

    // Request body
    File upfile = new File(dataPath("tmp.png"));
    FileEntity fentity = new FileEntity(upfile, ContentType.create("application/octet-stream", "UTF-8"));
    request.setEntity(fentity);

    HttpClient httpclient = HttpClients.createDefault();
    HttpResponse response = httpclient.execute(request);
    HttpEntity entity = response.getEntity();

    if (entity != null)
    {
      String ret = EntityUtils.toString(entity);
      //println(ret);
      ret = ret.replace("[","").replace("]","");
      JSONObject json = parseJSONObject(ret);
      dispResult(json);

      isDetected = true; // detected!
    }
  }
  catch (Exception e)
  {
    System.out.println(e.getMessage());
  }
}
public String maxreact() {
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

public void dispResult(JSONObject json)
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "faceDetector" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
