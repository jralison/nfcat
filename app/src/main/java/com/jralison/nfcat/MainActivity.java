package com.jralison.nfcat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private NfcAdapter mAdapter;
    private PendingIntent pendingIntent;
    private ImageView mContactlessIcon;
    private AnimatedVectorDrawableCompat animContactlessIcon;
    private TextView mTextAguardando;
    private View mRootView;

    private static IntentFilter nfcStateChangeIntentFilter = new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
    private BroadcastReceiver nfcStateChangeBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContactlessIcon = findViewById(R.id.img_contactless);
        mTextAguardando = findViewById(R.id.text_aguardando_cartao);
        mRootView = findViewById(R.id.mainLayout);

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        animContactlessIcon = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_nfc_card);

        if (mAdapter == null)
            onNfcIndisponivel();
        else if (!mAdapter.isEnabled())
            onNfcDesativado();
        else
            onNfcHabilitado();
    }

    public void startCheckingNfcState() {
        registerReceiver(nfcStateChangeBroadcastReceiver, nfcStateChangeIntentFilter);
    }

    public void stopCheckinfNfcState() {
        unregisterReceiver(nfcStateChangeBroadcastReceiver);
    }

    public void animateBackgroundColor(final View view, int colorFrom, int colorTo, long duration) {
        final ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public void animateBackgroundColor(View view, int colorTo) {
        final Drawable backgroundDrawable = view.getBackground();
        final int colorFrom;
        if (backgroundDrawable instanceof ColorDrawable)
            colorFrom = ((ColorDrawable) backgroundDrawable).getColor();
        else
            colorFrom = getResources().getColor(R.color.colorPrimary);
        animateBackgroundColor(view, colorFrom, getResources().getColor(colorTo), 300);
    }

    private void onNfcDesativado() {
        mTextAguardando.setText(R.string.nfc_desabilitado);
        mContactlessIcon.setVisibility(ImageView.GONE);

        final Handler updateNfcCheck = new Handler();
        final Runnable runCheck = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Verificando estado do NFC...");
                if (mAdapter.isEnabled())
                    onNfcHabilitado();
                else
                    updateNfcCheck.postDelayed(this, 750);
            }
        };
        runCheck.run();

        animateBackgroundColor(mRootView, R.color.backgroundNfcDesativado);
    }

    private void onNfcIndisponivel() {
        mTextAguardando.setText(R.string.nfc_indisponivel);
        mContactlessIcon.setVisibility(ImageView.GONE);
        animateBackgroundColor(mRootView, R.color.backgroundNfcIndisponivel);
    }

    private void onNfcHabilitado() {
        animateBackgroundColor(mRootView, R.color.backgroundNfcAtivo);

        nfcStateChangeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final int state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, -1);
                if (state == NfcAdapter.STATE_OFF)
                    onNfcDesativado();
                else if (state == NfcAdapter.STATE_ON)
                    onNfcHabilitado();
            }
        };

        startCheckingNfcState();

        if (animContactlessIcon != null) {
            animContactlessIcon.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    mContactlessIcon.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animContactlessIcon.start();
                        }
                    }, 750);
                }
            });
            mContactlessIcon.setVisibility(View.VISIBLE);
            mContactlessIcon.setImageDrawable(animContactlessIcon);
            animContactlessIcon.start();
        }

        mTextAguardando.setText(R.string.nfc_aguardando_leitura);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            startCheckingNfcState();
            mAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
            if (animContactlessIcon != null)
                animContactlessIcon.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            stopCheckinfNfcState();
            mAdapter.disableForegroundDispatch(this);
            if (animContactlessIcon != null && animContactlessIcon.isRunning())
                animContactlessIcon.stop();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        final Intent displayIntent = new Intent(this, TagContentActivity.class);
        displayIntent.putExtra(TagContentActivity.EXTRA_TAG_CONTENT, tag.toString());
        displayIntent.putExtra(TagContentActivity.EXTRA_TAG, tag);
        startActivity(displayIntent);
    }

}
