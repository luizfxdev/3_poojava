package com.example.reservation;

import java.time.localDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            System.out.print("Placa do Veículo: ");
            String licensePlate = sc.nextLine();

            System.out.print("Data de saída (dd/MM/yyyy): ");
            LocalDate checkoutDate = LocalDate.parse(sc.nextLine(), fmt);

            System.out.print("Data de devolução (dd/MM/yyyy): ");
            LocalDate returnDate = LocalDate.parse(sc.nextLine(), fmt);

            VehicleReservation reservation = new VehicleReservation(licensePlate, checkoutDate, returnDate);

            System.out.println("\nDados da reserva:");
            System.out.println(reservation);

            System.out.print("\nNova data de saída (dd/MM/yyyy): ");
            LocalDate newCheckoutDate = LocalDate = LocalDate.parse(sc.nextLine(), fmt);

            System.out.print("Nova data de devolução (dd/MM/yyyy): ");
            LocalDate newReturnDate = LocalDate.parse(sc.nextLine(), fmt);

            reservation.updateDates(newCheckoutDate, newReturnDate);

            System.out.println("\nDados da reserva (atualizado): ");
            System.out.println(reservation);

        } catch (PastDateException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (InvalidDateRangeException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }finally {
            sc.close();
        }
    }
}