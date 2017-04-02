package com.springer.patryk.uam_android.picture;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;

import com.springer.patryk.uam_android.BasePresenter;
import com.springer.patryk.uam_android.BaseView;

/**
 * Created by Patryk on 2017-03-22.
 */

public class SendPictureContract {

    interface View extends BaseView<Presenter> {

        void setImageResource(Bitmap bmp);

    }

    interface Presenter extends BasePresenter {

        void savePicture(String userId, Location location, boolean isPublic, String description);

        void convertUriToBitmap(Uri imageUri);
    }

}
