import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/bancodb?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA BANCÁRIO ===");
        System.out.println("1. Acessar Conta Existente (Login)");
        System.out.println("2. Criar Nova Conta");
        System.out.print("Escolha uma opção: ");
        int opcaoInicial = scanner.nextInt();
        scanner.nextLine();

        ContaBancaria conta = null;

        if (opcaoInicial == 1) {
            System.out.print("Digite seu Nome de Titular: ");
            String nomeTitular = scanner.nextLine();
            conta = autenticarConta(nomeTitular, scanner);
        } else if (opcaoInicial == 2) {
            conta = cadastrarNovaConta(scanner);
        } else {
            System.out.println("Opção inválida! Encerrando.");
            scanner.close();
            return;
        }

        if (conta == null) {
            System.out.println("Falha no acesso. Encerrando o programa.");
            scanner.close();
            return;
        }

        int opcao = 0;
        do {
            System.out.println("\n--- MENU DE OPÇÕES ---");
            System.out.println("1. Depositar");
            System.out.println("2. Sacar");
            System.out.println("3. Ver Extrato");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
            } else {
                scanner.next();
                System.out.println("Opção inválida! Digite um número.");
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.print("Digite o valor do depósito: R$ ");
                    double valorDeposito = scanner.nextDouble();
                    conta.depositar(valorDeposito);
                    atualizarSaldoNoBanco(conta.getNumeroConta(), conta.getSaldo());
                    break;
                case 2:
                    System.out.print("Digite o valor do saque: R$ ");
                    double valorSaque = scanner.nextDouble();
                    if (conta.sacar(valorSaque)) {
                        atualizarSaldoNoBanco(conta.getNumeroConta(), conta.getSaldo());
                    }
                    break;
                case 3:
                    conta.exibirExtrato();
                    break;
                case 4:
                    System.out.println("Saindo... Dados salvos no MySQL!");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 4);

        scanner.close();
    }

    private static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver do MySQL não encontrado na pasta!");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static ContaBancaria autenticarConta(String titular, Scanner scanner) {
        String sqlSelect = "SELECT * FROM contas WHERE titular = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {

            pstmt.setString(1, titular);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int numConta = rs.getInt("numero_conta");
                double saldo = rs.getDouble("saldo");
                String hashBanco = rs.getString("senha_hash");

                System.out.print("Digite sua senha para entrar: ");
                String senhaDigitada = scanner.nextLine();

                if (SecurePassword.verificarSenha(senhaDigitada, hashBanco)) {
                    System.out.println("\nLogin efetuado com sucesso! Bem-vindo(a), " + titular + "!");
                    return new ContaBancaria(titular, numConta, saldo, hashBanco);
                } else {
                    System.out.println("\n[ERRO] Senha incorreta!");
                    return null;
                }
            } else {
                System.out.println("\n[ERRO] Nenhum usuário encontrado com o nome '" + titular + "'!");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Erro de Conexão: " + e.getMessage());
            return null;
        }
    }

    private static ContaBancaria cadastrarNovaConta(Scanner scanner) {
        System.out.print("Digite o nome do titular para o cadastro: ");
        String titular = scanner.nextLine();

        System.out.print("Crie uma senha de acesso: ");
        String senhaLimpa = scanner.nextLine();

        String hashGerado = SecurePassword.gerarHash(senhaLimpa);
        String sqlInsert = "INSERT INTO contas (titular, saldo, senha_hash) VALUES (?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, titular);
            pstmt.setDouble(2, 0.0);
            pstmt.setString(3, hashGerado);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("\n=== CONTA CRIADA COM SUCESSO! ===");
                System.out.println("Conta gerada (ID " + idGerado + "). Para os próximos acessos, use seu NOME (" + titular + ") e SENHA!");

                return new ContaBancaria(titular, idGerado, 0.0, hashGerado);
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("\n[ERRO] Já existe um cadastrado com este nome! Escolha outro nome.");
            } else {
                System.out.println("Erro ao criar nova conta: " + e.getMessage());
            }
        }
        return null;
    }

    private static void atualizarSaldoNoBanco(int numeroConta, double novoSaldo) {
        String sqlUpdate = "UPDATE contas SET saldo = ? WHERE numero_conta = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

            pstmt.setDouble(1, novoSaldo);
            pstmt.setInt(2, numeroConta);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar saldo: " + e.getMessage());
        }
    }
}