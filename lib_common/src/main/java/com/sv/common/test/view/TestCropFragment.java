package com.sv.common.test.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.sv.common.AbstractBaseFragment;
import com.sv.common.R;
import com.sv.common.R2;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class TestCropFragment extends AbstractBaseFragment {
    private static final String TAG = TestCropFragment.class.getName();

    @BindView(R2.id.btnCrop) Button btnCrop;
    @BindView(R2.id.image) ImageView image;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_crop, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {

    }

    @OnClick({
            R2.id.btnCrop
    })
    public void onViewClicked(View view) {
        int id = view.getId();
        if(id == R.id.btnCrop){
            image.setImageDrawable(null);
            Crop.pickImage(this.getActivity(), this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(this.getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this.getActivity(), this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            image.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this.getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
