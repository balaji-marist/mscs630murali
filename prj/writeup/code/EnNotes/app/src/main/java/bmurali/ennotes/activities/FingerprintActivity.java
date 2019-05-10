/*
* This activity will prompt the user for finger print authentication
*/
package bmurali.ennotes.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import bmurali.ennotes.R;

public class FingerprintActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fingerprint);

    FingerprintHandler fingerprintHandler;

    // No finger print support prior to marshmallow
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      FingerprintManager fingerprintManager =
          (FingerprintManager) this.getSystemService(FINGERPRINT_SERVICE);

      if (fingerprintManager != null) {
        // Check if Permission is granted
        if (ContextCompat.checkSelfPermission(
            this, Manifest.permission.USE_FINGERPRINT) !=
            PackageManager.PERMISSION_GRANTED) {
          Toast.makeText(this, "Please review finger print permissions " +
              "granted to this app", Toast.LENGTH_SHORT).show();
          finish();
        }
        else {
          fingerprintHandler = new FingerprintHandler(this);
          fingerprintHandler.startAuth(fingerprintManager, null);

          new Handler().postDelayed(() -> {
            fingerprintHandler.stopListeningAuthentication();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result",
                fingerprintHandler.fingerprintHandlerResult);
            setResult(Activity.RESULT_OK, returnIntent);
            //fingerprintDialog.dismiss();
            finish();
          }, 3000);
        }
      }
    }
  }
}
