package base;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author konecta
 */
public class BaseClass {
 
    protected HashMap<String, Object> setearFiltros(HashMap<String, Object> filtros, String path) 
            throws NoSuchFieldException {
        
        HashMap<String, Object> res = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : filtros.entrySet()) {
            try {
                String clave = entry.getKey();
                String valor = (String) entry.getValue();  
                    res.put(clave, valor);
                
            } catch (Exception e) {
                System.err.println("Error Filtro : " + e.getMessage());
            }
        }
        
        return res;
    }
    
   
}
