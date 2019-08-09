package com.example.picupload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {
	private Button mTakePhoto;
	private ImageView mImageView;
	private EditText mTextview1;
	private static final String TAG = "upload";
	private static String sku_id = "10000148";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 15)
		{
			StrictMode.ThreadPolicy policy = new
					StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		mTakePhoto = (Button) findViewById(R.id.take_photo);
		mImageView = (ImageView) findViewById(R.id.imageview);
        mTextview1 = (EditText) findViewById(R.id.editText1);
		mTakePhoto.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.take_photo:
		    sku_id = mTextview1.getText().toString();
			takePhoto();
			break;
		}
	}

	private void takePhoto() {
//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//		startActivityForResult(intent, 0);
		dispatchTakePictureIntent();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onActivityResult: " + this);
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
			setPic();
//			Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//			if (bitmap != null) {
//				mImageView.setImageBitmap(bitmap);
//				try {
//					sendPhoto(bitmap);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}
	}
	
	private void sendPhoto(Bitmap bitmap) throws Exception {
		new UploadTask().execute(bitmap);
	}

	private class UploadTask extends AsyncTask<Bitmap, Void, Void> {
		
		protected Void doInBackground(Bitmap... bitmaps) {
			Log.i(TAG, "before send request -- 1");
			if (bitmaps[0] == null)
				return null;
			setProgress(0);
			
			Bitmap bitmap = bitmaps[0];
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Log.i(TAG, "before send request -- 2");
			//bitmap.compress(Bitmap.CompressFormat.PNG,100, stream); // convert Bitmap to ByteArrayOutputStream
			Log.i(TAG, "before send request -- 3");
			InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream
			Log.i(TAG, "before send request -- 4");
			DefaultHttpClient httpclient = new DefaultHttpClient();
			try {
				HttpPost httppost = new HttpPost(
						"http://192.168.104.215:8081/uploads/simple/"); // server

				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("myfile",
						System.currentTimeMillis() + ".jpg", in);
				reqEntity.addPart("skuid" , "10000148");
				httppost.setEntity(reqEntity);

				Log.i(TAG, "request " + httppost.getRequestLine());
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
					Log.i(TAG, "doInBackground: "+response.toString());
					String resp_body = EntityUtils.toString(response.getEntity());
					Log.i("resp_body", resp_body.toString());
					JSONObject jsonbj = new JSONObject(resp_body);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (JSONException ex)
				{
					ex.printStackTrace();
				}
				try {
					if (response != null)
						Log.i(TAG, "response " + response.getStatusLine().toString());
				} finally {

				}
			} finally {

			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(MainActivity.this, R.string.uploaded, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume: " + this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
	}
	
	String mCurrentPhotoPath;
	
	static final int REQUEST_TAKE_PHOTO = 1;
	File photoFile = null;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File

	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	            		Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}

	/**
	 * http://developer.android.com/training/camera/photobasics.html
	 */
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
	    File dir = new File(storageDir);
	    if (!dir.exists())
	    	dir.mkdir();
	    
	    File image = new File(storageDir + "/" + imageFileName + ".jpg");

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
//		FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", image.getAbsolutePath());
	    Log.i(TAG, "photo path = " + mCurrentPhotoPath);
	    return image;
	}
	
	private void setPic() {
		// Get the dimensions of the View


	    int targetW = mImageView.getWidth();
	    int targetH = mImageView.getHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor << 1;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    
	    Matrix mtx = new Matrix();
	    mtx.postRotate(90);
	    // Rotating Bitmap
	    Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);

	    if (rotatedBMP != bitmap)
	    	bitmap.recycle();
	    
	    //mImageView.setImageBitmap(rotatedBMP);
	    
	    try {
			//sendPhoto(rotatedBMP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //nageswara
		JSONObject outputJsonbj = null;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			List<NameValuePair> nameValuePairs = new ArrayList<>(2);
			nameValuePairs.add(new BasicNameValuePair("sku_id", "1000148"));
			//RequestBody requestBody = createRequestForImage(file);
			//return MultipartBody.Part.createFormData("file_name", file.getName(), requestody);
			HttpPost httppost = new HttpPost(
					"http://192.168.104.215:8081/uploads/simple/");
			HttpParams params = new BasicHttpParams();
			//params.setParameter("sku_id" , "10000148");
            //HashMap<String, String> headers = httppost.getAllHeaders();
			httppost.setHeader("sku_id" , "10000148");

            Log.i("Headers" , httppost.getAllHeaders().toString());


			File image = new File(mCurrentPhotoPath);
			httppost.setEntity(new FileEntity(image, "jpg"));
			//httppost.setParams(nameValuePairs);
			HttpResponse response = null;
			try {
				Log.i(TAG, "Before Post Request..." + httppost);
				response = httpclient.execute(httppost);
				Log.i(TAG, "doInBackground: " + response.toString());
				String resp_body = EntityUtils.toString(response.getEntity());
				Log.i("resp_body", resp_body.toString());
				outputJsonbj = new JSONObject(resp_body);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		//ends


//test samp

		//bandi
		try {
			Log.i(TAG, "setPic: our code started");
			final Context context = this;
			Intent intent = new Intent(context, ResultActivity.class);
			//Create the bundle
			Bundle bundle = new Bundle();
			//Add your data from getFactualResults method to bundle
			//{"breadth": "10", "sku_id": "1234", "sku_desc": "10 Kg Atta", "height": "15", "length": "22", "file_path": "/media/1565318526653.jpg"}
			intent.putExtra("sku_id", sku_id);
			intent.putExtra("sku_desc", outputJsonbj.getString("sku_desc"));
			intent.putExtra("height", outputJsonbj.getString("height"));
			intent.putExtra("length", outputJsonbj.getString("length"));
			intent.putExtra("breadth", outputJsonbj.getString("breadth"));

			Log.i(TAG, "setPic: "+outputJsonbj);
			//Add the bundle to the intent
			intent.putExtras(bundle);
			context.startActivity(intent);
			Log.i(TAG, "setPic: our code ended");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		//
	}


}
