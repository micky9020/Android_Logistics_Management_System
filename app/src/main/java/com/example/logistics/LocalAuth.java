package com.example.logistics;

// Permite acceder al contexto de la aplicación Android.
import android.content.Context;

// Permite preparar registros para SQLite.
import android.content.ContentValues;

// Permite consultar registros de SQLite.
import android.database.Cursor;

// Permite trabajar con la base de datos SQLite.
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Permite generar números aleatorios criptográficamente seguros.
import java.security.SecureRandom;

// Permite comparar hashes de forma segura.
import java.security.MessageDigest;

// Permite almacenar y verificar contraseñas mediante PBKDF2.
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

// Permite limpiar arreglos temporales.
import java.util.Arrays;

// Clase responsable de la autenticación local de LOGISTICS.
public final class LocalAuth {

    // Nombre de la base de datos local.
    private static final String DATABASE_NAME = "logistics_local.db";

    // Versión de la estructura de la base de datos.
    private static final int DATABASE_VERSION = 1;

    // Número de iteraciones del algoritmo PBKDF2.
    private static final int PBKDF2_ITERATIONS = 210000;

    // Longitud del hash generado: 256 bits.
    private static final int HASH_BITS = 256;

    // Longitud del salt individual: 16 bytes.
    private static final int SALT_BYTES = 16;

    // Longitud mínima permitida para una contraseña.
    private static final int MIN_PASSWORD_LENGTH = 6;

    // Longitud máxima permitida para una contraseña.
    private static final int MAX_PASSWORD_LENGTH = 128;

    // Máximo de intentos fallidos permitidos.
    private static final int MAX_ATTEMPTS = 5;

    // Duración del bloqueo temporal: cinco minutos.
    private static final long LOCK_DURATION = 5 * 60 * 1000L;

    // Generador criptográfico de números aleatorios.
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // Constructor privado para impedir crear instancias.
    private LocalAuth() {
    }

    // Clase interna responsable de administrar SQLite.
    private static class LocalDatabase extends SQLiteOpenHelper {

        // Constructor de la base de datos.
        LocalDatabase(Context context) {

            // Inicializa SQLite con el nombre y versión definidos.
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Crea las tablas cuando se instala la aplicación.
        @Override
        public void onCreate(SQLiteDatabase db) {

            // Crea la tabla de usuarios.
            db.execSQL(
                    "CREATE TABLE users (" +
                            "dni TEXT PRIMARY KEY NOT NULL, " +
                            "salt BLOB NOT NULL, " +
                            "password_hash BLOB NOT NULL, " +
                            "created_at INTEGER NOT NULL" +
                            ")"
            );

            // Crea la tabla de control de intentos fallidos.
            db.execSQL(
                    "CREATE TABLE auth_attempts (" +
                            "dni TEXT PRIMARY KEY NOT NULL, " +
                            "failed_count INTEGER NOT NULL DEFAULT 0, " +
                            "blocked_until INTEGER NOT NULL DEFAULT 0" +
                            ")"
            );
        }

        // Administra futuras actualizaciones de la base de datos.
        @Override
        public void onUpgrade(
                SQLiteDatabase db,
                int oldVersion,
                int newVersion
        ) {

            // No elimina información automáticamente.
            // Las migraciones se implementarán cuando cambie el esquema.
        }
    }

    // Registra un usuario nuevo en la base de datos local.
    public static boolean registerUser(
            Context context,
            String dni,
            char[] password
    ) throws Exception {

        // Verifica que el contexto exista.
        if (context == null) {
            throw new IllegalArgumentException("Contexto inválido.");
        }

        // Valida el formato del DNI.
        if (dni == null || !dni.matches("[0-9]{8}")) {
            throw new IllegalArgumentException("DNI inválido.");
        }

        // Valida los requisitos de la contraseña.
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Contraseña no válida.");
        }

        // Inicializa los datos criptográficos temporales.
        byte[] salt = null;
        byte[] passwordHash = null;

        // Inicializa el administrador de la base de datos.
        LocalDatabase helper = new LocalDatabase(
                context.getApplicationContext()
        );

