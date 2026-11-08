package com.example.logistics;

// Ciclo de vida Android.
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

// Mensajes al usuario.
import android.widget.Toast;
import android.widget.TextView;

// Validación de correo.
import android.util.Patterns;

// Eventos táctiles.
import android.view.View;
import android.view.MotionEvent;
import android.view.WindowManager;

// Colores y fondos metálicos.
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;

// Procesamiento en segundo plano.
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Arrays;

// Campos Material Design.
import com.google.android.material.textfield.TextInputEditText;

// Actividad AndroidX.
import androidx.appcompat.app.AppCompatActivity;

// Registro local de usuarios.
public class RegisterActivity extends AppCompatActivity {

    // Campos del formulario.
    private TextInputEditText etDni;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;

    // Botones.
    private View btnRegister;
    private View btnBackToLogin;

    // Procesamiento en segundo plano.
    private ExecutorService executor;

    // Hilo principal.
    private Handler mainHandler;

    // Control de registros simultáneos.
    private boolean registrationInProgress = false;

    // =====================================================
    // COLORES CORPORATIVOS
    // =====================================================

    private final int burgundy = Color.rgb(100, 29, 53);
    private final int burgundyDark = Color.rgb(75, 19, 38);

    private final int gold = Color.rgb(197, 164, 109);
    private final int champagne = Color.rgb(244, 225, 193);

    private final int metallicWhite = Color.WHITE;
    private final int metallicSilver = Color.rgb(226, 229, 232);
    private final int metallicGray = Color.rgb(165, 171, 177);

    // =====================================================
    // CICLO DE VIDA
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Cargar diseño XML.
        setContentView(R.layout.activity_register);

        // Configurar protección de pantalla.
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        // Configurar enlaces sociales.
        SocialLinks.configure(this);

        // Inicializar ejecutor.
        executor = Executors.newSingleThreadExecutor();

        // Inicializar hilo principal.
        mainHandler = new Handler(Looper.getMainLooper());

        // Vincular controles.
        initializeComponents();

        // Evitar guardar contraseñas automáticamente.
        etPassword.setSaveEnabled(false);
        etConfirmPassword.setSaveEnabled(false);

