package SteveJobs.encuestas.algoritmos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetectorPatronesFrecuencia {

   
    public <T> Map<T, Integer> calcularFrecuenciaRespuestas(List<T> respuestas) {
        Map<T, Integer> frecuencias = new HashMap<>();
        if (respuestas == null || respuestas.isEmpty()) {
            return frecuencias;
        }

        for (T respuesta : respuestas) {
           
            frecuencias.put(respuesta, frecuencias.getOrDefault(respuesta, 0) + 1);
        }
        return frecuencias;
    }

    
    public <T> Map<T, Integer> obtenerRespuestasMasFrecuentes(List<T> respuestas) {
        Map<T, Integer> frecuencias = calcularFrecuenciaRespuestas(respuestas);
        Map<T, Integer> masFrecuentes = new HashMap<>();

        if (frecuencias.isEmpty()) {
            return masFrecuentes;
        }

        int maxFrecuencia = 0;
       
        for (Integer frecuencia : frecuencias.values()) {
            if (frecuencia > maxFrecuencia) {
                maxFrecuencia = frecuencia;
            }
        }

        
        if (maxFrecuencia > 0) { 
            for (Map.Entry<T, Integer> entry : frecuencias.entrySet()) {
                if (entry.getValue() == maxFrecuencia) {
                    masFrecuentes.put(entry.getKey(), maxFrecuencia);
                }
            }
        }
        return masFrecuentes;
    }

    
}
