package im.delight.android.examples.webview;

import im.delight.android.webview.AdvancedWebView;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.widget.Toast;
import android.webkit.WebView;
import android.view.View;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity implements AdvancedWebView.Listener {

	private AdvancedWebView mWebView;

	private void emulateClick(final WebView webview) {
		long delta = 100;
		long downTime = SystemClock.uptimeMillis();
		float x = webview.getLeft() + webview.getWidth()/2; //in the middle of the webview
		float y = webview.getTop() + webview.getHeight()/2;

		final MotionEvent downEvent = MotionEvent.obtain( downTime, downTime + delta, MotionEvent.ACTION_DOWN, x, y, 0 );
		// change the position of touch event, otherwise, it'll show the menu.
		final MotionEvent upEvent = MotionEvent.obtain( downTime, downTime+ delta, MotionEvent.ACTION_UP, x+10, y+10, 0 );

		webview.post(new Runnable() {
			@Override
			public void run() {
			if (webview != null) {
				webview.dispatchTouchEvent(downEvent);
				webview.dispatchTouchEvent(upEvent);
			}
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mWebView = (AdvancedWebView) findViewById(R.id.webview);
		mWebView.setListener(this, this);
		mWebView.setGeolocationEnabled(false);
		mWebView.setMixedContentAllowed(false);
		mWebView.setCookiesEnabled(true);
		mWebView.setThirdPartyCookiesEnabled(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				emulateClick(view);
			}
		});
		mWebView.addHttpHeader("X-Requested-With", "Android");
		mWebView.loadUrl(getResources().getString(R.string.app_url));
	}

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		// mWebView.onResume();
		// ...
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		// mWebView.onPause();
		// ...
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mWebView.onDestroy();
		// ...
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		mWebView.onActivityResult(requestCode, resultCode, intent);
		// ...
	}

	@Override
	public void onBackPressed() {
		if (!mWebView.onBackPressed()) { return; }
		// ...
		super.onBackPressed();
	}

	@Override
	public void onPageStarted(String url, Bitmap favicon) {
		mWebView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onPageFinished(String url) {
		mWebView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPageError(int errorCode, String description, String failingUrl) {
		Toast.makeText(MainActivity.this, "onPageError(errorCode = "+errorCode+",  description = "+description+",  failingUrl = "+failingUrl+")", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
		Toast.makeText(MainActivity.this, "onDownloadRequested(url = "+url+",  suggestedFilename = "+suggestedFilename+",  mimeType = "+mimeType+",  contentLength = "+contentLength+",  contentDisposition = "+contentDisposition+",  userAgent = "+userAgent+")", Toast.LENGTH_LONG).show();

		/*if (AdvancedWebView.handleDownload(this, url, suggestedFxilename)) {
			// download successfully handled
		}
		else {
			// download couldn't be handled because user has disabled download manager app on the device
		}*/
	}

	@Override
	public void onExternalPageRequest(String url) {
		Toast.makeText(MainActivity.this, "onExternalPageRequest(url = "+url+")", Toast.LENGTH_SHORT).show();
	}
}
