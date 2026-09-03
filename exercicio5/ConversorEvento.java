import java.time.LocalDateTime;
import java.util.time.Instant;
import java.util.time.LocalDateTime;
import java.util.time.ZoneId;
import java.util.time.format.DateTimeFormatter;
import java.util.time.temporal.ChronoUnit;


public class ConversorEvento {
public static void main(String[] args) {

    String eventoUTC = "2026-08-15T20:00:00Z";
    Instant instant = Instant.parse(eventoUTC);

    ZoneId zonaSpSP = ZoneId.of("America/Sao_Paulo");
    
    LocalDateTime eventoLocal = LocalDateTime.ofInstant(instant, zonaSpSP);
    LocalDateTime agora = LocalDateTime.now(zonaSpSP);
    long horasRestantes = ChronoUnit.HOURS.between(agora, eventoLocal);

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern('HH');

    String dataFormatada = eventoLocal.format(fmt);
    String horaFormatada = eventoLocal.format(fmtHora);

    String resultado = String.format(
    "%s às %sh (São Paulo)",
    dataFormatada,
    horaFormatada
);

System.out.println("Evento:" + resultado);
System.out.println("Faltam" + horasRestantes + "horas");

    }    
}