        try {

            // Genera un salt aleatorio exclusivo para el usuario.
            salt = generateSalt();

            // Calcula el hash protegido de la contraseña.
            passwordHash = generateHash(password, salt);

            // Abre la base de datos para escritura.
            SQLiteDatabase db = helper.getWritableDatabase();

            // Prepara los valores del nuevo usuario.
            ContentValues values = new ContentValues();

            // Almacena el DNI como identificador.
            values.put("dni", dni);

            // Almacena el salt individual.
            values.put("salt", salt);

            // Almacena exclusivamente el hash de la contraseña.
            values.put("password_hash", passwordHash);

            // Registra la fecha de creación del usuario.
            values.put("created_at", System.currentTimeMillis());

            // Inserta el usuario sin sobrescribir cuentas existentes.
            long result = db.insertWithOnConflict(
                    "users",
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_IGNORE
            );

            // Devuelve verdadero si el registro fue creado.
            return result != -1;

        } finally {

            // Limpia el salt temporal.
            if (salt != null) {
                Arrays.fill(salt, (byte) 0);
            }

            // Limpia el hash temporal.
            if (passwordHash != null) {
                Arrays.fill(passwordHash, (byte) 0);
            }

            // Cierra el administrador de la base de datos.
            helper.close();
        }
    }

    // Verifica las credenciales de un usuario registrado.
    public static boolean authenticate(
            Context context,
            String dni,
            char[] password
    ) throws Exception {

        // Verifica que el contexto exista.
        if (context == null) {
            throw new IllegalArgumentException("Contexto inválido.");
        }

        // Verifica el formato del DNI.
        if (dni == null || !dni.matches("[0-9]{8}")) {
            return false;
        }

        // Verifica que la contraseña tenga una longitud permitida.
        if (password == null ||
                password.length < MIN_PASSWORD_LENGTH ||
                password.length > MAX_PASSWORD_LENGTH) {
            return false;
        }

        // Inicializa el administrador de la base de datos.
        LocalDatabase helper = new LocalDatabase(
                context.getApplicationContext()
        );

        // Inicializa el salt del usuario.
        byte[] salt = null;

        // Inicializa el hash almacenado.
        byte[] storedHash = null;

        // Controla si el usuario existe.
        boolean userExists = false;

        try {

            // Abre la base de datos para escritura.
            SQLiteDatabase db = helper.getWritableDatabase();

            // Inicia una transacción para controlar los intentos.
            db.beginTransaction();

            try {

                // Obtiene la hora actual.
                long currentTime = System.currentTimeMillis();

                // Inicializa el contador de intentos fallidos.
                int failedCount = 0;

                // Consulta el estado de bloqueo del usuario.
                try (Cursor attemptsCursor = db.rawQuery(
                        "SELECT failed_count, blocked_until " +
                                "FROM auth_attempts WHERE dni = ?",
                        new String[]{dni}
                )) {

                    // Comprueba si existe un registro de intentos.
                    if (attemptsCursor.moveToFirst()) {

                        // Recupera el número de intentos fallidos.
                        failedCount = attemptsCursor.getInt(0);

                        // Obtiene la fecha de finalización del bloqueo.
                        long blockedUntil = attemptsCursor.getLong(1);

                        // Comprueba si el bloqueo continúa vigente.
                        if (blockedUntil > currentTime) {

                            // Confirma la transacción.
                            db.setTransactionSuccessful();

                            // Impide el acceso durante el bloqueo.
                            return false;
                        }

                        // Comprueba si el bloqueo ya terminó.
                        if (blockedUntil > 0 && blockedUntil <= currentTime) {

                            // Reinicia el contador después del bloqueo.
                            failedCount = 0;

                            // Prepara la actualización del historial.
                            ContentValues resetValues = new ContentValues();

                            // Reinicia los intentos fallidos.
                            resetValues.put("failed_count", 0);

                            // Elimina el bloqueo vencido.
                            resetValues.put("blocked_until", 0);

                            // Actualiza el registro del usuario.
                            db.update(
                                    "auth_attempts",
                                    resetValues,
                                    "dni = ?",
                                    new String[]{dni}
                            );
                        }
                    }
                }

                // Consulta el salt y el hash del usuario.
                try (Cursor userCursor = db.rawQuery(
                        "SELECT salt, password_hash " +
                                "FROM users WHERE dni = ?",
                        new String[]{dni}
                )) {

                    // Comprueba si el usuario existe.
                    if (userCursor.moveToFirst()) {

                        // Obtiene el salt individual.
                        salt = userCursor.getBlob(0);

                        // Obtiene el hash almacenado.
                        storedHash = userCursor.getBlob(1);

                        // Confirma que el usuario está registrado.
                        userExists = true;
                    }
                }

                // Si el usuario no existe, utiliza datos ficticios.
                if (!userExists) {

                    // Genera un salt temporal.
                    salt = new byte[SALT_BYTES];

                    // Genera valores aleatorios para el salt ficticio.
                    SECURE_RANDOM.nextBytes(salt);

                    // Crea un hash ficticio de 256 bits.
                    storedHash = new byte[HASH_BITS / 8];
                }

                // Calcula el hash de la contraseña ingresada.
                byte[] calculatedHash = generateHash(password, salt);

                // Inicializa el resultado de la comparación.
                boolean passwordMatches;

                try {

                    // Compara los hashes de forma segura.
                    passwordMatches = MessageDigest.isEqual(
                            calculatedHash,
                            storedHash
                    );

                } finally {

                    // Limpia el hash calculado temporal.
                    Arrays.fill(calculatedHash, (byte) 0);
                }

                // Autoriza solamente usuarios existentes con contraseña correcta.
                boolean authenticated = userExists && passwordMatches;

                // Comprueba si las credenciales son correctas.
                if (authenticated) {

                    // Elimina el historial de intentos fallidos.
                    db.delete(
                            "auth_attempts",
                            "dni = ?",
                            new String[]{dni}
                    );

                } else {

                    // Incrementa el número de intentos fallidos.
                    failedCount++;

                    // Calcula la fecha de finalización del bloqueo.
                    long blockedUntil = failedCount >= MAX_ATTEMPTS
                            ? currentTime + LOCK_DURATION
                            : 0;

                    // Prepara la actualización del historial.
                    ContentValues values = new ContentValues();

                    // Guarda el nuevo contador de intentos.
                    values.put("dni", dni);

                    // Guarda la cantidad de intentos fallidos.
                    values.put("failed_count", failedCount);

                    // Guarda la fecha de bloqueo.
                    values.put("blocked_until", blockedUntil);

                    // Inserta o actualiza el registro de intentos.
                    db.insertWithOnConflict(
                            "auth_attempts",
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE
                    );
                }

                // Confirma los cambios de la transacción.
                db.setTransactionSuccessful();

                // Devuelve el resultado de la autenticación.
                return authenticated;

            } finally {

                // Finaliza la transacción de SQLite.
                db.endTransaction();
            }

        } finally {

            // Limpia el salt temporal.
            if (salt != null) {
                Arrays.fill(salt, (byte) 0);
            }

            // Limpia el hash almacenado en memoria.
            if (storedHash != null) {
                Arrays.fill(storedHash, (byte) 0);
            }

            // Cierra el administrador de la base de datos.
            helper.close();
        }
    }