        // Configurar interacciones.
        configureEvents();
    }

    // =====================================================
    // INICIALIZAR COMPONENTES
    // =====================================================

    private void initializeComponents() {

        etDni = findViewById(R.id.etDni);

        etEmail = findViewById(R.id.etEmail);

        etPassword = findViewById(R.id.etPassword);

        etConfirmPassword =
                findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);

        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        // Aplicar acabados metálicos.
        configureMetallicButton(
                btnRegister,
                true
        );

        configureMetallicButton(
                btnBackToLogin,
                false
        );
    }

    // =====================================================
    // CONFIGURAR BOTONES METÁLICOS
    // =====================================================

    private void configureMetallicButton(
            View button,
            boolean primary
    ) {

        if (button == null) {
            return;
        }

        GradientDrawable background;

        if (primary) {

            // Botón principal champagne y oro.
            background = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{
                            metallicWhite,
                            champagne,
                            gold,
                            Color.rgb(190, 157, 111)
                    }
            );

            background.setCornerRadius(dp(15));

            background.setStroke(
                    dp(2),
                    Color.rgb(255, 248, 230)
            );

            button.setElevation(dp(9));

            if (button instanceof TextView) {

                TextView textButton = (TextView) button;

                // LETRAS BLANCAS EN CREAR CUENTA.
                textButton.setTextColor(Color.WHITE);

                textButton.setTypeface(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                );
            }

        } else {

            // Botón secundario acero y champagne.
            background = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{
                            metallicWhite,
                            metallicSilver,
                            metallicGray,
                            Color.rgb(205, 209, 213)
                    }
            );

            background.setCornerRadius(dp(15));

            background.setStroke(
                    dp(2),
                    Color.rgb(255, 255, 255)
            );

            button.setElevation(dp(7));

            if (button instanceof TextView) {

                TextView textButton = (TextView) button;

                textButton.setTextColor(burgundy);

                textButton.setTypeface(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                );
            }
        }

        button.setBackground(background);

        button.setClickable(true);
        button.setFocusable(true);
    }

    // =====================================================
    // CONFIGURAR EVENTOS
    // =====================================================

    private void configureEvents() {

        // Efecto visual del botón registrar.
        applyTouchEffect(btnRegister);

        // Acción de registro.
        btnRegister.setOnClickListener(
                view -> registerUser()
        );

        // Efecto visual del botón regresar.
        applyTouchEffect(btnBackToLogin);

        // Acción de regreso al Login.
        btnBackToLogin.setOnClickListener(view -> {

            finish();

            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
        });
    }

    // =====================================================
    // EFECTOS VISUALES DE LOS BOTONES
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
                            .scaleX(0.95f)
                            .scaleY(0.95f)
                            .alpha(0.86f)
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

            // Permite ejecutar el clic normalmente.
            return false;
        });
    }

    // =====================================================
    // REGISTRAR USUARIO
    // =====================================================

    private void registerUser() {

        // Evitar registros simultáneos.
        if (registrationInProgress) {
            return;
        }

        // Obtener datos.
        String dni = getTextValue(etDni);

        String email = getTextValue(etEmail);

        char[] password =
                getPasswordCharacters(etPassword);

        char[] confirmPassword =
                getPasswordCharacters(etConfirmPassword);

        // Validar DNI.
        if (!dni.matches("^[0-9]{8}$")) {

            clearPasswordArrays(password, confirmPassword);

            etDni.setError(
                    "Ingresa un DNI válido de 8 dígitos"
            );

            return;
        }

        // Validar correo.
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            clearPasswordArrays(password, confirmPassword);

            etEmail.setError(
                    "Ingresa un correo electrónico válido"
            );

            return;
        }

        // Validar seguridad de contraseña.
        if (!isStrongPassword(password)) {

            clearPasswordArrays(password, confirmPassword);

            etPassword.setError(
                    "Mínimo 8 caracteres, con mayúscula, minúscula, número y símbolo"
            );

            return;
        }

        // Validar coincidencia.
        if (!Arrays.equals(password, confirmPassword)) {

            clearPasswordArrays(password, confirmPassword);

            etConfirmPassword.setError(
                    "Las contraseñas no coinciden"
            );

            return;
        }

        // Evitar solicitudes simultáneas.
        registrationInProgress = true;

        btnRegister.setEnabled(false);
        btnRegister.setAlpha(0.65f);

        try {

            // Ejecutar registro local en segundo plano.
            executor.execute(() -> {

                boolean registrationResult;

                try {

                    // Guardar usuario en SQLite.
                    registrationResult = LocalAuth.registerUser(
                            getApplicationContext(),
                            dni,
                            password
                    );

                } catch (Exception e) {

                    registrationResult = false;
                }

                final boolean registered = registrationResult;

                // Limpiar contraseñas temporales.
                clearPasswordArrays(password, confirmPassword);

                // Regresar al hilo principal.
                mainHandler.post(() -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    registrationInProgress = false;

                    btnRegister.setEnabled(true);
                    btnRegister.setAlpha(1f);

                    // Limpiar campos de contraseña.
                    clearPasswordFields();

                    if (registered) {

                        Toast.makeText(
                                RegisterActivity.this,
                                "Usuario registrado correctamente",
                                Toast.LENGTH_LONG
                        ).show();

                        // Regresar al Login.
                        finish();

                        overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                        );

                    } else {

                        Toast.makeText(
                                RegisterActivity.this,
                                "No se pudo registrar. Verifica si el DNI ya existe.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            });

        } catch (RuntimeException e) {

            clearPasswordArrays(password, confirmPassword);

            registrationInProgress = false;

            btnRegister.setEnabled(true);
            btnRegister.setAlpha(1f);

            Toast.makeText(
                    RegisterActivity.this,
                    "No fue posible iniciar el registro.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // =====================================================
    // VALIDAR SEGURIDAD DE CONTRASEÑA
    // =====================================================

    private boolean isStrongPassword(char[] password) {

        if (password == null ||
                password.length < 8 ||
                password.length > 128) {

            return false;
        }

        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSymbol = false;

        for (char character : password) {

            if (Character.isLowerCase(character)) {

                hasLowercase = true;

            } else if (Character.isUpperCase(character)) {

                hasUppercase = true;

            } else if (Character.isDigit(character)) {

                hasNumber = true;

            } else if (!Character.isWhitespace(character)) {

                hasSymbol = true;
            }
        }

        return hasLowercase &&
                hasUppercase &&
                hasNumber &&
                hasSymbol;
    }

    // =====================================================
    // OBTENER TEXTO
    // =====================================================

    private String getTextValue(TextInputEditText editText) {

        if (editText.getText() == null) {
            return "";
        }

        return editText.getText().toString().trim();
    }

    // =====================================================
    // OBTENER CONTRASEÑA
    // =====================================================

    private char[] getPasswordCharacters(
            TextInputEditText editText
    ) {

        if (editText.getText() == null) {
            return new char[0];
        }

        return editText.getText().toString().toCharArray();
    }

    // =====================================================
    // LIMPIAR ARREGLOS DE CONTRASEÑAS
    // =====================================================

    private void clearPasswordArrays(
            char[] password,
            char[] confirmPassword
    ) {

        if (password != null) {
            Arrays.fill(password, '\0');
        }

        if (confirmPassword != null) {
            Arrays.fill(confirmPassword, '\0');
        }
    }

    // =====================================================
    // LIMPIAR CAMPOS VISUALES
    // =====================================================

    private void clearPasswordFields() {

        etPassword.setText("");

        etConfirmPassword.setText("");
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

        if (executor != null) {
            executor.shutdownNow();
        }

        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }

        super.onDestroy();
    }
}
