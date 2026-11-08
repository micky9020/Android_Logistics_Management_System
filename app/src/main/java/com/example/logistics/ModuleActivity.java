package com.example.logistics;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class ModuleActivity extends AppCompatActivity {

    // Colores corporativos.
    private final int burgundy = Color.rgb(100, 29, 53);
    private final int burgundyDark = Color.rgb(75, 19, 38);
    private final int cream = Color.rgb(245, 240, 231);
    private final int gold = Color.rgb(197, 164, 109);
    private final int darkText = Color.rgb(52, 52, 52);
    private final int secondaryText = Color.rgb(120, 112, 105);
    private final int white = Color.WHITE;
    private final int borderColor = Color.rgb(218, 204, 183);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String moduleName = getIntent().getStringExtra("MODULE_NAME");

        if (moduleName == null || moduleName.trim().isEmpty()) {
            moduleName = "Módulo LOGISTICS";
        }

        createModuleScreen(moduleName);
    }

    // =====================================================
    // CONSTRUIR PANTALLA DEL MÓDULO
    // =====================================================

    private void createModuleScreen(String moduleName) {

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(cream);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLayout.setPadding(
                dp(20),
                dp(25),
                dp(20),
                dp(25)
        );

        scrollView.addView(mainLayout);

        // =================================================
        // ENCABEZADO CORPORATIVO
        // =================================================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(
                dp(15),
                dp(22),
                dp(15),
                dp(22)
        );

        GradientDrawable headerBackground = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{burgundy, burgundyDark}
        );

        headerBackground.setCornerRadius(dp(20));
        headerBackground.setStroke(dp(2), gold);

        header.setBackground(headerBackground);
        header.setElevation(dp(9));

        TextView logo = new TextView(this);
        logo.setText("LOGISTICS");
        logo.setTextSize(29);
        logo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        logo.setTextColor(white);
        logo.setLetterSpacing(0.12f);
        logo.setGravity(Gravity.CENTER);
        logo.setShadowLayer(dp(5), 0, dp(2), Color.BLACK);

        header.addView(logo);

        View goldLine = new View(this);
        goldLine.setBackground(createMetallicDrawable(
                Color.rgb(255, 239, 190),
                gold,
                Color.rgb(165, 119, 57),
                gold
        ));

        LinearLayout.LayoutParams lineParams =
                new LinearLayout.LayoutParams(dp(65), dp(4));

        lineParams.setMargins(0, dp(10), 0, dp(9));
        header.addView(goldLine, lineParams);

        TextView subtitle = new TextView(this);
        subtitle.setText("SMART LOGISTICS");
        subtitle.setTextSize(12);
        subtitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        subtitle.setTextColor(Color.rgb(245, 225, 190));
        subtitle.setLetterSpacing(0.18f);
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
        // IDENTIFICACIÓN DEL MÓDULO
        // =================================================

        TextView sectionLabel = new TextView(this);
        sectionLabel.setText("PLATAFORMA DE GESTIÓN");
        sectionLabel.setTextSize(12);
        sectionLabel.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        sectionLabel.setTextColor(burgundy);
        sectionLabel.setLetterSpacing(0.14f);
        sectionLabel.setGravity(Gravity.CENTER);
        sectionLabel.setPadding(
                0,
                dp(30),
                0,
                dp(14)
        );

        mainLayout.addView(sectionLabel);

        // =================================================
        // TARJETA PRINCIPAL METÁLICA
        // =================================================

        MaterialCardView moduleCard = new MaterialCardView(this);

        moduleCard.setRadius(dp(21));
        moduleCard.setCardElevation(dp(10));
        moduleCard.setCardBackgroundColor(Color.rgb(250, 247, 240));
        moduleCard.setStrokeWidth(dp(2));
        moduleCard.setStrokeColor(gold);
        moduleCard.setUseCompatPadding(true);

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setGravity(Gravity.CENTER);
        cardContent.setPadding(
                dp(20),
                dp(30),
                dp(20),
                dp(30)
        );

        GradientDrawable cardBackground = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.WHITE,
                        Color.rgb(248, 242, 230),
                        Color.rgb(232, 220, 199)
                }
        );

        cardBackground.setCornerRadius(dp(17));
        cardBackground.setStroke(dp(1), Color.rgb(224, 207, 178));

        cardContent.setBackground(cardBackground);

        // Placa metálica para el icono.

        TextView moduleIcon = new TextView(this);
        moduleIcon.setText(getModuleIcon(moduleName));
        moduleIcon.setTextSize(42);
        moduleIcon.setGravity(Gravity.CENTER);
        moduleIcon.setElevation(dp(7));

        GradientDrawable iconBackground = createMetallicDrawable(
                Color.WHITE,
                Color.rgb(244, 224, 188),
                Color.rgb(211, 180, 127),
                Color.rgb(238, 225, 202)
        );

        iconBackground.setCornerRadius(dp(20));
        iconBackground.setStroke(dp(2), Color.rgb(255, 255, 255));

        moduleIcon.setBackground(iconBackground);

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(dp(92), dp(82));

        iconParams.setMargins(0, 0, 0, dp(12));

        cardContent.addView(moduleIcon, iconParams);

        // Nombre del módulo.

        TextView title = new TextView(this);
        title.setText(moduleName);
        title.setTextSize(25);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextColor(burgundy);
        title.setGravity(Gravity.CENTER);
        title.setPadding(
                0,
                dp(12),
                0,
                dp(8)
        );

        cardContent.addView(title);

        // Línea decorativa dorada.

        View cardLine = new View(this);

        cardLine.setBackground(createMetallicDrawable(
                Color.rgb(255, 235, 190),
                gold,
                Color.rgb(157, 112, 54),
                gold
        ));

        LinearLayout.LayoutParams cardLineParams =
                new LinearLayout.LayoutParams(dp(55), dp(4));

        cardLineParams.setMargins(
                0,
                dp(8),
                0,
                dp(17)
        );

        cardContent.addView(cardLine, cardLineParams);

        // Descripción.

        TextView description = new TextView(this);

        description.setText(
                "Bienvenido al módulo " + moduleName
                        + "\n\nGestiona tus operaciones desde LOGISTICS."
        );

        description.setTextSize(15);
        description.setTextColor(secondaryText);
        description.setGravity(Gravity.CENTER);
        description.setLineSpacing(dp(5), 1f);

        cardContent.addView(
                description,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        moduleCard.addView(cardContent);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, dp(20));

        mainLayout.addView(moduleCard, cardParams);

        // =================================================
        // INDICADOR DE ESTADO
        // =================================================

        LinearLayout statusLayout = new LinearLayout(this);
        statusLayout.setOrientation(LinearLayout.HORIZONTAL);
        statusLayout.setGravity(Gravity.CENTER);
        statusLayout.setPadding(
                dp(14),
                dp(12),
                dp(14),
                dp(12)
        );

        GradientDrawable statusBackground = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.rgb(250, 247, 240),
                        Color.rgb(235, 225, 207)
                }
        );

        statusBackground.setCornerRadius(dp(25));
        statusBackground.setStroke(dp(1), borderColor);

        statusLayout.setBackground(statusBackground);
        statusLayout.setElevation(dp(3));

        TextView statusIndicator = new TextView(this);
        statusIndicator.setText("●");
        statusIndicator.setTextSize(13);
        statusIndicator.setTextColor(Color.rgb(47, 140, 90));

        statusLayout.addView(statusIndicator);

        TextView statusText = new TextView(this);
        statusText.setText("  MÓDULO SELECCIONADO");
        statusText.setTextSize(11);
        statusText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        statusText.setTextColor(secondaryText);
        statusText.setLetterSpacing(0.07f);

        statusLayout.addView(statusText);

        LinearLayout.LayoutParams statusParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        statusParams.setMargins(0, dp(5), 0, dp(15));

        mainLayout.addView(statusLayout, statusParams);

        // =================================================
        // BOTÓN REGRESAR METÁLICO
        // =================================================

        TextView backButton = new TextView(this);

        backButton.setText("‹   REGRESAR AL DASHBOARD");
        backButton.setTextSize(14);
        backButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        backButton.setTextColor(burgundyDark);
        backButton.setGravity(Gravity.CENTER);
        backButton.setLetterSpacing(0.04f);

        backButton.setPadding(
                dp(18),
                dp(17),
                dp(18),
                dp(17)
        );

        GradientDrawable buttonBackground = createMetallicDrawable(
                Color.WHITE,
                Color.rgb(244, 225, 193),
                Color.rgb(210, 181, 137),
                Color.rgb(235, 219, 192)
        );

        buttonBackground.setCornerRadius(dp(15));
        buttonBackground.setStroke(dp(2), gold);

        backButton.setBackground(buttonBackground);
        backButton.setElevation(dp(8));
        backButton.setClickable(true);
        backButton.setFocusable(true);

        applyTouchEffect(backButton);

        backButton.setOnClickListener(view -> {
            finish();

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
        });

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        buttonParams.setMargins(
                0,
                dp(18),
                0,
                dp(15)
        );

        mainLayout.addView(backButton, buttonParams);

        // =================================================
        // PIE DE PÁGINA
        // =================================================

        TextView footer = new TextView(this);

        footer.setText(
                "LOGISTICS  |  SMART LOGISTICS  |  v1.0"
        );

        footer.setTextSize(10);
        footer.setTextColor(secondaryText);
        footer.setGravity(Gravity.CENTER);
        footer.setLetterSpacing(0.04f);

        footer.setPadding(
                0,
                dp(15),
                0,
                dp(5)
        );

        mainLayout.addView(footer);

        setContentView(scrollView);
    }

    // =====================================================
    // FONDO METÁLICO CON DEGRADADO
    // =====================================================

    private GradientDrawable createMetallicDrawable(
            int color1,
            int color2,
            int color3,
            int color4
    ) {

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
                Color.rgb(255, 255, 255)
        );

        return drawable;
    }

    // =====================================================
    // ICONO SEGÚN EL MÓDULO
    // =====================================================

    private String getModuleIcon(String moduleName) {

        switch (moduleName) {

            case "Importaciones":
                return "🚢";

            case "Compras":
                return "🛒";

            case "Recepción":
            case "Recepcion":
                return "📥";

            case "Almacenaje":
                return "🏭";

            case "Inventarios":
                return "📦";

            case "Despachos":
                return "🚚";

            case "Tracking":
                return "📍";

            case "KPI":
                return "📊";

            case "Picking":
                return "🧾";

            case "Packing":
                return "📦";

            case "Ventas":
                return "💰";

            case "Finanzas":
                return "💳";

            case "CRM":
                return "👥";

            case "WMS":
                return "🏬";

            default:
                return "📋";
        }
    }

    // =====================================================
    // EFECTO VISUAL DE PRESIÓN
    // =====================================================

    private void applyTouchEffect(View view) {

        view.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    v.animate()
                            .scaleX(0.95f)
                            .scaleY(0.95f)
                            .alpha(0.88f)
                            .translationZ(dp(2))
                            .setDuration(100)
                            .start();

                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .translationZ(dp(8))
                            .setDuration(170)
                            .start();

                    break;
            }

            // Conserva el clic original.
            return false;
        });
    }

    // =====================================================
    // CONVERSIÓN DE DP A PÍXELES
    // =====================================================

    private int dp(int value) {

        return (int) (
                value * getResources()
                        .getDisplayMetrics().density
        );
    }
}
