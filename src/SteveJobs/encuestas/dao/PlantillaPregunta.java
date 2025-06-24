/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SteveJobs.encuestas.dao;

import java.util.List;

/**
 *
 * @author Pablo
 */
public class PlantillaPregunta {
  public static PlantillaPreguntaCat buscarPorId(List<PlantillaPreguntaCat> lista, int idBuscado) {
        for (PlantillaPreguntaCat Cat : lista) {
            if (Cat.getId() == idBuscado) {
                return Cat;
            }
        }
        return null;
    }

    public static PlantillaPreguntaCat buscarEnOrdenPorId(List<PlantillaPreguntaCat> lista, int idBuscado) {
        for (PlantillaPreguntaCat Cat : lista) {
            if (Cat.getId() == idBuscado) {
                return Cat;
            } else if (Cat.getId() > idBuscado) {
                break;
            }
        }
        return null;
    }
    
    public static PlantillaPreguntaCat busquedaBinariaPorId(List<PlantillaPreguntaCat> lista, int idBuscado) {
    int izquierda = 0;
    int derecha = lista.size() - 1;

    while (izquierda <= derecha) {
        int medio = (izquierda + derecha) / 2;
        PlantillaPreguntaCat actual = lista.get(medio);

        if (actual.getId() == idBuscado) {
            return actual;
        } else if (actual.getId() < idBuscado) {
            izquierda = medio + 1;
        } else {
            derecha = medio - 1;
        }
    }

    return null;
}     
}
