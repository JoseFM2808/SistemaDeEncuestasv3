/*
 * Responsable: José Flores
 * Relación con otras partes del código:
 * - Utilizado por las clases de interfaz de usuario (UI) para gestionar
 * el flujo de navegación entre menús, permitiendo un fácil retorno a estados anteriores.
 * Funcionalidad:
 * - Implementa una estructura de datos de Pila (Stack) utilizando una lista enlazada interna.
 * - Proporciona operaciones básicas de pila: push, pop, peek, isEmpty, size.
 * Modelos de Ordenamiento/Estructura de la Información:
 * - Emplea la estructura de datos Pila (Stack) para el seguimiento de la navegación.
 * - Internamente, utiliza una Lista Enlazada Simple para la implementación de la pila.
 */

package SteveJobs.encuestas.util;

import java.util.EmptyStackException;

public class PilaNavegacion<T> {

    public static final PilaNavegacion<String> instance = new PilaNavegacion<>();

    private static class Nodo<T> {
        T data;
        Nodo<T> siguiente;

        Nodo(T data) {
            this.data = data;
            this.siguiente = null;
        }
    }

    private Nodo<T> cima;
    private int contador;

    public PilaNavegacion() {
        cima = null;
        contador = 0;
    }

    public void push(T data) {
        Nodo<T> nuevoNodo = new Nodo<>(data);
        nuevoNodo.siguiente = cima;
        cima = nuevoNodo;
        contador++;
        System.out.println("PilaNavegacion: Pushed '" + data + "'. Tamaño actual: " + contador);
    }

    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T data = cima.data;
        cima = cima.siguiente;
        contador--;
        System.out.println("PilaNavegacion: Popped '" + data + "'. Tamaño actual: " + contador);
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return cima.data;
    }

    public boolean isEmpty() {
        return contador == 0;
    }

    public int size() {
        return contador;
    }
}
