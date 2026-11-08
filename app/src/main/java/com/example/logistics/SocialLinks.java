package com.example.logistics;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

public final class SocialLinks {

    private static final String WHATSAPP =
            "https://wa.me/51949698978";

    private static final String FACEBOOK =
            "https://www.facebook.com/";

    private static final String INSTAGRAM =
            "https://www.instagram.com/";

    private static final String TIKTOK =
            "https://www.tiktok.com/";

    private static final String YOUTUBE =
            "https://www.youtube.com/";

    private SocialLinks() {
    }

    public static void configure(Activity activity) {

        if (activity == null || activity.isFinishing()
                || activity.isDestroyed()) {
            return;
        }

        setLink(activity, R.id.ivWhatsappContact, WHATSAPP);
        setLink(activity, R.id.tvWhatsappPhone, WHATSAPP);
        setLink(activity, R.id.ivWhatsappSocial, WHATSAPP);
        setLink(activity, R.id.ivFacebook, FACEBOOK);
        setLink(activity, R.id.ivInstagram, INSTAGRAM);
        setLink(activity, R.id.ivTikTok, TIKTOK);
        setLink(activity, R.id.ivYouTube, YOUTUBE);
    }

    private static void setLink(
            Activity activity,
            int viewId,
            String url
    ) {
        View view = activity.findViewById(viewId);

        if (view == null) {
            return;
        }

        view.setClickable(true);
        view.setFocusable(true);

        view.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url)
                );

                activity.startActivity(intent);

            } catch (ActivityNotFoundException e) {
                Toast.makeText(
                        activity,
                        "No se encontró una aplicación para abrir el enlace.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}