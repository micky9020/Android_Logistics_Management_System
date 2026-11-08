package com.example.logistics;

// Importaciones Android.
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.text.Editable;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ColorDrawable;
import android.content.res.ColorStateList;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ScrollView;

// Gestión de contraseñas.
import java.util.Arrays;

// Ejecución en segundo plano.
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// AndroidX.
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

// Material Design.
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// Pantalla de autenticación LOGISTICS.
public class MainActivity extends AppCompatActivity {

    private TextInputEditText etDni;
    private TextInputEditText etPassword;

    private TextInputLayout tilDni;
    private TextInputLayout tilPassword;

    private MaterialButton btnLogin;

    // Controles de términos y condiciones.
    private CheckBox cbTermsAccepted;
    private TextView tvTermsConditions;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    private final Handler mainHandler =
            new Handler(Looper.getMainLooper());

    private boolean loginInProgress = false;

    // Colores corporativos.
    private final int burgundy = Color.rgb(112, 36, 59);
    private final int burgundyDark = Color.rgb(80, 24, 43);
    private final int burgundyLight = Color.rgb(138, 53, 79);
    private final int gold = Color.rgb(197, 164, 109);
    private final int metallicWhite = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Protección contra capturas.
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        // Cargar diseño.
        setContentView(R.layout.activity_main);

        // Configurar redes sociales.
        SocialLinks.configure(this);

        // Inicializar controles.
        initializeComponents();

        // Configurar eventos.
        configureEvents();

