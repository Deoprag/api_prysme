package com.deopraglabs.api_prysme.utils;

import com.deopraglabs.api_prysme.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class Utils {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    public static void checkField(List<String> validations, boolean condition, String message) {
        if (condition) validations.add(message);
    }

    public static String removeSpecialCharacters(String str) {
        return str.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String encryptPassword(String password) {
        return SecurityConfig.passwordEncoder().encode(password);
    }

    public static Date formatStringToDate(String date) {
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date resetTime(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String generateRandomKey() {
        final Random random = new Random();

        final String uf = String.format("%02d", random.nextInt(27) + 1); // UF entre 01 e 27
        final String anoMes = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMM")); // Data atual no formato yyMM
        final String cnpj = String.format("%014d", random.nextLong(1_000_000_000_000_00L)); // CNPJ com 14 dígitos
        final String modelo = String.format("%02d", random.nextInt(99) + 1); // Modelo aleatório entre 01 e 99
        final String serie = String.format("%03d", random.nextInt(999) + 1); // Série entre 001 e 999
        final String numeroNota = String.format("%09d", random.nextInt(1_000_000_000)); // Número da nota com 9 dígitos
        final String tipoEmissao = String.valueOf(random.nextInt(9) + 1); // Tipo de emissão entre 1 e 9
        final String codigoAleatorio = String.format("%08d", random.nextInt(100_000_000)); // Código numérico com 8 dígitos

        StringBuilder chave = new StringBuilder();
        chave.append(uf)
                .append(anoMes)
                .append(cnpj)
                .append(modelo)
                .append(serie)
                .append(numeroNota)
                .append(tipoEmissao)
                .append(codigoAleatorio);

        final String digitoVerificador = calcularDigito(chave.toString());
        chave.append(digitoVerificador);

        return chave.toString();
    }

    private static String calcularDigito(String chaveSemDigito) {
        int peso = 2;
        int soma = 0;

        for (int i = chaveSemDigito.length() - 1; i >= 0; i--) {
            int num = Character.getNumericValue(chaveSemDigito.charAt(i));
            soma += num * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }

        int resto = soma % 11;
        return resto < 2 ? "0" : String.valueOf(11 - resto);
    }
}
