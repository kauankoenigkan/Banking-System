import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurePassword {

    public static String gerarHash(String senha) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = algorithm.digest(senha.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao gerar Hash da senha", e);
        }
    }

    public static boolean verificarSenha(String senhaDigitada, String hashSalvo) {
        String hashDigitado = gerarHash(senhaDigitada);
        return hashDigitado.equals(hashSalvo);
    }
}