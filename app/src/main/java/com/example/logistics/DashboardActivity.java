package com.example.logistics;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class DashboardActivity extends AppCompatActivity {

    // =====================================================
    // LOS 14 MODULOS DE LOGISTICS
    // =====================================================

    private final String[] moduleNames = {
            "Importaciones",
            "Compras",
            "Recepcion",
            "Almacenaje",
            "Inventarios",
            "Despachos",
            "Tracking",
            "KPI",
            "Picking",
            "Packing",
            "Ventas",
            "Finanzas",
            "CRM",
            "WMS"
    };

    private final String[] moduleIcons = {
            "🚢", "🛒", "📥", "🏭",
            "📦", "🚚", "📍", "📊",
            "🧾", "📦", "💰", "💳",
            "👥", "🏬"
    };

    // =====================================================
    // PALETA CORPORATIVA
    // =====================================================

    private final int burgundy = Color.rgb(100, 29, 53);
    private final int burgundyDark = Color.rgb(75, 19, 38);
    private final int cream = Color.rgb(245, 240, 231);
    private final int gold = Color.rgb(197, 164, 109);
    private final int darkText = Color.rgb(52, 52, 52);
    private final int secondaryText = Color.rgb(120, 112, 105);
    private final int borderColor = Color.rgb(230, 221, 210);
    private final int white = Color.WHITE;

    // =====================================================
    // ENLACES DE REDES SOCIALES
    // =====================================================

    private static final String URL_WHATSAPP =
            "https://wa.me/51949698978";

    private static final String URL_FACEBOOK =
            "https://www.facebook.com/";

    private static final String URL_INSTAGRAM =
            "https://www.instagram.com/";

    private static final String URL_TIKTOK =
            "https://www.tiktok.com/";

    private static final String URL_YOUTUBE =
            "https://www.youtube.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDashboard();
    }

    // =====================================================
    // CONSTRUIR DASHBOARD
    // =====================================================

    private void createDashboard() {

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(cream);
        scrollView.setClipToPadding(false);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        mainLayout.setPadding(
                dp(18),
                dp(25),
                dp(18),
                dp(22)
        );

        scrollView.addView(mainLayout);

        // =================================================
        // ENCABEZADO CORPORATIVO
        // =================================================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);

        header.setPadding(
                dp(16),
                dp(18),
                dp(16),
                dp(14)
        );

        GradientDrawable headerBackground = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{burgundy, burgundyDark}
        );

        headerBackground.setCornerRadius(dp(20));
        headerBackground.setStroke(dp(1), gold);

        header.setBackground(headerBackground);
        header.setElevation(dp(5));

        TextView title = new TextView(this);
        title.setText("LOGISTICS");
        title.setTextSize(30);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextColor(white);
        title.setLetterSpacing(0.10f);
        title.setGravity(Gravity.CENTER);

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        View goldLine = new View(this);
        goldLine.setBackgroundColor(gold);

        LinearLayout.LayoutParams lineParams =
                new LinearLayout.LayoutParams(dp(58), dp(3));

        lineParams.setMargins(0, dp(9), 0, dp(8));
        header.addView(goldLine, lineParams);

        TextView subtitle = new TextView(this);
        subtitle.setText("SMART LOGISTICS");
        subtitle.setTextSize(13);
        subtitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        subtitle.setTextColor(Color.rgb(245, 225, 190));
        subtitle.setLetterSpacing(0.16f);
        subtitle.setGravity(Gravity.CENTER);

        header.addView(subtitle);

        mainLayout.addView(
                header,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        // =================================================
        // CONTACTO WHATSAPP
        // =================================================

        LinearLayout whatsappLayout = new LinearLayout(this);
        whatsappLayout.setOrientation(LinearLayout.HORIZONTAL);
        whatsappLayout.setGravity(Gravity.CENTER);

        whatsappLayout.setPadding(
                dp(12),
                dp(13),
                dp(12),
                dp(13)
        );

        ImageView whatsappIcon = createSocialIcon(
                R.drawable.ic_whatsapp,
                27,
                URL_WHATSAPP,
                "Contactar por WhatsApp"
        );

        whatsappLayout.addView(
                whatsappIcon,
                new LinearLayout.LayoutParams(dp(27), dp(27))
        );

        TextView phone = new TextView(this);
        phone.setText("+51 949698978");
        phone.setTextSize(15);
        phone.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        phone.setTextColor(darkText);
        phone.setGravity(Gravity.CENTER_VERTICAL);
        phone.setPadding(dp(9), 0, 0, 0);
        phone.setContentDescription("Telefono de contacto por WhatsApp");
        phone.setClickable(true);
        phone.setFocusable(true);

        applyTouchEffect(phone);

        phone.setOnClickListener(view ->
                openSocialPage(URL_WHATSAPP)
        );

        whatsappLayout.addView(phone);
        mainLayout.addView(whatsappLayout);

        // =================================================
        // ENCABEZADO DE MODULOS
        // =================================================

        LinearLayout sectionHeader = new LinearLayout(this);
        sectionHeader.setOrientation(LinearLayout.VERTICAL);
        sectionHeader.setGravity(Gravity.CENTER);

        sectionHeader.setPadding(
                0,
                dp(7),
                0,
                dp(13)
        );

        TextView sectionTitle = new TextView(this);
        sectionTitle.setText("MODULOS DE GESTION");
        sectionTitle.setTextSize(16);
        sectionTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        sectionTitle.setTextColor(burgundy);
        sectionTitle.setGravity(Gravity.CENTER);

        sectionHeader.addView(sectionTitle);

        TextView sectionDescription = new TextView(this);
        sectionDescription.setText(
                "Selecciona un modulo para continuar"
        );
        sectionDescription.setTextSize(12);
        sectionDescription.setTextColor(secondaryText);
        sectionDescription.setGravity(Gravity.CENTER);
        sectionDescription.setPadding(0, dp(4), 0, 0);

        sectionHeader.addView(sectionDescription);
        mainLayout.addView(sectionHeader);

        // =================================================
        // CUADRICULA DE 14 MODULOS
        // =================================================

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(2);
        grid.setUseDefaultMargins(false);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        mainLayout.addView(
                grid,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        for (int i = 0; i < moduleNames.length; i++) {

            final String moduleName = moduleNames[i];

            // TARJETA METALICA EXTERIOR

            MaterialCardView card = new MaterialCardView(this);

            GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

            params.width = 0;
            params.height = dp(112);

            params.columnSpec = GridLayout.spec(
                    GridLayout.UNDEFINED,
                    1f
            );

            params.setMargins(
                    dp(5),
                    dp(5),
                    dp(5),
                    dp(5)
            );

            card.setLayoutParams(params);
            card.setRadius(dp(16));
            card.setCardElevation(dp(7));
            card.setCardBackgroundColor(Color.TRANSPARENT);
            card.setStrokeWidth(dp(2));
            card.setStrokeColor(Color.rgb(185, 151, 94));
            card.setClickable(true);
            card.setFocusable(true);
            card.setUseCompatPadding(false);

            card.setRippleColor(
                    ColorStateList.valueOf(
                            Color.rgb(245, 232, 210)
                    )
            );

            // CONTENIDO DE LA TARJETA

            LinearLayout cardContent = new LinearLayout(this);
            cardContent.setOrientation(LinearLayout.VERTICAL);
            cardContent.setGravity(Gravity.CENTER);

            cardContent.setPadding(
                    dp(5),
                    dp(5),
                    dp(5),
                    dp(5)
            );

            GradientDrawable cardMetal = createMetallicDrawable(
                    Color.rgb(247, 239, 220),
                    Color.rgb(219, 198, 158),
                    Color.rgb(239, 225, 197),
                    Color.rgb(190, 157, 105)
            );

            cardContent.setBackground(cardMetal);

            // PLACA METALICA DEL ICONO

            TextView icon = new TextView(this);
            icon.setText(moduleIcons[i]);
            icon.setTextSize(25);
            icon.setGravity(Gravity.CENTER);
            icon.setIncludeFontPadding(true);

            GradientDrawable iconMetal = createMetallicDrawable(
                    Color.rgb(255, 250, 235),
                    Color.rgb(215, 190, 143),
                    Color.rgb(240, 221, 183),
                    Color.rgb(181, 143, 87)
            );

            iconMetal.setStroke(
                    dp(1),
                    Color.rgb(255, 255, 255)
            );

            icon.setBackground(iconMetal);
            icon.setElevation(dp(3));

            LinearLayout.LayoutParams iconParams =
                    new LinearLayout.LayoutParams(
                            dp(44),
                            dp(44)
                    );

            iconParams.setMargins(0, 0, 0, dp(5));

            cardContent.addView(icon, iconParams);

            // NOMBRE DEL MODULO

            TextView name = new TextView(this);
            name.setText(moduleName);
            name.setTextSize(12);
            name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            name.setTextColor(darkText);
            name.setGravity(Gravity.CENTER);
            name.setMaxLines(2);
            name.setPadding(dp(2), dp(2), dp(2), 0);

            cardContent.addView(
                    name,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );

            card.addView(cardContent);

            card.setContentDescription(
                    "Abrir modulo " + moduleName
            );

            // EFECTO DE PRESION

            applyTouchEffect(card);

            // ABRIR MODULO

            card.setOnClickListener(view -> {

                Intent intent = new Intent(
                        DashboardActivity.this,
                        ModuleActivity.class
                );

                intent.putExtra("MODULE_NAME", moduleName);

                startActivity(intent);

                overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
            });

            grid.addView(card);
        }

        // =================================================
        // REDES SOCIALES
        // =================================================

        TextView socialTitle = new TextView(this);
        socialTitle.setText("SIGUENOS");
        socialTitle.setTextSize(12);
        socialTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        socialTitle.setTextColor(burgundy);
        socialTitle.setGravity(Gravity.CENTER);
        socialTitle.setLetterSpacing(0.12f);

        socialTitle.setPadding(
                0,
                dp(22),
                0,
                dp(10)
        );

        mainLayout.addView(socialTitle);

        LinearLayout socialLayout = new LinearLayout(this);
        socialLayout.setOrientation(LinearLayout.HORIZONTAL);
        socialLayout.setGravity(Gravity.CENTER);

        socialLayout.setPadding(
                0,
                dp(3),
                0,
                dp(8)
        );

        socialLayout.addView(
                createSocialIcon(
                        R.drawable.ic_whatsapp,
                        25,
                        URL_WHATSAPP,
                        "WhatsApp"
                ),
                socialIconParams()
        );

        socialLayout.addView(
                createSocialIcon(
                        R.drawable.ic_facebook,
                        25,
                        URL_FACEBOOK,
                        "Facebook"
                ),
                socialIconParams()
        );

        socialLayout.addView(
                createSocialIcon(
                        R.drawable.ic_instagram,
                        25,
                        URL_INSTAGRAM,
                        "Instagram"
                ),
                socialIconParams()
        );

        socialLayout.addView(
                createSocialIcon(
                        R.drawable.ic_tiktok,
                        25,
                        URL_TIKTOK,
                        "TikTok"
                ),
                socialIconParams()
        );

        socialLayout.addView(
                createSocialIcon(
                        R.drawable.ic_youtube,
                        25,
                        URL_YOUTUBE,
                        "YouTube"
                ),
                socialIconParams()
        );

        mainLayout.addView(socialLayout);

        // =================================================
        // PIE DE PAGINA
        // =================================================

        View footerLine = new View(this);
        footerLine.setBackgroundColor(borderColor);

        LinearLayout.LayoutParams footerLineParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(1)
                );

        footerLineParams.setMargins(
                dp(12),
                dp(10),
                dp(12),
                dp(10)
        );

        mainLayout.addView(footerLine, footerLineParams);

        TextView footer = new TextView(this);
        footer.setText(
                "LOGISTICS  |  SMART LOGISTICS  |  v1.0"
        );

        footer.setTextSize(10);
        footer.setTextColor(secondaryText);
        footer.setGravity(Gravity.CENTER);
        footer.setLetterSpacing(0.04f);

        mainLayout.addView(
                footer,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        setContentView(scrollView);
    }

    // =====================================================
    // CREAR DEGRADADO METALICO CON BORDES
    // =====================================================

    private GradientDrawable createMetallicDrawable(
            int color1,
            int color2,
            int color3,
            int color4) {

        GradientDrawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{
                        color1,
                        color2,
                        color3,
                        color4
                }
        );

        drawable.setCornerRadius(dp(13));

        drawable.setStroke(
                dp(1),
                Color.rgb(255, 248, 225)
        );

        return drawable;
    }

    // =====================================================
    // EFECTO UNIVERSAL DE PRESION
    // =====================================================

    private void applyTouchEffect(View view) {

        view.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    v.animate()
                            .scaleX(0.94f)
                            .scaleY(0.94f)
                            .alpha(0.88f)
                            .translationZ(dp(1))
                            .setDuration(90)
                            .start();

                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .translationZ(0f)
                            .setDuration(150)
                            .start();

                    break;
            }

            return false;
        });
    }

    // =====================================================
    // CREAR ICONOS DE REDES SOCIALES
    // =====================================================

    private ImageView createSocialIcon(
            int drawableId,
            int size,
            String url,
            String description) {

        ImageView imageView = new ImageView(this);

        imageView.setImageResource(drawableId);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setContentDescription(description);

        imageView.setClickable(true);
        imageView.setFocusable(true);

        applyTouchEffect(imageView);

        imageView.setOnClickListener(view ->
                openSocialPage(url)
        );

        return imageView;
    }

    // =====================================================
    // ABRIR APLICACION O PAGINA WEB
    // =====================================================

    private void openSocialPage(String url) {

        try {

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
            );

            startActivity(intent);

        } catch (ActivityNotFoundException e) {

            Toast.makeText(
                    this,
                    "No se encontro una aplicacion para abrir este enlace.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // =====================================================
    // ESPACIADO ENTRE ICONOS SOCIALES
    // =====================================================

    private LinearLayout.LayoutParams socialIconParams() {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        dp(25),
                        dp(25)
                );

        params.setMargins(
                dp(10),
                0,
                dp(10),
                0
        );

        return params;
    }

    // =====================================================
    // CONVERSION DE DP A PIXELES
    // =====================================================

    private int dp(int value) {

        return (int) (
                value * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }
}