    // Comprueba los requisitos de seguridad de una contraseña.
    private static boolean isValidPassword(char[] password) {

        // Rechaza contraseñas fuera del rango permitido.
        if (password == null ||
                password.length < MIN_PASSWORD_LENGTH ||
                password.length > MAX_PASSWORD_LENGTH) {
            return false;
        }

        // Indica si contiene una letra minúscula.
        boolean hasLowercase = false;

        // Indica si contiene una letra mayúscula.
        boolean hasUppercase = false;

        // Indica si contiene un número.
        boolean hasNumber = false;

        // Indica si contiene un símbolo.
        boolean hasSymbol = false;

        // Recorre los caracteres de la contraseña.
        for (char character : password) {

            // Comprueba si es una letra minúscula.
            if (Character.isLowerCase(character)) {
                hasLowercase = true;

                // Comprueba si es una letra mayúscula.
            } else if (Character.isUpperCase(character)) {
                hasUppercase = true;

                // Comprueba si es un número.
            } else if (Character.isDigit(character)) {
                hasNumber = true;

                // Comprueba si es un símbolo distinto de un espacio.
            } else if (!Character.isWhitespace(character)) {
                hasSymbol = true;
            }
        }

        // Acepta únicamente si cumple todos los requisitos.
        return hasLowercase &&
                hasUppercase &&
                hasNumber &&
                hasSymbol;
    }

    // Genera un salt aleatorio para cada contraseña.
    private static byte[] generateSalt() {

        // Reserva espacio para los bytes del salt.
        byte[] salt = new byte[SALT_BYTES];

        // Genera valores criptográficamente seguros.
        SECURE_RANDOM.nextBytes(salt);

        // Devuelve el salt generado.
        return salt;
    }

    // Genera el hash de una contraseña mediante PBKDF2.
    private static byte[] generateHash(
            char[] password,
            byte[] salt
    ) throws Exception {

        // Define el algoritmo de derivación de contraseñas.
        PBEKeySpec specification = new PBEKeySpec(
                password,
                salt,
                PBKDF2_ITERATIONS,
                HASH_BITS
        );

        try {

            // Obtiene el generador PBKDF2 con SHA-256.
            SecretKeyFactory factory = SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA256"
            );

            // Deriva el hash utilizando la contraseña y el salt.
            return factory.generateSecret(specification).getEncoded();

        } finally {

            // Limpia la contraseña almacenada en la especificación.
            specification.clearPassword();
        }
    }
}