        // No guardar automáticamente la contraseña.
        etPassword.setSaveEnabled(false);
    }

    // =====================================================
    // INICIALIZAR COMPONENTES
    // =====================================================

    private void initializeComponents() {

        etDni = findViewById(R.id.etDni);
        etPassword = findViewById(R.id.etPassword);

        tilDni = findViewById(R.id.tilDni);
        tilPassword = findViewById(R.id.tilPassword);

        btnLogin = findViewById(R.id.btnLogin);

        cbTermsAccepted = findViewById(R.id.cbTermsAccepted);
        tvTermsConditions = findViewById(R.id.tvTermsConditions);

        // Aplicar acabado guinda metálico.
        configureMetallicLoginButton();

        // Exigir aceptación antes de ingresar.
        btnLogin.setEnabled(false);
        btnLogin.setAlpha(0.55f);

        // Casilla desmarcada inicialmente.
        cbTermsAccepted.setChecked(false);
    }

    // =====================================================
    // CONFIGURAR BOTÓN GUINDA METÁLICO
    // =====================================================

    private void configureMetallicLoginButton() {

        if (btnLogin == null) {
            return;
        }

        GradientDrawable metallicBackground =
                new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{
                                burgundyLight,
                                burgundy,
                                burgundyDark
                        }
                );

        metallicBackground.setCornerRadius(dp(15));

        // Borde champagne.
        metallicBackground.setStroke(
                dp(2),
                gold
        );

        btnLogin.setBackground(metallicBackground);

        // Letras blancas sobre fondo guinda.
        btnLogin.setTextColor(metallicWhite);

        btnLogin.setTypeface(
                btnLogin.getTypeface(),
                Typeface.BOLD
        );

        btnLogin.setElevation(dp(9));

        btnLogin.setStateListAnimator(null);

        btnLogin.setRippleColor(
                ColorStateList.valueOf(
                        Color.argb(75, 255, 255, 255)
                )
        );

        btnLogin.setAllCaps(false);
    }

    // =====================================================
    // CONFIGURAR EVENTOS
    // =====================================================

    private void configureEvents() {

        // Efecto visual del botón.
        applyTouchEffect(btnLogin);

        // Aceptación de términos.
        cbTermsAccepted.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    btnLogin.setEnabled(isChecked);

                    btnLogin.animate()
                            .alpha(isChecked ? 1f : 0.55f)
                            .setDuration(180)
                            .start();
                }
        );

        // Abrir documento de términos.
        tvTermsConditions.setOnClickListener(view ->
                showTermsDialog()
        );

        applyTouchEffect(tvTermsConditions);

        // Acción de inicio de sesión.
        btnLogin.setOnClickListener(view -> validateLogin());

        // Recuperación de contraseña.
        View forgotPassword =
                findViewById(R.id.tvForgotPassword);

        if (forgotPassword != null) {

            applyTouchEffect(forgotPassword);

            forgotPassword.setOnClickListener(view ->
                    Toast.makeText(
                            MainActivity.this,
                            "La recuperación requiere un mecanismo local de verificación.",
                            Toast.LENGTH_LONG
                    ).show()
            );
        }

        // Registro de usuarios.
        View createAccount =
                findViewById(R.id.tvCreateAccount);

        if (createAccount != null) {

            applyTouchEffect(createAccount);

            createAccount.setOnClickListener(view -> openRegister());
        }
    }

    // =====================================================
    // MOSTRAR TÉRMINOS Y CONDICIONES
    // =====================================================

    private void showTermsDialog() {

        String terms =

                "TÉRMINOS Y CONDICIONES DE USO\n" +
                        "LOGISTICS | SMART LOGISTICS\n" +
                        "Versión 1.0 - Noviembre de 2026\n\n" +

                        "1. ACEPTACIÓN DE LOS TÉRMINOS\n\n" +
                        "Al marcar la casilla de aceptación y utilizar LOGISTICS, " +
                        "el usuario declara que ha leído y acepta estas condiciones. " +
                        "Si no está de acuerdo, deberá abstenerse de utilizar la aplicación.\n\n" +

                        "2. FINALIDAD DE LA APLICACIÓN\n\n" +
                        "LOGISTICS es una herramienta de apoyo para la gestión y el " +
                        "control de procesos logísticos y operativos. Incluye módulos " +
                        "que pueden comprender compras, importaciones, recepción, " +
                        "almacenaje, inventarios, despachos, tracking, picking, packing, " +
                        "ventas, finanzas, CRM, indicadores de gestión y WMS.\n\n" +

                        "3. REGISTRO Y ACCESO\n\n" +
                        "El usuario deberá proporcionar información veraz y utilizar " +
                        "sus propias credenciales. Es responsable de mantener la " +
                        "confidencialidad de su contraseña y evitar compartirla con " +
                        "personas no autorizadas.\n\n" +

                        "4. ALMACENAMIENTO Y FUNCIONAMIENTO\n\n" +
                        "LOGISTICS está diseñada para funcionar mediante almacenamiento " +
                        "local en el dispositivo Android. Las funciones autónomas no " +
                        "requieren conexión permanente a Internet.\n\n" +
                        "Los datos pueden perderse al desinstalar la aplicación, borrar " +
                        "sus datos o restablecer o perder el dispositivo. El usuario " +
                        "debe conservar respaldos cuando disponga de esa funcionalidad.\n\n" +
                        "Los enlaces a WhatsApp y otras redes sociales pueden requerir " +
                        "conexión a Internet y están sujetos a las condiciones de sus " +
                        "respectivos proveedores.\n\n" +

                        "5. USO ADECUADO\n\n" +
                        "El usuario se compromete a utilizar la aplicación de manera " +
                        "lícita y responsable. No deberá intentar acceder sin autorización " +
                        "a funciones restringidas, alterar su funcionamiento, introducir " +
                        "contenido malicioso ni utilizarla para actividades fraudulentas " +
                        "o que vulneren derechos de terceros.\n\n" +

                        "6. PROTECCIÓN DE LA INFORMACIÓN\n\n" +
                        "El usuario debe evitar registrar información personal o " +
                        "confidencial que no sea necesaria. Se recomienda proteger " +
                        "el dispositivo mediante bloqueo de pantalla y otros mecanismos " +
                        "de seguridad. La aplicación no constituye un servicio de " +
                        "almacenamiento remoto ni de respaldo en la nube.\n\n" +

                        "7. DISPONIBILIDAD Y ACTUALIZACIONES\n\n" +
                        "El funcionamiento puede depender de la compatibilidad del " +
                        "dispositivo y de la versión instalada. LOGISTICS podrá recibir " +
                        "correcciones, mejoras y actualizaciones.\n\n" +

                        "8. PROPIEDAD INTELECTUAL\n\n" +
                        "El nombre, diseño, identidad gráfica, estructura y componentes " +
                        "propios de LOGISTICS están sujetos a los derechos de propiedad " +
                        "intelectual que correspondan. No se autoriza su reproducción " +
                        "o comercialización sin los permisos correspondientes, salvo " +
                        "los usos permitidos por la legislación aplicable.\n\n" +

                        "9. LIMITACIÓN DE RESPONSABILIDAD\n\n" +
                        "LOGISTICS es una herramienta de apoyo. El usuario y la " +
                        "organización que la utilice deben verificar la exactitud " +
                        "de los datos y resultados antes de tomar decisiones operativas, " +
                        "comerciales, financieras o administrativas.\n\n" +
                        "La aplicación no sustituye los controles internos, procedimientos " +
                        "ni verificaciones profesionales de cada organización. " +
                        "Nada de estos términos excluye responsabilidades que legalmente " +
                        "no puedan limitarse.\n\n" +

                        "10. MODIFICACIONES\n\n" +
                        "Estos términos podrán actualizarse cuando existan cambios " +
                        "en las funcionalidades, condiciones de uso o requisitos legales. " +
                        "Cuando corresponda, se solicitará nuevamente la aceptación " +
                        "del usuario.\n\n" +

                        "11. LEGISLACIÓN APLICABLE\n\n" +
                        "Estos términos se interpretarán conforme a la legislación " +
                        "vigente de la República del Perú, incluyendo las disposiciones " +
                        "aplicables sobre protección de datos personales y protección " +
                        "al consumidor.\n\n" +

                        "12. CONTACTO\n\n" +
                        "Para consultas relacionadas con LOGISTICS, el usuario puede " +
                        "utilizar el siguiente canal de contacto:\n\n" +
                        "WhatsApp: +51 949 698 978\n\n" +

                        "LOGISTICS | SMART LOGISTICS\n" +
                        "Herramienta de apoyo para la gestión logística y operativa.\n\n" +

                        "Al seleccionar la casilla «He leído y acepto los Términos " +
                        "y Condiciones de Uso», el usuario declara haber leído y " +
                        "aceptado este documento.";

        // Fondo corporativo guinda.
        int dialogBackground = burgundy;

        // Contenedor desplazable.
        ScrollView scrollView = new ScrollView(this);

        scrollView.setFillViewport(false);

        scrollView.setBackgroundColor(dialogBackground);

        scrollView.setPadding(
                dp(20),
                dp(15),
                dp(20),
                dp(15)
        );

        // Texto completo de los términos.
        TextView termsText = new TextView(this);

        termsText.setText(terms);

        // Letras blancas.
        termsText.setTextColor(Color.WHITE);

        termsText.setTextSize(14);

        termsText.setLineSpacing(dp(4), 1f);

        termsText.setTextIsSelectable(true);

        scrollView.addView(termsText);

        // Título personalizado.
        TextView dialogTitle = new TextView(this);

        dialogTitle.setText("TÉRMINOS Y CONDICIONES DE USO");

        dialogTitle.setTextColor(Color.WHITE);

        dialogTitle.setTextSize(17);

        dialogTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        dialogTitle.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(15)
        );

        dialogTitle.setBackgroundColor(dialogBackground);

        // Crear ventana.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCustomTitle(dialogTitle)
                .setView(scrollView)
                .setPositiveButton("CERRAR", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                })
                .create();

        dialog.show();

        // Aplicar fondo guinda a toda la ventana.
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(dialogBackground)
            );
        }

        // Botón CERRAR con letras blancas.
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.WHITE);

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTypeface(
                            null,
                            Typeface.BOLD
                    );
        }
    }

    // =====================================================
    // EFECTO VISUAL DE LOS CONTROLES
    // =====================================================

    private void applyTouchEffect(View view) {

        if (view == null) {
            return;
        }

        view.setClickable(true);

        view.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    v.animate()
                            .scaleX(0.96f)
                            .scaleY(0.96f)
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
                            .setDuration(180)
                            .start();

                    break;
            }

            return false;
        });
    }

    // =====================================================
    // ABRIR REGISTRO
    // =====================================================

    private void openRegister() {

        if (isFinishing() || isDestroyed()) {
            return;
        }

        Intent intent = new Intent(
                MainActivity.this,
                RegisterActivity.class
        );

        startActivity(intent);

        overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
    }

    // =====================================================
    // VALIDAR INICIO DE SESIÓN
    // =====================================================

    private void validateLogin() {

        if (isFinishing() || isDestroyed()) {
            return;
        }

        if (loginInProgress) {
            return;
        }

        // Verificar aceptación obligatoria.
        if (!cbTermsAccepted.isChecked()) {

            Toast.makeText(
                    this,
                    "Debes aceptar los Términos y Condiciones para ingresar.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // Obtener DNI.
        String dni = etDni.getText() == null
                ? ""
                : etDni.getText().toString().trim();

        // Obtener contraseña.
        Editable passwordInput = etPassword.getText();

        int passwordLength = passwordInput == null
                ? 0
                : passwordInput.length();

        char[] password = new char[passwordLength];

        for (int i = 0; i < passwordLength; i++) {
            password[i] = passwordInput.charAt(i);
        }

        // Limpiar campo visual.
        etPassword.setText("");

        // Validar DNI.
        if (!dni.matches("[0-9]{8}")) {

            tilDni.setError(
                    "Ingresa un DNI válido de 8 dígitos."
            );

            Arrays.fill(password, '\0');
            return;
        }

        // Validar contraseña vacía.
        if (password.length == 0) {

            tilPassword.setError(
                    "Ingresa tu contraseña."
            );

            return;
        }

        // Limitar longitud.
        if (password.length > 128) {

            tilPassword.setError(
                    "La contraseña supera el límite permitido."
            );

            Arrays.fill(password, '\0');
            return;
        }

        // Eliminar errores anteriores.
        tilDni.setError(null);
        tilPassword.setError(null);

        // Bloquear botón mientras se procesa.
        btnLogin.setEnabled(false);
        btnLogin.setAlpha(0.65f);

        loginInProgress = true;

        try {

            // Autenticación en segundo plano.
            executor.execute(() -> {

                boolean authenticated = false;
                boolean authenticationCompleted = false;

                try {

                    // Verificación local SQLite.
                    authenticated = LocalAuth.authenticate(
                            getApplicationContext(),
                            dni,
                            password
                    );

                    authenticationCompleted = true;

                } catch (Exception exception) {

                    authenticated = false;

                } finally {

                    // Limpiar contraseña temporal.
                    Arrays.fill(password, '\0');
                }

                final boolean loginResult = authenticated;
                final boolean completed = authenticationCompleted;

                // Regresar al hilo principal.
                mainHandler.post(() -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    // Restaurar controles según aceptación.
                    btnLogin.setEnabled(cbTermsAccepted.isChecked());

                    btnLogin.setAlpha(
                            cbTermsAccepted.isChecked() ? 1f : 0.55f
                    );

                    loginInProgress = false;

                    // Verificar resultado.
                    if (!completed) {

                        Toast.makeText(
                                MainActivity.this,
                                "No fue posible completar la autenticación.",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }

                    if (loginResult) {

                        Toast.makeText(
                                MainActivity.this,
                                "Acceso autorizado.",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Abrir Dashboard.
                        Intent intent = new Intent(
                                MainActivity.this,
                                DashboardActivity.class
                        );

                        startActivity(intent);

                        overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                        );

                        // Cerrar Login.
                        finish();

                    } else {

                        Toast.makeText(
                                MainActivity.this,
                                "Credenciales incorrectas o acceso temporalmente bloqueado.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            });

        } catch (RuntimeException exception) {

            Arrays.fill(password, '\0');

            loginInProgress = false;

            btnLogin.setEnabled(cbTermsAccepted.isChecked());

            btnLogin.setAlpha(
                    cbTermsAccepted.isChecked() ? 1f : 0.55f
            );

            Toast.makeText(
                    MainActivity.this,
                    "No fue posible iniciar la autenticación.",
                    Toast.LENGTH_LONG
            ).show();
        }
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

    // =====================================================
    // DESTRUIR ACTIVIDAD
    // =====================================================

    @Override
    protected void onDestroy() {

        mainHandler.removeCallbacksAndMessages(null);

        executor.shutdownNow();

        super.onDestroy();
    }
}
