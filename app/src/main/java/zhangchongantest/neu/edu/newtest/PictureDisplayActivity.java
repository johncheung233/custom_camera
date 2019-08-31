package zhangchongantest.neu.edu.newtest;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureDisplayActivity extends AppCompatActivity {
    @BindView(R.id.image_display)
    ImageView imageDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_display);
        ActionBar actionBar = getActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        File imageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Pictures/temp.jpg");
        if (!imageFile.exists()) {
            displayImgFail();
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            Bitmap imgBitmap = BitmapFactory.decodeStream(fis);
            fis.close();
            imageDisplay.setImageBitmap(imgBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            displayImgFail();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayImgFail() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
