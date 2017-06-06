# Android Camera2 Secret Picture Taker (AC2SPT)
Take pictures secretly (without preview or launching device's camera app) using CAMERA2 API
## Preview
<img src="preview/demo.png" alt="preview android camera2 API secret picture taker" width="30%">

## Usage
```java
//implement  OnPictureCapturedListener to get pictures taken; count = NB AVAILABLE CAMERAS on the device
//implement  OnRequestPermissionsResultCallback in order to check for camera and external storage
//permissions because they are needed by the PictureService
public class MainActivity extends AppCompatActivity implements OnPictureCapturedListener, ActivityCompat.OnRequestPermissionsResultCallback {

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //check for camera and external storage permissions
        checkPermissions();
        final Button btn = (Button) findViewById(R.id.startCaptureBtn);
        //start capturing when clicking on the button
        btn.setOnClickListener(v ->
                //call PictureService#startCapturing method by passing the activity (this) 
                //and the OnPictureCapturedListener (this) in parameters
                new PictureService().startCapturing(this, this)
        );
    }
    //override this method to get a Map<PictureUrl, PictureData> 
     //it is called when we've done taking pictures from ALL AVAILABLE cameras
    //OR when NO camera was detected on the device
 @Override
    public void onDoneCapturingAllPhotos(TreeMap<String, byte[]> picturesTaken) {
        if (picturesTaken != null && !picturesTaken.isEmpty()) {
            picturesTaken.forEach((pictureUrl, pictureData) -> {
               //convert the byte array 'pictureData' to a bitmap (no need to read the file from the external storage) but in case you
               //want to then use 'pictureUrl' which stores the picture taken's location on the device
                final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
            });
            showToast("Done capturing all photos!");
            return;
        }
        showToast("No camera detected!");
    }

//override this method to get a couple (picture Url, picture Data)
//use this method if you don't want to wait for ALL pictures to be ready 
//(it is called when we've done taking picture from a single camera)
 @Override
    public void onCaptureDone(String pictureUrl, byte[] pictureData) {
        if (pictureData != null && pictureUrl != null) {
            runOnUiThread(() -> {
                //convert byte array 'pictureData' to a bitmap (no need to read the file from the external storage)
                final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
                //scale image to avoid POTENTIAL "Bitmap too large to be uploaded into a texture" when displaying into an ImageView
                final int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                final Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
               //do whatever you want with the bitmap or the scaled one
            });
            showToast("Picture saved to " + pictureUrl);
        }
    }

}
```
## Contributors

Hamed ZITOUN <zitoun.hamed@gmail.com>

## Help

If you run into issues, please don't hesitate to find help on the GitHub project.

## License

The android-camera2-secret-picture-taker is covered by the MIT License.

The MIT License (MIT)

Copyright (c) 2017 Hamed ZITOUN and contributors to the android-camera2-secret-picture-taker project.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

