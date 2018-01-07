package base;

import java.util.ArrayList;
import java.util.List;

/**
 * Excepcion a propagar cuando ocurra alguna exccepcion de regla de negocio.
 *
 * @author rafael.benegas@konecta.com.py
 *
 */
public abstract class BusinessException extends Exception {

    private static final long serialVersionUID = 9122458587681754074L;

    public BusinessException(String message) {
        super(message);
    }

    /**
     * Esta clase representa a una violacion de una o mas reglas de negocio
     * relacionadas a restricciones del modelo
     *
     * @author Rafael Benegas
     *
     */
    public static class ModelValidationException extends BusinessException {

        private static final long serialVersionUID = 1149303182428995606L;
        private List<String> violationMessages;

        /**
         *
         * @param message Mensaje general de la excepcion.
         * @param violationMessages representa la lista de violaciones o
         * contraints que fueron violadas.
         */
        public ModelValidationException(String message, List<String> violationMessages) {

            super(message);

            if (violationMessages != null && !violationMessages.isEmpty()) {
                this.violationMessages = violationMessages;
            } else {
                this.violationMessages = new ArrayList<String>();
            }
        }

        /**
         *
         * @param message Mensaje general de la excepcion.
         * @param violation representa la lista de violaciones o contraints que
         * fueron violadas.
         */
        public ModelValidationException(String message, String violation) {

            super(message);

            if (violation != null && !violation.isEmpty()) {
                this.violationMessages = new ArrayList<String>();
                this.violationMessages.add(violation);

            } else {

                this.violationMessages = new ArrayList<String>();

            }
        }

        public List<String> getViolationMessages() {
            return violationMessages;
        }

        public void setViolationMessages(List<String> violationMessages) {
            this.violationMessages = violationMessages;
        }
    }

    /**
     * Esta clase representa a una violacion de una regla de negocios.
     *
     * @author Rafael Benegas
     *
     */
    public static class RuleValidationException extends BusinessException {

        private static final long serialVersionUID = 1149303182428995606L;

        /**
         *
         * @param violation representa la violacion cometida sobre una regla de
         * negocio.
         */
        public RuleValidationException(String violation) {
            super(violation);
        }
    }
}
