public class ContaBancaria {
    private String titular;
    private int numeroConta;
    private double saldo;
    private String senhaHash;

    public ContaBancaria(String titular, int numeroConta, double saldoInicial, String senhaHash) {
        this.titular = titular;
        this.numeroConta = numeroConta;
        this.saldo = saldoInicial;
        this.senhaHash = senhaHash;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
            System.out.printf("Depósito de R$ %.2f realizado com sucesso!%n", valor);
        } else {
            System.out.println("Valor de depósito inválido.");
        }
    }

    public boolean sacar(double valor) {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            System.out.printf("Saque de R$ %.2f realizado com sucesso!%n", valor);
            return true;
        } else {
            System.out.println("Saldo insuficiente ou valor de saque inválido.");
            return false;
        }
    }

    public void exibirExtrato() {
        System.out.println("\n--- EXTRATO DE CONTA ---");
        System.out.println("Titular: " + titular);
        System.out.println("Número da Conta: " + numeroConta);
        System.out.printf("Saldo Atual: R$ %.2f%n", saldo);
        System.out.println("------------------------");
    }

    public String getTitular() { return titular; }
    public int getNumeroConta() { return numeroConta; }
    public double getSaldo() { return saldo; }
    public String getSenhaHash() { return senhaHash; }
}