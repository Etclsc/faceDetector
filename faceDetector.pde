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
Capture cap;

String result = null;
boolean isDetected = false;
float square[] = new float[4];
float react[] = new float[7];
String r[] = {"anger", "contempt", "disgust", "happiness", "neutral", "sadness", "surprise"};

int width = 480; int height = 320;

void settings() {
  size (width, height*2);

}
void setup(){
  cap = new Capture(this, width, height, "FaceTime HD Camera");
  cap.start();
}

void draw()
{
  cap.read();
  image(cap, 0, 0);

  if(isDetected) {
    // 表示がそれっぽくなるように座標を指定
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

void captureEvent(Capture cap) {
  cap.read();
}

void keyPressed()
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
