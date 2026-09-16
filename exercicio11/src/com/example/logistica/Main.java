package com.example.logistica;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        try {
            System.out.print("Número do endereço: ");
            int numero = sc.nextInt();
            sc.nextLine();
            
            System.out.print("Código do pallet: ");
            String codigoPallet = sc.nextLine();
            
            System.out.print("Quantidade inicial: ");
            double quantidade = sc.nextDouble();
            
            System.out.print("Capacidade máxima do endereço: ");
            double capacidadeMaxima = sc.nextDouble();
            
            EnderecoPallet endereco = new EnderecoPallet(numero, codigoPallet, quantidade, capacidadeMaxima);
            
            System.out.println("\nDados do endereço:");
            System.out.println(endereco);
            
            System.out.print("\nQuantidade a armazenar: ");
            double armazenar = sc.nextDouble();
            endereco.armazenar(armazenar);
            System.out.println("Armazenamento realizado com sucesso!");
            
            System.out.println("\nDados do endereço (atualizado):");
            System.out.println(endereco);
            
            System.out.print("\nQuantidade a retirar: ");
            double retirar = sc.nextDouble();
            endereco.retirar(retirar);
            System.out.println("Retirada realizada com sucesso!");
            
            System.out.println("\nDados do endereço (atualizado):");
            System.out.println(endereco);
            
        } catch (CapacidadeExcedidaException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (QuantidadeInsuficienteException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}