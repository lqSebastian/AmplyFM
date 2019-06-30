package com.cibertec.amplyfm.ui.dialogs;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.utils.CopyToCb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LyricsDialog  extends DialogFragment {

    private static  final  String TAG   = "LyricsDialog";


    @BindView(R.id.tvHeading)
    TextView tvHeading;


    @BindView(R.id.tvLyrics)
    TextView tvLyrics;


    @BindView(R.id.action_close)
    TextView action_close;

    @BindView(R.id.action_copy_all)
    TextView action_copy_all;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment,container,false);
        String data = getArguments().getString("data");
        String title = getArguments().getString("title");
        ButterKnife.bind(this, view);

        tvLyrics.setText(data);
        tvHeading.setText(title);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.windowAnimations = R.style.DialogAnimation;
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.dimAmount = 0.5f;
        //p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        p.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getDialog().getWindow().setAttributes(p);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

    }

    public static LyricsDialog newInstance(String data,String title) {
        LyricsDialog fragment = new LyricsDialog();

        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("title", title);
        fragment.setArguments(bundle);

        return fragment;
    }

    @OnClick(R.id.action_close)
    public void close(View view) {
        getDialog().dismiss();
    }

    @OnClick(R.id.action_copy_all)
    public void CopyToClipboard(View view) {
        CopyToCb.setClipboard(view.getContext(),tvLyrics.getText() + "");
        Toast.makeText(getContext(), "Letra copiada al portapapeles", Toast.LENGTH_SHORT).show();
    }
}
