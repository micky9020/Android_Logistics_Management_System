package com.example.logistics;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Recibe los enlaces de retorno enviados por Supabase.
 * No muestra, registra ni almacena tokens de autenticación.
 */
public class AuthCallbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtiene el enlace que Android recibió desde el navegador o correo.
        Uri callbackUri = getIntent().getData();

        processCallback(callbackUri);
    }

    /**
     * Procesa la respuesta de Supabase.
     */
    private void processCallback(Uri callbackUri) {

        // Verifica que el enlace corresponda a nuestra aplicación.
        if (callbackUri == null
                || !"com.example.logistics".equals(callbackUri.getScheme())
                || !"login-callback".equals(callbackUri.getHost())) {

            showMessage("El enlace de autenticación no es válido.");
            finish();
            return;
        }

        // Supabase puede devolver los parámetros dentro del fragmento (#).
        String fragment = callbackUri.getFragment();

        Uri fragmentUri = null;

        if (fragment != null && !fragment.isEmpty()) {
            fragmentUri = Uri.parse("https://callback.local/?" + fragment);
        }

        // Busca primero los errores devueltos por Supabase.
        String error = getParameter(callbackUri, fragmentUri, "error");
        String errorCode = getParameter(callbackUri, fragmentUri, "error_code");

        if (error != null || errorCode != null) {
            showMessage("No se pudo completar la confirmación. Solicita un nuevo enlace.");
            finish();
            return;
        }

        // Comprueba si Supabase devolvió un token de acceso.
        // El token nunca se muestra ni se guarda en esta etapa.
        String accessToken = getParameter(
                callbackUri,
                fragmentUri,
                "access_token"
        );

        if (accessToken != null && !accessToken.isEmpty()) {
            showMessage("Confirmación recibida. Ya puedes volver a LOGISTICS.");
        } else {
            showMessage("No se recibió una respuesta completa de Supabase.");
        }

        finish();
    }

    /**
     * Busca un parámetro tanto en la URL como en su fragmento.
     */
    private String getParameter(
            Uri originalUri,
            Uri fragmentUri,
            String parameter
    ) {

        String value = originalUri.getQueryParameter(parameter);

        if (value == null && fragmentUri != null) {
            value = fragmentUri.getQueryParameter(parameter);
        }

        return value;
    }

    /**
     * Muestra mensajes generales sin revelar información sensible.
     */
    private void showMessage(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }
}