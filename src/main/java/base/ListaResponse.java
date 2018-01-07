package base;

import java.util.List;

/**
 * Clase generica utlizada para encapsular las listas.
 *
 * @author mbaez
 *
 */
public class ListaResponse<T> {

    private int count;
    private List<T> rows;

    public ListaResponse() {
    }

    public ListaResponse(int count, List<T> rows) {
        this.count = count;
        this.rows = rows;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
