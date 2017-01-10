void upload(PImage img)
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
