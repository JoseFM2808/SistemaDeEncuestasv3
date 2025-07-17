package SteveJobs.encuestas.algoritmos;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class CalculadoraEstadisticasBasicas {

    
    public double calcularPromedio(List<Double> datos) {
        if (datos == null || datos.isEmpty()) {
            return 0.0;
        }
        
        List<Double> datosValidos = datos.stream()
                                       .filter(Objects::nonNull)
                                       .collect(Collectors.toList());

        if (datosValidos.isEmpty()) {
            return 0.0;
        }

        double suma = 0;
        for (Double dato : datosValidos) {
            suma += dato;
        }
        return suma / datosValidos.size();
    }

   
    public double calcularMediana(List<Double> datos) {
        if (datos == null || datos.isEmpty()) {
            return 0.0;
        }

        List<Double> datosOrdenados = datos.stream()
                                           .filter(Objects::nonNull)
                                           .sorted()
                                           .collect(Collectors.toList());

        if (datosOrdenados.isEmpty()) {
            return 0.0;
        }

        int n = datosOrdenados.size();
        if (n % 2 == 1) {
            
            return datosOrdenados.get(n / 2);
        } else {
            
            double medio1 = datosOrdenados.get(n / 2 - 1);
            double medio2 = datosOrdenados.get(n / 2);
            return (medio1 + medio2) / 2.0;
        }
    }

   
}
